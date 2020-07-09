package com.java.spring.factory;

public class SysLogController {
	
	SysLogService s=new SysLogService();
	public String find() {
		return s.findObjects();
	}
	
	public static void main(String[] args) {
		SysLogController con=new SysLogController();
		con.find();
	}

}
class SysLogService{
	public String findObjects() {
		//有一万行代码
		return "ddd";
	}
}