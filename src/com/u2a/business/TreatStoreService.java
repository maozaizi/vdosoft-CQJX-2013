package com.u2a.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;

public class TreatStoreService extends Service {
		/**
		 * 
		 *  描述: 获取加工件库存信息列表
		 *  方法: gettreatstorelist方法
		 *  作者: duch
		 *  时间: Mar 11, 2014
		 *  版本: 1.0
		 */
		public IMap<String,Object> gettreatstorelist(IMap<String,Object> in){
			IMap<String,Object> result = new DataMap<String,Object>();
			//查询加工件库存信息
			HttpServletRequest request = (HttpServletRequest) in.get("request");
			//分页参数
			Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
			Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
			//查询分页结果
			//当前登录用户
			IMap userMap = (IMap) in.get("userMap");
			in.put("orgCode", userMap.get("orgId"));
			
			Page page = db.pageQuery(in,"getTreatStoreList","","id",currentPageNO,perCount,null);
			//回写
			page.setAction(request);
			result.put("page", page);
			
			return result;
		}
		/**
		 * 
		 *  描述: 跳转到库存修改即出库页面
		 *  方法: toOutTreatStore方法
		 *  作者: duch
		 *  时间: Mar 11, 2014
		 *  版本: 1.0
		 */
		public IMap<String,Object> toOutTreatStore(IMap<String,Object> in){
			IMap<String,Object> result = new DataMap<String,Object>();
			IMap map = BeanFactory.getClassBean("com.treatstore");
			//获取需要出库的库存加工件信息
			map.put("id", in.get("id"));
			map = db.get(map);
			IMap userMap = (IMap) in.get("userMap");
			String orgCode = (String) userMap.get("orgid");
			String[] orgCodes = orgCode.replace(".", ",").split(",");
			//取得本机加车间所在厂区的所有车间
			if(orgCodes.length>2){
				String deptCode = orgCodes[0]+"."+orgCodes[1]+"."+orgCodes[2];
				IMap baseMap = BeanFactory.getClassBean("com.baseorganization");
				baseMap.put("orgcode",deptCode);
				List list = db.getList(baseMap, "getWorkshopList", "com.baseorganization");
				result.put("deptList", list);
			}
			result.put("storeInfo", map);
			return result;
		}
		/**
		 * 
		 *  描述: 加工件保存记录
		 *  方法: saveOutTreatStore方法
		 *  作者: duch
		 *  时间: Mar 11, 2014
		 *  版本: 1.0
		 */
		public IMap<String,Object> saveOutTreatStore(IMap<String,Object> in){
			IMap<String,Object> result = new DataMap<String,Object>();
			IMap storeMap = (IMap) in.get("treatStoreInfo"); 
			//获取加工件的库存信息
			storeMap = db.get(storeMap);
			//获取加工件的出库信息
			IMap outMap = (IMap) in.get("outTreatStoreInfo"); 
			//获取出库组织结构的信息
			String type = (String) outMap.get("type");
			//如果是车间出库信息则将proDept存储为"厂区-车间"的模式
			if("1".equals(type)){
				if(Util.checkNotNull(outMap.get("orgCode"))){
					String orgCode = (String) outMap.get("orgCode");
					String[] orgCodes = orgCode.replace(".", ",").split(",");
					String deptCode = orgCodes[0]+"."+orgCodes[1]+"."+orgCodes[2];
					IMap baseMap = BeanFactory.getClassBean("com.baseorganization");
					baseMap.put("orgcode", deptCode);
					baseMap.put("isvalid", "1");
					String factoryname = (String)(db.getList(baseMap, null).get(0).get("orgname"));
					baseMap.put("orgcode", orgCode);
					String deptname = (String)(db.getList(baseMap, null).get(0).get("orgname"));
					outMap.put("proDept", factoryname+"_"+deptname);
				}
			}else{
				String proDept = (String) outMap.get("proDept");
				IMap baseMap = BeanFactory.getClassBean("com.DataItemBaseInfo");
				baseMap.put("dataItemCode", proDept);
				String deptname = (String)(db.getList(baseMap, null).get(0).get("dataItemName"));
				
				outMap.put("proDept", deptname);
			}
			IMap userMap = (IMap) in.get("userMap");
			//获取页面上填写的出库数量
			String outnum = (String) outMap.get("outnum");
			int outnumstore = Integer.valueOf(outnum);
			//获取库存数量
			int numstore = (Integer) storeMap.get("mtnum");
			//在库存中减去出库数量
			numstore = numstore-outnumstore;
			//将修改后的数量更新到加工件库存中 
			storeMap.put("mtnum", numstore);
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap),storeMap);
			db.update(storeMap);
			//将出库信息插入到出库表中
			String id= HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.treatoutstore");
			outMap.put("id", id);
			outMap.put("name", storeMap.get("name"));
			outMap.put("unity", storeMap.get("unity"));
					
			db.insert(outMap);
			
			result.put("method.url", in.get("url"));
			result.put("method.infoMsg", OperateHelper.getSaveMsg());
			return result;
		}
		/**
		 * 
		 *  描述: 跳转到出库记录页面
		 *  方法: toViewOutstore方法
		 *  作者: duch
		 *  时间: Mar 11, 2014
		 *  版本: 1.0
		 */
		public IMap<String,Object> toViewtreatOutstore(IMap<String,Object> in){
			IMap<String,Object> result = new DataMap<String,Object>();
			//分页参数
			Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
			Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
			//查询分页结果
			//当前登录用户
			IMap userMap = (IMap) in.get("userMap");
			in.put("orgCode", userMap.get("orgId"));
			
			Page page = db.pageQuery(in,"getTreatoutStoreList","","id",currentPageNO,perCount,null);

			result.put("page", page);
			return result;
		}	
		
}
