package com.ginkgocap.ywxt.knowledge.model;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gintong on 9/19/17.
 */
public class BusinessTrackLog {
    public BusinessTrackLog(Logger logger, Logger trackLogger, int businessModel, int modelFunction, int optType, long knowledgeId, long userId, String userName, HttpServletRequest request) {
        this.logger = logger;
        this.trackLogger = trackLogger;
        this.businessModel = businessModel;
        this.modelFunction = modelFunction;
        this.optType = optType;
        this.knowledgeId = knowledgeId;
        this.userId = userId;
        this.userName = userName;
        this.request = request;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getTrackLogger() {
        return trackLogger;
    }

    public void setTrackLogger(Logger trackLogger) {
        this.trackLogger = trackLogger;
    }

    public int getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(int businessModel) {
        this.businessModel = businessModel;
    }

    public int getModelFunction() {
        return modelFunction;
    }

    public void setModelFunction(int modelFunction) {
        this.modelFunction = modelFunction;
    }

    public int getOptType() {
        return optType;
    }

    public void setOptType(int optType) {
        this.optType = optType;
    }

    public long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private Logger logger;
    private Logger trackLogger;
    private int businessModel;
    private int modelFunction;
    private int optType;
    private long knowledgeId;
    private long userId;
    private String userName;
    private HttpServletRequest request;
}
