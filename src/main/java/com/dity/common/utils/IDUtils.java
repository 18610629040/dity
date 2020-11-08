package com.dity.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IDUtils {

	private static byte[] lock = new byte[0];
	 
	// 位数，默认是8位
	private final static long w = 1000;
 
	public static String createID() {
		long r = 0;
		synchronized (lock) {
			r = (long) ((Math.random() + 1) * w);
		}
 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date()) + String.valueOf(r);
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(createID());
		}
	}
}
