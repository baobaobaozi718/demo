package com.java.spring.factory;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.java.spring.annotation.ComponentScan;
import com.java.spring.annotation.Lazy;
import com.java.spring.annotation.Service;
import com.java.spring.config.SpringConfig;
import com.java.spring.service.SysLogService;
import com.java.spring.service.SysMenuService;
import com.java.spring.vo.BeanDefinition;

public class AnnotationConfigApplicationContext {
	private Map<String,Object> instanceMap=new HashMap<>();//id--就是xxxService上的@Service注解后的字符串      对象--xxxService对象
	
	
	//beanMap放的 是类的定义信息
	private Map<String,BeanDefinition> beanMap=new HashMap<>();//id   v---xxxService/XXXController的包名.类名
	public AnnotationConfigApplicationContext(Class<?> configCls)throws Exception {
		//1.configCls----SpringConfig.class
		ComponentScan cs=configCls.getDeclaredAnnotation(ComponentScan.class);
		String pkg=cs.value();
		//pkg---->com.java.spring.service
		String classPath=pkg.replace(".", "/");
//		classPath---->com/java/spring/service   new File("磁盘中的路径/文件");
		//@SuppressWarnings("static-access")
		
		
		@SuppressWarnings("static-access")//压制警告
		URL url=configCls.getClassLoader().getSystemResource(classPath);
		File fileDir=new File(url.getPath());
		File[] listFiles = fileDir.listFiles();//列出所有service文件夹下所有的文件以及文件夹
		for (File file : listFiles) {
			boolean flag = file.isDirectory();//判断是否为文件夹
			if(flag) {
				file.listFiles();
			}
			
		}
		File[] classFile=fileDir.listFiles(new FileFilter() {//过滤
			@Override
			public boolean accept(File file) {
				return file.isFile()&&file.getName().endsWith(".class");//文件的后缀
			}
		});
		//3
		processClassFiles(pkg,classFile);
	}
	private void processClassFiles(String pkg,File[] classFiles)throws Exception{
//		pkg---->com.java.spring.service    classFiles---->SysLogService.class/SysxxxService.class
		//Class.forName("包名.类名");
		for(File f:classFiles){
			System.out.println("文件的名字"+f.getName());//SysLogService.class
			System.out.println( f.getName().lastIndexOf(".")); 
			//Class.forname("包名.类名")  --->com.java.spring.service.SysLogService
			String pkgClass=pkg+"."+f.getName().substring(0,f.getName().lastIndexOf("."));

			Class<?> targetCls=Class.forName(pkgClass);//SysLogService.class   SysxxxService.class
			Service service=
					targetCls.getDeclaredAnnotation(Service.class);
			if(service==null) {
				continue;
			}else {
				BeanDefinition bd=new BeanDefinition();
				bd.setId(service.value());//sysLogService   sysxxxService
				bd.setPkgClass(pkgClass);
				Lazy lazy=
						targetCls.getDeclaredAnnotation(Lazy.class);
				if(lazy!=null){
					bd.setLazy(lazy.value());
				}
				beanMap.put(bd.getId(),bd);
				if(!bd.getLazy()){
					Object obj=newBeanInstance(targetCls);//targetCls---->SysLogService.class
					//newBeanInstance方法是用来创建一个类的对象
					instanceMap.put(bd.getId(), obj);
				}
			}
		}
	}
	public Object newBeanInstance(Class<?> targetCls)throws Exception{
		
		Constructor<?> con=targetCls.getDeclaredConstructor();
		con.setAccessible(true);
		return con.newInstance();
	}
	@SuppressWarnings("unchecked")
	public <T>T getBean(String key,Class<T> cls)throws Exception{
		Object obj=instanceMap.get(key);
		if(obj!=null)return (T)obj;
		obj=newBeanInstance(cls);//懒加载
		instanceMap.put(key, obj);
		return (T)obj;
	}
	public static void main(String[] args)throws Exception {
		AnnotationConfigApplicationContext ctx=
				new AnnotationConfigApplicationContext(SpringConfig.class);
		SysLogService logService=
				ctx.getBean("sysLogService",SysLogService.class);
		SysMenuService sysMenuService =ctx.getBean("sysMenuService", SysMenuService.class);
		System.out.println("sysMenuService="+sysMenuService);
	}
}
