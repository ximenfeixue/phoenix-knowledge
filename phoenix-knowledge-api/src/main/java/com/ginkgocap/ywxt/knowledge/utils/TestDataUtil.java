package com.ginkgocap.ywxt.knowledge.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Chen Peifeng on 2016/1/23.
 */
public class TestDataUtil {
    public static String jsonPath = null;
    private static ObjectMapper objectMapper = null;
    static {
        if (TestDataUtil.jsonPath == null) {
            TestDataUtil.jsonPath = KnowledgeUtil.defaultJsonPath();
        }
        objectMapper = new ObjectMapper();
    }

	public static String getJsonFile(String fileName)
    {
        return jsonPath + fileName + ".json";
    }
	
    public static String writeJsonData(Object jsonObject) throws IOException
    {
        String jsonContent = null;

        try {
        	String jsonFile = getJsonFile(jsonObject.getClass().getSimpleName());
            objectMapper.setFilters(KnowledgeUtil.assoSimpleFilterProvider(null));
            jsonContent = objectMapper.writeValueAsString(jsonObject);
            System.out.print(jsonContent);
            //objectMapper.writeValue(new File(jsonFile), jsonContent);
            writeToFile(jsonFile, jsonContent);
            //Add json to API document
            //appendJson(jsonObject,jsonContent);
            //End
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonContent;
    }

    private static String jsonNode(String jsonNode)
    {
        return String.format("\"%s\"",jsonNode);
    }

    public static String getJsonContentFromFile(String filePath)
    {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println(filePath + " not exist!");
                return null;
            }
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                buffer.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return buffer.toString();
    }

