package com.emt.monitor.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.emt.monitor.util.GUID;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;


@Aspect
@Configuration
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.emt.monitor.controller.*.*(..))")
    public void executeService() {
    }


    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
    	Object obj = new Object();
    	StringWriter sw = new StringWriter();
        try {
            logger.info("START|" + proceedingJoinPoint.getSignature().getName() + "|(后面字段无要求，按照个人需求添加)请求开始XXXX..........");
            obj = proceedingJoinPoint.proceed();
            return obj;
        } catch (Throwable throwable) {
        	throwable.printStackTrace(new PrintWriter(sw, true));
        	logger.info("\n" + sw.toString());
        	return JSONObject.toJSON(obj); 
        }finally{
        	logger.info("END|" + proceedingJoinPoint.getSignature().getName() + "|(后面字段无要求，按照个人需求添加)请求结束XXXXX..........");
        }
		
    }
    
    public void returnHandler(Object name) { //在返回通知中获取返回值
        System.out.println("返回通知:" + name);
    }


}

