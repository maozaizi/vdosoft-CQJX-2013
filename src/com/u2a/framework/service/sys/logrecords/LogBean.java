package com.u2a.framework.service.sys.logrecords;

import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.util.Util;

public class LogBean {
	/**
	 * @Fields userId : 用户ID
	 */
	private String userId;
	
	/**
	 * @Fields userName : 用户名
	 */
	private String userName;
	
	/**
	 * @Fields busId : 业务表Id
	 */
	private String busId;
	
	/**
	 * @Fields busName : 业务表表名
	 */
	private String busName;
	
	/**
	 * @Fields reason : 原因（删除原因）
	 */
	private String reason;
	
	/**
	 * @Fields remark :备注（拼接好的信息）
	 */
	
	private String remark ;
	/**
	 * @Fields logType : 种类（0：普通；1：审批）
	 */
		
	private String logType;
	
	/**
	 * @Fields docId : 附件表Id
	 */
	private String docId;
	
	
	
	public LogBean(IMap user,String busId,String busName,String reason,String remark,String logType){
		if (user!=null){
			this.userId=(String) user.get("name");
			this.userName=(String) user.get("userName");
		}
//		if (Util.checkNull(busId)){
//			throw new BusinessException("日志数据busId为空！");
//		}
//		if (Util.checkNull(busName)){
//			throw new BusinessException("日志数据busName为空！");
//		}
//		if (Util.checkNull(logType)){
//			throw new BusinessException("日志数据logType为空！");
//		}
		this.busId=busId;
		this.busName=busName;
		this.reason=reason;
		this.remark=remark;
		this.logType=logType;
	}
	
	public LogBean(IMap user,String busId,String busName,String reason,String remark,String logType,String docId){
		if (user!=null){
			this.userId=(String) user.get("name");
			this.userName=(String) user.get("userName");
		}
		if (Util.checkNull(busId)){
			throw new BusinessException("日志数据busId为空！");
		}
		if (Util.checkNull(busName)){
			throw new BusinessException("日志数据busName为空！");
		}
		if (Util.checkNull(logType)){
			throw new BusinessException("日志数据logType为空！");
		}
		if (Util.checkNull(docId)){
			throw new BusinessException("日志数据docId为空！");
		}
		this.busId=busId;
		this.busName=busName;
		this.reason=reason;
		this.remark=remark;
		this.logType=logType;
		this.docId=docId;
	}
	
	
	/**
	 * @Fields describe : 操作描述
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	
	
	
}
