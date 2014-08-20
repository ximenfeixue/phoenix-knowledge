package com.ginkgocap.ywxt.knowledge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCatalogDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCatalog;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCatalogService;

@Service("knowledgeCatelogService")
public class KnowledgeCatalogServiceImpl implements KnowledgeCatalogService {

    @Autowired
    private KnowledgeCatalogDao knowledgeCatalogDao;
    
    @Override
    public KnowledgeCatalog saveOrUpdate(KnowledgeCatalog kc) {
        long id = kc.getId();
        if (id > 0) {
            knowledgeCatalogDao.update(kc);
        } else {
            kc = knowledgeCatalogDao.insert(kc);
        }
        return kc;
    }

    @Override
    public KnowledgeCatalog queryById( long id) {
        return knowledgeCatalogDao.queryById(id);
    }

}
