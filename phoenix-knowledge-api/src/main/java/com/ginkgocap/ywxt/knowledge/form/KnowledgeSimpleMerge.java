package com.ginkgocap.ywxt.knowledge.form;

import java.io.Serializable;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
/**
 * 
 * @author guangyuan
 *
 */
public class KnowledgeSimpleMerge extends Knowledge implements Serializable {
    
    private static final long serialVersionUID = 6917178105853559919L;
    
    private int me_type;

    public KnowledgeSimpleMerge() {
    }

    public int getMe_type() {
        return me_type;
    }

    public void setMe_type(int me_type) {
        this.me_type = me_type;
    }
    
    

}
