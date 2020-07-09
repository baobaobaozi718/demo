package com.java.spring.service;

import com.java.spring.annotation.Lazy;
import com.java.spring.annotation.Service;

@Lazy(true)//懒加载
@Service("sysLogService")
public class SysLogService {
	public SysLogService() {
		System.out.println("SysLogService()");
	}
}
