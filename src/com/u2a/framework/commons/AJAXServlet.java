package com.u2a.framework.commons;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.MethodBean;
import com.brick.data.ParamBean;
import com.brick.data.ParamSourceTypes;
import com.brick.manager.ExecBean;
import com.brick.manager.ExecMethodHelper;


public class AJAXServlet extends HttpServlet {
	static Logger logger = Logger.getLogger(AJAXServlet.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AJAXServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	private void parseOutMap(MethodBean methodBean, IMap out,HttpServletRequest request, HttpServletResponse response) {
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
		// 获取mehtodbean
		// 解析输入参数
		// 调用services
		// 解析返回参数
		// 返回
		String path = request.getRequestURI();
		// String method = request.getParameter("method");

		String method = ExecMethodHelper.pathToMethodName(path, request);
		ExecBean bean = new ExecBean();
		bean.setMethodName(method);
		ExecMethodHelper.ExecWebCall(bean, request, response);
		if (bean.getFlag() == -1) {
			response.getWriter().println(method + "方法不存在");
			return;
		}
		IMap outMap;
		outMap = bean.getOutMap();
		if (bean.getFlag() != 1) {
			outMap = new DataMap();
			outMap.put("method.flag", 0);
			outMap.put("errmsg", bean.getErrMsg());
		}
		
		// outMap.put("method.flag", 1);
		response.getWriter().print(JSONObject.fromObject(outMap));
		response.getWriter().flush();
		response.getWriter().close();
	}

	public void init() throws ServletException {
	}
}
