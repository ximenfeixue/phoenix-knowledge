package com.ginkgocap.ywxt.knowledge.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;

import java.io.*;

public final class DataUtil {

	 public static String jsonPath = null;
	    private static ObjectMapper objectMapper = null;
	    static {
	        if (DataUtil.jsonPath == null) {
	            DataUtil.jsonPath = defaultJsonPath();
	        }
	        objectMapper = new ObjectMapper();
	    }

		public static String getJsonFile(String fileName)
	    {
	        return jsonPath + fileName + ".json";
	    }
		
	    public static void writeJsonData(KnowledgeReference reference) throws IOException
	    {
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	        	String jsonFile = getJsonFile(reference.getClass().getSimpleName());
	            String jsonContent = objectMapper.writeValueAsString(reference);
	            System.out.print(jsonContent);
	            //objectMapper.writeValue(new File(jsonFile), jsonContent);
	            writeToFile(jsonFile, jsonContent);
	            //Add json to API document
	            //appendJson(reference,jsonContent);
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
		
    public static String defaultJsonPath()
    {
        String defaultJsonPath = null;
        String usrHome = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        defaultJsonPath = new StringBuffer().append(usrHome)
                                            .append(fileSeparator)
                                            .append("JSON")
                                            .append(fileSeparator).toString();
        File file = new File(defaultJsonPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return defaultJsonPath;
    }
		
	public static KnowledgeReference generateKnowledge()
	{
		KnowledgeReference reference = new KnowledgeReference();
		reference.setArticleName("KnowledgeTitle");
		reference.setCreateDate(System.currentTimeMillis());
		reference.setModifyDate(System.currentTimeMillis());
		
		return reference;
	}
}
