package com.u2a.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

/**
 * 
 * 系统名称：长庆钻井机修公司 - 设备维修管理系统   
 * 类名称：BusMechanicService   
 * 类描述：   
 * 创建人：崔佳华
 * 创建时间：Jul 31, 2013 9:00:36 AM
 */
@SuppressWarnings("unchecked")
public class BusInrepairInfoService extends Service {
	
	/**
	 * 进修单列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getBusInrepairInfoList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		String userid = (String) userMap.get("userid");
		//获取当前用户的权限名称
		IMap conditionmap = new DataMap();
		conditionmap.put("userid",userid);
		List list = db.getList(conditionmap, "queryuserposts", "com.PostInfo");
		if(!list.isEmpty()){
			int checkflag = 1;
			for(int i=0;i<list.size();i++){
				IMap postMap = BeanFactory.getClassBean("com.PostInfo");
				postMap = (IMap) list.get(i);
				String postname = (String) postMap.get("postName");
				if("监造主管".equals(postname)){
					checkflag++;
				}
			}
			//判断用户权限中有监造主管的话，则开放编辑的权限
			if(checkflag>1){
				result.put("flag", "1");
			}
		}
		//状态为1的不通过的
		//in.put("repairStep","1");
		//查询分页结果
		Page page = db.pageQuery(in,"queryBusInrepairInfoList","com.BusInrepairInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 导向修改进修单
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> toUpdateBusInrepairInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//进修单信息
		IMap busInrepairInfo =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", in.get("id"));
		IMap busInrepairInfoMap = db.get(busInrepairInfo,"getBusInrepairInfoById","com.BusInrepairInfo");
		
		//获取选择设备的信息
		if(!busInrepairInfoMap.isEmpty()){
			String eqId = (String) busInrepairInfoMap.get("equipmentId");
						
			IMap eqInfo =BeanFactory.getClassBean("com.EquipmentInfo");
			eqInfo.put("id", eqId);
			eqInfo = db.get(eqInfo);
			result.put("eqInfoMap",eqInfo );
		}
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentMap.put("busId",busInrepairInfoMap.get("id"));
		attachmentMap.put("busName","com.BusInrepairInfo");
		attachmentMap.put("isValid","1");
		List fileList = db.getList(attachmentMap, null);
		result.put("busInrepairInfoMap",busInrepairInfoMap );
		result.put("fileList", fileList);
		return result;
	}
	/**
	 * 删除附件信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 */
	public IMap<String, Object> deleteFile(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap  attachmentInfoMap= BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentInfoMap.put("id",in.get("attachmentInfoId"));
		attachmentInfoMap.put("isValid",Constants.ISNOTVALID);
		db.update(attachmentInfoMap);
		result.put("method.infoMsg","删除附件成功");
		result.put("method.url", in.get("attachmentInfoUrl"));
		return result;
	}
	/**
	 * 修改进修单
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> updateBusInrepairInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap busInrepairInfoMap = (IMap) in.get("BusInrepairInfo");
		IMap userMap = (IMap) in.get("userMap");
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		busInrepairInfoMap.put("orgId",orgCode );
		busInrepairInfoMap.put("isValid",Constants.ISVALID);
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),busInrepairInfoMap);
		//删除进厂检验通过的附件信息
//		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
//		attachmentMap.put("busId",busInrepairInfoMap.get("id"));
//		attachmentMap.put("busName","com.BusInrepairInfo");
//		attachmentMap.put("isValid","1");
//		List fileList = db.getList(attachmentMap, null);
//		for(int a=0;a<fileList.size();a++){
//		IMap  attachmentInfo= (IMap)fileList.get(a);
//		attachmentInfo.put("isValid",Constants.ISNOTVALID);
//		db.update(attachmentInfo);
//		};
		//上传附件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
				attachmentInfoMap.put("busId",busInrepairInfoMap.get("id"));
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.BusInrepairInfo");
				attachmentInfoMap.put("busType",1);
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
			}
		}
		//1:进修单 2：厂检 3：调度
		busInrepairInfoMap.put("repairStep","2");
		db.update(busInrepairInfoMap);
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="监造："+userMap.get("name")+"在"+dftow.format(new Date())+"修改了进修单信息。";
		LogBean logb=new LogBean(userMap,busInrepairInfoMap.get("id").toString(), "com.BusInrepairInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 登记进修单
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> addBusInrepairInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap busInrepairInfoMap = (IMap) in.get("BusInrepairInfo");
		IMap userMap = (IMap) in.get("userMap");
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		String busInrepairInfoId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busInrepairInfoId );
		busInrepairInfoMap.put("orgId",orgCode );
		busInrepairInfoMap.put("isValid",Constants.ISVALID);
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),busInrepairInfoMap);
		//上传附件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
				attachmentInfoMap.put("busId",busInrepairInfoId);
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.BusInrepairInfo");
				attachmentInfoMap.put("busType",1);
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
			}
		}
		//1:进修单 2：厂检 3：调度
		busInrepairInfoMap.put("repairStep","2");
		//判断修理类型 从而插入不同的产值
		String eqid = (String)busInrepairInfoMap.get("equipmentId");
		IMap map = BeanFactory.getClassBean("com.EquipmentInfo");
		map.put("id", eqid);
		map = db.get(map);
		
		String repairType = (String)busInrepairInfoMap.get("repairType");
		if("1".equals(repairType)){
			busInrepairInfoMap.put("outputValue",map.get("majorCost"));
			//busInrepairInfoMap.put("quotaHour",map.get("quotaHour"));
		}else if("2".equals(repairType)){
			busInrepairInfoMap.put("outputValue",map.get("mediumCost"));
			//busInrepairInfoMap.put("quotaHour",map.get("checkHour"));
		}else{
			busInrepairInfoMap.put("outputValue",map.get("checkCost"));
			//busInrepairInfoMap.put("quotaHour",map.get("mediumHour"));
		}
		if(map.get("personNum")!=null){
			busInrepairInfoMap.put("personNum",map.get("personNum"));
		}
		db.insert(busInrepairInfoMap);
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="监造："+userMap.get("name")+"在"+dftow.format(new Date())+"添加了进修单信息。";
		LogBean logb=new LogBean(userMap,busInrepairInfoId, "com.BusInrepairInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 新增进修单，选择设备页面
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 */
	public IMap chooseEquipmentInfo(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		
		//查询结果
		List<IMap> list = db.getList(in,"queryEquipmentInfoList","com.EquipmentInfo");
		
		List<IMap> rlist = new ArrayList<IMap>();
		//组合设备名称和编号
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				IMap map = (IMap) list.get(i);
				map.put("wholename", (String)map.get("equipmentCode")+" "+(String)map.get("equipmentName"));
				rlist.add(map);
			}
		}
		result.put("equipmentList", rlist);
		result.put("index",(String)in.get("index"));
		return result;
	}
	/**
	 * 
	 * @param in
	 * @return
	 */
	public IMap chooseEquipmentInfoBytype(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		
		//查询结果
		List<IMap> list = db.getList(in,"queryEquipmentInfoListBytype","com.EquipmentInfo");
		
		List<IMap> rlist = new ArrayList<IMap>();
		//组合设备名称和编号
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				IMap map = (IMap) list.get(i);
				map.put("wholename", (String)map.get("equipmentCode")+" "+(String)map.get("equipmentName"));
				rlist.add(map);
			}
		}
		result.put("equipmentList", rlist);
		result.put("index",(String)in.get("index"));
		return result;
	}
	/**
	 * 进修单信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @date Jul 16, 2013 5:39:18 PM
	 */
	public IMap<String, Object> getBusInrepairInfoDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//进修单信息
		IMap busInrepairInfo =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", in.get("id"));
		IMap busInrepairInfoMap = db.get(busInrepairInfo);
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentMap.put("busId",busInrepairInfoMap.get("id"));
		attachmentMap.put("busName","com.BusInrepairInfo");
		attachmentMap.put("isValid","1");
		List fileList = db.getList(attachmentMap, null);
		result.put("busInrepairInfoMap",busInrepairInfoMap );
		result.put("fileList", fileList);
		//审批详情列表
		IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
		logRecordsMap.put("busId",in.get("id"));
		logRecordsMap.put("busName","com.BusMechanic");
		logRecordsMap.put("logType","1");
		List logRecordsList = db.getList(logRecordsMap, null);
		result.put("logRecordsList",logRecordsList);
		return result;
	}
	
	/**
	 * 设备进厂检验列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getTestBusInrepairInfoList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//状态为2的是需检验的设备
		in.put("repairStep","2");
		//查询分页结果
		Page page = db.pageQuery(in,"queryBusInrepairInfoList","com.BusInrepairInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 设备进厂检验
	 * @param  in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
		
	public IMap<String,Object> modifyBusInrepairInfoTest(IMap<String,Object> in){
		IMap<String,Object> result = new DataMap<String,Object>();
		IMap userMap = (IMap) in.get("userMap");
		IMap busInrepairInfo =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", in.get("id"));
		busInrepairInfo.put("repairStep", in.get("repairStep"));
		busInrepairInfo.put("testRemark",in.get("testRemark"));
		busInrepairInfo.put("tester",HelperApp.getUserName(userMap));
		Date date=new Date();	
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		busInrepairInfo.put("testDate",date_str);
		db.update(busInrepairInfo);
		//删除进厂检验不通过的附件信息
//		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
//		attachmentMap.put("busId",busInrepairInfo.get("id"));
//		attachmentMap.put("busName","com.BusInrepairInfo");
//		attachmentMap.put("isValid","1");
//		List fileList = db.getList(attachmentMap, null);
//		for(int a=0;a<fileList.size();a++){
//			IMap  attachmentInfo= (IMap)fileList.get(a);
//			attachmentInfo.put("isValid",Constants.ISNOTVALID);
//			db.update(attachmentInfo);
//		};
		//上传检验文件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
			attachmentInfoMap.put("busId", busInrepairInfo.get("id"));
			attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.AttachmentInfo"));
			attachmentInfoMap.put("isValid",Constants.ISVALID);
			attachmentInfoMap.put("busName","com.BusInrepairInfo");
			attachmentInfoMap.put("busType", 1);
			//为创建数据赋值
			HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
			db.insert(attachmentInfoMap);
			}
		}
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String str="1".equals(in.get("repairStep"))?"不通过。":"通过。";
		String remark="厂检："+userMap.get("name")+"在"+dftow.format(new Date())+"对设备进行了进厂检验，意见为："+in.get("testRemark")+"；结果为："+str;
		LogBean logb=new LogBean(userMap,in.get("id").toString(), "com.BusInrepairInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		if("1".equals(in.get("repairStep"))){
			result.put("method.infoMsg","检验不通过！");
		}else{
			result.put("method.infoMsg","检验完成！");
		}
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 维修调派列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getWorkAssignList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//状态为1的不通过的
		in.put("repairStep","3");
		//查询分页结果
		Page page = db.pageQuery(in,"queryBusInrepairInfoList","com.BusInrepairInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 导向进修单详情信息页面
	 * @Description 
	 * @param @param in
	 * @param @return  
	 * @author cuijh  
	 * @return IMap<String,Object>
	 * @date Jul 17, 2013 5:39:18 PM
	 */
	public IMap<String, Object> toBusInrepairInfoPage(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//进修单信息
		IMap busInrepairInfo =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", in.get("id"));
		IMap busInrepairInfoMap = db.get(busInrepairInfo);
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentMap.put("busId",busInrepairInfo.get("id"));
		attachmentMap.put("busName","com.BusInrepairInfo");
		attachmentMap.put("isValid","1");
		List fileList = db.getList(attachmentMap, null);
		//进修单操作信息
		IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
		logRecordsMap.put("busId",busInrepairInfo.get("id"));
		logRecordsMap.put("busName","com.BusInrepairInfo");
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		List logRecordsList = db.getList(logRecordsMap, orderBean);
		result.put("busInrepairInfoMap", busInrepairInfoMap);
		result.put("logRecordsList", logRecordsList);
		result.put("fileList", fileList);
		return result;
	}
	/**
	 * 跳转到维修调派页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> toWorkAssign(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		// 工作流信息
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		//获取基本信息
		IMap busInrepairInfo =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", in.get("id"));
		IMap busInrepairInfoMap = db.get(busInrepairInfo);
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		//传入条件为机修业务的某一条数据
		attachmentMap.put("busId",busInrepairInfo.get("id"));
		attachmentMap.put("busName","com.BusInrepairInfo");
		attachmentMap.put("isValid","1");
		//获取附件列表
		List fileList = db.getList(attachmentMap, null);
		
		//根据设备判断是否有子节点，有的话就取下级节点的设备
		String equipmentid = (String) busInrepairInfoMap.get("equipmentId");
		IMap eqInfo =BeanFactory.getClassBean("com.EquipmentInfo");
		eqInfo.put("id", equipmentid);
		List eqList = db.getList(eqInfo, "equipmentChildList", "com.EquipmentInfo");
		if(!eqList.isEmpty()){
			result.put("equipmentList",eqList );
		}
		
		//返回值
		result.put("busInrepairInfoMap",busInrepairInfoMap );
		result.put("fileList", fileList);
		result.put("instanceId", instanceId);
		result.put("nodeId", nodeId);
		result.put("stepId", stepId);
		return result;
	}
	/**
	 * 跳转到选择车间页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Jul 17, 2013 14:07:44 AM
	 */
	public IMap<String, Object> toChooseDept(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取当前登录用户信息
		IMap userMap = (IMap) in.get("userMap");
		//获取当前登录用户所在的组织ID
		String orgCode = (String) userMap.get("orgId");
		//查询当前登录用户所在组织所在厂区
		//查询厂区下所有的车间，并封装成LIST传到页面供选择
		IMap condition = new DataMap();
		String codes[] = orgCode.replace(".",",").split(",");
		condition.put("orgCode", codes[0]+"."+codes[1]+"."+codes[2]);
		String deptIds = (String)in.get("deptIds");
		if(deptIds!=null&&deptIds.length()>2){
			//去掉拼接depId前后逗号，
			deptIds = deptIds.substring(1,deptIds.length()-1);
			condition.put("deptIds", deptIds);
		}
		//获取车间列表 同时获取车间的当前工卡号
		List<IMap> deptList = db.getList(condition, "getorgdeptList",null);
		result.put("deptList", deptList);
		result.put("rowId", in.get("rowId"));
		return result;
	}
	/**
	 * 
	 * @Description 保存维修派工结果
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Jul 18, 2013 11:31:01 AM
	 */
	public IMap<String, Object> saveWorkAssign(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取当前登录用户信息
		IMap userMap = (IMap) in.get("userMap");
		//获取固定6位编码
		String[] codes  = (String[]) in.get("code");
		//获取卡号
		//String[] firstIds  = (String[]) in.get("firstId");
		//获取主卡号
		String[] domainNos  = (String[]) in.get("domainNo");
		//获取车间名称
		String[] deptNames  = (String[]) in.get("deptName");
		//获取车间ID
		String[] deptIds  = (String[]) in.get("deptId");
		//获取设备ID
		String[] equipmentIds  = (String[]) in.get("equipmentIds");
		//进修单信息ID
		String inrepairId  = (String) in.get("inrepairId");
		//获取
		String[] serialnum  = (String[]) in.get("serialnum");
		//获取组织ID
		String[] orgId  = (String[]) in.get("orgId");
		//
		String outStr = "";
		//循环结果，将结果插入到派工表中
		//一个工卡对应一个流程
		for(int i=0;i<codes.length;i++){
			IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
			String workCard =  codes[i]+"/"+domainNos[i];
			String deptName = deptNames[i];
			String deptId = deptIds[i];
			workCardMap.put("workCard", workCard);
			workCardMap.put("assistCard", codes[i]);
			workCardMap.put("mainCard", domainNos[i]);
			List list = db.getList(workCardMap, null);
			if(!list.isEmpty()){
				outStr += workCard +"，";
				continue;
			}
			workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.BusWorkCard"));
			workCardMap.put("deptName", deptName);
			workCardMap.put("orgId", deptId);
			
			//业务Id
			String busId = "";
			//业务名称
			String busName ="";
			//流程描述
			String flowDesc ="";
			//流程编号 mechanic
			String flowCode ="";
			//查看流程URL
			String flowUrl="";
			//分别判断五个车间
			IMap busMap = new DataMap();
			if(deptName.indexOf("机修")>-1){
				//如果为机修，则将基本信息插入到机修信息表中
				busMap = BeanFactory.getClassBean("com.BusMechanic");
				busMap.put("inrepairId",inrepairId);
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusMechanic");
				busName ="com.BusMechanic";
				flowDesc = "机修";
				flowCode = "mechanic";
				flowUrl = "/api/busmechanic/toBusMechanicDetailsPage";
			}else if(deptName.indexOf("电修")>-1){
				//如果为机修，则将基本信息插入到机修信息表中
				busMap = BeanFactory.getClassBean("com.BusElectrical");
				busMap.put("inrepairId",inrepairId);
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusElectrical");
				busName ="com.BusElectrical";
				flowDesc = "电修";
				flowCode = "electrical";
				flowUrl = "/api/buselectrical/toBusElectricalDetailsPage";
				
			}else if(deptName.indexOf("钻修")>-1){
				//如果为机修，则将基本信息插入到机修信息表中
				busMap = BeanFactory.getClassBean("com.BusDrilling");
				busMap.put("inrepairId",inrepairId);
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusDrilling");
				busName ="com.BusDrilling";
				flowDesc = "钻修";
				flowCode = "drilling";
				flowUrl = "/api/busdrilling/toBusDrillingDetailsPage";
			}else if(deptName.indexOf("铆焊")>-1){
				busMap = BeanFactory.getClassBean("com.BusRivetweld");
				busMap.put("inrepairId",inrepairId);
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusRivetweld");
				busName ="com.BusRivetweld";
				flowDesc = "铆焊";
				flowCode = "rivetweld";
				flowUrl = "/api/busrivetweld/toBusRivetweldDetailsPage";
			}else if(deptName.indexOf("机加")>-1){
//				busMap = BeanFactory.getClassBean("com.MechTreatment");
//				busMap.put("inrepairId",inrepairId);
//				busId =HelperApp.getAutoIncrementPKID(HelperApp
//						.getPackageName(), "com.MechTreatment");
//				busName ="com.MechTreatment";
//				flowDesc = "机加";
//				flowCode = "mechtreatment";
//				flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
			}
			
			// 工作流信息
			if(deptName.indexOf("机加")>-1){
			}else{
				//进修单信息
				IMap busInrepairInfoMap = BeanFactory.getClassBean("com.BusInrepairInfo");
				busInrepairInfoMap.put("id", inrepairId);
				busInrepairInfoMap = db.get(busInrepairInfoMap);
				//修改进修单 状态 4
				busInrepairInfoMap.put("distributeRemark",in.get("distributeRemark"));
				busInrepairInfoMap.put("repairStep",4);
				busInrepairInfoMap.put("workcard",domainNos[i]);
				db.update(busInrepairInfoMap);
				//工卡信息
				workCardMap.put("busId", busId);
				workCardMap.put("busName",busName);
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
				db.insert(workCardMap);
				//插入业务信息
				IMap equipmentInfo=BeanFactory.getClassBean("com.EquipmentInfo");
				equipmentInfo.put("id", equipmentIds[i]);
				equipmentInfo=db.get(equipmentInfo);
				busMap.put("equipmentName", equipmentInfo.get("equipmentName"));
				busMap.put("equipmentModel", equipmentInfo.get("equipmentModel"));
				busMap.put("equipmentValue", equipmentInfo.get("value"));
				busMap.put("unity", equipmentInfo.get("unity"));
				String repairType = (String) busInrepairInfoMap.get("repairType");
				if("1".equals(repairType)){
					busMap.put("quotaHour", equipmentInfo.get("quotaHour"));
					busMap.put("repairValue", equipmentInfo.get("majorCost"));
				}else if("2".equals(repairType)){
					busMap.put("quotaHour", equipmentInfo.get("mediumHour"));
					busMap.put("repairValue", equipmentInfo.get("mediumCost"));
				}else{
					busMap.put("quotaHour", equipmentInfo.get("checkHour"));
					busMap.put("repairValue", equipmentInfo.get("checkCost"));
				}
				busMap.put("repairType", busInrepairInfoMap.get("repairType"));
				busMap.put("repairCategory", busInrepairInfoMap.get("repairCategory"));
				busMap.put("repairProperties", busInrepairInfoMap.get("repairProperties"));
				busMap.put("bodyCode", busInrepairInfoMap.get("bodyCode"));
				busMap.put("selfCode", busInrepairInfoMap.get("selfCode"));
				busMap.put("id", busId);
				busMap.put("planEndTime", in .get("planEndTime"));
				busMap.put("workCard",workCard);
				busMap.put("orgId", deptId);
				busMap.put("deptName", deptName);
				busMap.put("teamNumber", busInrepairInfoMap.get("deptFrom"));
				busMap.put("isValid",1);
				busMap.put("repairState",1);
				busMap.put("mainCard", domainNos[i]);
				busMap.put("assistCard", codes[i]);
				//收益类型0：内部创收  1：外部创收
				busMap.put("revenueType",0);
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),busMap);
				db.insert(busMap);
				// 发起工作流
				IMap wfmap = new DataMap();
				//wfmap.put("result", flag);// 判断是否受理
				//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
				IMap condition = new DataMap();
				//获取基础组织编码
				condition.put("orgId", deptId);
				condition.put("postCode", "车间派工");
				List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
				if(userList.isEmpty()){
					throw new BusinessException("请联系管理员指派车间的车间派工人员！");
				}
				String cbids = "";
				for(int j=0;j<userList.size();j++){
					IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
					usersMap = userList.get(j);
					cbids = cbids+ (String)usersMap.get("userId")+",";
				}
				String[] nextUserIds = cbids.split(",");
				IMap wf_users = new DataMap();
				wf_users.put("u", nextUserIds);
				JSONObject str_wf_user = JSONObject.fromObject(wf_users);
				// 发起工作流
				wfmap.put("assignedbyuser", str_wf_user.toString());
				SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
				String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"调度此业务到"+deptName+"。";
				
				flowDesc = workCard+"("+dftow.format(new Date())+")"+equipmentInfo.get("equipmentName") + "设备的"+flowDesc;

				FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
						flowDesc,flowUrl,0,wfmap);
				
				LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
				LogRecordsService.saveOperationLog(logb,db);
			}
			//保存成功 则更新baseOrg表中的序列号
			IMap baseMap = BeanFactory.getClassBean("com.baseorganization");
			baseMap.put("id", orgId[i]);
			baseMap.put("serialnum", serialnum[i]);
			
			db.update(baseMap);
			
		}
		if(!"".equals(outStr)&&outStr!=null){
			outStr = outStr.substring(0,outStr.length()-1);
			throw new BusinessException(outStr+"工卡号已使用!");
		}
		
		
		
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 添加外部创收调派页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> addExternalRevenue(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap busInrepairInfoMap = (IMap) in.get("BusInrepairInfo");
		IMap userMap = (IMap) in.get("userMap");
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		String busInrepairInfoId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busInrepairInfoId );
		busInrepairInfoMap.put("orgId",orgCode );
		busInrepairInfoMap.put("isValid",Constants.ISVALID);
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),busInrepairInfoMap);
		//上传附件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
				attachmentInfoMap.put("busId",busInrepairInfoId);
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.BusInrepairInfo");
				attachmentInfoMap.put("busType",1);
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
			}
		}
		//1:进修单 2：厂检 3：调度
		busInrepairInfoMap.put("repairStep","4");
		db.insert(busInrepairInfoMap);
		//获取卡号
		String[] codes  = (String[]) in.get("codes");
		//获取卡号
		String[] firstIds  = (String[]) in.get("firstId");
		//获取主卡号
		String[] domainNos  = (String[]) in.get("domainNo");
		//获取车间名称
		String[] deptNames  = (String[]) in.get("deptName");
		//获取车间ID
		String[] deptIds  = (String[]) in.get("deptId");
		//获取设备ID
		String[] equipmentIds  = (String[]) in.get("equipmentIds");
		//循环结果，将结果插入到派工表中
		//一个工卡对应一个流程
		String outStr="";
		for(int i=0;i<firstIds.length;i++){
			IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
			String workCard =  codes[i]+firstIds[i]+"/"+domainNos[i];
			workCardMap.put("assistCard", codes[i]+firstIds[i]);
			workCardMap.put("mainCard", domainNos[i]);
			workCardMap.put("workCard", workCard);
			List list = db.getList(workCardMap, null);
			if(!list.isEmpty()){
				outStr += workCard +"，";
				continue;
			}
			String deptName = deptNames[i];
			String deptId = deptIds[i];
			workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.BusWorkCard"));
			workCardMap.put("deptName", deptName);
			workCardMap.put("orgId", deptId);
			//业务Id
			String busId = "";
			//业务名称
			String busName ="";
			//流程描述
			String flowDesc ="";
			//流程编号 mechanic
			String flowCode ="";
			//查看流程URL
			String flowUrl="";
			//分别判断五个车间
			IMap busMap = new DataMap();
			if(deptName.indexOf("机修")>-1){
				//如果为机修，则将基本信息插入到机修信息表中
				busMap = BeanFactory.getClassBean("com.BusMechanic");
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusMechanic");
				busName ="com.BusMechanic";
				flowDesc = "机修";
				flowCode = "mechanic";
				flowUrl = "/api/busmechanic/toBusMechanicDetailsPage";
			}else if(deptName.indexOf("电修")>-1){
				//如果为机修，则将基本信息插入到机修信息表中
				busMap = BeanFactory.getClassBean("com.BusElectrical");
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusElectrical");
				busName ="com.BusElectrical";
				flowDesc = "电修";
				flowCode = "electrical";
				flowUrl = "/api/buselectrical/toBusElectricalDetailsPage";
				
			}else if(deptName.indexOf("钻修")>-1){
				//如果为机修，则将基本信息插入到机修信息表中
				busMap = BeanFactory.getClassBean("com.BusDrilling");
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusDrilling");
				busName ="com.BusDrilling";
				flowDesc = "钻修";
				flowCode = "drilling";
				flowUrl = "/api/busdrilling/toBusDrillingDetailsPage";
			}else if(deptName.indexOf("铆焊")>-1){
				busMap = BeanFactory.getClassBean("com.BusRivetweld");
				busId =HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusRivetweld");
				busName ="com.BusRivetweld";
				flowDesc = "铆焊";
				flowCode = "rivetweld";
				flowUrl = "/api/busrivetweld/toBusRivetweldDetailsPage";
			}else if(deptName.indexOf("机加")>-1){
//				busMap = BeanFactory.getClassBean("com.MechTreatment");
//				busId =HelperApp.getAutoIncrementPKID(HelperApp
//						.getPackageName(), "com.MechTreatment");
//				busName ="com.MechTreatment";
//				flowDesc = "机加";
//				flowCode = "mechtreatment";
//				flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
			}
			//工卡信息
			workCardMap.put("busId", busId);
			workCardMap.put("busName",busName);
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
			db.insert(workCardMap);
			//插入业务信息
			IMap equipmentInfo=BeanFactory.getClassBean("com.EquipmentInfo");
			equipmentInfo.put("id", equipmentIds[i]);
			equipmentInfo=db.get(equipmentInfo);
			busMap.put("equipmentName", equipmentInfo.get("equipmentName"));
			busMap.put("equipmentModel", equipmentInfo.get("equipmentModel"));
			busMap.put("equipmentValue", equipmentInfo.get("value"));
			busMap.put("unity", equipmentInfo.get("unity"));
			busMap.put("quotaHour", equipmentInfo.get("quotaHour"));
			busMap.put("repairType", busInrepairInfoMap.get("repairType"));
			if("1".equals(busInrepairInfoMap.get("repairType"))){
				busMap.put("repairValue", equipmentInfo.get("majorCost"));
			}else if("2".equals(busInrepairInfoMap.get("repairType"))){
				busMap.put("repairValue", equipmentInfo.get("mediumCost"));
			}else{
				busMap.put("repairValue", equipmentInfo.get("checkCost"));
			}
			busMap.put("id", busId);
			busMap.put("planEndTime", in .get("planEndTime"));
			busMap.put("inrepairId",busInrepairInfoId);
			busMap.put("workCard",workCard);
			busMap.put("orgId", deptId);
			busMap.put("isValid",1);
			busMap.put("repairState",1);
			busMap.put("mainCard", domainNos[i]);
			busMap.put("assistCard",codes[i]+firstIds[i]);
			//收益类型0：内部创收  1：外部创收
			busMap.put("revenueType",1);
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap),busMap);
			db.insert(busMap);
			// 发起工作流
			IMap wfmap = new DataMap();
			//wfmap.put("result", flag);// 判断是否受理
			//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
			IMap condition = new DataMap();
			//获取基础组织编码
			condition.put("orgId", deptId);
			condition.put("postCode", "车间主任");
			List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
			String cbids = "";
			for(int j=0;j<userList.size();j++){
				IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
				usersMap = userList.get(j);
				cbids = cbids+ (String)usersMap.get("userId")+",";
			}
			String[] nextUserIds = cbids.split(",");
			IMap wf_users = new DataMap();
			wf_users.put("u", nextUserIds);
			JSONObject str_wf_user = JSONObject.fromObject(wf_users);
			// 发起工作流
			wfmap.put("assignedbyuser", str_wf_user.toString());
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			// 工作流信息
			FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
					workCard+"("+dftow.format(new Date())+")"+equipmentInfo.get("equipmentName") + "设备的"+flowDesc,flowUrl,0,wfmap);
			String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"调度此业务到"+deptName+"。";
			LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
		}
		if(!"".equals(outStr)&&outStr!=null){
			outStr = outStr.substring(0,outStr.length()-1);
			throw new BusinessException(outStr+"工卡号已使用!");
		}
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 登记机加信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> addMachiningMessage(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap machiningMessageMap = (IMap) in.get("machiningMessage");
		IMap userMap = (IMap) in.get("userMap");
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		String machiningMessageId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.MachiningMessage");
		machiningMessageMap.put("id",machiningMessageId );
		machiningMessageMap.put("orgId",orgCode );
		machiningMessageMap.put("isValid",Constants.ISVALID);
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),machiningMessageMap);
		//上传附件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
				attachmentInfoMap.put("busId",machiningMessageId);
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.MachiningMessage");
				attachmentInfoMap.put("busType",1);
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
			}
		}
		//1:进修单 2：内部车间调派 3：调度
		machiningMessageMap.put("repairStep","3");
		db.insert(machiningMessageMap);
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"添加了机加信息登记单。";
		LogBean logb=new LogBean(userMap,machiningMessageId, "com.MachiningMessage","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 机加调派列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getMachiningWorkAssignList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//状态为1的不通过的
		in.put("repairStep","'3','2'");
		//查询分页结果
		Page page = db.pageQuery(in,"queryMachiningList","com.MachiningMessage","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 跳转到机加调派页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> toMachiningAssign(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取基本信息
		IMap machining =BeanFactory.getClassBean("com.MachiningMessage");
		machining.put("id", in.get("id"));
		IMap machiningMap = db.get(machining);
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		//传入条件为机修业务的某一条数据
		attachmentMap.put("busId",machiningMap.get("id"));
		attachmentMap.put("busName","com.MachiningMessage");
		attachmentMap.put("isValid","1");
		//获取附件列表
		List fileList = db.getList(attachmentMap, null);
		//返回值
		result.put("machiningMap",machiningMap );
		result.put("fileList", fileList);
		return result;
	}
	/**
	 * 保存机加调派结果
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @date Jul 18, 2013 11:31:01 AM
	 */
	public IMap<String, Object> addWorkCode(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取当前登录用户信息
		IMap userMap = (IMap) in.get("userMap");
		//进修单信息ID
		String inrepairId  = (String) in.get("inrepairId");
		//循环结果，将结果插入到派工表中
		//一个工卡对应一个流程
			IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
			String workCard = in.get("firstId")+"/"+in.get("domainNo");
			workCardMap.put("workCard", workCard);
			workCardMap.put("assistCard",in.get("firstId"));
			workCardMap.put("mainCard",in.get("domainNo"));
			List list  = db.getList(workCardMap, null);
			if(!list.isEmpty()){
				throw new BusinessException(workCard+"工卡号已使用!");
			}
			//获取当前登录用户所在的组织ID
			String orgCode = (String) userMap.get("orgId");
			//查询当前登录用户所在组织所在厂区
			//查询厂区下所有的车间，并封装成LIST传到页面供选择
			IMap condition = new DataMap();
			condition.put("orgCode", orgCode);
			List<IMap> deptList = db.getList(condition, "getMachiningList",null);
			String deptId = deptList.get(0).get("org_code").toString();
			workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.BusWorkCard"));
			workCardMap.put("deptName", "机加车间");
			workCardMap.put("orgId", deptId);
			//业务Id
			String busId = "";
			//业务名称
			String busName ="";
			//流程描述
			String flowDesc ="";
			//流程编号 mechanic
			String flowCode ="";
			//查看流程URL
			String flowUrl="";
			//分别判断五个车间
			IMap busMap = new DataMap();
			busMap = BeanFactory.getClassBean("com.MechTreatment");
			busId =HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.MechTreatment");
			busName ="com.MechTreatment";
			flowDesc = "机加";
			flowCode = "mechtreatment";
			flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
			//工卡信息
			workCardMap.put("busId", busId);
			workCardMap.put("busName",busName);
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
			db.insert(workCardMap);
			//插入业务信息
			busMap.put("id", busId);
			busMap.put("planEndTime", in .get("planEndTime"));
			busMap.put("inrepairId",inrepairId);
			busMap.put("workCard",workCard);
			busMap.put("orgId", deptId);
			busMap.put("isValid",1);
			busMap.put("repairState",1);
			busMap.put("assistCard",in.get("firstId"));
			busMap.put("mainCard",in.get("domainNo"));
			//收益类型0：内部创收  1：外部创收
			busMap.put("revenueType",0);
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),busMap);
			db.insert(busMap);
			// 发起工作流
			IMap wfmap = new DataMap();
			//wfmap.put("result", flag);// 判断是否受理
			//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
			//获取基础组织编码
			condition.put("orgId", deptId);
			condition.put("postCode", "车间派工");
			List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
			if(userList.isEmpty()){
				throw new BusinessException("请联系管理员指派机加车间的车间派工人员！");
			}
			String cbids = "";
			for(int j=0;j<userList.size();j++){
				IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
				usersMap = userList.get(j);
				cbids = cbids+ (String)usersMap.get("userId")+",";
			}
			String[] nextUserIds = cbids.split(",");
			IMap wf_users = new DataMap();
			wf_users.put("u", nextUserIds);
			JSONObject str_wf_user = JSONObject.fromObject(wf_users);
			// 发起工作流
			wfmap.put("assignedbyuser", str_wf_user.toString());
			//进修单信息
			IMap MachiningMap = BeanFactory.getClassBean("com.MachiningMessage");
			MachiningMap.put("id", inrepairId);
			MachiningMap = db.get(MachiningMap);
			//修改进修单 状态 4：调度结束    2：内部车间调派
			MachiningMap.put("distributeRemark",in.get("distributeRemark"));
			MachiningMap.put("repairStep",4);
			db.update(MachiningMap);
			// 工作流信息
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
					workCard+"("+dftow.format(new Date())+")"+MachiningMap.get("equipmentName") + "设备的"+flowDesc,flowUrl,0,wfmap);
			
			String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"调度此业务到机加车间。";
			LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 导向机加详情信息页面
	 * @Description 
	 * @param @param in
	 * @param @return  
	 * @author cuijh  
	 * @return IMap<String,Object>
	 * @date Jul 17, 2013 5:39:18 PM
	 */
	public IMap<String, Object> toMachiningPage(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//进修单信息
		IMap machiningMessage =BeanFactory.getClassBean("com.machiningMessage");
		machiningMessage.put("id", in.get("id"));
		IMap machiningMessageMap = db.get(machiningMessage);
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentMap.put("busId",machiningMessageMap.get("id"));
		attachmentMap.put("busName","com.MachiningMessage");
		attachmentMap.put("isValid","1");
		List fileList = db.getList(attachmentMap, null);
		//进修单操作信息
		IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
		logRecordsMap.put("busId",machiningMessageMap.get("id"));
		logRecordsMap.put("busName","com.MachiningMessage");
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		List logRecordsList = db.getList(logRecordsMap, orderBean);
		result.put("machiningMap", machiningMessageMap);
		result.put("logRecordsList", logRecordsList);
		result.put("fileList", fileList);
		return result;
	}
	/**
	 * 添加机加外部创收调派信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> addExternalMachining(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap machiningMessageMap = (IMap) in.get("machiningMessage");
		IMap userMap = (IMap) in.get("userMap");
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId")+".|JJ|";
		String machiningMessageId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.MachiningMessage");
		machiningMessageMap.put("id",machiningMessageId );
		machiningMessageMap.put("orgId",orgCode );
		machiningMessageMap.put("isValid",Constants.ISVALID);
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),machiningMessageMap);
		//上传附件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
				attachmentInfoMap.put("busId",machiningMessageId);
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.MachiningMessage");
				attachmentInfoMap.put("busType",1);
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
			}
		}
		//1:进修单 2：内部车间调派 3：调度
		machiningMessageMap.put("distributeRemark",in.get("distributeRemark"));
		machiningMessageMap.put("repairStep",4);
		db.insert(machiningMessageMap);
		IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
		String workCard = (String)in.get("code")+(String)in.get("firstId")+"/"+in.get("domainNo");
		workCardMap.put("workCard", workCard);
		workCardMap.put("assistCard",(String)in.get("code")+(String)in.get("firstId"));
		workCardMap.put("mainCard",in.get("domainNo"));
		List list = db.getList(workCardMap, null);
		if(!list.isEmpty()){
			throw new BusinessException(workCard+"工卡号已使用!");
		}
		//获取当前登录用户所在的组织ID
		//查询当前登录用户所在组织所在厂区
		//查询厂区下所有的车间，并封装成LIST传到页面供选择
		IMap condition = new DataMap();
		condition.put("orgCode", orgCode);
		List<IMap> deptList = db.getList(condition, "getMachiningList",null);
		String deptId = deptList.get(0).get("org_code").toString();
		workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusWorkCard"));
		workCardMap.put("deptName", "机加车间");
		workCardMap.put("orgId", deptId);
		//业务Id
		String busId = "";
		//业务名称
		String busName ="";
		//流程描述
		String flowDesc ="";
		//流程编号 mechanic
		String flowCode ="";
		//查看流程URL
		String flowUrl="";
		//分别判断五个车间
		IMap busMap = new DataMap();
		busMap = BeanFactory.getClassBean("com.MechTreatment");
		busId =HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.MechTreatment");
		busName ="com.MechTreatment";
		flowDesc = "机加";
		flowCode = "mechtreatment";
		flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
		//工卡信息
		workCardMap.put("busId", busId);
		workCardMap.put("busName",busName);
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
		db.insert(workCardMap);
		//插入业务信息
		busMap.put("id", busId);
		busMap.put("planEndTime", in .get("planEndTime"));
		busMap.put("inrepairId",machiningMessageId);
		busMap.put("workCard",workCard);
		busMap.put("orgId", deptId);
		busMap.put("isValid",1);
		busMap.put("repairState",1);
		busMap.put("assistCard",(String)in.get("code")+(String)in.get("firstId"));
		busMap.put("mainCard",in.get("domainNo"));
		//收益类型0：内部创收  1：外部创收
		busMap.put("revenueType",1);
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),busMap);
		db.insert(busMap);
		// 发起工作流
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
	    condition = new DataMap();
		//获取基础组织编码
		condition.put("orgId", deptId);
		condition.put("postCode", "车间派工");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		if(userList.isEmpty()){
			throw new BusinessException("请联系管理员添加机加车间车间派工人员!");
		}
		for(int j=0;j<userList.size();j++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(j);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		// 工作流信息
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
				workCard+"("+dftow.format(new Date())+")"+machiningMessageMap.get("equipmentName") + "设备的"+flowDesc,flowUrl,0,wfmap);
		String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"调度此业务到机加车间。";
		LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 导向修改机加信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> toUpdateExternalMachining(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//进修单信息
		IMap machiningMessage =BeanFactory.getClassBean("com.machiningMessage");
		machiningMessage.put("id", in.get("id"));
		IMap machiningMessageMap = db.get(machiningMessage);
		result.put("machiningMessageMap",machiningMessageMap );
		return result;
	}
	/**
	 * 修改机加信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 */
	public IMap<String, Object> updateExternalMachining(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap machiningMessageMap = (IMap) in.get("machiningMessage");
		IMap userMap = (IMap) in.get("userMap");
		//上传附件
		List attachmentInfoList=(List) in.get("attachmentInfo");
		for(int i=0;i<attachmentInfoList.size();i++){
			IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
			if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
				attachmentInfoMap.put("busId",machiningMessageMap.get("id"));
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.MachiningMessage");
				attachmentInfoMap.put("busType",1);
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
			}
		}
		//1:进修单 2：厂检 3：调度
		machiningMessageMap.put("distributeRemark",in.get("distributeRemark"));
		machiningMessageMap.put("repairStep",4);
		db.update(machiningMessageMap);
		IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
		String workCard = in.get("firstId")+"/"+in.get("domainNo");
		workCardMap.put("workCard", workCard);
		List list = db.getList(workCardMap, null);
		if(!list.isEmpty()){
			throw new BusinessException(workCard+"工卡号已使用!");
		}
		//获取当前登录用户所在的组织ID
		//查询当前登录用户所在组织所在厂区
		//查询厂区下所有的车间，并封装成LIST传到页面供选择
		IMap condition = new DataMap();
		condition.put("orgCode",machiningMessageMap.get("org_id").toString());
		List<IMap> deptList = db.getList(condition, "getMachiningList",null);
		String deptId = deptList.get(0).get("org_code").toString();
		workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusWorkCard"));
		workCardMap.put("deptName", "机加车间");
		workCardMap.put("orgId", deptId);
		//业务Id
		String busId = "";
		//业务名称
		String busName ="";
		//流程描述
		String flowDesc ="";
		//流程编号 mechanic
		String flowCode ="";
		//查看流程URL
		String flowUrl="";
		//分别判断五个车间
		IMap busMap = new DataMap();
		busMap = BeanFactory.getClassBean("com.MechTreatment");
		busId =HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.MechTreatment");
		busName ="com.MechTreatment";
		flowDesc = "机加";
		flowCode = "mechtreatment";
		flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
		//工卡信息
		workCardMap.put("busId", busId);
		workCardMap.put("busName",busName);
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
		db.insert(workCardMap);
		//插入业务信息
		busMap.put("id", busId);
		busMap.put("planEndTime", in .get("planEndTime"));
		busMap.put("inrepairId",machiningMessageMap.get("id"));
		busMap.put("workCard",workCard);
		busMap.put("orgId", deptId);
		busMap.put("isValid",1);
		busMap.put("repairState",1);
		//收益类型0：内部创收  1：外部创收
		busMap.put("revenueType",0);
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),busMap);
		db.insert(busMap);
		// 发起工作流
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
	    condition = new DataMap();
		//获取基础组织编码
		condition.put("orgId", deptId);
		condition.put("postCode", "车间主任");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int j=0;j<userList.size();j++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(j);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		// 工作流信息
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
				workCard+"("+dftow.format(new Date())+")"+machiningMessageMap.get("equipmentName") + "设备的"+flowDesc
				,flowUrl,0,wfmap);
		String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"调度此业务到机加车间。";
		LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
		/**
	 * 调度管理列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getFactoryList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		in.put("repairStep", 5);
		//查询分页结果
		Page page = db.pageQuery(in,"getFactoryList","com.BusInrepairInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 调度管理
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 19, 2013 9:38:03 AM
	 */
	public IMap doSchedulingManage(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		//wfmap.put("result", flag);// 判断是否受理
		//获取基础组织编码
		IMap busInrepairInfo = BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", in.get("id"));
		busInrepairInfo.put("factoryWhere", in.get("factoryWhere"));
		if(in.get("factoryDate")!=null&&!"".equals(in.get("factoryDate"))){
			busInrepairInfo.put("factoryDate", in.get("factoryDate"));
		}
		
		String rem="";
		
		if("1".equals(in.get("result"))){
			rem="把设备发给监造主管进行出厂。";
			busInrepairInfo.put("repairStep",6);//状态为2调度发现有错误然后提交给监造出厂
		}else{
			rem="确认把设备出厂到"+in.get("factoryWhere")+"。";
			busInrepairInfo.put("repairStep",7);//状态为0调度确认自己出厂
		}
		db.update(busInrepairInfo);
		//log日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="调度："+userMap.get("name")+"在"+sdf.format(new Date())+rem;
		LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.BusInrepairInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "操作成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 监造管理列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getEngineerList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		in.put("repairStep", 6);
		//查询分页结果
		Page page = db.pageQuery(in,"getFactoryList","com.BusInrepairInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	
	/**
	 * 
	 *  描述: 
	 *  方法: saveEngineerManage方法
	 *  作者: duch
	 *  时间: Feb 25, 2014
	 *  版本: 1.0
	 */
	public IMap saveEngineerManage(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		IMap factoryMap = (IMap) in.get("engineerManage");
		factoryMap.put("repairStep",7);
		db.update(factoryMap);
		
		result.put("method.url", (String)in.get("url"));
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		return result;
	}
	
	
	
	public IMap toChooseEquips(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		
		//查询分页结果
		List<IMap> list = db.getList(in,"queryEquipmentInfoList","com.EquipmentInfo");


		List<IMap> rlist = new ArrayList<IMap>();
		//组合设备名称和编号
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				IMap map = (IMap) list.get(i);
				map.put("wholename", (String)map.get("equipmentCode")+" "+(String)map.get("equipmentName"));
				rlist.add(map);
			}
		}
		result.put("equipmentList", rlist);
		result.put("rowId", in.get("rowId"));
		return result;
	}
	/**
	 * 维修结算管理列表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 23, 2013 4:41:56 PM
	 */
	public IMap getInrepairMainCardList(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap userMap = (IMap) in.get("userMap");
		in.put("orgId", userMap.get("orgId"));
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//查询分页结果
		OrderBean bean = new OrderBean();
		bean.put("createDate",OrderBean.ASC);
		Page page = db.pageQuery(in,"getInrepairMainCardList",null,"ID",currentPageNO,perCount,null);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 根据工卡号细分
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 23, 2013 4:43:25 PM
	 */
	public IMap getRepairSettlementList(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		in.put("orgId", userMap.get("orgId"));
		List busRepairSettlementList =db.getList(in,"getRepairSettlementList",null);
		result.put("busRepairSettlementList",busRepairSettlementList);
		return result;
	}
	/**
	 * 费用结算
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * x@date Aug 23, 2013 4:43:25 PM
	 */
	public IMap updateRepairSettlement(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap)in.get("userMap");
		List settlementList = (List)in.get("busRepairSettlement");
		if(!settlementList.isEmpty()){
			for(int i=0;i<settlementList.size();i++){
				IMap settlement = (IMap)settlementList.get(i);
				HelperDB.setModifyInfo(HelperApp.getUserName(userMap),settlement);
				db.update(settlement);
			}
		}
		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
		return result;
	}
	
	
	public IMap saveResupplyOutStore(IMap in){
		IMap result = new DataMap();
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		IMap orgInfo =BeanFactory.getClassBean("com.baseorganization");
		orgInfo.put("orgCode", orgId);
		orgInfo = db.get(orgInfo);
		String busId = (String) in.get("busId");
		String busName = (String) in.get("busName");
		String workCard = (String) in.get("workCard");
		String orgName = (String) orgInfo.get("orgName");
		//获取页面上填写的出库数量
		List<IMap> outList = (List<IMap>) in.get("outStoreInfo");
		//按照先进先出的原则，进行材料的增减
		//循环页面返回的值
		//判断物料编码相同的，则取数量进行对比，
		//如果第一条数量大于页面填写数量，剩余数量为第一条数量减去填写数量
		//如果第一条数量等于页面填写数量，剩余数量为0 并将其置为无效
		//依次递归，知道页面填写数量减为0为止
		String outStr = "";
		for(int i=0;i<outList.size();i++){
			//获取库存中某一种材料的数量
			IMap tempout = outList.get(i);
			String outString = "";
			tempout.put("busId", busId);
			tempout.put("busName", busName);
			tempout.put("workCard", workCard);
			String code = (String) tempout.get("materialCode");
			//获取材料库存材料
			IMap selMap =BeanFactory.getClassBean("com.StoreDetail");
			selMap.put("materialCode", code);
			selMap.put("orgId", orgId);
			selMap.put("isValid", Constants.ISVALID);
			OrderBean ob = new OrderBean();
			ob.put("createDate", OrderBean.DESC);
			List<IMap> storeList = db.getList(selMap, ob);
			//判断如果库存为空则提示此材料库存不足
			if(storeList.isEmpty()){
				outStr += (String)tempout.get("materialDetail")+",";
			}else{
				//获取页面填写的出库数量 转换为int
				double outNum = Double.valueOf((String) tempout.get("storeNum"));
				//循环库存结果，对比数量
				int j = 0;
				double store = 0;
				double amount = 0;
				//对库存结果进行递归直到出库数量为0为止
				//插入到出库信息表
				tempout.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreInfo"));
				tempout.put("workCard", in.get("workCard"));
				tempout.put("orgId", orgId);
				tempout.put("deptName", orgName);
				HelperDB.setCreate(HelperApp.getUserName(userMap),tempout);
				
				db.insert(tempout);
				
				//对比库存数量 从库存中减除
				outStr += saveRatioStore(storeList,j,outNum,store,outString,tempout,userMap,amount);
			}
		}
		if(!"".equals(outStr)&&outStr!=null){
			outStr = outStr.substring(0,outStr.length()-1);
			throw new BusinessException("材料"+outStr+"库存不足!");
		}else{
			//成功出库，当前出库计划，修改状态
			IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			busPlanInfoMap.put("busId",busId);
			busPlanInfoMap.put("busName","com.BusElectrical");
			busPlanInfoMap.put("isValid", 1);
			List busPlanInfoMapList = db.getList(busPlanInfoMap,null);
			if(busPlanInfoMapList!=null&&busPlanInfoMapList.size()>0){
				busPlanInfoMap = (IMap)busPlanInfoMapList.get(0);
				busPlanInfoMap.put("isValid", 0);
				db.update(busPlanInfoMap);
			}
		}
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"将物料出库。";
		LogBean logb=new LogBean(userMap,busId, "com.BusElectrical","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		
		result.put("method.url", (String)in.get("url"));
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		return result;
	}
	/**
	 * 
	 * @Description 在库存数量中减去单机应用的出库数量,将材料对应到单机
	 * @param @param storeList
	 * @param @param material    
	 * @return void   
	 * @date Jul 24, 2013 9:32:27 AM
	 */
	private String saveRatioStore(List<IMap> storeList,int j,double out,double store,String outString,IMap outStore,IMap userMap,double amount) {
		IMap storeMap =BeanFactory.getClassBean("com.StoreDetail");
		IMap outstoreMap =BeanFactory.getClassBean("com.outStoreDetail");
		//如果J小于storeList的长度 则进行循环
		if(j<storeList.size()){
			storeMap = storeList.get(j);
			double jstore = (Double)storeMap.get("num");
			store = store + jstore;
			//如果最近的库存数量大于出库数量，则减去库存数量，并更新数据库
			if(store > out){
				storeMap.put("num", store-out);
				db.update(storeMap);
			//在出库表中插入一条数据
				outstoreMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreDetail"));
				outstoreMap.put("materialClass", outStore.get("materialClass"));
				outstoreMap.put("materialGroup", outStore.get("materialGroup"));
				outstoreMap.put("materialDetail", outStore.get("materialDetail"));
				outstoreMap.put("materialCode", outStore.get("materialCode"));
				outstoreMap.put("workCard", outStore.get("workCard"));
				outstoreMap.put("unity", outStore.get("unity"));
				outstoreMap.put("unitPrice", storeMap.get("unitPrice"));
				outstoreMap.put("amount", (jstore-(store-out))*((Double)storeMap.get("unitPrice")));
				outstoreMap.put("materialNum", jstore-(store-out));
				outstoreMap.put("orgId", outStore.get("orgId"));
				outstoreMap.put("deptName", outStore.get("deptName"));
				outstoreMap.put("busId", outStore.get("busId"));
				outstoreMap.put("busName", outStore.get("busName"));
				outstoreMap.put("outId", outStore.get("id"));
				outstoreMap.put("outType", storeMap.get("materialType"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreMap);
				
				amount = amount +out*((Double)storeMap.get("unitPrice"));
				
				db.insert(outstoreMap);
				
			}else if(store == out){
			//如果最近库存数量等于出库数量，置为0，并将数据置为无效
				storeMap.put("num", store-out);
				storeMap.put("isValid", Constants.ISNOTVALID);
				db.update(storeMap);
				
				outstoreMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreDetail"));
				outstoreMap.put("materialClass", outStore.get("materialClass"));
				outstoreMap.put("materialGroup", outStore.get("materialGroup"));
				outstoreMap.put("materialDetail", outStore.get("materialDetail"));
				outstoreMap.put("materialCode", outStore.get("materialCode"));
				outstoreMap.put("workCard", outStore.get("workCard"));
				outstoreMap.put("unity", outStore.get("unity"));
				outstoreMap.put("materialNum", jstore-(store-out));
				outstoreMap.put("unitPrice", storeMap.get("unitPrice"));
				outstoreMap.put("amount", (jstore-(store-out))*((Double)storeMap.get("unitPrice")));
				outstoreMap.put("orgId", outStore.get("orgId"));
				outstoreMap.put("deptName", outStore.get("deptName"));
				outstoreMap.put("busId", outStore.get("busId"));
				outstoreMap.put("busName", outStore.get("busName"));
				outstoreMap.put("outId", outStore.get("id"));
				outstoreMap.put("outType", storeMap.get("materialType"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreMap);
				
				amount = amount + out*((Double)storeMap.get("unitPrice"));
				
				db.insert(outstoreMap);
			}else if(store < out){
			//如果最近库存数量小于出库数量，则将最近库存置为0，并将数据置为无效。且进行下次递归
				outstoreMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreDetail"));
				outstoreMap.put("materialClass", outStore.get("materialClass"));
				outstoreMap.put("materialGroup", outStore.get("materialGroup"));
				outstoreMap.put("materialDetail", outStore.get("materialDetail"));
				outstoreMap.put("materialCode", outStore.get("materialCode"));
				outstoreMap.put("workCard", outStore.get("workCard"));
				outstoreMap.put("unity", outStore.get("unity"));
				outstoreMap.put("materialNum", jstore);
				outstoreMap.put("unitPrice", storeMap.get("unitPrice"));
				outstoreMap.put("amount", jstore*((Double)storeMap.get("unitPrice")));
				outstoreMap.put("orgId", outStore.get("orgId"));
				outstoreMap.put("deptName", outStore.get("deptName"));
				outstoreMap.put("busId", outStore.get("busId"));
				outstoreMap.put("busName", outStore.get("busName"));
				outstoreMap.put("outId", outStore.get("id"));
				outstoreMap.put("outType", storeMap.get("materialType"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreMap);
				
				db.insert(outstoreMap);
				amount = amount + jstore*((Double)storeMap.get("unitPrice"));
				
				storeMap.put("num", 0);
				storeMap.put("isValid", Constants.ISNOTVALID);
				db.update(storeMap);
				
				j=j+1;
				outString += saveRatioStore(storeList,j,out,store,outString,outStore,userMap,amount);
				System.out.print(outString);
			}
			if(amount!=0&&"".equals(outString)){
				outStore.put("amount", amount);
				db.update(outStore);
			}
		}else{
			storeMap = storeList.get(j-1);
			outString += (String)storeMap.get("materialDetail") +",";
		}
		return outString;
	}
	/**
	 * 跳转选择补充材料页面
	 * @param in
	 * @return
	 */
	public IMap toResupplyEdit(IMap in){
		IMap result = new DataMap();
		String tableName = (String)in.get("tableName");
		IMap selectMap = BeanFactory.getClassBean(tableName);
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap busMap = db.get(selectMap);
		result.put("busMap",busMap);
		//操作详情列表
		IMap condition = new DataMap();
		condition.put("busIdOne",busMap.get("id"));
		condition.put("busNameOne",tableName);
		//进修单
		condition.put("busIdTow",busMap.get("inrepairId"));
		condition.put("busNameTow","com.BusInrepairInfo");
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		//进修单基本信息
		IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busMap.get("inrepairId"));
		busInrepairInfoMap =db.get(busInrepairInfoMap);
		result.put("busInrepairInfoMap",busInrepairInfoMap);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		result.put("tableName",tableName);
		return result;
	}
	
	/**
	 * 所以车间在修设备的材料信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getInrepairListByState(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("org_name",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//查询分页结果
		Page page = db.pageQuery(in,"getOrgMaterialList","","id",currentPageNO,perCount,orderBean);
		//List orgList = db.getList(null, "getOrgList","");
		//回写
		page.setAction(request);
		result.put("page", page);
		//result.put("orgList",orgList);
		return result;
	}
	
	/**
	 * 打印页面
	 * @param in
	 * @return
	 */
	public IMap toPrint(IMap in){
		IMap result = new DataMap();
		String tableName = (String)in.get("tableName");
		IMap selectMap = BeanFactory.getClassBean(tableName);
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap busMap = db.get(selectMap);
		result.put("busMap",busMap);
		//进修单基本信息
		IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busMap.get("inrepairId"));
		busInrepairInfoMap =db.get(busInrepairInfoMap);
		result.put("busInrepairInfoMap",busInrepairInfoMap);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		result.put("tableName",tableName);
		List materialPlanList =db.getList(in, "getInrepairMaterialList","");
		result.put("materialList", materialPlanList);
		return result;
	}
	/**
	 * 根据厂区编号获取车间信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author gaoy
	 * @date Mar 5, 2014 12:45:27 PM
	 */
	public IMap getWorkshopList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		String cq = (String) in.get("cq");
		IMap map = new DataMap();
		map.put("orgcode", cq);
		List workshopList =db.getList(map, "getWorkshopList","");
		result.put("workshopList", workshopList);
		return result;
	}
	/**
	 * 跳转库存信息页
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author gaoy
	 * @date Mar 5, 2014 3:28:27 PM
	 */
	public IMap toStoreInfo(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		result.put("cj1", in.get("cj1"));
		return result;
	}
	
	/**
	 * 
	 * @Description 库存列表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author gaoy
	 * @date Mar 5, 2014 12:45:27 PM
	 */
	public IMap getStoreList(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询分页结果
		IMap storeDetailMap = (IMap)in.get("storeDetail");
		//当前登录用户
		Page page = db.pageQuery(storeDetailMap,"queryStoreDetailList1","com.StoreDetail","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 库存修改，记录出库明细
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author gaoy
	 * @date Mar 5, 2014 6:10:33 PM
	 */
	public IMap<String, Object> saveOutStoreDetail(IMap<String, Object> in) {
		
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) in.get("orgId");
		//获取材料计划列表
		List<IMap> storeList = (List<IMap>) in.get("storeList");
		//获取车间库存信息
		List<IMap> storeDetailList = (List<IMap>) in.get("storeDetailList");
		//转入车间，库存信息
		List<IMap> inStoreDetailList = (List)in.get("inStoreDetailList");
		//deptName 部门名称
		IMap orgMap = BeanFactory.getClassBean("com.baseorganization");
		orgMap.put("orgCode", orgId);
		List<IMap> orgList = db.getList(orgMap,null);
		String orgName = "";
		if(orgList.size()>0){
			orgName = (String)(orgList.get(0)).get("orgName");
		}
		for (int i = 0 ;i<storeList.size();i++){
			//材料出库明细
			IMap map = storeList.get(i);
			IMap outstoreDetailMap =BeanFactory.getClassBean("com.outStoreDetail");
			//库存信息
			IMap storeMap =storeDetailList.get(i);
			storeMap = db.get(storeMap);
			double num = Double.parseDouble((storeMap.get("num").toString()));
			double materialNum = Double.parseDouble((map.get("materialNum").toString()));
			if(num < materialNum){
				throw new BusinessException("转入数量不能大于实际库存数量!");
			}
			storeMap.put("num",num-materialNum);
			// 修改转出车间库存数量
			db.update(storeMap);
			//转入车间，新增库存信息
			IMap inStoreDetail = inStoreDetailList.get(i);
			inStoreDetail.put("orgId", orgId);
			inStoreDetail.put("deptName",orgName);
			inStoreDetail.put("id",HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.StoreDetail"));
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), inStoreDetail);
			db.insert(inStoreDetail);
			Double unitPrice = Double.parseDouble(map.get("unitPrice").toString());
			//插入出库信息
			IMap outStoreInfo = BeanFactory.getClassBean("com.OutStoreInfo");
			String outId =HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.OutStoreInfo");
			outStoreInfo.put("id",outId);
			outStoreInfo.put("materialDetail", map.get("materialDetail"));
			outStoreInfo.put("materialClass", map.get("materialClass"));
			outStoreInfo.put("materialGroup", map.get("materialGroup"));
			outStoreInfo.put("materialCode", map.get("materialCode"));
			outStoreInfo.put("storeNum", map.get("materialNum"));
			outStoreInfo.put("amount", materialNum*unitPrice);
			outStoreInfo.put("orgId", orgId);
			outStoreInfo.put("deptName",orgName);
			HelperDB.setCreate(HelperApp.getUserName(userMap), outStoreInfo);
			db.insert(outStoreInfo);
			// 插入出库明细
			outstoreDetailMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.outStoreDetail"));
			outstoreDetailMap.put("materialClass", map.get("materialClass"));
			outstoreDetailMap.put("materialGroup", map.get("materialGroup"));
			outstoreDetailMap.put("materialDetail", map.get("materialDetail"));
			outstoreDetailMap.put("materialCode", map.get("materialCode"));
			outstoreDetailMap.put("workCard", "");// 无工卡号
			outstoreDetailMap.put("unity", map.get("unity"));
			outstoreDetailMap.put("materialNum", map.get("materialNum"));
			outstoreDetailMap.put("unitPrice", map.get("unitPrice"));
			outstoreDetailMap.put("amount", materialNum*unitPrice);
			outstoreDetailMap.put("orgId", orgId);
			outstoreDetailMap.put("deptName", orgName);
			outstoreDetailMap.put("busId", "");
			outstoreDetailMap.put("busName", "");
			outstoreDetailMap.put("outId", outId);
			//出库类型 1 领用
			outstoreDetailMap.put("outType", "1");
			HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreDetailMap);
			
			db.insert(outstoreDetailMap);
			
		}
		
		result.put("method.infoMsg", "提交完成!");
		return result;
	}
	/**
	 * 调度需要调派的加工件列表，选择后进行派工
	 * @param in
	 * @return
	 */
	public IMap<String, Object> tolistjg(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap mechdeptMap = BeanFactory.getClassBean("com.mechdept");
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		
		mechdeptMap.put("orgid", orgId);
		mechdeptMap.put("isappoint", "0");
		List<IMap> mechList = db.getList(mechdeptMap,"getmechdeptlist","com.mechdept");
		
		//根据用户的组织结构代码 获取本厂的车间
		IMap condition = new DataMap();
		//String codes[] = orgId.replace(".",",").split(",");
		condition.put("orgCode", orgId);
		//获取车间列表 同时获取车间的当前工卡号
		List<IMap> deptList = db.getList(condition, "getselforgdeptList",null);
		
		result.put("mechlist", mechList);
		result.put("deptlist", deptList);
		return result;
	}
	
	
	/**
	 * 根据调度所选的车间以及规定的工卡号，发起某一个车间的机加流程
	 * @param in
	 * @return
	 */
	public IMap<String, Object> addeverywf(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		//获取工卡号
		String workcard = (String) in.get("workcard");
		//获取分派的零件
		String[] materials = (String[]) in.get("chooseid");
		//获取分派的车间
		String dept = (String) in.get("dept");
		String prodept = "";
		
		if(dept.indexOf("|JJ|")>-1){
			prodept = "机加车间";
		}else if(dept.indexOf("|JX01|")>-1){
			prodept = "机修车间";
		}else if(dept.indexOf("|ZX|")>-1){
			prodept = "钻修车间";
		}else if(dept.indexOf("|DX|")>-1){
			prodept = "电修车间";
		}else if(dept.indexOf("|MH|")>-1){
			prodept = "铆焊车间";
		}
		//根据车间的不同 发起机加流程
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		//机加登记信息单
		IMap mechTreatment = BeanFactory.getClassBean("com.MechTreatment");
		String machiningid = HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), 
		"com.MechTreatment");
		
		mechTreatment.put("id", machiningid);
		//主卡号
		mechTreatment.put("mainCard",workcard);
		//送修单位
		//machiningMessage.put("userName", busInrepairInfoMap.get("deptFrom"));
		//项目部
		mechTreatment.put("proDept", prodept);
		//orgId
		mechTreatment.put("orgId", dept);
		mechTreatment.put("repairState",2);
		mechTreatment.put("isValid",1);
		HelperDB.setCreate4isValid((String)userMap.get("userName"),mechTreatment);
		db.insert(mechTreatment);
		
		//插入机加基本信息表
		//获取当前登录用户所在的组织ID
		//查询当前登录用户所在组织所在厂区
		//查询厂区下的机加车间
		IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
		workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusWorkCard"));
		workCardMap.put("orgId", dept);
		workCardMap.put("deptName", prodept);
		
		//工卡信息workcard
		workCardMap.put("busId", machiningid);
		workCardMap.put("workcard", workcard);
		workCardMap.put("busName","com.MechTreatment");
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
		db.insert(workCardMap);
		
		for(int i=0;i<materials.length;i++){
			String saveid = materials[i];
			IMap mechdeptMap = BeanFactory.getClassBean("com.mechdept");
			mechdeptMap.put("id", saveid);
			mechdeptMap.put("assistCard", workcard);
			mechdeptMap.put("orgid", dept);
			mechdeptMap.put("busid", machiningid);
			mechdeptMap.put("isappoint", "1");
			db.update(mechdeptMap);
		}
		
		
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		condition.put("orgId", dept);
		condition.put("postCode", "车间派工");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		if(userList.isEmpty()){
			throw new BusinessException("请联系管理员指派车间的车间派工人员！");
		}
		String cbids = "";
		for(int j=0;j<userList.size();j++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(j);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		
		String flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
		String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+"调度此业务到"+prodept;

		String flowDesc = workcard+"("+dftow.format(new Date())+")修理加工件的工作流程";

		FireNetHelper.wf_start(db,userMap,"mechtreatment",machiningid,"com.MechTreatment",
				flowDesc,flowUrl,0,wfmap);
		
		LogBean logb=new LogBean(userMap,machiningid,"com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		
		result.put("method.infoMsg", "分派成功");
		result.put("method.url", in.get("url"));
		return result;
	}
	
	
	
}