package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.UserTag;
import com.ginkgocap.ywxt.knowledge.entity.UserTagExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeTagValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserTagMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeTagService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.Page;
import com.ginkgocap.ywxt.metadata.service.userTag.UserTagService;

@Service("knowledgeTagService")
public class KnowledgeTagServiceImpl implements KnowledgeTagService {
	
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private UserTagMapper userTagMapper;
	@Resource
	private UserTagService userTagService;
	@Resource
	private KnowledgeTagValueMapper knowledgeTagValueMapper;

	@Override
	public boolean updateKnowledgeTag(long kid, int type, String tags) {
		String obj = Constants.getTableName(type+"");
		boolean result = false;
		try {
		Criteria c = Criteria.where("_id").is(kid);
		Update update = new Update();
		update.set("tags", tags);
		Query query = new Query(c);
		mongoTemplate.updateFirst(query, update,
				obj.substring(obj.lastIndexOf(".") + 1, obj.length()));
		result = true;
		} catch (Exception e) {
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.service.KnowledgeTagService#saveOrUpdateUserTag(java.lang.Long, java.lang.String, long, java.lang.Long, java.lang.Long)
	 * Administrator
	 */
	@Override
	public Map<String, Object> saveOrUpdateUserTag(Long id, String tag,long userId, Long knowledgeid, Long webTagId) {
        Map<String, Object> remap = new HashMap<String, Object>();
        String resultMessage = "";
        UserTag ut = null;
        String name = tag.trim();
        //名称重复
        ut = ExistUserTag(userId, name);
        if (ut != null) {
            remap.put("resultType", Constants.fail);
            remap.put("resultMessage", Constants.ErrorMessage.tagExist.c());
            return remap;
        }
        if(Long.valueOf(9999).intValue()==id){
       
            ut = new UserTag();
            ut.setTag(name);
            ut.setUserid(userId);
            long tagId = sysSaveOrUpdate("i", userId, name, id);
            ut.setId(tagId);
            userTagMapper.insert(ut);
            UserTag utt = ExistUserTag(userId, name);
            ut.setId(utt.getId());
        	 //如果是金桐脑的tag插入到tb_demand_tag
            List<Long> nl  =new ArrayList<Long>();
            nl.add(utt.getId());
            knowledgeTagValueMapper.batchInsert(knowledgeid,nl);
            //然后将最初tb_demand_tag表中插入的金桐脑标签删除
            knowledgeTagValueMapper.deleteTAG(Long.valueOf(webTagId),Long.valueOf(knowledgeid)); 
            
        }
        else if (null == id || id == 0) {
            ut = new UserTag();
            ut.setTag(name);
            ut.setUserid(userId);
            long tagId = sysSaveOrUpdate("i", userId, name, id);
            ut.setId(tagId);
            userTagMapper.insert(ut);
            UserTag utt = ExistUserTag(userId, name);
            ut.setId(utt.getId());
           
        } else {
            //更新对象是否存在
            ut = ExistUserTagById(id);
            if (ut != null) {
                ut.setTag(name);
                sysSaveOrUpdate("u", userId, name, id);
                userTagMapper.updateByPrimaryKeySelective(ut);
            }
        }
        remap.put("resultType", Constants.success);
        remap.put("resultMessage", resultMessage);
        remap.put("obj", ut);
        return remap;
	}
	
    public UserTag ExistUserTag(long userId, String tag) {
        UserTagExample e = new UserTagExample();
        e.createCriteria().andUseridEqualTo(userId).andTagEqualTo(tag);
        List<UserTag> utl = userTagMapper.selectByExample(e);
        if (utl != null && utl.size() > 0) {
            return utl.get(0);
        } else {
            return null;
        }
    }
    
    private long sysSaveOrUpdate(String t, long userId, String name, long id) {
        long tagId=0;
        com.ginkgocap.ywxt.metadata.model.UserTag ut1 = new com.ginkgocap.ywxt.metadata.model.UserTag();
        ut1.setTagName(name);
        ut1.setUserId(userId);
        if ("u".equals(t)) {
            ut1.setTagId(id);
            userTagService.update(ut1);
        } else if ("i".equals(t)) {
            ut1 = userTagService.insert(ut1);
            tagId= ut1.getTagId();
        }
        return tagId;
    }
    
    private UserTag ExistUserTagById(Long id) {
        UserTag utl = userTagMapper.selectByPrimaryKey(id);
        return utl;
    }

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.service.KnowledgeTagService#queryUserTag(long, int, int)
	 * caihe
	 */
	@Override
	public Map<String, Object> queryUserTag(long userId, int page, int size) {
		
	        Map<String, Object> remap = new HashMap<String, Object>();
	        UserTagExample ute = new UserTagExample();
	        ute.createCriteria().andUseridEqualTo(userId);
	        if(size!=-1){//分页
	            ute.setLimitStart((page - 1) * size);
	            ute.setLimitEnd(size);
	        }
	        Object ul = userTagMapper.selectByExample(ute);
	        int count = userTagMapper.countByExample(ute);
	        Page returnPage = new Page();
	        returnPage.setTotalItems(count);
	        returnPage.setPageSize(size);
	        returnPage.setPageNo(page);
	        returnPage.setResult((List) ul);
	        remap.put("page", returnPage);
	        return remap;
	}
}
