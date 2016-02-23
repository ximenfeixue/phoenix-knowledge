package com.ginkgocap.ywxt.knowledge.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppFilter implements Filter {
	
	private Logger logger = LoggerFactory.getLogger(AppFilter.class);
	
	private static final String Content_Type = "application/x-www-form-urlencoded";
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		//HttpServletResponse res = (HttpServletResponse) response;
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}
}