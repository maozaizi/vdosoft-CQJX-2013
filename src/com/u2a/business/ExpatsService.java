package com.u2a.business;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
/**
 * 
 * 系统名称：长庆机修  
 * 类名称：ExpatsService   
 * 类描述：   外派人员管理
 * 创建人：duch 
 * 创建时间：Aug 12, 2013 9:51:15 AM
 */
@SuppressWarnings("unchecked")
public class ExpatsService extends Service {
	/**
	 * 
	 * @Description 保存上井记录
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 23, 2013 9:30:01 AM
	 */
	public IMap<String,Object> saveExpatsInfo(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap expatsInfo = (IMap) in.get("expatsInfo");
		IMap userMap = (IMap) in.get("userMap");
		//插入主表
		String expatsId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.expatsInfo");
		expatsInfo.put("id",expatsId );
		expatsInfo.put("orgCode",userMap.get("orgId") );
		expatsInfo.put("isComplete","0" );
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),expatsInfo);
		db.insert(expatsInfo);
		//将详细信息插入到子表中
		//获取车间名称
		String[] deptNames  = (String[]) in.get("deptName");
		//获取车间ID
		String[] deptIds  = (String[]) in.get("deptId");
		//获取设备ID
		String[] eqIds  = (String[]) in.get("equipmentId");
		
		if(deptIds.length>0){
			for(int i=0;i<deptIds.length;i++){
				IMap expatsDetail = BeanFactory.getClassBean("com.expatsDetail");
				IMap equipmentInfo = BeanFactory.getClassBean("com.EquipmentInfo");
				//获取设备信息
				equipmentInfo.put("id", eqIds[i]);
				equipmentInfo = db.get(equipmentInfo);
				String deptName = deptNames[i];
				String deptId = deptIds[i];
				expatsDetail.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.expatsDetail"));
				expatsDetail.put("deptName", deptName);
				expatsDetail.put("equipmentName", equipmentInfo.get("equipmentName"));
				expatsDetail.put("equipmentModel", equipmentInfo.get("equipmentModel"));
				expatsDetail.put("equipmentValue", equipmentInfo.get("majorCost"));
				expatsDetail.put("orgCode", deptId);
				expatsDetail.put("expatsId", expatsId);
				expatsDetail.put("isComplete","0" );
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),expatsDetail);
				db.insert(expatsDetail);
			}
		}
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 
	 * @Description 获取本厂区的所有上井服务
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 12, 2013 4:04:58 PM
	 */
	public IMap<String,Object> getExpatsInfoList(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		orderBean.put("isComplete",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgCode", userMap.get("orgId"));
		//查询分页结果
		Page page = db.pageQuery(in,"queryExpatsInfoList","com.expatsInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		
		return result;
	}
	/**
	 * 
	 * @Description 跳转到调度编辑页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 13, 2013 10:35:08 AM
	 */
	public IMap<String,Object> toUpdateExpats(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();
		//根据ID查询实体
		IMap expatsMap = (IMap) in.get("expatsInfo");
		expatsMap = db.get(expatsMap);
		//根据主表ID 查询详细表ID
		String id = (String) expatsMap.get("id");
		IMap expatsDetail = BeanFactory.getClassBean("com.expatsDetail");
		expatsDetail.put("expatsId", id);
		
		List<IMap> list = db.getList(expatsDetail, null);
		String flag = "1";
		List materialList = new ArrayList();
		//判断是否有未完成的车间派工 如果有 则不能结束
		if(!list.isEmpty()){
			int j = 0 ;
			for(int i=0;i<list.size();i++){
				IMap expatsTemp = BeanFactory.getClassBean("com.expatsDetail");
				expatsTemp = list.get(i);
				String isComplete =  (String) expatsTemp.get("isComplete");
				IMap outStore = BeanFactory.getClassBean("com.outStoreInfo");
				outStore.put("busName", "com.ExpatsDetail");
				outStore.put("busId", expatsTemp.get("id"));
				List outList = db.getList(outStore, null);
				materialList.addAll(outList);
				if(!"1".equals(isComplete)){
					j++;
					break;
				}
			}
			if(j!=0){
				flag = "0";
			}
		}
		result.put("flag", flag);
		result.put("materialList", materialList);
		result.put("expatsMap", expatsMap);
		result.put("expatsDetailList", list);
		return result;
	}
	/**
	 * 
	 * @Description 保存调度确认的结果
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 13, 2013 3:05:20 PM
	 */
	public IMap<String,Object> updateExpats(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		//将此上井服务的状态置为完成
		IMap expatsInfo = (IMap) in.get("expatsInfo");
		expatsInfo.put("isComplete", "1");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),expatsInfo);
		db.update(expatsInfo);
		
		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 
	 * @Description 获取本车间的所有需要上井的信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 12, 2013 3:47:44 PM
	 */
	public IMap<String,Object> getExpatsList(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("create_date",OrderBean.DESC);
		orderBean.put("is_complete",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgCode", userMap.get("orgId"));
		//查询分页结果
		Page page = db.pageQuery(in,"queryExpatsDetailList","","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 
	 * @Description 跳转到车间上井服务页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 13, 2013 4:04:35 PM
	 */
	public IMap<String,Object> toUpdateExpatsDetail(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap expatsdeatailMap = (IMap) in.get("expatsDetail");
		expatsdeatailMap = db.get(expatsdeatailMap);
		
		String expatsId  = (String) expatsdeatailMap.get("expatsId");
		//查询主表信息
		IMap expatsInfo = BeanFactory.getClassBean("com.expatsInfo");
		expatsInfo.put("id", expatsId);
		expatsInfo = db.get(expatsInfo);
		
		
		
		result.put("expatsInfo", expatsInfo);
		result.put("expatsdeatail", expatsdeatailMap);
		return result;
	}
	
	/**
	 * 
	 * @Description 保存上井信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 12, 2013 4:35:56 PM
	 */
	public IMap<String,Object> saveExpats(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		//所属机构信息
		String orgId = (String) userMap.get("orgid");
		IMap orgInfo =BeanFactory.getClassBean("com.baseorganization");
		orgInfo.put("orgCode", orgId);
		orgInfo = db.get(orgInfo);
		String orgName = (String) orgInfo.get("orgName");
		IMap expatsDetail = (IMap) in.get("expatsDetail");
		String persons = (String) expatsDetail.get("expatsPerson");
		int personLength = persons.split(",").length;
		
		expatsDetail.put("expatsNum", personLength);
		expatsDetail.put("isComplete", "1");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),expatsDetail);
				
		db.update(expatsDetail);
		//保存上井使用材料信息(出库)
		IMap expatsDetailIMap = BeanFactory.getClassBean("com.ExpatsDetail");
		expatsDetailIMap.put("id", expatsDetail.get("id"));
		expatsDetailIMap = db.get(expatsDetailIMap);
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
			tempout.put("busId", expatsDetailIMap.get("id"));
			tempout.put("busName", "com.ExpatsDetail");
			
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
				int outNum = Integer.valueOf((String) tempout.get("storeNum"));
				//循环库存结果，对比数量
				int j = 0;
				double store = 0;
				double amount = 0;
				//对库存结果进行递归直到出库数量为0为止
				//插入到出库信息表
				tempout.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreInfo"));
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
		}

		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 
	 * @Description 在库存数量中减去单机应用的出库数量,将材料对应到单机
	 * @param @param storeList
	 * @param @param material    
	 * @return void   
	 * @author duch
	 * @date Jul 24, 2013 9:32:27 AM
	 */
	private String saveRatioStore(List<IMap> storeList,int j,int out,double store,String outString,IMap outStore,IMap userMap,double amount) {
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
	  
	public IMap<String,Object> saveSupply(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		//所属机构信息
		String orgId = (String) userMap.get("orgid");
		IMap orgInfo =BeanFactory.getClassBean("com.baseorganization");
		orgInfo.put("orgCode", orgId);
		orgInfo = db.get(orgInfo);
		String orgName = (String) orgInfo.get("orgName");
		IMap expatsDetail = (IMap) in.get("expatsDetail");
		//保存上井使用材料信息(出库)
		IMap expatsDetailIMap = BeanFactory.getClassBean("com.ExpatsDetail");
		expatsDetailIMap.put("id", expatsDetail.get("id"));
		expatsDetailIMap = db.get(expatsDetailIMap);
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
			tempout.put("busId", expatsDetailIMap.get("id"));
			tempout.put("busName", "com.ExpatsDetail");
			
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
				int outNum = Integer.valueOf((String) tempout.get("storeNum"));
				//循环库存结果，对比数量
				int j = 0;
				double store = 0;
				double amount = 0;
				//对库存结果进行递归直到出库数量为0为止
				//插入到出库信息表
				tempout.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreInfo"));
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
		}

		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
		return result;
	}
}
