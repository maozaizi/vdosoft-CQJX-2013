package com.u2a.business;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.export.ooxml.DocxBorderHelper;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
import com.u2a.framework.util.Util;

/**
 * 系统名称：长庆钻井机修公司 - 设备维修管理系统   
 * 类名称：StoreDetailService   
 * 类描述：   
 * 创建人：孟勃婷 
 * 创建时间：Jul 23, 2013 2:30:51 PM
 */
public class StoreDetailService extends Service{
	/**
	 * 
	 * @Description 获取出库信息中的所有工卡号的汇总
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Aug 15, 2013 7:10:49 PM
	 */
	public IMap<String,Object> getStoreList(IMap<String,Object> in){
		IMap<String,Object> result = new DataMap<String,Object>();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//查询分页结果
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		in.put("orgCode", userMap.get("orgId"));
		
		Page page = db.pageQuery(in,"queryStoreworkcardList","","workCard",currentPageNO,perCount,null);
		//回写
		page.setAction(request);
		result.put("page", page);
		
		return result;
	}
	
	/**
	 * 出库明细列表 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 23, 2013 2:34:24 PM
	 */
	public IMap getStoreDetailList(IMap in){
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
		IMap userMap = (IMap) in.get("userMap");
		storeDetailMap.put("orgId", userMap.get("orgId"));
		Page page = db.pageQuery(storeDetailMap,"queryStoreDetailList","com.StoreDetail","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 批量添加库存信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 23, 2013 3:58:26 PM
	 */
	public IMap<String, Object> saveStoreDetail(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取材料计划列表
		List storeDetailList = (List) in.get("storeDetail");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		//登录用户组织部门信息
		IMap  orgMap = BeanFactory.getClassBean("com.baseorganization");
		//获取org信息
		orgMap.put("orgCode",userMap.get("orgId"));
		orgMap = db.get(orgMap);
		//循环添加出库信息
		if(storeDetailList !=null && storeDetailList.size()>0){
			for (int i = 0; i < storeDetailList.size(); i++) {
				IMap storeDetail = (IMap)storeDetailList.get(i);
				storeDetail.put("id", HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.StoreDetail"));
				storeDetail.put("orgId", userMap.get("orgId"));
				storeDetail.put("deptName",orgMap.get("orgName"));
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), storeDetail);
				db.insert(storeDetail);
				//log日志
				String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"添加物料出库信息";
				LogBean logb=new LogBean(userMap,(String)storeDetail.get("id"), "com.StoreDetail","",remark, "0");
				LogRecordsService.saveOperationLog(logb,db);
			}
		}
		result.put("method.infoMsg",OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 跳转修改页面 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 24, 2013 9:25:25 AM
	 */
	public IMap<String, Object> toUpdateStoreDetail(IMap<String, Object> in){
		IMap result = new DataMap();
		IMap  storeMap = BeanFactory.getClassBean("com.StoreDetail");
		storeMap.put("id", in.get("id"));
		storeMap = db.get(storeMap);
		result.put("storeMap", storeMap);
		return result;
	}
	/**
	 * 修改 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 24, 2013 9:26:05 AM
	 */
	public IMap<String, Object> modifyStoreDetail(IMap<String, Object> in){
		IMap result = new DataMap();
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		IMap<String,Object>  storeMap =BeanFactory.getClassBean("com.StoreDetail");
		storeMap.put("id", in.get("id"));
		storeMap = db.get(storeMap);
		storeMap.put("num", in.get("num"));
		storeMap.put("materialType", in.get("materialType"));
		storeMap.put("unitPrice", in.get("unitPrice"));
		//为修改数据赋值
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), storeMap);
		db.update(storeMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"修改物料出库信息";
		LogBean logb=new LogBean(userMap,(String)storeMap.get("id"), "com.StoreDetail","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg",OperateHelper.getUpdateMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 删除
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 11, 2013 1:25:16 PM
	 */
	public IMap<String, Object> deleteStoreDetail(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.StoreDetail");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		selectMap.put("id", in.get("id"));
		selectMap = db.get(selectMap);
		selectMap.put("isValid",Constants.ISNOTVALID);
		db.update(selectMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"作废物料出库信息";
		LogBean logb=new LogBean(userMap,(String)selectMap.get("id"), "com.StoreDetail","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg",OperateHelper.getDeleteMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	
	
	/**
	 * 出库历史信息列表 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 23, 2013 2:34:24 PM
	 */
	public IMap getOutStoreInfoList(IMap in){
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
		IMap outStoreInfo = (IMap)in.get("outStoreInfo");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		outStoreInfo.put("orgId", userMap.get("orgId"));
		Page page = db.pageQuery(outStoreInfo,"queryOutStoreInfoList","com.OutStoreInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 出库详细信息列表 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 23, 2013 2:34:24 PM
	 */
	public IMap getOutStoreDetailList(IMap in){
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
		Page page = db.pageQuery(in,"queryOutStoreDetailList","com.OutStoreDetail","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		result.put("workCard", in.get("workCard"));
		return result;
	}
	/**
	 * 退料管理
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 25, 2013 3:47:27 PM
	 */
	public IMap getOutStoreReturnList(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap userMap = (IMap) in.get("userMap");
		in.put("orgId", userMap.get("orgId"));
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询分页结果
		Page page = db.pageQuery(in,"getOutStoreReturnList","com.OutStoreInfo","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	
	public IMap saveOutStoreReturn(IMap in){//获取页面上填写的出库数量
		IMap userMap = (IMap)in.get("userMap");
		List<IMap> outList = (List<IMap>) in.get("outStoreInfo");
		//按照先进先出的原则，进行材料的增减
		//循环页面返回的值
		//判断物料编码相同的，则取数量进行对比，
		//如果第一条数量大于页面填写数量，剩余数量为第一条数量减去填写数量
		//如果第一条数量等于页面填写数量，剩余数量为0 并将其置为无效
		//依次递归，知道页面填写数量减为0为止
		String outStr = "";
		String orgId = (String) userMap.get("orgid");
		IMap orgInfo =BeanFactory.getClassBean("com.baseorganization");
		orgInfo.put("orgCode", orgId);
		orgInfo = db.get(orgInfo);
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String orgName = (String) orgInfo.get("orgName");
		for(int i=0;i<outList.size();i++){
			//获取库存中某一种材料的数量
			IMap tempout = outList.get(i);
			String outString = "";
			String code = (String) tempout.get("materialCode");
			//获取材料库存材料
			IMap selMap =BeanFactory.getClassBean("com.StoreDetail");
			selMap.put("materialCode", code);
			//selMap.put("orgId", orgId);
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
				String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"将物料退回。";
				LogBean logb=new LogBean(userMap,(String)tempout.get("id"), "com.OutStoreInfo","",remark, "0");
				LogRecordsService.saveOperationLog(logb,db);
				
				//对比库存数量 从库存中减除
				outStr += saveRatioStore(storeList,j,outNum,store,outString,tempout,userMap,amount);
			}
		}
		if(!"".equals(outStr)&&outStr!=null){
			outStr = outStr.substring(0,outStr.length()-1);
			throw new BusinessException("材料"+outStr+"库存不足!");
		}
		IMap result = new DataMap();
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
	 * @author duch
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
	 * 退料详细信息
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 25, 2013 3:47:27 PM
	 */
	public IMap toReturnDetail(IMap in){
		// 设置结果集
		IMap<String,Object> result = new DataMap<String,Object>();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		IMap outStoreDetail = BeanFactory.getClassBean("com.OutStoreDetail");
		outStoreDetail.put("outId",in.get("id"));
		List outStoreDetailList  =  db.getList(outStoreDetail, orderBean);
		result.put("outStoreDetailList",outStoreDetailList);
		return result;
	}
	
}
