package com.ginkgocap.ywxt.knowledge.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
/**
 * 
 * @author guangyuan
 *
 */
public class KnowledgeSimpleMerge extends Knowledge implements Serializable {
    
    private static final long serialVersionUID = 6917178105853559919L;
    
    private int me_type;
    
    private List<Object> me_param_name=new ArrayList<Object>(); 
    
    private Map<String, Object> me_param=new HashMap<String, Object>(0);

    public KnowledgeSimpleMerge() {
    }

    public int getMe_type() {
        return me_type;
    }

    public void setMe_type(int me_type) {
        this.me_type = me_type;
    }

    public List<Object> getMe_param_name() {
        return me_param_name;
    }

    public void setMe_param_name(List<Object> me_param_name) {
        this.me_param_name = me_param_name;
    }

    public Map<String, Object> getMe_param() {
        return me_param;
    }

    public void setMe_param(Map<String, Object> me_param) {
        this.me_param = me_param;
    }

}
