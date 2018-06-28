package com.emt.monitor.filter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LogInterceptor implements HandlerInterceptor {
	    
	    private final static String SESSION_TOKEN_KEY = "sessionTokenId";
	    
	    private final static String PROJECT_NAME = "projectName";
	    
	    
	    @Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
	        String token = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	        MDC.put(SESSION_TOKEN_KEY, token);
	        //项目名
	        MDC.put(PROJECT_NAME, "wechat");
	        return true;
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
			MDC.remove(SESSION_TOKEN_KEY);
			MDC.remove(PROJECT_NAME);
		}

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) throws Exception {
	        MDC.remove(SESSION_TOKEN_KEY);
	        MDC.remove(PROJECT_NAME);
		}

	    
}
