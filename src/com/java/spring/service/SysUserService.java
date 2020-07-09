package com.java.spring.service;

import com.java.spring.annotation.Lazy;
import com.java.spring.annotation.Service;

@Lazy(false)
@Service("sysUserService")
public class SysUserService {

	public SysUserService() {
		System.out.println("SysUserService()");
	}
}
