package com.java.spring.vo;
import java.io.Serializable;
/**
 * VO (Value Object)
 * @author tarena
 * 把要交给容器管理的类的信息
 */
public class BeanDefinition implements Serializable{
	private static final long serialVersionUID = -2289637660459785939L;
	private String id;//java中用对象来保存数据
    private String pkgClass;//=包名.类名-->Class.forName("包名.类名");
    private Boolean lazy=false;
     //......
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPkgClass() {
		return pkgClass;
	}
	public void setPkgClass(String pkgClass) {
		this.pkgClass = pkgClass;
	}
	public Boolean getLazy() {
		return lazy;
	}
	public void setLazy(Boolean lazy) {
		this.lazy = lazy;
	}
	@Override
	public String toString() {
		return "BeanDefinition [id=" + id + ", pkgClass=" + pkgClass + ", lazy=" + lazy + "]";
	}
     
}
