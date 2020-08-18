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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.manager.BeanFactory;
import com.lowagie.text.Cell;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;


public class ChargeoffDetailService extends Service{
	
	/**
	 * 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 23, 2013 9:13:19 AM
	 */
	public IMap getChargeoffDetailInfoList(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("materialClass",OrderBean.ASC);
		//查询分页结果
		IMap chargeoffDetailMap = (IMap)in.get("chargeoffDetail");
		Page page = db.pageQuery(chargeoffDetailMap,"queryChargeoffDetailList","com.ChargeoffDetail","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	
	/**
	 * 材料信息导入
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 22, 2013 11:13:56 AM
	 */
	
	public IMap saveChargeoffDetailImport(IMap in) {
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		String fileUrl = (String)in.get("fileUrl");
	    String message = "";
	    String messageDetails="";
	    int err=0;//错误数据
        int success=0;//导入数据
        int importNum=0;//需要导入条数
        List successMapList = new ArrayList();//数据正确待保存chargeoff
        IMap userMap = (IMap) in.get("userMap");
        InputStream input = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
      //导入文件路径
        fileUrl=request.getSession().getServletContext().getRealPath("/")+ fileUrl;
       
        //导入文件信息
        IMap attachmentInfoMap=(IMap) in.get("attachmentInfo");
        //导入文件上传后ID
        String attId =HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.AttachmentInfo");
		//为创建数据赋值
		attachmentInfoMap.put("id",attId);
		result.put("attId", attId);
		attachmentInfoMap.put("isValid",Constants.ISVALID);
		attachmentInfoMap.put("busType",3);//导入
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
		try {
			input = new BufferedInputStream(new FileInputStream(fileUrl));
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);
			if(sheet == null){
				messageDetails +="Excel图表不存在，请重新导入";
			}else{
				int total = sheet.getLastRowNum();
				//message +="共有数据"+total+"行。<br />";
				importNum = total;
				HSSFRow c = sheet.getRow(0);
				//判断excel表头符合要求
				boolean flag = true;
				if(!"大类".equals(c.getCell((short)0).getStringCellValue().trim())){
					messageDetails +="Excel图表中第1列的应为大类！<br/>";
					flag = false;
				}
				if(!"物料组".equals(c.getCell((short)1).getStringCellValue().trim())){
					messageDetails +="Excel图表中第2列的应为物料组！<br/>";
					flag = false;
				}
				if(!"物资编码".equals(c.getCell((short)2).getStringCellValue().trim())){
					messageDetails +="Excel图表中第3列的应为物资编码组！<br/>";
					flag = false;
				}
				if(!"物资描述".equals(c.getCell((short)3).getStringCellValue().trim())){
					messageDetails +="Excel图表中第4列的应为物资描述！<br/>";
					flag = false;
				}
				if(!"计量单位".equals(c.getCell((short)4).getStringCellValue().trim())){
					messageDetails +="Excel图表中第5列的应为计量单位！<br/>";
					flag = false;
				}
				if(!"数量".equals(c.getCell((short)5).getStringCellValue().trim())){
					messageDetails +="Excel图表中第6列的应为数量！<br/>";
					flag = false;
				}
				if(!"单价".equals(c.getCell((short)6).getStringCellValue().trim())){
					messageDetails +="Excel图表中第7列的应为单价！<br/>";
					flag = false;
				}
				if(!"金额".equals(c.getCell((short)7).getStringCellValue().trim())){
					messageDetails +="Excel图表中第8列的应为金额！<br/>";
					flag = false;
				}
				if(!"发料日期".equals(c.getCell((short)8).getStringCellValue().trim())){
					messageDetails +="Excel图表中第9列的应为发料日期！<br/>";
					flag = false;
				}
				if(!"成本中心描述".equals(c.getCell((short)9).getStringCellValue().trim())){
					messageDetails +="Excel图表中第10列的应为成本中心描述！<br/>";
					flag = false;
				}
				if(!"发料单号".equals(c.getCell((short)10).getStringCellValue().trim())){
					messageDetails +="Excel图表中第11列的应为发料单号！<br/>";
					flag = false;
				}
				if(!"领料单位".equals(c.getCell((short)11).getStringCellValue().trim())){
					messageDetails +="Excel图表中第12列的应为领料单位！<br/>";
					flag = false;
				}
				if(!"批次".equals(c.getCell((short)12).getStringCellValue().trim())){
					messageDetails +="Excel图表中第13列的应为批次！<br/>";
					flag = false;
				}
				if(!"制单人".equals(c.getCell((short)13).getStringCellValue().trim())){
					messageDetails +="Excel图表中第14列的应为制单人！<br/>";
					flag = false;
				}
				if(!"备注".equals(c.getCell((short)14).getStringCellValue().trim())){
					messageDetails +="Excel图表中第15列的应为备注！<br/>";
					flag = false;
				}
				
				//循环读取excel数据
				for(int j=1;j<sheet.getLastRowNum()+1&&flag;j++) {
					IMap chargeoffDetailParam = BeanFactory.getClassBean("com.ChargeoffDetail");
					chargeoffDetailParam.put("id", HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.ChargeoffDetail"));
					chargeoffDetailParam.put("attId",attId);//导入文件
					HSSFRow hssfRow = sheet.getRow(j);
					if (hssfRow == null && j != sheet.getLastRowNum()) {
						messageDetails +="Excel图表中第"+(j+1)+"行内容为空！<br/>";
					}
					//cell0 大类
					HSSFCell cell0 = hssfRow.getCell((short)0);
					if(cell0!=null && cell0.getStringCellValue()!=""){
						chargeoffDetailParam.put("materialClass",cell0.getStringCellValue());
					}else{
						chargeoffDetailParam.put("materialClass","");
					}
					//物料组
					HSSFCell cell1 = hssfRow.getCell((short)1);
					if(cell1!=null ){
						chargeoffDetailParam.put("materialGroup",cell1.getStringCellValue());
					}else{
						chargeoffDetailParam.put("materialGroup","");
					}
					//物资编码
					HSSFCell cell2 = hssfRow.getCell((short)2);
					if(cell2!=null){
						String materialCode = cell2.getStringCellValue();
						if(materialCode.length()!=11&&materialCode.matches("[0-9]+")){
							messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(3)+"列的物资编码应为11位数字！<br/>";
							err++;
							continue;
						}
						//材料编码 对比材料
						chargeoffDetailParam.put("materialCode",cell2.getStringCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(3)+"列的物资编码不能为空！<br/>";
						err++;
						continue;
					}
					//物资描述
					HSSFCell cell3 = hssfRow.getCell((short)3);
					if(cell3!=null){
						chargeoffDetailParam.put("materialDetail",cell3.getStringCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(4)+"列的物资描述不能为空！<br/>";
						err++;
						continue;
					}
					//计量单位
					HSSFCell cell4 = hssfRow.getCell((short)4);
					if(cell4!=null && cell4.getStringCellValue()!=""){
						chargeoffDetailParam.put("measureUnit",cell4.getStringCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(5)+"列的计量单位不能为空！<br/>";
						err++;
						continue;
					}
					//数量
					HSSFCell cell5 = hssfRow.getCell((short)5);
					if(cell5!=null&&cell5.getNumericCellValue()+""!=""){
						chargeoffDetailParam.put("num",cell5.getNumericCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(6)+"列的数量不能为空！<br/>";
						err++;
						continue;
					}
					//单价
					HSSFCell cell6 = hssfRow.getCell((short)6);
					if(cell6!=null&&cell6.getNumericCellValue()+""!=""){
						chargeoffDetailParam.put("unitPrice",cell6.getNumericCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(7)+"列的单价不能为空！<br/>";
						err++;
						continue;
					}
					//金额
					HSSFCell cell7 = hssfRow.getCell((short)7);
					if(cell7!=null&&cell7.getNumericCellValue()+""!=""){
						chargeoffDetailParam.put("amount",cell7.getNumericCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(8)+"列的金额不能为空！<br/>";
						err++;
						continue;
					}
					//cell1 发料日期
					HSSFCell cell8 = hssfRow.getCell((short)8);

					//发料日期为空，不导入
					if(cell8!=null && cell8.getStringCellValue()!=""){
						Date receiptDate=sdf.parse(cell8.getStringCellValue().toString().trim());
						chargeoffDetailParam.put("receiptDate",sdf.format(receiptDate));
					}else{
						importNum--;
						continue;
					}
					//成本中心描述
					HSSFCell cell9 = hssfRow.getCell((short)9);
					if(cell9!=null){
						chargeoffDetailParam.put("accountDetail",cell9.getStringCellValue());
					}else{
						chargeoffDetailParam.put("accountDetail","");
					}
					//发料单号
					HSSFCell cell10 = hssfRow.getCell((short)10);
					if(cell10!=null&&cell10.getStringCellValue()!=""){
						chargeoffDetailParam.put("receiptCode",cell10.getStringCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(11)+"列的发料单号不能为空！<br/>";
						err++;
						continue;
					}
					//领料单位
					HSSFCell cell11 = hssfRow.getCell((short)11);
					if(cell11!=null&&cell11.getStringCellValue()!=""){
						chargeoffDetailParam.put("deptGet",cell11.getStringCellValue());
					}else{
						chargeoffDetailParam.put("deptGet","");
					}
					//批次
					HSSFCell cell12 = hssfRow.getCell((short)12);
					if(cell12!=null&&cell12.getStringCellValue()!=""){
						chargeoffDetailParam.put("batch",cell12.getStringCellValue());
					}else{
						chargeoffDetailParam.put("batch","");
					}
					//制单人
					HSSFCell cell13 = hssfRow.getCell((short)13);
					if(cell13!=null){
						chargeoffDetailParam.put("maker",cell13.getStringCellValue());
					}else{
						chargeoffDetailParam.put("maker","");
					}
					//备注
					HSSFCell cell14 = hssfRow.getCell((short)14);
					if(cell14!=null && cell14.getStringCellValue()!=""){
						chargeoffDetailParam.put("remark",cell14.getStringCellValue());
					}else{
						chargeoffDetailParam.put("remark","");
					}
					HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),chargeoffDetailParam);
					//存入待保存的正确信息
					successMapList.add(chargeoffDetailParam);
					//db.insert(chargeoffDetailParam);
					success+=1;
				}
			}
					 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭input流
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int flag =0; //标记是否导入
			if(importNum==success){//数据全部正确
				//插入导入文件信息
				db.insert(attachmentInfoMap);
				//全部导入
				for (int i = 0; i < successMapList.size(); i++) {
					IMap chargeoff = (IMap)successMapList.get(i);
					String orgStr = (String)chargeoff.get("accountDetail");
					String orgCode = "%";
					if(orgStr.indexOf("庆阳")>=0){
						orgCode +="|QY|.%";
					}
					if(orgStr.indexOf("银川")>=0){
						orgCode +="|YC|.%";
					}
					if(orgStr.indexOf("机修车间")>0){
						orgCode +="|JX|%";
					}
					if(orgStr.indexOf("机加车间")>0){
						orgCode +="|JJ|%";
					}
					if(orgStr.indexOf("电修车间")>0){
						orgCode +="|DX|%";
					}
					if(orgStr.indexOf("钻修车间")>0){
						orgCode +="|ZX|%";
					}
					if(orgStr.indexOf("铆焊车间")>0){
						orgCode +="|MH|%";
					}
					OrderBean order = new OrderBean();
					order.put("create_date");
					IMap con = new DataMap();
					con.put("materialCode", chargeoff.get("materialCode"));
					con.put("orgId", orgCode);
					List  outList = db.getList(con,"getOutStoreList","com.OutStoreDetail");
					List  list = db.getList(con,"getStoreList","com.StoreDetail");
					//判断材料计划是否有未出帐材料
					if(list.size()>0||outList.size()>0){
						//出账材料
						Double chargeNum = (Double)chargeoff.get("num");
						//循环出库出账材料计划
						for(int j = 0;j <outList.size()&&chargeNum>0;j++){
							IMap material = (IMap)outList.get(j);
							double realnum = (Double)material.get("materialNum");
							//出账单价
							Double realPrice = (Double)chargeoff.get("unitPrice");
							double realTotalPrice = 0;
							//材料计划数量小于出账数量
							if(realnum < chargeNum){
								//材料计划出账
								realTotalPrice = realPrice*realnum;
								material.put("realPrice", realPrice);
								material.put("realTotalPrice", realTotalPrice);
								db.update(material);
								chargeNum -= realnum;
							}else if(realnum == chargeNum){
								//材料计划出账
								realTotalPrice = realPrice*realnum;
								material.put("realPrice", realPrice);
								material.put("realTotalPrice", realTotalPrice);
								db.update(material);
								//出账完成
								chargeNum = 0.0;
							}else if(realnum > chargeNum){
								//实际数量大于出账数量
								realTotalPrice = realPrice*chargeNum;
								//拆分实际数量
								IMap newMaterial = material;
								newMaterial.put("id",HelperApp.getAutoIncrementCode(HelperApp.getPackageName(),
								"com.OutStoreDetail"));
								//未出帐数量
								newMaterial.put("num",realnum -chargeNum);
								//新增未出帐材料计划
								db.insert(newMaterial);
								//修改已出账材料信息
								material.put("realPrice", realPrice);
								material.put("realTotalPrice", realTotalPrice);
								material.put("num", chargeNum);
								db.update(material);
								chargeNum = 0.0;
							}
						}
						//循环库存出账材料计划
						for(int j = 0;j <list.size()&&chargeNum>0;j++){
							IMap material = (IMap)list.get(j);
							double realnum = (Double)material.get("num");
							//出账单价
							Double realPrice = (Double)chargeoff.get("unitPrice");
							double realTotalPrice = 0;
							//材料计划数量小于出账数量
							if(realnum < chargeNum){
								//材料计划出账
								realTotalPrice = realPrice*realnum;
								material.put("realPrice", realPrice);
								material.put("realTotalPrice", realTotalPrice);
								db.update(material);
								chargeNum -= realnum;
							}else if(realnum == chargeNum){
								//材料计划出账
								realTotalPrice = realPrice*realnum;
								material.put("realPrice", realPrice);
								material.put("realTotalPrice", realTotalPrice);
								db.update(material);
								//出账完成
								chargeNum = 0.0;
							}else if(realnum > chargeNum){
								//实际数量大于出账数量
								realTotalPrice = realPrice*chargeNum;
								//拆分实际数量
								IMap newMaterial = material;
								newMaterial.put("id",HelperApp.getAutoIncrementCode(HelperApp.getPackageName(),
										"com.StoreDetail"));
								//未出帐数量
								newMaterial.put("num",realnum -chargeNum);
								//新增未出帐材料计划
								db.insert(newMaterial);
								//修改已出账材料信息
								material.put("realPrice", realPrice);
								material.put("realTotalPrice", realTotalPrice);
								material.put("num", chargeNum);
								db.update(material);
								chargeNum = 0.0;
							}
						}
						//循环出账完成 ，chargeNum >0
						if(chargeNum>0){
							chargeoff.put("num", chargeNum);
							db.insert(chargeoff);
						}
					}else{
						//直接插入出账明细表
						db.insert(chargeoff);
					}
					
//					db.insert(chargeoff);
//					//库存
//					IMap storeDetailMap = BeanFactory.getClassBean("com.StoreDetail");
//					//对应到的材料信息
//					IMap materialInfoMap = BeanFactory.getClassBean("com.MaterialInfo");
//					materialInfoMap.put("materialCode", chargeoff.get("materialCode"));
//					List list = db.getList(materialInfoMap, null);
//					if(!list.isEmpty()){//对应材料信息
//						materialInfoMap = (IMap)list.get(0);
//						storeDetailMap.put("materialClass",materialInfoMap.get("materialClass"));
//						storeDetailMap.put("materialGroup",materialInfoMap.get("materialGroup"));
//						storeDetailMap.put("materialDetail",materialInfoMap.get("materialDetail"));
//						storeDetailMap.put("materialCode",materialInfoMap.get("materialCode"));
//						storeDetailMap.put("unitPrice",materialInfoMap.get("unitPrice"));
//						//storeDetailMap.put("remark",chargeoff.get("remark"));
//						storeDetailMap.put("unity",materialInfoMap.get("unity"));
//						storeDetailMap.put("num",chargeoff.get("num"));
//					}else{//导入文件 填写的 材料信息
//						storeDetailMap.put("materialClass",chargeoff.get("materialClass"));
//						storeDetailMap.put("materialGroup",chargeoff.get("materialGroup"));
//						storeDetailMap.put("materialDetail",chargeoff.get("materialDetail"));
//						storeDetailMap.put("materialCode",chargeoff.get("materialCode"));
//						storeDetailMap.put("unitPrice",chargeoff.get("unitPrice"));
//						//storeDetailMap.put("remark",chargeoff.get("remark"));
//						storeDetailMap.put("unity",chargeoff.get("measureUnit"));
//						storeDetailMap.put("num",chargeoff.get("num"));
//					}
//					storeDetailMap.put("orgId",userMap.get("orgId"));
//					storeDetailMap.put("isValid",1);
//					storeDetailMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
//							.getPackageName(), "com.StoreDetail"));
//					HelperDB.setCreate4isValid((String)userMap.get("userName"), storeDetailMap);
//					db.insert(storeDetailMap);
				}
				//log日志
				String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"导入材料出账明细";
				LogBean logb=new LogBean(userMap,"", "","",remark, "0");
				LogRecordsService.saveOperationLog(logb,db);
				flag =1;
			}else{//删除已上传的EXCEL文件
				java.io.File file = new java.io.File(fileUrl);
				if (file.exists()) {
					file.delete();
				}
			}
			if(messageDetails==""){
				messageDetails +="暂无。";
			}
			message += "需导入："+importNum+"条，正确数据："+success +"条，";
			message += "错误数据：<font color='red'>"+err+"</font>条。<br/>";
			message += "错误信息：<br/>"+messageDetails;
			result.put("message", message);
			result.put("flag",flag);
			//result.put("attId",attId);
		}
		return result;
	}
	/**
	 * 手工对账
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jun 19, 2014 5:51:02 PM
	 */
	public IMap getChargeoffDetail(IMap in){
		IMap result = new DataMap();
		IMap chargeoffDetail = (IMap)in.get("chargeoffDetail");
		chargeoffDetail = db.get(chargeoffDetail);
		result.put("chargeoffDetail",chargeoffDetail);
		String deptId = (String)in.get("deptId");
		String workCard = (String)in.get("workCard");
		if(!"".equals(deptId)&&!"".equals(workCard)){
			List list = db.getList(in,"getNoOutStoreList","com.OutStoreDetail");
			result.put("list", list);
			result.put("deptId", deptId);
			result.put("workCard", workCard);
			result.put("deptName", in.get("deptName"));
		}
		return result;
	}
	/**
	 * 剩余出账材料手工对账
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jun 20, 2014 2:00:49 PM
	 */
	public IMap saveDz(IMap in){
		IMap result = new DataMap();
		//剩余出账材料
		IMap chargeoffDetail = (IMap)in.get("chargeoffDetail");
		//手工出账的出库信息
		IMap outStoreDetail = (IMap)in.get("outStoreDetail");
		chargeoffDetail = db.get(chargeoffDetail);
		outStoreDetail = db.get(outStoreDetail);
		Double chargeNum = (Double)chargeoffDetail.get("num");
		//循环出库出账材料计划
		double realnum = (Double)outStoreDetail.get("materialNum");
		//出账单价
		Double realPrice = (Double)chargeoffDetail.get("unitPrice");
		double realTotalPrice = 0;
		//材料计划数量小于出账数量
		if(realnum < chargeNum){
			//材料计划出账
			realTotalPrice = realPrice*realnum;
			outStoreDetail.put("realPrice", realPrice);
			outStoreDetail.put("realTotalPrice", realTotalPrice);
			db.update(outStoreDetail);
			chargeNum -= realnum;
		}else if(realnum == chargeNum){
			//材料计划出账
			realTotalPrice = realPrice*realnum;
			outStoreDetail.put("realPrice", realPrice);
			outStoreDetail.put("realTotalPrice", realTotalPrice);
			db.update(outStoreDetail);
			//出账完成
			chargeNum = 0.0;
		}else if(realnum > chargeNum){
			//实际数量大于出账数量
			realTotalPrice = realPrice*chargeNum;
			//拆分实际数量
			IMap newMaterial = outStoreDetail;
			newMaterial.put("id",HelperApp.getAutoIncrementCode(HelperApp.getPackageName(),
			"com.OutStoreDetail"));
			//未出帐数量
			newMaterial.put("num",realnum -chargeNum);
			//新增未出帐材料计划
			db.insert(newMaterial);
			//修改已出账材料信息
			outStoreDetail.put("realPrice", realPrice);
			outStoreDetail.put("realTotalPrice", realTotalPrice);
			outStoreDetail.put("num", chargeNum);
			db.update(outStoreDetail);
			chargeNum = 0.0;
		}
		if(chargeNum>0){
			chargeoffDetail.put("num", chargeNum);
			db.update(chargeoffDetail);
			result.put("method.infoMsg","对账成功！");
			result.put("method.url", in.get("url"));
		}else{
			result.put("method.infoMsg","剩余对账完成！");
			result.put("method.url", in.get("listUrl"));
		}
		return result;
	}
}
