package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfo;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfoExample;
import com.ginkgocap.ywxt.knowledge.mapper.ConnectionInfoMapper;
import com.ginkgocap.ywxt.knowledge.service.ConnectInfoService;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("connectInfoService")
public class ConnectInfoServiceImpl implements ConnectInfoService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ConnectionInfoMapper connectInfoMapper;

    @Override
    public Map<String, Object> findConnectInfo(Long kid, Integer connType, String tag,int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", kid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", connType);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", tag);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", page);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", size);
        Map<String, Object> m = new HashMap<String, Object>();
        ConnectionInfoExample example = new ConnectionInfoExample();
        if (connType != null && connType > 0) {
            int start = (page - 1) * size;
            if("".equals(tag)){
            	example.createCriteria().andKnowledgeidEqualTo(kid).andConntypeEqualTo(connType);
            }else{
            	example.createCriteria().andKnowledgeidEqualTo(kid).andConntypeEqualTo(connType).andTagEqualTo(tag);
            }
            example.setOrderByClause("connName ");
            example.setLimitStart(start);
            example.setLimitEnd(size);
            m = getResult(example, page, size);
        } else {
            List<ConnectionInfo> kcl = getList(kid, page, size);
            m.put("page", "");
            m.put("list", kcl);
        }
        return m;
    }

    private Map<String, Object> getResult(ConnectionInfoExample example, int page, int size) {
        int count = connectInfoMapper.countByExample(example);
        List<ConnectionInfo> kcl = connectInfoMapper.selectByExample(example);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

    private List<ConnectionInfo> getList(Long kid, int page, int size) {
        List<ConnectionInfo> k = getListByParam(kid, 1, page, size);
        k.addAll(getListByParam(kid, 2, page, size));
        k.addAll(getListByParam(kid, 5, page, size));
        k.addAll(getListByParam(kid, 6, page, size));
        return k;
    }

    private List<ConnectionInfo> getListByParam(Long kid, int connType, int page, int size) {
        int start = (page - 1) * size;
        ConnectionInfoExample example = new ConnectionInfoExample();
        example.createCriteria().andKnowledgeidEqualTo(kid).andConntypeEqualTo(connType);
        example.setOrderByClause("connName ");
        example.setLimitStart(start);
        example.setLimitEnd(size);
        return connectInfoMapper.selectByExample(example);
    }

}
