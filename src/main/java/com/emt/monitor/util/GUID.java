package com.emt.monitor.util;

import java.util.Random;
import java.util.UUID;

/**
 * GUID生产类
 * @author demo
 *
 */
public class GUID {
	
	/**
	 * 获取42位不重复的主键UUID（无-）
	 * @return
	 */
	public static String getUUID() {
		Random ra =new Random();
		return UUID.randomUUID().toString().toUpperCase()+"-"+(ra.nextInt(8)+1)*10000;
	}
	
	/**
	 * 获取36位不重复的主键UUID
	 * @return
	 */
	public static String get36UUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	/**
	 * 获取32位不重复的主键UUID
	 * @return
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().toUpperCase().replace("-", "");
	}

	/**
	 * 获取50位不重复的主键UUID
	 * @return
	 */
	public static String getGuid(){
		String guid = UUID.randomUUID().toString() +"-"+UUID.randomUUID().toString().substring(0, 13);
		return guid.toUpperCase().replaceAll("-", "0");
	}
	
	public static String getRandomId(){
		Random r = new Random();
		String str = System.currentTimeMillis()+""+r.nextLong();
		if(str.length()>32){
			return str.substring(0, 31);
		}
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(getGuid().length());
		System.out.println(GUID.get32UUID());
	}
}
