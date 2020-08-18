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
 * 类名称：OtherOutStoreService   
 * 类描述：   
 * 创建人：孟勃婷 
 * 创建时间：Jul 23, 2013 2:30:51 PM
 */
public class OtherOutStoreService extends Service{
	/**
	 * 
	 * @param in
	 * @return
	 */
	public IMap getOtherOutStoreList(IMap in){
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("create_date",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//查询分页结果
		Page page = db.pageQuery(in,"getOtherOutStoreList","","id",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 
	 * @Description 保存车间其他材料
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Jul 18, 2013 5:54:55 PM
	 */
	public IMap<String, Object> saveOutStore(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		IMap orgInfo =BeanFactory.getClassBean("com.baseorganization");
		orgInfo.put("orgCode", orgId);
		orgInfo = db.get(orgInfo);
		String orgName = (String) orgInfo.get("orgName");
		String outDate = (String)in.get("outDate");
		String materialType = (String)in.get("materialType");
		//获取页面上填写的出库数量
		List<IMap> outList = (List<IMap>) in.get("otherOutStore");
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
				int store = 0;
				double amount = 0;
				//对库存结果进行递归直到出库数量为0为止
				//插入到出库信息表
				tempout.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.OtherOutStore"));
				tempout.put("orgId", orgId);
				tempout.put("deptName", orgName);
				tempout.put("materialType",materialType);
				tempout.put("outDate",outDate);
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
		//log 日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+sdf.format(new Date())+"将物料出库。";
		LogBean logb=new LogBean(userMap,"", "com.OtherOutStore","",remark, "0");
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
				amount = out*((Double)storeMap.get("unitPrice"));
				storeMap.put("num", store-out);
				db.update(storeMap);
			}else if(store == out){
			//如果最近库存数量等于出库数量，置为0，并将数据置为无效
				amount =out*((Double)storeMap.get("unitPrice"));
				storeMap.put("num", store-out);
				storeMap.put("isValid", Constants.ISNOTVALID);
				db.update(storeMap);
			}else if(store < out){
			//如果最近库存数量小于出库数量，则将最近库存置为0，并将数据置为无效。且进行下次递归
				amount = amount + jstore*((Double)storeMap.get("unitPrice"));
				storeMap.put("num", 0);
				storeMap.put("isValid", Constants.ISNOTVALID);
				db.update(storeMap);
				j=j+1;
				outString += saveRatioStore(storeList,j,out,store,outString,outStore,userMap,amount);
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
}
