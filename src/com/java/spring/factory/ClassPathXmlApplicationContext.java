package com.java.spring.factory;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.java.spring.vo.BeanDefinition;

public class ClassPathXmlApplicationContext {
	/**��������������map������bean��������������*/
    private Map<String,Object> instanceMap=new HashMap<>();
	/**��������map�������������������������������������������bean�����������������������������������*/
	private Map<String,BeanDefinition> beanMap=new HashMap<>();
	public ClassPathXmlApplicationContext(String file)throws Exception {
         //1.����������������������������
		ClassLoader cl=getClass().getClassLoader();
		System.out.println(cl);
		InputStream in=cl.getResourceAsStream(file);
		 
		 //2.����������������������������������������
		 parse(in);
	}
	/**������������xml��������������������������dom��������
	 * ��������������������xml������������������dom,dom4j,sax,pull,...*/
	private void parse(InputStream in)throws Exception{
		//1.������������������������������������������(�����������������xml��������������������)
		DocumentBuilder builder=
		DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    //2.������������������������������
		Document doc=builder.parse(in);
		//DOM DOM4J SAX
		//3.������������document
		processDocument(doc);
	}
	private void processDocument(Document doc)throws Exception{
		//1.��������������������bean��������
		NodeList list=doc.getElementsByTagName("bean");
		//2.������������bean��������,������������������������������������������������
		for(int i=0;i<list.getLength();i++){
			Node node=list.item(i);//bean
			//��������node�������������������������BeanDefinition������������
			BeanDefinition bd=new BeanDefinition();//vo
			NamedNodeMap nMap=node.getAttributes();
			bd.setId(nMap.getNamedItem("id").getNodeValue());
			bd.setPkgClass(nMap.getNamedItem("class").getNodeValue());
			bd.setLazy(Boolean.valueOf(nMap.getNamedItem("lazy").getNodeValue()));
			//��������������������������
			beanMap.put(bd.getId(),bd);
			//��������������������������������������lazy��������������������������������������������������������������������������
			if(!bd.getLazy()){
				Class<?> cls=Class.forName(bd.getPkgClass());
				Object obj=newBeanInstance(cls);
				instanceMap.put(bd.getId(), obj);
			}
			//System.out.println(instanceMap);
		}
	}
	/**�������������������������������������������������������������������*/
	public Object newBeanInstance(Class<?> cls)throws Exception{
		//Class<?> cls=Class.forName(pkgClass);
		Constructor<?> con=cls.getDeclaredConstructor();
		con.setAccessible(true);
		return con.newInstance();
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getBean(String key,Class<T> t)throws Exception{
		//1.����������������instanceMap��������������������bean��������������
		Object obj=instanceMap.get(key);
		if(obj!=null)return (T)obj;
		//2.��������map�������������������������������������������������������������map
		obj=newBeanInstance(t);
		instanceMap.put(key, obj);
		return (T)obj;
	}
	
	public static void main(String[] args)throws Exception {
		//1.��������������spring������������
		ClassPathXmlApplicationContext ctx=
		new ClassPathXmlApplicationContext("spring-configs.xml");
	    //2.������spring��������������������bean��������
		//Object obj=ctx.getBean("obj",Object.class);
		Date date=ctx.getBean("date",Date.class);
		//System.out.println(obj);
		System.out.println(date);
	}
	
}
