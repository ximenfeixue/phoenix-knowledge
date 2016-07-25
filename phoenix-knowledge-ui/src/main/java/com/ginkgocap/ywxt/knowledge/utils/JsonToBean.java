package com.ginkgocap.ywxt.knowledge.utils;

/**
 * Created by gintong on 2016/7/23.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ginkgocap.ywxt.knowledge.model.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import com.ginkgocap.ywxt.cloud.model.CaseFile;
import com.ginkgocap.ywxt.knowledge.model.mobile.JTFile;
import com.ginkgocap.ywxt.file.model.FileIndex;
//import com.ginkgocap.ywxt.model.inc.customer.BusinessCustomer;
//import com.ginkgocap.ywxt.model.inc.people.BusinessPeople;

public class JsonToBean {
    /**
     * 人脉精简对象 jtContactMini
     * @param jsonObj
     * @return
     */
    public static JTContactMini getJTContactMiniBean(JSONObject jsonObj){
        JTContactMini jtContactMini=null;
        if(jsonObj!=null&&!"null".equals(jsonObj.toString())){
            jtContactMini=new JTContactMini();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                jtContactMini.setId(jsonObj.get("id").toString());
            }
            if(jsonObj.get("name")!=null&&strIsNotBank(jsonObj.get("name").toString())){
                jtContactMini.setName(jsonObj.getString("name"));
            }
            if(jsonObj.get("lastName")!=null&&strIsNotBank(jsonObj.get("lastName").toString())){
                jtContactMini.setLastName(jsonObj.getString("lastName"));
            }
            if(jsonObj.get("company")!=null&&strIsNotBank(jsonObj.get("company").toString())){
                jtContactMini.setCompany(jsonObj.getString("company"));
            }
            if(jsonObj.get("image")!=null&&strIsNotBank(jsonObj.get("image").toString())){
                jtContactMini.setImage(jsonObj.getString("image"));
            }
            if(jsonObj.get("friendState")!=null&&strIsNotBank(jsonObj.get("friendState").toString())){
                jtContactMini.setFriendState(Integer.parseInt(jsonObj.get("friendState").toString()));
            }
            if(null!=jsonObj.get("isOnline")&&strIsNotBank(jsonObj.get("isOnline").toString())){
                jtContactMini.setIsOnline(Boolean.parseBoolean(jsonObj.get("isOnline").toString()));
            }
            if(null!=jsonObj.get("isOffline")&&strIsNotBank(jsonObj.get("isOffline").toString())){
                jtContactMini.setIsOffline(Boolean.parseBoolean(jsonObj.get("isOffline").toString()));
            }
            if(null!=jsonObj.get("isWorkmate")&&strIsNotBank(jsonObj.get("isWorkmate").toString())){
                jtContactMini.setIsWorkmate(Boolean.parseBoolean(jsonObj.get("isWorkmate").toString()));
            }
        }
        return jtContactMini;
    }

    /**
     * 机构精简对象
     * @param jsonObj
     * @return
     */
    public static OrganizationMini getOrganizationMiniBean(JSONObject jsonObj){
        OrganizationMini organizationMini=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            organizationMini=new OrganizationMini();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                organizationMini.setId(jsonObj.optString("id"));
            }
            if(strIsNotBank(jsonObj.getString("logo"))){
                organizationMini.setLogo(jsonObj.getString("logo"));
            }

            if(strIsNotBank(jsonObj.getString("shortName"))){
                organizationMini.setShortName(jsonObj.getString("shortName"));
            }

            if(strIsNotBank(jsonObj.getString("fullname"))){
                organizationMini.setFullName(jsonObj.getString("fullname"));
            }
            if(jsonObj.get("guestType")!=null&&strIsNotBank(jsonObj.get("guestType").toString())){
                organizationMini.setGuestType(Integer.parseInt(jsonObj.get("guestType").toString()));
            }
            if(jsonObj.get("friendState")!=null&&strIsNotBank(jsonObj.get("friendState").toString())){
                organizationMini.setFriendState(jsonObj.getString("friendState")==null?null:Integer.parseInt(jsonObj.getString("friendState")));
            }
            if(jsonObj.get("joinState")!=null&&strIsNotBank(jsonObj.get("joinState").toString())){
                organizationMini.setJoinState(Integer.parseInt(jsonObj.get("joinState").toString()));
            }
        }

        return organizationMini;
    }

    /**
     * 地区对象
     * @param jsonObj
     * @return
     */
    public static Area getAreaBean(JSONObject jsonObj){
        Area area=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            area=new Area();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                area.setId(Integer.parseInt(jsonObj.get("id").toString()));//"地区id"
            }
            if(jsonObj.get("name")!=null&&strIsNotBank(jsonObj.get("name").toString())){
                area.setName(jsonObj.getString("name"));//"地区名称"
            }

            if(listJsonNotBank(jsonObj.get("listArea"))){
                JSONArray jsonObjArray=jsonObj.getJSONArray("listArea");
                List<Area> list=null;
                if(null!=jsonObjArray&&jsonObjArray.size()>0){
                    list=new ArrayList<Area>();
                    for(int i=0;i<jsonObjArray.size();i++){
                        JSONObject jsonObject=(JSONObject) jsonObjArray.get(i);
                        list.add(getAreaBean(jsonObject.getJSONObject("area")));
                    }
                }
                area.setListArea(list);
            }
        }
        return area;
    }

    /**
     * 事务迷你对象
     * @param jsonObj
     * @return
     */
    public static AffairMini getAffairMiniBean(JSONObject jsonObj){
        AffairMini affairMini=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            affairMini=new AffairMini();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                affairMini.setId(Integer.parseInt(jsonObj.get("id").toString()));//"id"
            }
            if(jsonObj.get("title")!=null&&strIsNotBank(jsonObj.get("title").toString())){
                affairMini.setTitle(jsonObj.getString("title"));//标题
            }
            if(jsonObj.get("name")!=null&&strIsNotBank(jsonObj.get("name").toString())){
                affairMini.setName(jsonObj.getString("name"));//发布人名字
            }
            if(jsonObj.get("time")!=null&&strIsNotBank(jsonObj.get("time").toString())){
                affairMini.setTime(jsonObj.getString("time"));//时间
            }
            if(jsonObj.get("type")!=null&&strIsNotBank(jsonObj.get("type").toString())){
                affairMini.setType(Integer.parseInt(jsonObj.get("type").toString()));// 1-业务需求；2-任务；3-项目
            }
            if(jsonObj.get("content")!=null&&strIsNotBank(jsonObj.get("content").toString())){
                affairMini.setContent(jsonObj.getString("content"));//事务描述
            }
            if(jsonObj.get("deadline")!=null&&strIsNotBank(jsonObj.get("deadline").toString())){
                affairMini.setDeadline(jsonObj.getString("deadline"));//完成时间
            }
        }
        return affairMini;
    }

    /**
     * 投融资类型对象
     * @param jsonObj
     * @return
     */
    public static InvestType getInvestTypeBean(JSONObject jsonObj){
        InvestType investType=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            investType=new InvestType();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                investType.setId(jsonObj.get("id").toString());//投融资类型id
            }
            if(jsonObj.get("title")!=null&&strIsNotBank(jsonObj.get("title").toString())){
                investType.setTitle(jsonObj.getString("title"));//标题
            }
        }
        return investType;
    }

    /**
     * 行业类型对象
     * @param jsonObj
     * @return
     */
    public static Trade getTradeBean(JSONObject jsonObj){
        Trade trade=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            trade=new Trade();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                trade.setId(jsonObj.get("id").toString());//行业类型id
            }
            if(jsonObj.get("title")!=null&&strIsNotBank(jsonObj.get("title").toString())){
                trade.setTitle(jsonObj.getString("title"));//标题
            }
        }
        return trade;
    }
    /**
     * 知识略缩对象
     * @param jsonObj
     * @return
     */
    public static KnowledgeMini getKnowledgeMiniBean(JSONObject jsonObj){
        KnowledgeMini knowledgeMini=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            knowledgeMini=new KnowledgeMini();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                knowledgeMini.setId(Integer.parseInt(jsonObj.get("id").toString()));//相关知识列表，知识id
            }
            if(jsonObj.get("title")!=null&&strIsNotBank(jsonObj.get("title").toString())){
                knowledgeMini.setTitle(jsonObj.getString("title"));//知识标题
            }
            if(jsonObj.get("url")!=null&&strIsNotBank(jsonObj.get("url").toString())){
                knowledgeMini.setUrl(jsonObj.getString("url"));//知识URL
            }
            if(jsonObj.get("time")!=null&&strIsNotBank(jsonObj.get("time").toString())){
                long time = KnowledgeUtil.parserTimeToLong(jsonObj.getString("time"));
                knowledgeMini.setTime(time);//发布时间
            }
        }
        return knowledgeMini;
    }

    /**
     * 工作经历对象
     * @param jsonObj
     * @return
     */
    public static WorkExperience getWorkExperienceBean(JSONObject jsonObj){
        WorkExperience workExperience=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            workExperience=new WorkExperience();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                workExperience.setId(Integer.parseInt(jsonObj.get("id").toString()));//id
            }
            if(jsonObj.get("title")!=null&&strIsNotBank(jsonObj.get("title").toString())){
                workExperience.setTitle(jsonObj.getString("title"));//职位
            }
            if(jsonObj.get("startTime")!=null&&strIsNotBank(jsonObj.get("startTime").toString())){
                workExperience.setStartTime(jsonObj.getString("startTime"));//开始时间
            }
            if(jsonObj.get("endTime")!=null&&strIsNotBank(jsonObj.get("endTime").toString())){
                workExperience.setEndTime(jsonObj.getString("endTime"));//结束时间
            }
            if(jsonObj.get("company")!=null&&strIsNotBank(jsonObj.get("company").toString())){
                workExperience.setCompany(jsonObj.getString("company"));//
            }
            if(jsonObj.get("content")!=null&&strIsNotBank(jsonObj.get("content").toString())){
                workExperience.setContent(jsonObj.getString("content"));
            }
        }
        return workExperience;
    }
    /**
     * 需求略缩对象
     * @param jsonObj
     * @return
     */
    public static RequirementMini getRequirementMiniBean(JSONObject jsonObj){
        RequirementMini requirementMini=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            requirementMini=new RequirementMini();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                requirementMini.setId(Integer.parseInt(jsonObj.get("id").toString()));//需求id
            }
            if(jsonObj.get("title")!=null&&strIsNotBank(jsonObj.get("title").toString())){
                requirementMini.setTitle(jsonObj.getString("title"));//标题
            }
            if(jsonObj.get("typeName")!=null&&strIsNotBank(jsonObj.get("typeName").toString())){
                requirementMini.setTypeName(jsonObj.getString("typeName"));//需求类型名称，如我要投资，我要融资
            }
            if(jsonObj.get("content")!=null&&strIsNotBank(jsonObj.get("content").toString())){
                requirementMini.setContent(jsonObj.getString("content"));//需求内容
            }
            if(jsonObj.get("time")!=null&&strIsNotBank(jsonObj.get("time").toString())){
                requirementMini.setTime(jsonObj.getString("time"));//发布时间
            }
        }
        return requirementMini;
    }
    /**
     * 货币类型对象
     * @param jsonObj
     * @return
     */
    public static MoneyType getMoneyTypeBean(JSONObject jsonObj){
        MoneyType moneyType=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            moneyType=new MoneyType();
            if(jsonObj.get("tag")!=null&&strIsNotBank(jsonObj.get("tag").toString())){
                moneyType.setTag(jsonObj.getString("tag"));//标题
            }
            if(jsonObj.get("name")!=null&&strIsNotBank(jsonObj.get("name").toString())){
                moneyType.setName(jsonObj.getString("name"));//标题
            }
        }
        return moneyType;
    }
    /**
     * 关系精简对象
     * @param jsonObj
     * @return
     */
    public static ConnectionsMini getConnectionsMiniBean(JSONObject jsonObj){
        ConnectionsMini connectionsMini=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            connectionsMini=new ConnectionsMini();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                connectionsMini.setId(Integer.parseInt(jsonObj.get("id").toString()));//需求id
            }
            if(jsonObj.get("image")!=null&&strIsNotBank(jsonObj.get("image").toString())){
                connectionsMini.setImage(jsonObj.getString("image"));//标题
            }
            if(jsonObj.get("name")!=null&&strIsNotBank(jsonObj.get("name").toString())){
                connectionsMini.setName(jsonObj.getString("name"));//标题
            }
            if(jsonObj.get("type")!=null&&strIsNotBank(jsonObj.get("type").toString())){
                connectionsMini.setType(Integer.parseInt(jsonObj.get("type").toString()));//需求id
            }
        }
        return connectionsMini;
    }
    /**
     * 关系精简对象
     * @param jsonObj
     * @return
     */
    public static JTFile getJTFileBean(JSONObject jsonObj){
        JTFile jtFile=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            jtFile=new JTFile();
            if(jsonObj.get("url")!=null&&strIsNotBank(jsonObj.get("url").toString())){
                jtFile.setUrl(jsonObj.getString("url"));//文件地址
            }
            if(jsonObj.get("suffixName")!=null&&strIsNotBank(jsonObj.get("suffixName").toString())){
                jtFile.setSuffixName(jsonObj.getString("suffixName"));//jpg,png,amr,pdf等
            }
            if(jsonObj.get("type")!=null&&strIsNotBank(jsonObj.get("type").toString())){
                jtFile.setType(jsonObj.get("type").toString());//0-video,1-audio,2-file,3-image,4-other
            }
            if(jsonObj.get("fileSize")!=null&&strIsNotBank(jsonObj.get("fileSize").toString())){
                jtFile.setFileSize(Long.parseLong(jsonObj.get("fileSize").toString()));
            }
            if(jsonObj.get("fileName")!=null&&strIsNotBank(jsonObj.get("fileName").toString())){
                jtFile.setFileName(jsonObj.get("fileName").toString());
            }
            if(jsonObj.get("moduleType")!=null&&strIsNotBank(jsonObj.get("moduleType").toString())){
                jtFile.setModuleType(jsonObj.get("moduleType").toString());
            }
            if(jsonObj.get("taskId")!=null&&strIsNotBank(jsonObj.get("taskId").toString())){
                jtFile.setTaskId(jsonObj.get("taskId").toString());
            }
        }
        return jtFile;
    }

    /**
     * 投融资意向关键字对象
     * @param jsonObj
     * @return
     */
    public static InvestKeyword getInvestKeywordBean(JSONObject jsonObj){
        InvestKeyword investKeyword=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            investKeyword=new InvestKeyword();

            investKeyword.setArea(getAreaBean(jsonObj.getJSONObject("area")));//地区
            investKeyword.setMoneyType(getMoneyTypeBean(jsonObj.getJSONObject("moneyType")));//CNY,USD等，配置登录里返回的数组里选择

            if(strIsNotBank(jsonObj.getString("moneyRange"))){
                investKeyword.setMoneyRange(jsonObj.getString("moneyRange"));//职位
            }
            //投融资类型 investType
            if(listJsonNotBank(jsonObj.get("listInvestType"))){
                JSONArray listInvestTypeArray=jsonObj.getJSONArray("listInvestType");
                List<InvestType> investTypeList=new ArrayList<InvestType>();
                for(int i=0;i<listInvestTypeArray.size();i++){
                    JSONObject jsonObject=(JSONObject) listInvestTypeArray.get(i);
                    investTypeList.add(JsonToBean.getInvestTypeBean(jsonObject));
                    //	    		investTypeList.add(JsonToBean.getInvestTypeBean(jsonObj.getJSONObject("investType")));
                }
                investKeyword.setListInvestType(investTypeList);
            }

            //投资行业
            if(listJsonNotBank(jsonObj.get("listTrade"))){
                JSONArray listTradeArray=jsonObj.getJSONArray("listTrade");
                List<Trade> tradeArrayList=null;
                if(null!=listTradeArray&&listTradeArray.size()>0){
                    tradeArrayList=new ArrayList<Trade>();
                    for(int i=0;i<listTradeArray.size();i++){
                        JSONObject jsonObject=(JSONObject) listTradeArray.get(i);
                        tradeArrayList.add(getTradeBean(jsonObject));
                        //					tradeArrayList.add(JsonToBean.getTradeBean(jsonObj.getJSONObject("investType")));
                    }
                }
                investKeyword.setListTrade(tradeArrayList);
            }

        }
        return investKeyword;
    }


    /**
     * 金桐人脉对象 jtContact
     * @param jsonObj
     * @return
     */
    public static JTContact getJTContactBean(JSONObject jsonObj){
        JTContact jtContact=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            jtContact=new JTContact();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                jtContact.setId(Integer.parseInt(jsonObj.get("id").toString()));
            }
            if(strIsNotBank(jsonObj.getString("name"))){
//				jtContact.setName(jsonObj.getString("name"));
            }
            if(strIsNotBank(jsonObj.getString("lastName"))){
//				jtContact.setLastName(jsonObj.getString("lastName"));
            }
            if(strIsNotBank(jsonObj.getString("company"))){
//				jtContact.setCompany(jsonObj.getString("company"));
            }
            if(null!=jsonObj.get("isOnline")&&strIsNotBank(jsonObj.get("isOnline").toString())){
                jtContact.setIsOnline(Boolean.parseBoolean(jsonObj.get("isOnline").toString()));
            }
            if(null!=jsonObj.get("isOffline")&&strIsNotBank(jsonObj.get("isOffline").toString())){
                jtContact.setIsOffline(Boolean.parseBoolean(jsonObj.get("isOffline").toString()));
            }
            if(jsonObj.get("friendState")!=null&&strIsNotBank(jsonObj.get("friendState").toString())){
                jtContact.setFriendState(Integer.parseInt(jsonObj.get("friendState").toString()));
            }
            if(null!=jsonObj.get("isWorkmate")&&strIsNotBank(jsonObj.get("isWorkmate").toString())){
                jtContact.setWorkmate(Boolean.parseBoolean(jsonObj.get("isWorkmate").toString()));
            }
            if(strIsNotBank(jsonObj.getString("fromDes"))){
                jtContact.setFromDes(jsonObj.getString("fromDes"));
            }
            if(strIsNotBank(jsonObj.getString("fax"))){
//				jtContact.setFax(jsonObj.getString("fax"));
            }
            if(strIsNotBank(jsonObj.getString("address"))){
//				jtContact.setAddress(jsonObj.getString("address"));
            }
            if(strIsNotBank(jsonObj.getString("url"))){
//				jtContact.setUrl(jsonObj.getString("url"));
            }
            if(strIsNotBank(jsonObj.getString("comment"))){
//				jtContact.setComment(jsonObj.getString("comment"));
            }

            //邮箱
            if(listJsonNotBank(jsonObj.get("listEmail"))){
                JSONArray listEmailArray=jsonObj.getJSONArray("listEmail");
                List<String> emailArrayList=null;
                if(null!=listEmailArray&&listEmailArray.size()>0){
                    emailArrayList=new ArrayList<String>();
                    for(int i=0;i<listEmailArray.size();i++){
                        emailArrayList.add(listEmailArray.get(i).toString());
                    }
                }
//				jtContact.setListEmail(emailArrayList);
            }

            //投资类别
            if(listJsonNotBank(jsonObj.get("listInvestType"))){
                JSONArray listInvestTypeArray=jsonObj.getJSONArray("listInvestType");
                List<InvestType> investTypeArrayList=null;
                if(null!=listInvestTypeArray&&listInvestTypeArray.size()>0){
                    investTypeArrayList=new ArrayList<InvestType>();
                    for(int i=0;i<listInvestTypeArray.size();i++){
                        investTypeArrayList.add(getInvestTypeBean((JSONObject) listInvestTypeArray.get(i)));
                    }
                }
            }

            //投资行业
            if(listJsonNotBank(jsonObj.get("listTrade"))){
                JSONArray listTradeArray=jsonObj.getJSONArray("listTrade");
                List<Trade> tradeArrayList=null;
                if(null!=listTradeArray&&listTradeArray.size()>0){
                    tradeArrayList=new ArrayList<Trade>();
                    for(int i=0;i<listTradeArray.size();i++){
                        tradeArrayList.add(getTradeBean((JSONObject) listTradeArray.get(i)));
                    }
                }
            }

