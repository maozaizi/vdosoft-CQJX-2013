package com.u2a.framework.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.brick.data.MethodBean;
import com.brick.manager.ContextUtil;
import com.brick.manager.LoadModuleManager;
import com.u2a.framework.auth.ILimitAuth;
import com.u2a.framework.util.Util;
/**
 * 按钮权限代码标签
 * @author 王冲
 * @date 2010-9-8 9:33:26
 */
@SuppressWarnings("unchecked")
public class AuthTag extends TagSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	private String code; //权限代码
	

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int doStartTag() throws JspException {
		if(code==null || "".equals(code)) return SKIP_BODY;
		String[] codes = code.split(",");
		boolean control = false;
		boolean execute = false;
		for(String code : codes){
			MethodBean methodBean = LoadModuleManager.getLoadManager().get(code);
			if(methodBean!=null){ 	//判断methodbean不为空
				String limitName = methodBean.getLimit(); 	//得到limit权限spring的bean名称
				if(Util.checkNotNull(limitName)){
					ILimitAuth limit = (ILimitAuth) ContextUtil.getSpringBean(limitName); //得到limit验证接口
					if(limit!=null){	//limit不为空
						if(!execute) control = true;
						Boolean bool = limit.check((HttpServletRequest)this.pageContext.getRequest(), (HttpServletResponse)this.pageContext.getResponse(),code);
						control &= bool; 
						execute = true;
					}
				}
			}
		}
		if(control && execute){
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}
}
