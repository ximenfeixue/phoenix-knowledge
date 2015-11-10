/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 
 * @author zhangle
 *
 */
public class JedisCacheClient {
    private static Logger logger = Logger.getLogger(JedisCacheClient.class);

    private ShardedJedisPool jedisPool;

    private ShardedJedis jedis;

    public void setJedisPool(ShardedJedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public ShardedJedis getJedis() {
        return jedisPool.getResource();
    }

    public void returnJedis(ShardedJedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public void setJedis(ShardedJedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 获得一个对象
     */
    public Object get(String key) {
        Object obj = null;
        ShardedJedis jedis = jedisPool.getResource();
        try {
            obj = byte2Object(jedis.get(getKey(key)));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }

        return obj;
    }

    /**
     *
     * 判断对象是否存在
     * @param key
     * @return
     */
    public boolean isExist(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 保存一个对象
     *
     * @param key
     * @param value
     */
    public void save(String key, Object value, int outTime) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.set(getKey(key), object2Bytes(value));
            if (outTime != 0) {
                jedis.expire(getKey(key), outTime);// 设置过期时间
            }
        } catch (Exception e) {
            logger.error("===save key===" + key + "===" + e.getLocalizedMessage());
        } finally {
            jedisPool.returnResource(jedis);

        }
    }

    public Long del(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.del(key);
            // return 1L;
        } catch (Exception e) {
            logger.error("===del key===" + key + "===" + e.getLocalizedMessage());
            return null;
        } finally {
            jedisPool.returnResource(jedis);

        }
    }

    /**
     * 字节转化为对象
     *
     *
     * @param bytes
     * @return
     */
    public static Object byte2Object(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = inputStream.readObject();

            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * 对象转化为字节
     *
     * @param value
     * @return
     */
    public static byte[] object2Bytes(Object value) {
        if (value == null) {
            return null;
        }

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);

            outputStream.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arrayOutputStream.toByteArray();
    }

    public static byte[] getKey(String key) {
        return key.getBytes();
    }
}
