package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;

/**
 * Created by Chen Peifeng on 2016/2/16.
 */
public abstract class BaseTestCase extends TestCase
{
    protected static boolean noTestHost = false;
    protected static boolean debugModel = false;
    protected static boolean skipTestCase = false;

    @Override
    protected void runTest() throws Throwable
    {
        if (!skipTestCase) {
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
	    	System.err.println(logMesage+"\r\n");
    	}
    }
    //end
}
