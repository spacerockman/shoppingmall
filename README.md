# shoppingmall
A shopping mall project for practicing

## 1.Three concept you need to know
**Spring**
**SpringMVC**
**Mybatis**
  
## 2.build your development environment

## 3.[create a Maven project](http://blog.csdn.net/zhshulin/article/details/37921705)

## 4.begin to combine the SpringMVC,Spring and Mybatis
	
  ### 4.1 make Maven import the needed jars-->[pom.xml](https://github.com/spacerockman/shoppingmall/blob/master/shop/pom.xml)
  
  After importing all the needed jars, the most important thing is to combine **Spring** with **Mybatis**
   ### 4.2 the combination of Spring and mybatis 
   * to scan through the structure:
  ![structure](https://github.com/spacerockman/shoppingmall/blob/master/shop/imgs/configuration.png) 
  ### 4.2.1 create the file of JDBC-->[jdbc.properties](https://github.com/spacerockman/shoppingmall/blob/master/shop/src/main/resources/jdbc.properties)
  
    driver=com.mysql.jdbc.Driver>
    url=jdbc:mysql://10.221.10.111:8080/db_zsl
    username=demao
    password=demao
    #定义初始连接数
    initialSize=0
    #定义最大连接数
    maxActive=20
    #定义最大空闲
    maxIdle=20 
    #定义最小空闲
    minIdle=1
    #定义最长等待时间
    maxWait=60000 


  ### 4.2.2 create the configuration of [spring-mybatis.xml](https://github.com/spacerockman/shoppingmall/blob/master/shop/src/main/resources/spring-mybatis.xml)
  <details>
<summary>spring-mybatis.xml</summary>

	 <?xml version="1.0" encoding="UTF-8"?>
	    <beans xmlns="http://www.springframework.org/schema/beans"
	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
	      xmlns:context="http://www.springframework.org/schema/context"
	      xmlns:mvc="http://www.springframework.org/schema/mvc"
	      xsi:schemaLocation="http://www.springframework.org/schema/beans  
				    http://www.springframework.org/schema/beans/spring-beans-3.1.xsd  
				    http://www.springframework.org/schema/context  
				    http://www.springframework.org/schema/context/spring-context-3.1.xsd  
				    http://www.springframework.org/schema/mvc  
				    http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd">
	      <!-- 自动扫描 -->
	      <context:component-scan base-package="com.cn.shop" />
	      <!-- 引入配置文件 -->
	      <bean id="propertyConfigurer"
		class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="location" value="classpath:jdbc.properties" />
	      </bean>
 
	<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
		destroy-method="close">
		<property name="driverClassName" value="${driver}" />
		<property name="url" value="${url}" />
		<property name="username" value="${username}" />
		<property name="password" value="${password}" />
		<!-- 初始化连接大小 -->
		<property name="initialSize" value="${initialSize}"></property>
		<!-- 连接池最大数量 -->
		<property name="maxActive" value="${maxActive}"></property>
		<!-- 连接池最大空闲 -->
		<property name="maxIdle" value="${maxIdle}"></property>
		<!-- 连接池最小空闲 -->
		<property name="minIdle" value="${minIdle}"></property>
		<!-- 获取连接最大等待时间 -->
		<property name="maxWait" value="${maxWait}"></property>
	</bean>
 
	<!-- spring和MyBatis完美整合，不需要mybatis的配置映射文件 -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<!-- 自动扫描mapping.xml文件 -->
		<property name="mapperLocations" value="classpath:com/cn/shop/mapping/*.xml"></property>
	</bean>
 
	<!-- DAO接口所在包名，Spring会自动查找其下的类 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="com.cn.shop.dao" />
		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
	</bean>
 
	<!-- (事务管理)transaction manager, use JtaTransactionManager for global tx -->
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
    </beans>
</details>
     
  
  
  
  ### 4.2.3、configurate the [Log4j.properties](https://github.com/spacerockman/shoppingmall/blob/master/shop/src/main/resources/log4j.properties)
  
  *the structure of resources
     ![resources](https://github.com/spacerockman/shoppingmall/blob/master/shop/imgs/log4j.png)
  ### 4.2.4、JUnit Test
  <details>
<summary> create a testing table</summary>

	 DROP TABLE IF EXISTS `user_t`;
	    CREATE TABLE `user_t` (
	     `id` int(11) NOT NULL AUTO_INCREMENT,
	     `user_name` varchar(40) NOT NULL,
	      `password` varchar(255) NOT NULL,
	     `age` int(4) NOT NULL,
	     PRIMARY KEY (`id`)
	    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
 
    /*Data for the table `user_t` */
 
    insert  into `user_t`(`id`,`user_name`,`password`,`age`) values (1,'测试','test',24);
</details>
   
   
  ### 4.2.4.2  use the tool --MyBatis Generator to create class automatically [steps](https://blog.csdn.net/cllaure/article/details/81483858)
  ![generator](https://github.com/spacerockman/shoppingmall/blob/master/shop/imgs/generator.png)
  
  ### 4.2.4.3  Service and ServiceImpl
  ![serviceandserviceimpl](https://github.com/spacerockman/shoppingmall/blob/master/shop/imgs/serviceandserviceimpl.png)
  
  <details>
<summary>UserService.java</summary>

	package com.cn.shop.service;
  
        import com.cn.shop.pojo.User;

        public interface IUserService {
          public User getUserById(int userId);
        }
</details>
      
        
  
   <details>
<summary>UserServiceImpl.java</summary>

    package com.cn.shop.service.impl;
 
    import javax.annotation.Resource;

    import org.springframework.stereotype.Service;

    import com.cn.shop.dao.IUserDao;
    import com.cn.shop.pojo.User;
    import com.cn.shop.service.IUserService;

    @Service("userService")
    public class UserServiceImpl implements IUserService {
      @Resource
      private IUserDao userDao;
      @Override
      public User getUserById(int userId) {
        // TODO Auto-generated method stub
        return this.userDao.selectByPrimaryKey(userId);
      }

    }
</details>
   
### 4.2.4.4、JunitTest.java
<details>
<summary>JunitTest.java</summary>

    package org.zsl.testmybatis;

    import javax.annotation.Resource;

    import org.apache.log4j.Logger;
    import org.junit.Before;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

    import com.alibaba.fastjson.JSON;
    import com.cn.shop.pojo.User;
    import com.cn.shop.service.IUserService;

    @RunWith(SpringJUnit4ClassRunner.class)		
    @ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})

    public class TestMyBatis {
      private static Logger logger = Logger.getLogger(TestMyBatis.class);
   
      @Resource
      private IUserService userService = null;
 
	@Test
	public void test1() {
		User user = userService.getUserById(1);
		// System.out.println(user.getUserName());
		// logger.info("value："+user.getUserName());
		logger.info(JSON.toJSONString(user));
	}
	}
</details>
   

### 4.3  join **SpringMVC**

#### 4.3.1 [spring-mvc.xml](https://github.com/spacerockman/shoppingmall/blob/master/shop/src/main/resources/spring-mvc.xml)
#### 4.3.2 [web.xml](https://github.com/spacerockman/shoppingmall/blob/master/shop/src/main/webapp/WEB-INF/web.xml)
#### 4.3.3.1  create a jsp for testing
  ![jsptest](https://github.com/spacerockman/shoppingmall/blob/master/shop/imgs/serviceandserviceimpl.png)
 
  
      <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <html>
      <head>
      <title>test</title>
      </head>

      <body>
        ${user.userName}
      </body>
    </html>
    
 #### 4.3.3.2 UserController.java
 
 <details>
<summary>UserController.java</summary>

    package com.cn.shop.controller;

    import javax.annotation.Resource;
    import javax.servlet.http.HttpServletRequest;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.RequestMapping;

    import com.cn.shop.pojo.User;
    import com.cn.shop.service.IUserService;

    @Controller
    @RequestMapping("/user")
    public class UserController {
      @Resource
      private IUserService userService;

      @RequestMapping("/showUser")
      public String toIndex(HttpServletRequest request,Model model){
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = this.userService.getUserById(userId);
        model.addAttribute("user", user);
        return "showUser";
      }
    }
</details>
 
    
  
  #### 4.3.3.3 Test
    localhost:8080/shop/user/showUser?id=1
