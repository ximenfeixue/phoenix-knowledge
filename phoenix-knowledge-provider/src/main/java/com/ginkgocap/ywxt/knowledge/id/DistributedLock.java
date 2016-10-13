package com.ginkgocap.ywxt.knowledge.id;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Shared意味着锁是全局可见的，客户端都可以请求锁。
 * DistributedLock 应该是线程安全的。有待验证
 */
public class DistributedLock implements Watcher {

	private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);
	
    private String ADDR = "192.168.101.131:2181";
    private static final String LOCK_NODE = "guid-lock-";
    private String rootLockNode; //锁目录
    private ZooKeeper zk = null;
    private Integer mutex;
    private Integer currentLock;

    /**
     * 构造函数实现
     * 连接zk服务器
     * 创建zk锁目录
     *
     * @param rootLockNode
     */
    public DistributedLock(final String address, final String rootLockNode) {
    	this.ADDR = address;
        this.rootLockNode = rootLockNode;
        try {
            //连接zk服务器
            zk = new ZooKeeper(ADDR, 10 * 10000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mutex = new Integer(-1);
        // Create ZK node name
        if (zk != null) {
            try {
                //建立根目录节点
                Stat s = zk.exists(rootLockNode, false);
                if (s == null) {
                    zk.create(rootLockNode, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
            	logger.error("Keeper exception when instantiating queue: " + e.toString());
            } catch (InterruptedException e) {
            	logger.error("Interrupted exception");
            }
        }
    }

    /**
     * 请求zk服务器，获得锁
     *
     * @throws org.apache.zookeeper.KeeperException
     * @throws InterruptedException
     */
    public void acquire() throws KeeperException, InterruptedException {
        ByteBuffer b = ByteBuffer.allocate(4);
        byte[] value;
        // Add child with value i
        b.putInt(ThreadLocalRandom.current().nextInt(10));
        value = b.array();

        // 创建锁节点
        String lockName = zk.create(rootLockNode + "/" + LOCK_NODE, value, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        synchronized (mutex) {
            while (true) {
                // 获得当前锁节点的number，和所有的锁节点比较
                Integer acquireLock = new Integer(lockName.substring(lockName.lastIndexOf('-') + 1));
                List<String> childLockNode = zk.getChildren(rootLockNode, true);

                SortedSet<Integer> sortedLock = new TreeSet<Integer>();
                for (String temp : childLockNode) {
                    Integer tempLockNumber = new Integer(temp.substring(temp.lastIndexOf('-') + 1));
                    sortedLock.add(tempLockNumber);
                }

                currentLock = sortedLock.first();

                //如果当前创建的锁的序号是最小的那么认为这个客户端获得了锁
                if (currentLock >= acquireLock) {
                	logger.debug("thread_name=" + Thread.currentThread().getName() + "|attend lcok|lock_num=" + currentLock);
                    return;
                } else {
                    //没有获得锁则等待下次事件的发生
                	logger.debug("thread_name=" + Thread.currentThread().getName() + "|wait lcok|lock_num=" + currentLock);
                    mutex.wait();
                }
            }
        }
    }


    /**
     * 释放锁
     *
     * @throws org.apache.zookeeper.KeeperException
     * @throws InterruptedException
     */
    public void release() throws KeeperException, InterruptedException {
        String lockName = String.format("%010d", currentLock);
        zk.delete(rootLockNode + "/" + LOCK_NODE + lockName, -1);
        logger.debug("thread_name=" + Thread.currentThread().getName() + "|release lcok|lock_num=" + currentLock);
    }

    @Override
    public void process(WatchedEvent event) {
        synchronized (mutex) {
            mutex.notify();
        }
    }
}