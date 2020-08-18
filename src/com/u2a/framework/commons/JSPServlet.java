package com.u2a.framework.commons;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.brick.data.IMap;
import com.brick.data.MethodBean;
import com.brick.data.ParamBean;
import com.brick.data.ParamSourceTypes;
import com.brick.exception.BrickException;
import com.brick.init.InitLoad;
import com.brick.manager.ContextUtil;
import com.brick.manager.ExecBean;
import com.brick.manager.ExecMethodHelper;
import com.brick.manager.LoadModuleManager;
import com.brick.manager.SqlMapClientFactoryBean;
import com.brick.util.Util;
import com.u2a.framework.auth.ILimitAuth;

/**
 * 框架页面调用API
 * 
 * @author 吴明强
 * 
 */
public class JSPServlet extends HttpServlet {
	static Logger logger = Logger.getLogger(JSPServlet.class.getName());
	private static final long serialVersionUID = 1L;

	public JSPServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	private void parseOutMap(MethodBean methodBean, IMap out,
			HttpServletRequest request, HttpServletResponse response) {
		// out 参数解析
		for (ParamBean paramBean : methodBean.getOutParams()) {
			switch (paramBean.getSource()) {
			case ParamSourceTypes.TOREQUEST:
				// 存进请求中
				request.setAttribute(paramBean.getName(), out.get(paramBean
						.getReal()));
				break;
			case ParamSourceTypes.TOSESSION:
				// 存进session中
				request.getSession().setAttribute(paramBean.getName(),
						out.get(paramBean.getReal()));
				break;
			case ParamSourceTypes.TOAPPLICATION:
				// 存进application
				request.getSession().getServletContext().setAttribute(
						paramBean.getName(), out.get(paramBean.getReal()));
				break;
			}
		}
	}

	

	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getRequestURI();

		/**
		 * 权限控制
		 */
		String method = ExecMethodHelper.pathToMethodName(path, request);
		//得到methodbean
		MethodBean methodBean = LoadModuleManager.getLoadManager().get(method);
		if(Util.checkNotNull(methodBean)){ 	//判断methodbean不为空
			
			String limitName = methodBean.getLimit(); 	//得到limit权限spring的bean名称
			if(Util.checkStringNotNull(limitName)){
				ILimitAuth limit = (ILimitAuth) ContextUtil.getSpringBean(limitName); //得到limit验证接口
				if(Util.checkNotNull(limit)){	//limit不为空
					Boolean bool = limit.check(request, response,method);
					if(!bool){
						//抛出提示
						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().println("<script>alert('当前用户没有该权限！');history.back();</script>");
						return ;
					}
				}else{
					//抛出异常
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().println("limit接口的实例不存在！");
					return ;
				}
			}
		}
		if ("init.reload".equals(method) && InitLoad.reloadProject()) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println("project reload success!");
			return;
		}
		if ("init.reloadsql".equals(method)) {
			Object obj=ContextUtil.getSpringBean("&sqlMapClient");
			SqlMapClientFactoryBean sqlMapClientFactoryBean=(SqlMapClientFactoryBean)obj;
			try {
				sqlMapClientFactoryBean.resetSqlMapClient();
				response.getWriter().println("sql reload success!");
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println("sql reload errer!"+e.getMessage());
			}
			return;
		}
		ExecBean bean = new ExecBean();
		bean.setMethodName(method);
		ExecMethodHelper.ExecWebCall(bean, request, response);
		if (bean.getFlag() == -1) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println(method + "方法不存在");
			return;
		}
		if (bean.getFlag() != 1) {
			request.setAttribute("errMsg", bean.getErrMsg());
			if (!Util.checkNotNull(bean.getMethodBean().getError()))
				request.getSession().getServletContext().getRequestDispatcher("/jsp/info/info.jsp?type=err").forward(request,response);
			else
				request.getRequestDispatcher("/" + bean.getMethodBean().getError()).forward(request,response);
			return;
		}

		try {
			parseOutMap(bean.getMethodBean(), bean.getOutMap(), request,
					response);
		} catch (BrickException e) {
			logger.error("解析输出参数错误：" + method, e);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println("解析输出参数错误：" + method);
			return;
		}
		
		
		request.setAttribute("method.flag", "1");
		request.setAttribute("context.map", bean.getOutMap());
		request.setAttribute("infoMsg", bean.getOutMap().get("method.infoMsg"));
		String forward = (String) bean.getOutMap().get("method.forward");
		String pageback = (String) bean.getOutMap().get("method.pageback");
		String redirect = (String) bean.getOutMap().get("method.redirect");
		if(Util.checkNotNull(pageback)){
			response.sendRedirect(request.getContextPath()+"/"+pageback);
		}else if (Util.checkNotNull(forward)) {
			request.getRequestDispatcher("/" + forward).forward(request,response);
		}
		else if(Util.checkNotNull(bean.getOutMap().get("method.url"))){
			request.setAttribute("url", bean.getOutMap().get("method.url"));
			request.getSession().getServletContext().getRequestDispatcher("/jsp/info/info.jsp").forward(request, response);
		}else if(Util.checkNotNull(redirect)){
			response.sendRedirect(redirect);
		}else if (Util.checkNotNull(bean.getMethodBean().getDest())){
			request.getRequestDispatcher("/" + bean.getMethodBean().getDest()).forward(request, response);
		}
			

	}

	public void init() throws ServletException {
	}
}
