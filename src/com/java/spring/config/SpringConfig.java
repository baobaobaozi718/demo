package com.java.spring.config;

import java.lang.annotation.Annotation;

import com.java.spring.annotation.ComponentScan;
import com.java.spring.annotation.Service;
@Service
@ComponentScan("com.java.spring.service")//包扫描
public class SpringConfig {
	public static void main(String[] args) throws ClassNotFoundException {
		Class<?> c1 = Class.forName("com.java.spring.config.SpringConfig");
		Class<?> c2=SpringConfig.class;
		Class<?> c3=new SpringConfig().getClass();
		ComponentScan declaredAnnotation = c1.getDeclaredAnnotation(ComponentScan.class);
		String value = declaredAnnotation.value();
		System.out.println(value);
//		Annotation[] declaredAnnotations = c1.getDeclaredAnnotations();
//		for (Annotation annotation : declaredAnnotations) {
////			System.out.println(annotation);
//			
//			 if (annotation instanceof ComponentScan) {
//			 
//				System.out.println(222);
//			}else {
//				System.out.println(333);
//			}
//		}
	}

}
