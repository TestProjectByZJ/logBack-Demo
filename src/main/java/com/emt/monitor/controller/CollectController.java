package com.emt.monitor.controller;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.emt.monitor.StaticStr.StaticStr;
import com.emt.monitor.service.CollectService;
import com.emt.monitor.util.MonitorSignUtils;
import com.emt.monitor.util.StaticUtils;


@RestController
@RequestMapping(value = "/collect" ,method = RequestMethod.POST)
public class CollectController {
	
//	@Autowired
//	private CollectService collectService;
	
	private static final Logger logger = LoggerFactory.getLogger(CollectController.class);
	
	@RequestMapping("/getInfo")
    public JSONObject getInfo(@RequestBody String str) {
		logger.info("getInfo");
        return MonitorSignUtils.reJson(false, "方法名错误！");
    }
	@RequestMapping("/getError")
	public JSONObject getError(@RequestBody String str) {
		logger.info("getError");
		return MonitorSignUtils.reJson(false, "方法名错误！");
	}
	
	@RequestMapping("/getException")
    public JSONObject getException(@RequestBody String str) {
		System.out.println(1/0);
        return MonitorSignUtils.reJson(false, "方法名错误！");
    }
	
//	@RequestMapping("/{method}")
//    public JSONObject chooseMethod(@PathVariable String method, @RequestBody String str) {
//		//加密验证
//		if(!MonitorSignUtils.signCheck(str)){
//			logger.info("报文检查错误!");
//			return MonitorSignUtils.reJson(false, "报文检查错误！");
//		}
//		//采集IP验证
//		if(!StaticUtils.getProperty("staticIP").equals(str.substring(str.indexOf("|")+1))){
//			logger.info("采集IP错误！");
//			return MonitorSignUtils.reJson(false, "采集IP错误！");
//		}
//		JSONObject jsonObject = null;
//        switch (method) {
//			case StaticStr.COLLECTIONCPU:
//				jsonObject = getCpuRatio();
//				break;
//			default:
//				break;
//		}
//        if(jsonObject!=null){
//        	return jsonObject;
//        }
//        logger.info("方法名错误！");
//        return MonitorSignUtils.reJson(false, "方法名错误！");
//    }
	
//	public JSONObject getCpuRatio(){
//		try {
//			double res = collectService.getCpuRatio();
//			if(res==0){
//				logger.info("采集发生错误！");
//				return MonitorSignUtils.reJson(false, "采集发生错误！");
//			}
//			DecimalFormat format = new DecimalFormat("######0.00");
//			logger.info("CPU使用率：" + format.format(res));
//			return MonitorSignUtils.reJson(true,format.format(res));
//		} catch (Exception e) {
//			logger.info("采集发生错误！");
//			return MonitorSignUtils.reJson(false, "采集发生错误！");
//		}
//	}
	
	
}