//			private List<Integer> listOrganizationID;
//			private List<MobilePhone> listMobilePhone;
//			private List<Sns> listSns;
//			private List<PersonInfo> listPersonInfo;
//			private List<JTFile> listJTFile;
//			private List<RequirementMini> listRequirementMini;

            //知识略缩对象列表
            if(listJsonNotBank(jsonObj.get("listKnowledgeMini"))){
                JSONArray listKnowledgeMiniArray=jsonObj.getJSONArray("listKnowledgeMini");
                List<KnowledgeMini> knowledgeMiniList=new ArrayList<KnowledgeMini>();
                for(int i=0;i<listKnowledgeMiniArray.size();i++){
                    JSONObject jsonObject=(JSONObject) listKnowledgeMiniArray.get(i);
                    //	    		connectionsList.add(JsonToBean.getConnectionsBean(jsonObject));
                    knowledgeMiniList.add(JsonToBean.getKnowledgeMiniBean(jsonObject.getJSONObject("knowledgeMini")));
                }
//		    	jtContact.setListKnowledgeMini(knowledgeMiniList);
            }
            //相关机构与联系人(相关关系)
            if(listJsonNotBank(jsonObj.get("listConnections"))){
                JSONArray relationJTContactArray=jsonObj.getJSONArray("listConnections");
                List<Connections> connectionsList=new ArrayList<Connections>();
                for(int i=0;i<relationJTContactArray.size();i++){
                    JSONObject jsonObject=(JSONObject) relationJTContactArray.get(i);
                    //	    		connectionsList.add(JsonToBean.getConnectionsBean(jsonObject));
                    connectionsList.add(JsonToBean.getConnectionsBean(jsonObject.getJSONObject("connections")));
                }
                //jtContact.setListConnections(connectionsList);
            }
            //工作经历
            if(listJsonNotBank(jsonObj.get("listWorkExperience"))){
                JSONArray listWorkExperienceArray=jsonObj.getJSONArray("listWorkExperience");
                List<WorkExperience> workExperienceList=new ArrayList<WorkExperience>();
                for(int i=0;i<listWorkExperienceArray.size();i++){
                    JSONObject jsonObject=(JSONObject) listWorkExperienceArray.get(i);
                    //	    		connectionsList.add(JsonToBean.getConnectionsBean(jsonObject));
                    workExperienceList.add(JsonToBean.getWorkExperienceBean(jsonObject.getJSONObject("workExperience")));
                }
                jtContact.setListWorkExperience(workExperienceList);
            }
        }

        return jtContact;
    }

    /**
     * 关系对象
     * @param jsonObj
     * @return
     */
    public static Connections getConnectionsBean(JSONObject jsonObj){
        Connections connections=null;
        if(null!=jsonObj&&!"null".equals(jsonObj.toString())){
            connections=new Connections();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                connections.setId(Integer.parseInt(jsonObj.get("id").toString()));//行业类型id
            }
            if(strIsNotBank(jsonObj.getString("type"))){
                connections.setType(jsonObj.getInt("type"));//0-个人、1-机构
            }
            if(strIsNotBank(jsonObj.getString("sourceFrom"))){
                connections.setSourceFrom(jsonObj.getString("sourceFrom"));//第二行显示的文字，根据不同的关系对象， 存放不同的内容，目前存放的是关系来源
            }
            connections.setJtContactMini(getJTContactMiniBean((JSONObject) jsonObj.get("jtContactMini")));
            connections.setOrganizationMini(getOrganizationMiniBean((JSONObject) jsonObj.get("organizationMini")));

        }
        return connections;
    }

    /*
    public static Project getProjectbean(JSONObject project){
        Project projectBean=null;
        if(null!=project&&!"null".equals(project.toString())){
            projectBean=new Project();

            //"项目id，发布时为空"
            if(project.get("id")!=null&&strIsNotBank(project.get("id").toString())){
                projectBean.setId(Integer.parseInt(project.get("id").toString()));
            }
            //"发布人userid，发布时为空"
            if(project.get("userid")!=null&&strIsNotBank(project.get("userid").toString())){
                projectBean.setUserid(Integer.parseInt(project.get("userid").toString()));
            }
            projectBean.setName(project.get("name")!=null?project.get("name").toString():"");//"发布人姓名，发布时为空"
            projectBean.setUserAvatar(project.get("userAvatar")!=null?project.get("userAvatar").toString():"");//"发布人头像
            projectBean.setPublishTime(project.get("publishTime")!=null?project.get("publishTime").toString():"");//"发布时间，发布时为空"
            projectBean.setType(project.get("type")!=null?project.get("type").toString():"");//"0-投资，1-融资"
            projectBean.setTitle(project.get("title")!=null?project.get("title").toString():"");//"标题"
            projectBean.setContent(project.get("content")!=null?project.get("content").toString():"");//"简短描述下内容"
            projectBean.setDeadline(project.get("deadline")!=null?project.get("deadline").toString():"");//"完成期限"
            projectBean.setTaskId(project.get("taskId")!=null?project.get("taskId").toString():"");//附件索引

            //当前进度，百分比，整型
            if(project.get("progress")!=null&&strIsNotBank(project.get("progress").toString())){
                projectBean.setProgress(Integer.parseInt(project.get("progress").toString()));
            }
            //执行方式, 推介-1，承做-2，推介和承做-3
            if(project.get("exeMode")!=null&&strIsNotBank(project.get("exeMode").toString())){
                projectBean.setExeMode(Integer.parseInt(project.get("exeMode").toString()));
            }
            //0-仅自己可见，1-对所有人公开，2-对listConnections里的人可见
            if(project.get("publishRange")!=null&&strIsNotBank(project.get("publishRange").toString())){
                projectBean.setPublishRange(Integer.parseInt(project.get("publishRange").toString()));
            }

            if(null!=project.get("principal")){//负责人
                projectBean.setPrincipal(getJTContactMiniBean(project.getJSONObject("principal")));
            }
            if(null!=project.get("maintainer")){//维护人
                projectBean.setMaintainer(getJTContactMiniBean(project.getJSONObject("maintainer")));
            }

            //参与人列表
            if(listJsonNotBank(project.get("listJoinJTContactMini"))){
                JSONArray joinJTContactArray=project.getJSONArray("listJoinJTContactMini");
                List<JTContactMini> joinJTContactList=new ArrayList<JTContactMini>();
                for(int i=0;i<joinJTContactArray.size();i++){
                    JSONObject jsonObject=(JSONObject) joinJTContactArray.get(i);
                    joinJTContactList.add(JsonToBean.getJTContactMiniBean(jsonObject));
                }
                projectBean.setListJoinJTContactMini(joinJTContactList);
            }

            //相关机构与联系人(相关关系)
            if(listJsonNotBank(project.get("listConnectionsMini"))){
                JSONArray relationJTContactArray=project.getJSONArray("listConnectionsMini");
                List<ConnectionsMini> connectionsList=new ArrayList<ConnectionsMini>();
                for(int i=0;i<relationJTContactArray.size();i++){
                    JSONObject jsonObject=(JSONObject) relationJTContactArray.get(i);
                    connectionsList.add(JsonToBean.getConnectionsMiniBean(jsonObject));
                    //	    		connectionsList.add(JsonToBean.getConnectionsBean(jsonObject.getJSONObject("connections")));
                }
                projectBean.setListConnectionsMini(connectionsList);
            }

            projectBean.setInvestKeyword(getInvestKeywordBean(project.getJSONObject("investKeyword")));

            //相关的知识
            if(listJsonNotBank(project.get("listMatchKnowledgeMini"))){
                JSONArray knowledgeMiniArray=project.getJSONArray("listMatchKnowledgeMini");
                List<KnowledgeMini> knowledgeMiniList=new ArrayList<KnowledgeMini>();
                for(int i=0;i<knowledgeMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) knowledgeMiniArray.get(i);
                    knowledgeMiniList.add(JsonToBean.getKnowledgeMiniBean(jsonObj));
                }
                projectBean.setListMatchKnowledgeMini(knowledgeMiniList);
            }
            //相关的需求略缩对象
            if(listJsonNotBank(project.get("listMatchRequirementMini"))){
                JSONArray matchRequirementMiniArray=project.getJSONArray("listMatchRequirementMini");
                List<RequirementMini> matchRequirementMiniArrayList=new ArrayList<RequirementMini>();
                for(int i=0;i<matchRequirementMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) matchRequirementMiniArray.get(i);
                    matchRequirementMiniArrayList.add(JsonToBean.getRequirementMiniBean(jsonObj));
                }
                projectBean.setListMatchRequirementMini(matchRequirementMiniArray);
            }
            //相关的关系精简对象
            if(listJsonNotBank(project.get("listMatchConnectionsMini"))){
                JSONArray matchConnectionsMiniArray=project.getJSONArray("listMatchConnectionsMini");
                List<ConnectionsMini> matchConnectionsMiniList=new ArrayList<ConnectionsMini>();
                for(int i=0;i<matchConnectionsMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) matchConnectionsMiniArray.get(i);
                    matchConnectionsMiniList.add(JsonToBean.getConnectionsMiniBean(jsonObj));
                }
                projectBean.setListMatchConnectionsMini(matchConnectionsMiniList);
            }
            //相关的附件对象
            if(listJsonNotBank(project.get("listJTFile"))){
                JSONArray jtFileArray=project.getJSONArray("listJTFile");
                List<JTFile> jtFileList=new ArrayList<JTFile>();
                for(int i=0;i<jtFileArray.size();i++){
                    JSONObject jsonObj=(JSONObject) jtFileArray.get(i);
                    jtFileList.add(JsonToBean.getJTFileBean(jsonObj));
                }
                projectBean.setListJTFile(jtFileList);
            }

            //关联需求
            if(listJsonNotBank(project.get("listRelatedRequirementMini"))){
                JSONArray requirementMiniArray=project.getJSONArray("listRelatedRequirementMini");
                List<AffairMini> requirementMiniList=new ArrayList<AffairMini>();
                for(int i=0;i<requirementMiniArray.size();i++){
                    JSONObject jsonObject=(JSONObject) requirementMiniArray.get(i);
                    requirementMiniList.add(getAffairMiniBean(jsonObject));
                }
                projectBean.setListRelatedRequirementMini(requirementMiniList);
            }
            //相关任务
            if(listJsonNotBank(project.get("listRelatedTaskMini"))){
                JSONArray taskMiniArray=project.getJSONArray("listRelatedTaskMini");
                List<AffairMini> taskMiniList=new ArrayList<AffairMini>();
                for(int i=0;i<taskMiniArray.size();i++){
                    JSONObject jsonObject=(JSONObject) taskMiniArray.get(i);
                    taskMiniList.add(getAffairMiniBean(jsonObject));
                }
                projectBean.setListRelatedTaskMini(taskMiniList);
            }

        }

        return projectBean;
    }*/

    /*
    public static BusinessRequirement getBusinessRequirementBean(JSONObject jsonObject){
        BusinessRequirement businessRequirement=null;
        if(null!=jsonObject&&!"null".equals(jsonObject.toString())){
            businessRequirement=new BusinessRequirement();
            //需求id，发布时为空
            if(jsonObject.get("id")!=null&&strIsNotBank(jsonObject.get("id").toString())){
                businessRequirement.setId(Integer.parseInt(jsonObject.get("id").toString()));
            }
            //需求发布人id，发布时为空
            if(jsonObject.get("userid")!=null&&strIsNotBank(jsonObject.get("userid").toString())){
                businessRequirement.setUserid(Integer.parseInt(jsonObject.get("userid").toString()));
            }
            //当前进度，百分比，整型
            if(jsonObject.get("progress")!=null&&strIsNotBank(jsonObject.get("progress").toString())){
                businessRequirement.setProgress(Integer.parseInt(jsonObject.get("progress").toString()));
            }
            businessRequirement.setName(jsonObject.get("name")!=null?jsonObject.get("name").toString():"");//需求发布人姓名，发布时为空
            businessRequirement.setUserAvatar(jsonObject.get("userAvatar")!=null?jsonObject.get("userAvatar").toString():"");//需求发布人头像
            businessRequirement.setPublishTime(jsonObject.get("publishTime")!=null?jsonObject.get("publishTime").toString():"");//需求发布时间，发布时为空
            businessRequirement.setType(jsonObject.get("type")!=null?jsonObject.get("type").toString():"");//0-投资，1-融资
            businessRequirement.setTitle(jsonObject.get("title")!=null?jsonObject.get("title").toString():"");//任务标题
            businessRequirement.setContent(jsonObject.get("content")!=null?jsonObject.get("content").toString():"");//简短描述下内容
            businessRequirement.setTaskId(jsonObject.get("taskId")!=null?jsonObject.get("taskId").toString():"");//附件索引
            if(null!=jsonObject.get("principal")){
                businessRequirement.setPrincipal(getJTContactMiniBean(jsonObject.getJSONObject("principal")));//负责人
            }
            businessRequirement.setDeadline(jsonObject.get("deadline")!=null?jsonObject.get("deadline").toString():"");//完成期限

            //相关机构与联系人(相关关系)
            if(listJsonNotBank(jsonObject.get("listConnectionsMini"))){
                JSONArray connectionsMiniArray=jsonObject.getJSONArray("listConnectionsMini");
                List<ConnectionsMini> connectionsList=new ArrayList<ConnectionsMini>();
                for(int i=0;i<connectionsMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) connectionsMiniArray.get(i);
                    connectionsList.add(JsonToBean.getConnectionsMiniBean(jsonObj));
                }
                businessRequirement.setListConnectionsMini(connectionsList);
            }

            businessRequirement.setInvestKeyword(getInvestKeywordBean(jsonObject.getJSONObject("investKeyword")));

            //相关的知识
            if(listJsonNotBank(jsonObject.get("listMatchKnowledgeMini"))){
                JSONArray knowledgeMiniArray=jsonObject.getJSONArray("listMatchKnowledgeMini");
                List<KnowledgeMini> knowledgeMiniList=new ArrayList<KnowledgeMini>();
                for(int i=0;i<knowledgeMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) knowledgeMiniArray.get(i);
                    knowledgeMiniList.add(JsonToBean.getKnowledgeMiniBean(jsonObj));
                }
                businessRequirement.setListMatchKnowledgeMini(knowledgeMiniList);
            }
            //相关的需求略缩对象
            if(listJsonNotBank(jsonObject.get("listMatchRequirementMini"))){
                JSONArray matchRequirementMiniArray=jsonObject.getJSONArray("listMatchRequirementMini");
                List<RequirementMini> matchRequirementMiniArrayList=new ArrayList<RequirementMini>();
                for(int i=0;i<matchRequirementMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) matchRequirementMiniArray.get(i);
                    matchRequirementMiniArrayList.add(JsonToBean.getRequirementMiniBean(jsonObj));
                }
                businessRequirement.setListMatchRequirementMini(matchRequirementMiniArray);
            }
            //相关的关系精简对象
            if(listJsonNotBank(jsonObject.get("listMatchConnectionsMini"))){
                JSONArray matchConnectionsMiniArray=jsonObject.getJSONArray("listMatchConnectionsMini");
                List<ConnectionsMini> matchConnectionsMiniList=new ArrayList<ConnectionsMini>();
                for(int i=0;i<matchConnectionsMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) matchConnectionsMiniArray.get(i);
                    matchConnectionsMiniList.add(JsonToBean.getConnectionsMiniBean(jsonObj));
                }
                businessRequirement.setListMatchConnectionsMini(matchConnectionsMiniList);
            }
            //相关的附件对象
            if(listJsonNotBank(jsonObject.get("listJTFile"))){
                JSONArray jtFileArray=jsonObject.getJSONArray("listJTFile");
                List<JTFile> jtFileList=new ArrayList<JTFile>();
                for(int i=0;i<jtFileArray.size();i++){
                    JSONObject jsonObj=(JSONObject) jtFileArray.get(i);
                    jtFileList.add(JsonToBean.getJTFileBean(jsonObj));
                }
                businessRequirement.setListJTFile(jtFileList);
            }
            //相关任务
            if(listJsonNotBank(jsonObject.get("listRelatedTaskMini"))){
                JSONArray taskMiniArray=jsonObject.getJSONArray("listRelatedTaskMini");
                List<AffairMini> taskMiniList=new ArrayList<AffairMini>();
                for(int i=0;i<taskMiniArray.size();i++){
                    JSONObject jsonObj=(JSONObject) taskMiniArray.get(i);
                    taskMiniList.add(getAffairMiniBean(jsonObj));
                }
                businessRequirement.setListRelatedTaskMini(taskMiniList);
            }

        }
        return businessRequirement;
    }

    public static Task getTaskBean(JSONObject jsonObj){
        Task task=null;
        if(jsonObj!=null&&!"null".equals(jsonObj.toString())){
            task=new Task();
            if(jsonObj.get("id")!=null&&strIsNotBank(jsonObj.get("id").toString())){
                task.setId(Integer.parseInt(jsonObj.get("id").toString()));
            }
            if(jsonObj.get("userid")!=null&&strIsNotBank(jsonObj.get("userid").toString())){
                task.setUserid(Integer.parseInt(jsonObj.get("userid").toString()));
            }
            task.setName(jsonObj.get("name")!=null?jsonObj.get("name").toString():"");//任务发布人姓名，发布时为空
            task.setUserAvatar(jsonObj.get("userAvatar")!=null?jsonObj.get("userAvatar").toString():"");
            task.setPublishTime(jsonObj.get("publishTime")!=null?jsonObj.get("publishTime").toString():"");
            task.setTitle(jsonObj.get("title")!=null?jsonObj.get("title").toString():"");
            task.setContent(jsonObj.get("content")!=null?jsonObj.get("content").toString():"");
            task.setDeadline(jsonObj.get("deadline")!=null?jsonObj.get("deadline").toString():"");
            task.setName(jsonObj.get("name")!=null?jsonObj.get("name").toString():"");
            if(null!=jsonObj.get("principal")){
                task.setPrincipal(getJTContactMiniBean(jsonObj.getJSONObject("principal")));
            }
            //当前进度，百分比，整型
            if(jsonObj.get("progress")!=null&&strIsNotBank(jsonObj.get("progress").toString())){
                task.setProgress(Integer.parseInt(jsonObj.get("progress").toString()));
            }
            task.setInvestKeyword(getInvestKeywordBean(jsonObj.getJSONObject("investKeyword")));
            task.setTaskId(jsonObj.get("taskId")!=null?jsonObj.get("taskId").toString():"");//附件索引
            //参与人列表
            if(listJsonNotBank(jsonObj.get("listJoinJTContactMini"))){
                JSONArray joinJTContactArray=jsonObj.getJSONArray("listJoinJTContactMini");
                List<JTContactMini> joinJTContactList=new ArrayList<JTContactMini>();
                for(int i=0;i<joinJTContactArray.size();i++){
                    JSONObject jsonObject=(JSONObject) joinJTContactArray.get(i);
                    joinJTContactList.add(getJTContactMiniBean(jsonObject));
                    //	    		joinJTContactList.add(JsonToBean.getJTContactBean(jsonObject.getJSONObject("jtContact")));
                }
                task.setListJoinJTContactMini(joinJTContactList);
            }
            //相关机构与联系人(相关关系)
            if(listJsonNotBank(jsonObj.get("listConnectionsMini"))){
                JSONArray relationJTContactArray=jsonObj.getJSONArray("listConnectionsMini");
                List<ConnectionsMini> connectionsList=new ArrayList<ConnectionsMini>();
                for(int i=0;i<relationJTContactArray.size();i++){
                    JSONObject jsonObject=(JSONObject) relationJTContactArray.get(i);
                    connectionsList.add(getConnectionsMiniBean(jsonObject));
                    //	    		connectionsList.add(JsonToBean.getConnectionsBean(jsonObject.getJSONObject("connections")));
                }
                task.setListConnectionsMini(connectionsList);
            }
            //相关的附件对象
            if(listJsonNotBank(jsonObj.get("listJTFile"))){
                JSONArray jtFileArray=jsonObj.getJSONArray("listJTFile");
                List<JTFile> jtFileList=new ArrayList<JTFile>();
                for(int i=0;i<jtFileArray.size();i++){
                    JSONObject jsonObject=(JSONObject) jtFileArray.get(i);
                    jtFileList.add(JsonToBean.getJTFileBean(jsonObject));
                }
                task.setListJTFile(jtFileList);
            }
            //相关的业务需求
            if(listJsonNotBank(jsonObj.get("listRelatedBusinessRequirementMini"))){
                JSONArray requirementMiniArray=jsonObj.getJSONArray("listRelatedBusinessRequirementMini");
                List<AffairMini> listBusinessRequirement=new ArrayList<AffairMini>();
                for(int i=0;i<requirementMiniArray.size();i++){
                    JSONObject jsonObject=(JSONObject) requirementMiniArray.get(i);
                    listBusinessRequirement.add(getAffairMiniBean(jsonObject));
                }
                task.setListRelatedBusinessRequirementMini(listBusinessRequirement);
            }

            //相关的项目
            if(listJsonNotBank(jsonObj.get("listRelatedProjectMini"))){
                JSONArray projectMiniArray=jsonObj.getJSONArray("listRelatedProjectMini");
                List<AffairMini> ProjectMini=new ArrayList<AffairMini>();
                for(int i=0;i<projectMiniArray.size();i++){
                    JSONObject jsonObject=(JSONObject) projectMiniArray.get(i);
                    ProjectMini.add(getAffairMiniBean(jsonObject));
                }
                task.setListRelatedProjectMini(ProjectMini);
            }
        }
        return task;
    }*/

    //判断字符串是否空
    public static boolean strIsNotBank(String str){
        boolean boo=true;
        if(null==str||"".equals(str)){
            boo= false;
        }
        return boo;
    }

    public static boolean listJsonNotBank(Object jsonObj){
        boolean boo=false;
        if(null!=jsonObj&&!"[]".equals(jsonObj.toString())&&!"null".equals(jsonObj.toString())){
            boo=true;
        }

        return boo;
    }

    public static JTFile fileIndexToJTfile(FileIndex fileIndex){
        JTFile jtFile=new JTFile();
        if(null!=fileIndex){
//			fileIndex.get
            jtFile.setId(fileIndex.getId());
            jtFile.setFileName(fileIndex.getFileTitle());
            jtFile.setFileSize(fileIndex.getFileSize());
            jtFile.setModuleType(fileIndex.getModuleType()+"");
            String[] nameSplit=fileIndex.getFileTitle().split("\\.");
            jtFile.setSuffixName(nameSplit.length>0?nameSplit[nameSplit.length-1]:"");
            jtFile.setTaskId(fileIndex.getTaskId());
            ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
            String nginxRoot=resourceBundle.getString("nginx.root");
            jtFile.setUrl(nginxRoot+"/mobile/download?id="+fileIndex.getId());
        }
        return jtFile;
    }

    /*
    public static JTFile caseFileToJTfile(CaseFile fileIndex,String fileType){
        JTFile jtFile=new JTFile();
        if(null!=fileIndex){
//			fileIndex.get
            jtFile.setId(fileIndex.getId()+"");
            jtFile.setFileName(fileIndex.getFileRealName());
            jtFile.setFileSize(fileIndex.getFileSize());
            String[] nameSplit=fileIndex.getFileRealName().split("\\.");
            jtFile.setSuffixName(nameSplit.length>0?nameSplit[nameSplit.length-1]:"");
            jtFile.setTaskId(fileIndex.getTaskId());
            ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
            String nginxRoot=resourceBundle.getString("nginx.root");
            jtFile.setUrl(nginxRoot+"/mobile/download?id="+fileIndex.getId());
            jtFile.setType(fileType);
        }
        return jtFile;
    }


    public static JTFile fileIndexToJTfile(FileIndex fileIndex){
        JTFile jtFile=new JTFile();
        if(null!=fileIndex){
//			fileIndex.get.
            jtFile.setId(fileIndex.getId());
            jtFile.setFileName(fileIndex.getFileTitle());
            jtFile.setFileSize(fileIndex.getFileSize());
            jtFile.setModuleType(fileIndex.getModuleType()+"");
            String[] nameSplit=fileIndex.getFileTitle().split("\\.");
            jtFile.setSuffixName(nameSplit.length>0?nameSplit[nameSplit.length-1]:"");
            jtFile.setTaskId(fileIndex.getTaskId());
            ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
            String nginxRoot=resourceBundle.getString("nginx.root");
            jtFile.setUrl(nginxRoot+"/mobile/download?id="+fileIndex.getId());
        }
        return jtFile;
    }*/

    /**
     * 人脉对象翻译成关系mini对象
     * @param businessPeople
     * @return
     *
    public static ConnectionsMini businessPeopleToConnectionsMini(BusinessPeople businessPeople){
        ConnectionsMini connectionsMini=null;
        if(null!=businessPeople){
            connectionsMini=new ConnectionsMini();
            connectionsMini.setId(new Long(businessPeople.getId()).intValue());
            connectionsMini.setName(businessPeople.getName());
            //头像在controller层加
            connectionsMini.setType(1);
        }
        return connectionsMini;
    }*/

    /**
     * 客户对象翻译成关系mini对象
     * @param businessCustomer
     * @return
     *
    public static ConnectionsMini businessCustomerToConnectionsMini(BusinessCustomer businessCustomer){
        ConnectionsMini connectionsMini=null;
        if(null!=businessCustomer){
            connectionsMini=new ConnectionsMini();
            connectionsMini.setId(new Long(businessCustomer.getId()).intValue());
            connectionsMini.setName(businessCustomer.getName());
            connectionsMini.setType(2);
        }
        return connectionsMini;
    }*/
}
