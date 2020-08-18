package com.u2a.framework.service.base.mailManage;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;


/**
 * 系统名称：新疆生产建设兵团消防网络化信息管理系统 类名称：MailService 类描述：站内信功能 创建人：田鹏 创建时间：2012年3月6日
 * 09:59:46
 */
@SuppressWarnings("unchecked")
public class MailService extends Service {
	/**
	 * @Description查询和登录人有关的收件信息
	 * @param in
	 * @return IMap
	 * @author 田鹏
	 * @date 2012年3月6日 10:00:48
	 */
	public IMap getReceiveMail(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		// 调用框架中的分页组件.做查询分页
		IMap userMap = (IMap) in.get("userMap");
		in.put("reciveuserid", (String)userMap.get("userId"));
		Page page = db.pageQuery(in, "getreceive", "com.MailManage", in
				.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get(
				"currentPageNO").toString()), Constants.PAGESIZE);
		result.put("page", page);
		page.setAction(request);
		return result;
	}
	
	
	/** 
	 * @Description 获取我的消息
	 * @param @param in
	 * @return IMap   
	 * @author panghaichao
	 * @date 2012年5月26日 18:55:15
	 */ 
	public IMap getMyMail(IMap in) {
		IMap mailManage = BeanFactory.getClassBean("com.MailManage");
		mailManage.put("readStatus", "1");
		mailManage.put("reciveUserId", in.get("userId"));
		mailManage.put("isValid", Constants.ISVALID);
		List<IMap> taskList = db.getList(mailManage, 0, (Integer) in.get("count"));
		IMap result = new DataMap();
		result.put("list", taskList);
		return result;
	}
	
	/**
	 * @Description查询和登录人最新信息数
	 * @param in
	 * @return IMap
	 * @author 周夏冰
	 * @date 2012年3月6日 10:00:48
	 */
	@SuppressWarnings("unchecked")
	public IMap getNewMails(IMap in) {	
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap)in.get("userMap");
		IMap numMap = BeanFactory.getClassBean("com.MailManage");
		numMap.put("readStatus", "1");
		if(userMap!=null){
			numMap.put("reciveUserId", userMap.get("userId"));
			result.put("newMailsNum", db.getObjectTotal("getNewMailsNum",numMap));
		}		
		return result;
	}
	/**
	 * @Description查询收件箱的明细
	 * @param in
	 * @return IMap
	 * @author 田鹏
	 * @date 2012年3月6日 13:12:21
	 */
	@SuppressWarnings("unchecked")
	public IMap getReceiveDetail(IMap in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		// IMap userMap = (IMap) in.get("userMap");

		HttpServletRequest request = (HttpServletRequest) in.get("request");
		// 查询单条信息的明细
		IMap condition = BeanFactory.getClassBean("com.MailManage");

		condition.put("ID", in.get("ID"));
		condition.put("isValid", Constants.ISVALID);

		IMap detail = db.get(condition);
		detail.put("readStatus", 0);
		db.update(detail);
		result.put("detail", detail);

		// 调用框架中的分页组件.做查询分页，查询和发件人收件人有关的信息
		Page page = db.pageQuery(in, "getrecord", "com.MailManage", in
				.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get(
				"currentPageNO").toString()), Constants.PAGESIZE);
		page.setAction(request);
		result.put("ID", in.get("ID"));

		result.put("page", page);
		return result;
	}

	/**
	 * @Description查询和登录人有关的发件信息
	 * @param in
	 * @return IMap
	 * @author 周夏冰
	 * @date 2012年3月6日 19:06:56
	 */
	@SuppressWarnings("unchecked")
	public IMap getSendMail(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		// 调用框架中的分页组件.做查询分页
		IMap userMap = (IMap) in.get("userMap");
		in.put("senduserid", userMap.get("userId").toString());
		Page page = db.pageQuery(in, "getsend", "com.MailManage", in
				.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get(
				"currentPageNO").toString()), Constants.PAGESIZE);
		result.put("page", page);
		page.setAction(request);
		return result;
	}

	/**
	 * @Description发送信息
	 * @param in
	 * @return IMap
	 * @author 田鹏；修改人：周夏冰
	 * @date 2012年3月7日 10:14:20
	 */
	@SuppressWarnings("unchecked")
	public IMap add(IMap in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap paramMap = (IMap) in.get("mail");
		IMap userMap = (IMap) in.get("userMap");

		IMap reciveUsers = (IMap) in.get("mail");
		String[] reciveUserId = reciveUsers.get("reciveUserId").toString()
				.split(",");
		String[] reciveUserName = reciveUsers.get("reciveUserName").toString()
				.split(",");
		paramMap.put("ID", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.MailManage"));
		String taskCode = (String) paramMap.get("ID");
		for (int i = 0; i < reciveUserId.length; i++) {
			paramMap.put("taskCode",null);
			paramMap.put("ID", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.MailManage"));
			
			paramMap.put("sendUserId", userMap.get("userId").toString());
			paramMap.put("sendUserName", userMap.get("name").toString());
			
			HelperDB.setCreateInfo(HelperApp.getUserName(userMap), paramMap);
			paramMap.put("readStatus", 1);
			paramMap.put("isValid", 1);
			paramMap.put("reciveUserId", reciveUserId[i]);
			paramMap.put("reciveUserName", (reciveUserName[i]));
			paramMap.put("taskCode", taskCode);
			super.db.insert(paramMap);
		}
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		if ("0".equals(in.get("flag"))) {			
			result.put("method.infoMsg", "发送成功!");
			result.put("method.url", in.get("url"));
		} else {
			Page page = db.pageQuery(in, "getrecord", "com.MailManage", in
					.get("currentPageNO") == null ? 1 : Integer.parseInt(in
					.get("currentPageNO").toString()), Constants.PAGESIZE);

			page.setAction(request);
			result.put("ID", in.get("ID"));
			result.put("page", page);		
			
		}

		return result;
	}
	/**
	 * @Description查询发件箱的明细
	 * @param in
	 * @return IMap
	 * @author 田鹏；修改人：周夏冰
	 * @date 2012年3月8日 18:15:01；修改日期：2012年4月9日 18:00:00
	 */
	@SuppressWarnings("unchecked")
	public IMap getSendDetail(IMap in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		// IMap userMap = (IMap) in.get("userMap");

		HttpServletRequest request = (HttpServletRequest) in.get("request");
		// 查询单条信息的明细
		IMap condition = BeanFactory.getClassBean("com.MailManage");

		condition.put("ID", in.get("ID"));
		condition.put("isValid", Constants.ISVALID);

		IMap detail = db.get(condition);
		detail.put("readStatus", 0);
		db.update(detail);
		result.put("detail", detail);

		// 调用框架中的分页组件.做查询分页，查询和发件人收件人有关的信息
		Page page = db.pageQuery(in, "getrecord", "com.MailManage", in
				.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get(
				"currentPageNO").toString()), Constants.PAGESIZE);
		page.setAction(request);
		result.put("page", page);
		return result;
	}
}
