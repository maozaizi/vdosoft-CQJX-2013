package com.u2a.framework.util;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import org.apache.log4j.Logger;

import com.brick.data.IMap;
import com.brick.manager.AppCaller;
import com.brick.manager.ExecBean;


public class ServletListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(ServletListener.class);
	
	private Timer timer = new Timer();
	
	public void contextDestroyed(ServletContextEvent arg0) {
		//arg0.getServletContext().removeAttribute("OnlineUserMap");
	}

	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent arg0) {
		//arg0.getServletContext().setAttribute("OnlineUserMap", new LinkedHashMap<String,IMap<String,Object>>());
		
		// 方法名称
		String method = "";
		// 调用AuthService中的查询所有权限方法
		method = "auth.allAuth";
		ExecBean bean = new ExecBean();
		bean.setMethodName(method);
		// 执行方法
		AppCaller.ExeMethod(bean);
		if (bean.getFlag() == -1) {
			log.error(method + "方法不存在");
			return;
		} else if (bean.getFlag() != 1) {
			log.error(method + "方法执行异常");
			return;
		}
		// 获取输出值
		IMap result = bean.getOutMap();
		// 判断输出值是否为空
		List<IMap> allAuth = result.get("allAuth") == null ? 
				null : (List<IMap>) result.get("allAuth");
		// 向SerlvetContext中放入所有权限信息
		arg0.getServletContext().setAttribute("allAuth", allAuth);
		
		// 调用AuthService中的查询所有权限方法
		method = "auth.authTree";
		bean = new ExecBean();
		bean.setMethodName(method);
		// 执行方法
		AppCaller.ExeMethod(bean);
		if (bean.getFlag() == -1) {
			log.error(method + "方法不存在");
			return;
		} else if (bean.getFlag() != 1) {
			log.error(method + "方法执行异常");
			return;
		}
		// 获取输出值
		result = bean.getOutMap();
		// 判断输出值是否为空
		List<IMap> authTree = result.get("authTree") == null ? 
				null : (List<IMap>) result.get("authTree");
		// 向SerlvetContext中放入所有权限信息
		arg0.getServletContext().setAttribute("authTree", authTree);
		
		bean = new ExecBean();
		method = "authgroup.allAuthGroup";
		// 调用AuthGroupService中的查询方法查询所有权限组
		bean.setMethodName(method);
		// 执行方法
		AppCaller.ExeMethod(bean);
		if (bean.getFlag() == -1) {
			log.error(method + "方法不存在");
			return;
		} else if (bean.getFlag() != 1) {
			log.error(method + "方法执行异常");
			return;
		}
		// 获取输出值
		result = bean.getOutMap();
		// 判断输出值是否为空
		List<IMap> allAuthGroup = result.get("allAuthGroup") == null ? 
				null : (List<IMap>) result.get("allAuthGroup");
		// 向SerlvetContext中放入所有权限组信息
		arg0.getServletContext().setAttribute("allAuthGroup", allAuthGroup);
		
		bean = new ExecBean();
		method = "usergroup.allUserGroup";
		// 调用AuthGroupService中的查询方法查询所有权限组
		bean.setMethodName(method);
		// 执行方法
		AppCaller.ExeMethod(bean);
		if (bean.getFlag() == -1) {
			log.error(method + "方法不存在");
			return;
		} else if (bean.getFlag() != 1) {
			log.error(method + "方法执行异常");
			return;
		}
		// 获取输出值
		result = bean.getOutMap();
		// 判断输出值是否为空
		List<IMap> allUserGroup = result.get("allUserGroup") == null ? 
				null : (List<IMap>) result.get("allUserGroup");
		// 向SerlvetContext中放入所有用户组信息
		arg0.getServletContext().setAttribute("allUserGroup", allUserGroup);
		
		final String filePath = arg0.getServletContext().getRealPath("/report_html");
		// 定时删除生成的报表临时文件
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				File[] reportFiles = new File(filePath).listFiles();
				if (reportFiles!=null && reportFiles.length > 0) {
					for (File reportFile : reportFiles) {
						if (reportFile.isDirectory()) {
							File[] files = reportFile.listFiles();
							for (File file : files) {
								file.delete();
							}
							reportFile.delete();
						} else if (reportFile.isFile()) {
							reportFile.delete();
						}
					}
				} 
			}
			
		}, new Date(), 24*60*1000);
//		
		
//		//加定时器
//		List<Element> list=TimerConfig.getTimerList();
//		for(Element ele:list){
//			String xmlcalss=ele.getAttributeValue("class");
//			String xmltime=ele.getAttributeValue("time");
//			String xmlwaittime=ele.getAttributeValue("waittime");
//			
//			long time=0;
//			long waittime=0;
//			
//			if(xmltime!=null&&!xmltime.trim().equals("")){
//				try{
//					time=Long.parseLong(xmltime);
//					waittime=Long.parseLong(xmlwaittime);
//				}catch (Exception e) {}
//				
//			}
//			try {
//				System.out.println("启动定时器："+ele.getAttributeValue("name"));
//				timer.schedule((TimerTask)Class.forName(xmlcalss).newInstance(),time*1000,waittime*1000);
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("启动定时器 "+ele.getAttributeValue("name")+" 时遇到异常！");
//			}
//		}// end for
		
	}
}
