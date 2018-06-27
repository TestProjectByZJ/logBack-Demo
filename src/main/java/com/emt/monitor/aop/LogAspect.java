package com.emt.monitor.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.emt.monitor.util.GUID;

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
    	String guid = GUID.get32UUID();
    	Object obj = new Object();
        try {
            logger.info(guid+"|"+proceedingJoinPoint.getSignature().getName() + "|start:开始..........");
            obj = proceedingJoinPoint.proceed();
//            logger.info(guid + "|" + proceedingJoinPoint.getSignature().getName() + "|end:结束............");
            return obj;
        } catch (Throwable throwable) {
        	throwable.printStackTrace();
        	return obj; 
//            logger.error(guid + "|" + proceedingJoinPoint.getSignature().getName() + "|error:" + throwable);
        }finally{
        	logger.info((new StringBuilder()).append("END:"+guid+"|").append(proceedingJoinPoint.getSignature().getName()).toString());
        }
		
    }


}

