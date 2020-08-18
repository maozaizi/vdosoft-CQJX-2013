package com.u2a.framework.tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.u2a.datadict.TradeData;
import com.u2a.framework.util.Util;
/**
 * 行业代码显示行业名称标签
 * @author 王冲
 * @date 2010-8-27 17:10:03
 */
@SuppressWarnings("serial")
public class TradeTag extends TagSupport{
	private String tradeCode;//行业代码

	@Override
	public int doStartTag() throws JspException {
		StringBuffer buf = new StringBuffer("");
		if(!"".equals(tradeCode)){
			String[] codes = tradeCode.split(",");
			for(String code : codes){
				if(Util.checkNotNull(code)){
					String name = TradeData.getTradeData().getNameByCode(code);
					if(Util.checkNotNull(name)){
						buf.append(name).append(",");
					}
				}
			}
		}
		if(buf.length()>0){
			buf.deleteCharAt(buf.length()-1);
		}
		TagUtils.getInstance().write(this.pageContext, buf.toString());
		return super.doStartTag();
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	
}