	public static void writeToFile(String jsonFile, String jsonContent) {
		BufferedWriter bw = null;
		try {
			File file = new File(jsonFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(jsonContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
    
    public static void initBaseData(KnowledgeDetail knowledgeDetail)
    {
        //System.out.println("DemandType： "+knowledgeType);
        //demandBase.setId(1234L);
        knowledgeDetail.setColumnId((short)2);
        knowledgeDetail.setTitle("title1");
        knowledgeDetail.setContent("note");

        knowledgeDetail.setOwnerId(122344L);
        knowledgeDetail.setOwnerName("Jerry");
        knowledgeDetail.setCreateTime(System.currentTimeMillis());
        //knowledgeDetail.setDemandType(Long.valueOf(knowledgeType.getTypeId()));
        //knowledgeDetail.setContact("Jerry");
        knowledgeDetail.setPhone(18611386868L);
        knowledgeDetail.setTags("122,1223,345");
        knowledgeDetail.setCategoryIds("122,1223,345");
        knowledgeDetail.setMultiUrls(getImgVideos());
        knowledgeDetail.setAttachmentUrls(getAttachFiles());
        //demandBase.setPermisson(getPermission());
    }

    public static List<String> getImgVideos()
    {
        List<String> imgVideos = new ArrayList<String>(3);
        imgVideos.add("http://www.w3school.com.cn/html5/html5_video.asp");
        imgVideos.add("http://www.w3school.com.cn/html5/att_video_autoplay.asp");
        imgVideos.add("http://www.w3school.com.cn/tags/tag_video.asp");
        return imgVideos;
    }

    public static List<String> getAttachFiles()
    {
        List<String> attachFiles = new ArrayList<String>(3);
        attachFiles.add("http://blog.chinaunix.net/uid-20761674-id-3896051.html");
        attachFiles.add("http://blog.chinaunix.net/uid/20761674/cid-175346-list-1.html");
        attachFiles.add("http://blog.chinaunix.net/uid-20761674-id-3888951.html");
        return attachFiles;
    }

    /*
    public static Property getType(DemandServicesType knowledgeType)
    {
    	return getType(knowledgeType,0);
    }
    
    public static Property getType(DemandServicesType knowledgeType,int seq)
    {
        switch(knowledgeType)
        {	
	        case DEMAND_EDUCATION_FINDTEACHER: return new Property("12-121", "艺术培训");
	        case DEMAND_EDUCATION_CONSULT: return new Property("12-121", "互联网");
	        case DEMAND_LAW_LAWYER: return new Property("12-11", "合同纠纷");
	        case DEMAND_LAW_CONSULT: return new Property("12-11", "知识产权");
	        case DEMAND_HR_TRAIN: return new Property("12-1551", "互联网");
        	case DEMAND_PRODUCT_PROJECT: return new Property("12-11", "数学");
        	case DEMAND_PRODUCT_OUTSOURCE: return new Property("12-112", "人力资源 外包");
            case SERVICE_EDUCATION_FINDTEACHER:
            case SERVICE_EDUCATION_CONSULT: return new Property("12-11", "数学");
            case SERVICE_LAW_LAWYER:
            case SERVICE_LAW_CONSULT: return new Property("12-13", "知识产权");
            case DEMAND_HR_EMPLOY: {
            	if(seq==1) {
            		return new Property("122-18", "研究生学历");
            	}
            	if(seq==2) {
            		return new Property("162-18", "5-8年");
            	}
            }
            default: return new Property("", "");
        }
    }

    public static List<Property> getIndustries()
    {
        List<Property> industries = new ArrayList<Property>(3);
        industries.add(new Property("1213","industry1"));
        industries.add(new Property("1214","industry2"));
        industries.add(new Property("1215","industry3"));
        return industries;
    }

    public static List<Property> getAreas()
    {
        List<Property> areas = new ArrayList<Property>(3);
        areas.add(new Property("010","北京市"));
        areas.add(new Property("020","上海市"));
        areas.add(new Property("021","天津市"));
        return areas;
    }

    public static Area getArea()
    {
        Area area = new Area();
        area.setProvince(new Property("037","河南省"));
        area.setCity(new Property("0371","郑州市"));
        area.setDistrict(new Property("0371-1","中原区"));
        return area;
    }


    public static DemandAmount getDemandAmount()
    {
        return new DemandAmount(100, 1000, "112-222", "CNY");
    }

    public static Amount getAmount()
    {
        return getAmount(DemandServicesType.UNKNOWN_DEMAND, 0);
    }
    
    public static Amount getAmount(DemandServicesType knowledgeType,int seq)
    {
    	switch (knowledgeType) {
            case DEMAND_HR_EMPLOY: return new Amount(20000L,"人/元", "CNY");
    		case DEMAND_PRODUCT_PROJECT: {
    			if (seq==1)return new Amount(200L,"Day", "天");
    			else if(seq==2) return new Amount(200L,"CNY", "元");
    		}
    		case DEMAND_PRODUCT_OUTSOURCE: return new Amount(200L,"CNY/Hour", "元");
    		default: return new Amount(200L,"Hour/CNY", "CNY");
    	}
    }

    public static Property getIndustry(DemandServicesType knowledgeType)
    {
        switch (knowledgeType) {
            case SERVICE_PRODUCT_PROJECT:
            case SERVICE_PRODUCT_OUTSOURCE: return new Property("12-13", "制造业");
            case SERVICE_HR_TRAIN: return new Property("12-17", "金融业");
            default: return new Property("", "");
        }
    }

    public static Function getFunction(DemandServicesType knowledgeType)
    {
        switch(knowledgeType)
        {
            case DEMAND_FINANCE_INVESTMENT:
            case DEMAND_FINANCE_FINANCING:
            {
                return new Function(new Property("11-341","互联网"), new Property("11-221","移动端"));
            }
            case DEMAND_PRODUCT_PROJECT:
            case DEMAND_PRODUCT_OUTSOURCE:
            {
                return new Function(new Property("11-341","房地产"), new Property("11-221","商业地产"));
            }
            case DEMAND_HR_EMPLOY:
            case SERVICE_HR_EMPLOY:
            case SERVICE_HR_TRAIN:
            {
                Function function = new Function();
                function.setIndustry(new Property("11-11","互联网"));
                function.setCatagory(new Property("11-11-1","技术总监"));
                return function;
            }
            case SERVICE_FINANCE_FINANCING:
            case SERVICE_FINANCE_INVESTMENT:
            {
                return new Function(new Property("11-3413","金融投资"), new Property("11-221","证券"));
            }
            case SERVICE_PRODUCT_PROJECT:
            {
                return new Function(new Property("11-3413","房地产"), new Property("11-221","装修"));
            }
            case SERVICE_PRODUCT_OUTSOURCE:
            {
                return new Function(new Property("11-3413","人力资源"), new Property("11-221","保安"));
            }
            default: return null;
        }
    }

    public static Property getDepartment(DemandServicesType knowledgeType)
    {
        switch (knowledgeType) {
	        case DEMAND_MEDICAL_FINDDOCTOR: return new Property("12-19", "牙科");
	        case DEMAND_MEDICAL_CONSULT: return new Property("12-19", "内科");
            case SERVICE_MEDICAL_FINDDOCTOR: return new Property("12-19", "儿科");
            case SERVICE_MEDICAL_CONSULT: return new Property("12-22", "耳鼻喉科");
            default: return new Property("", "");
        }
    }

    public static String convertObjectToString(Object object)
    {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PermissionContent getPermission()
    {
        PermissionContent permission = null;
        try {
            permission = objectMapper.readValue(permissionContent(), PermissionContent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return permission;
    }*/

    private static String getPermissionContent()
    {
        return jsonNode(Constant.JsonNode.Permission) + ":" + permissionContent();
    }

    private static String permissionContent()
    {
        return "{\"joins\":[{\"id\":9,\"type\":0,\"name\":\"三腾\"}],\"views\":[{\"id\":7,\"type\":0,\"name\":\"金桐脑\"}],\"shares\":[{\"id\":5,\"type\":0,\"name\":\"金桐\"}]}";
    }

    private static String getAssoContent()
    {
        return jsonNode(Constant.JsonNode.Asso) + ":" + assoContent();
    }

    private static String assoContent()
    {
        return "{\"assoId\":1,\"isVisable\":\"1\",\"ownerId\":\"10001\",\"type\":\"2\",\"value\":{\"r\":[{\"assoConnTagId\":5,\"assoId\":1,\"conn\":[{\"assoConnTagId\":5,\"assoId\":2,\"assoValueId\":25,\"businessId\":\"111115\",\"picPath\":\"\",\"title\":\"测试11115\",\"type\":\"r\"}],\"tag\":\"测试k1\",\"type\":\"r\"}],\"p\":[{\"assoConnTagId\":4,\"assoId\":1,\"conn\":[{\"assoConnTagId\":4,\"assoId\":2,\"assoValueId\":24,\"businessId\":\"111114\",\"picPath\":\"\",\"title\":\"测试11114\",\"type\":\"p\"}],\"tag\":\"测试k1\",\"type\":\"p\"}],\"o\":[{\"assoConnTagId\":3,\"assoId\":1,\"conn\":[{\"assoConnTagId\":3,\"assoId\":2,\"assoValueId\":23,\"businessId\":\"111113\",\"picPath\":\"\",\"title\":\"测试11113\",\"type\":\"o\"}],\"tag\":\"测试k1\",\"type\":\"o\"}],\"k\":[{\"assoConnTagId\":1,\"assoId\":1,\"conn\":[{\"assoConnTagId\":1,\"assoId\":2,\"assoValueId\":21,\"businessId\":\"111111\",\"picPath\":\"\",\"title\":\"测试11111\",\"type\":\"k\"},{\"assoConnTagId\":1,\"assoId\":2,\"assoValueId\":211,\"businessId\":\"111111\",\"picPath\":\"\",\"title\":\"测试11111\",\"type\":\"k\"}],\"tag\":\"测试k1\",\"type\":\"k\"},{\"assoConnTagId\":2,\"assoId\":1,\"conn\":[{\"assoConnTagId\":2,\"assoId\":2,\"assoValueId\":22,\"businessId\":\"111112\",\"picPath\":\"\",\"title\":\"测试11112\",\"type\":\"k\"}],\"tag\":\"测试k2\",\"type\":\"k\"}]}}";
    }

    //This is just for generate API document
    private static StringBuffer jsonBuffer = new StringBuffer();
    private static Map<Long,String> knowledgeMap = new HashMap<Long,String>(24);
    public static String allJson() { return jsonBuffer.toString(); }

    public static void appendJson(KnowledgeDetail knowledgeDetail,String jsonContent)
    {
        if (knowledgeMap.size() <= 0) {
            //initDemandMap();
        }
        //jsonBuffer.append(knowledgeMap.get(demandBase.getDemandType())+"\r\n"+jsonContent+"\r\n\r\n");
    }
    //End
}
