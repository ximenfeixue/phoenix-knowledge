package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;

import junit.framework.TestCase;

/**
 * Created by Chen Peifeng on 2016/2/16.
 */
public abstract class BaseTestCase extends TestCase
{
    protected static boolean noTestHost = false;
    protected static boolean debugModel = false;
    protected static boolean runTestCase = false;
    protected static long userId;
    protected static String hostUrl = null;
    protected static SimpleFilterProvider assofilterProvider = null;
    private static String testEnv = "testOnline";
    //private static String testEnv = "dev";
    //private static String testEnv = "local";
    
    static {
        //-DdebugModel=true -DrunTestCase=true -DhostUrl=http://192.168.120.135:8080
        userId = KnowledgeUtil.getDummyUser().getId();
        debugModel = System.getProperty("debugModel", "false").equals("true");
        runTestCase = System.getProperty("runTestCase", "false").equals("true");
        if ("local".equals(testEnv)) {
            Util.loginUrl = "http://dev.gintong.com/cross/login/loginConfiguration.json";
            hostUrl = System.getProperty("hostUrl", "http://localhost:8080");
        }
        else if ("dev".equals(testEnv)) {
            Util.loginUrl = "http://dev.gintong.com/cross/login/loginConfiguration.json";
            hostUrl = System.getProperty("hostUrl", "http://192.168.120.135:8080");
            
        }
        else if ("testOnline".equals(testEnv)) {
            Util.loginUrl = "http://test.online.gintong.com/cross/login/loginConfiguration.json";
            //hostUrl = System.getProperty("hostUrl", "http://test.online.gintong.com/knowledge");
            //hostUrl = System.getProperty("hostUrl", "http://192.168.101.131:3017");
            hostUrl = System.getProperty("hostUrl", "http://192.168.130.103:8080");
        }
        assofilterProvider = KnowledgeUtil.assoFilterProvider(Associate.class.getName());
    }

    @Override
    protected void runTest() throws Throwable
    {
        if (runTestCase) {
            super.runTest();
        }
    }

    //For if no webservice opened skip this test case and package success
    protected void writeException(Exception e)
    {
        if (e.getMessage().contains("Connection refused")) {
            noTestHost = true;
            System.err.println("No opened web service for test....");
        }
        System.err.println("Exception: " + e.getMessage());
        //e.printStackTrace();
    }

    protected void tryFail()
    {
        if (!noTestHost) {
            fail();
        }
    }

    protected void checkResult(JsonNode notifNode)
    {
        if (!noTestHost) {
            Util.checkRequestResultSuccess(notifNode);
        }
    }

    protected void checkResultWithData(JsonNode notifNode)
    {
        if (!noTestHost) {
            Util.checkResponseWithData(notifNode);
        }
    }
    
    protected void checkResultFail(JsonNode notifNode)
    {
        if (!noTestHost) {
            Util.checkRequestResultHaveError(notifNode);
        }
    }
    
    protected void LOG(String logMesage)
    {
    	if (debugModel) {
	    	//Just for show the message
	    	System.err.println("======= "+logMesage+" ========");
    	}
    }

    protected String LogMethod()
    {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (debugModel) {
            System.out.println("======= "+methodName+" ========");
        }
        return methodName;
    }
    //end
}
