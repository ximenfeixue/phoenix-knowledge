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
    public Map<String, Object> findConnectInfo(Long kid, Integer connType, int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", kid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", connType);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", page);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},", size);
        int start = (page - 1) * size;
        ConnectionInfoExample example = new ConnectionInfoExample();
        if (connType != null) {
            example.createCriteria().andKnowledgeidEqualTo(kid).andConntypeEqualTo(connType); 
        } else {
            example.createCriteria().andKnowledgeidEqualTo(kid);
        }
        example.setOrderByClause("connName ");
        example.setLimitStart(start);
        example.setLimitEnd(size);
        int count = connectInfoMapper.countByExample(example);
        List<ConnectionInfo> kcl = connectInfoMapper.selectByExample(example);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

}
