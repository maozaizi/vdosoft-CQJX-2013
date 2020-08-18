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
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;


public class MaterialInfoService extends Service{
	
	/**
	 * 
	 * @Description 材料列表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author duch
	 * @date Jul 12, 2013 5:21:07 PM
	 */
	public IMap getList(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		// 排序信息
		OrderBean orderBean = new OrderBean();
		// 查询参数集合
		IMap paramMap = (IMap) in.get("materialInfo");
		Page page = null;
		String flag = (String)in.get("flag");
		if(flag==null||"".equals(flag)){
			//分页参数
			Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
			Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
			
			orderBean.put("materialClass",OrderBean.ASC);
			orderBean.put("materialGroup",OrderBean.ASC);
			// 自定义sql进行模糊分页查询
			page = 	db.pageQuery(paramMap, 
					"queryMaterialList", paramMap.getClassName(), "ID",
					currentPageNO,perCount,orderBean);
			//保留查询参数
			page.setAction(request);
			result.put("page", page);
		}
		
		return result;
	}
	/**
	 * 
	 * @Description 添加物料信息
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author duch
	 * @date Jul 16, 2013 2:58:06 PM
	 */
	public IMap addMaterialInfo(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap materialInfoMap = (IMap) in.get("materialInfo");
		IMap userMap = (IMap) in.get("userMap");
		IMap conMap = new DataMap();
		conMap.put("materialCode", materialInfoMap.get("materialCode"));
		Long total = db.getObjectTotal("queryMaterialList",conMap);
		if(total>0){
			throw new BusinessException("物料编码已使用，请重新填写！");
		}
		materialInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.MaterialInfo"));
		//为创建数据赋值
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),materialInfoMap);
		db.insert(materialInfoMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"添加物料信息";
		LogBean logb=new LogBean(userMap,materialInfoMap.get("id").toString(), "com.MaterialInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 
	 * @Description 跳转到修改物料信息
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author duch
	 * @date Jul 16, 2013 2:58:06 PM
	 */
	public IMap toUpdateMaterialInfo(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap materialInfoMap = (IMap) in.get("materialInfo");
		//查询已有数据
		materialInfoMap = db.get(materialInfoMap);
		
		result.put("materialInfo", materialInfoMap);
		return result;
	}
	/**
	 * 
	 * @Description 修改保存物料信息
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author duch
	 * @date Jul 16, 2013 2:58:06 PM
	 */
	public IMap saveMaterialInfo(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap materialInfoMap = (IMap) in.get("materialInfo");
		IMap userMap = (IMap) in.get("userMap");
		//验证物料编码是否重复
		IMap conMap = new DataMap();
		conMap.put("id", materialInfoMap.get("id"));
		conMap.put("materialCode", materialInfoMap.get("materialCode"));
		Long total = db.getObjectTotal("queryMaterialList",conMap);
		if(total>0){
			throw new BusinessException("物料编码已使用，请重新填写！");
		}
		//物料编码设为正式，单机替换正式编码
		IMap material = BeanFactory.getClassBean("com.MaterialInfo");
		material.put("id", (String)materialInfoMap.get("id"));
		material = db.get(material);
		if(!"1".equals(material.get("isFormal"))&&"1".equals(materialInfoMap.get("isFormal"))){
			String oldCode = (String)material.get("materialCode");
			String materialCode = (String)materialInfoMap.get("materialCode");
			if(oldCode.equals(materialCode)){
				conMap.put("oldCode", oldCode);
				db.update(conMap,"updateMaterialPlan");
				db.update(conMap,"updateMaterialPlanWf");
			}
		}
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),materialInfoMap);
		db.update(materialInfoMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"修改物料信息";
		LogBean logb=new LogBean(userMap,materialInfoMap.get("id").toString(), "com.MaterialInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 
	 * @Description 修改保存物料信息
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author duch
	 * @date Jul 16, 2013 2:58:06 PM
	 */
	public IMap deleteMaterialInfo(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap materialInfoMap = (IMap) in.get("materialInfo");
		IMap userMap = (IMap) in.get("userMap");
		//作废isvalid值置为0
		materialInfoMap.put("isValid", Constants.ISNOTVALID);
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),materialInfoMap);
		db.update(materialInfoMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"作废物料信息";
		LogBean logb=new LogBean(userMap,materialInfoMap.get("id").toString(), "com.MaterialInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		//提示增加成功
		result.put("method.infoMsg", OperateHelper.getDeleteMsg());
		//重定向到列表页面
		result.put("method.url", in.get("url"));
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
	
	public IMap saveMaterialInfoImport(IMap in) {
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
        //导入文件路径
        fileUrl=request.getSession().getServletContext().getRealPath("/")+ fileUrl;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
        //导入文件信息
        IMap attachmentInfoMap=(IMap) in.get("attachmentInfo");
        //导入文件上传后ID
        String attId =HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.AttachmentInfo");
		//为创建数据赋值
		attachmentInfoMap.put("id",attId);
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
					messageDetails +="Excel图表中第3列的应为物资编码！<br/>";
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
				if(!"参考单价".equals(c.getCell((short)5).getStringCellValue().trim())){
					messageDetails +="Excel图表中第6列应为参考单价！<br/>";
					flag = false;
				}
				if(!"备注".equals(c.getCell((short)6).getStringCellValue().trim())){
					messageDetails +="Excel图表中第7列的应为备注！<br/>";
					flag = false;
				}
				//循环读取excel数据
				for(int j=1;j<sheet.getLastRowNum()+1&&flag;j++) {
					IMap materialInfoParam = BeanFactory.getClassBean("com.MaterialInfo");
					materialInfoParam.put("id", HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.MaterialInfo"));
					HSSFRow hssfRow = sheet.getRow(j);
					if (hssfRow == null && j != sheet.getLastRowNum()) {
						messageDetails +="Excel图表中第"+(j+1)+"行内容为空！<br/>";
					}
					HSSFCell cell0 = hssfRow.getCell((short)0);
					//cell1 大类
					if(cell0!=null){
						String materialClass=cell0.getStringCellValue();
						materialInfoParam.put("materialClass",materialClass);
					}else{
						materialInfoParam.put("materialClass","");
					}
					//物料组
					HSSFCell cell1 = hssfRow.getCell((short)1);
					if(cell1!=null ){
						String materialGroup=cell1.getStringCellValue();
						if("0".equals(materialGroup)){
							materialGroup="";
						}
						materialInfoParam.put("materialGroup",materialGroup);
					}else{
						materialInfoParam.put("materialGroup","");
					}
					//物资编码
					HSSFCell cell2 = hssfRow.getCell((short)2);
					if(cell2!=null){
						String materialCode=cell2.getStringCellValue();
						if("0".equals(materialCode)){
							materialCode="";
						}
						materialInfoParam.put("materialCode", materialCode);
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(3)+"列的物资编码不能为空！<br/>";
						err++;
						continue;
					}
					//物资描述
					HSSFCell cell3 = hssfRow.getCell((short)3);
					if(cell3!=null){
						materialInfoParam.put("materialDetail",cell3.getStringCellValue());
						materialInfoParam.put("otherName",cell3.getStringCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(4)+"列的物资描述不能为空！<br/>";
						err++;
						continue;
					}
					//计量单位
					HSSFCell cell4 = hssfRow.getCell((short)4);
					if(cell4!=null && cell4.getStringCellValue()!=""){
						materialInfoParam.put("unity",cell4.getStringCellValue());
					}else{
						messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(5)+"列的计量单位不能为空！<br/>";
						err++;
						continue;
					}
					//参考单价
					HSSFCell cell5 = hssfRow.getCell((short)5);
					try {
						if(cell5!=null&&cell5.getNumericCellValue()+""!=""){
							materialInfoParam.put("unitPrice",cell5.getNumericCellValue());
						}else{
							messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(6)+"列的数量不能为空！<br/>";
							err++;
							continue;
						}
					} catch (RuntimeException e) {
						if(cell5!=null&&cell5.getStringCellValue()+""!=""){
							materialInfoParam.put("unitPrice",cell5.getStringCellValue());
						}else{
							messageDetails +="Excel图表中第<font color='red'>"+(j+1)+"</font>行第"+(6)+"列的数量不能为空！<br/>";
							err++;
							continue;
						}
					}
					
					//备注
					HSSFCell cell6 = hssfRow.getCell((short)6);
					if(cell6!=null){
						materialInfoParam.put("remark",cell6.getStringCellValue());
					}
					HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),materialInfoParam);
					//存入待保存的正确信息
					successMapList.add(materialInfoParam);
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
					IMap materialInfo = (IMap)successMapList.get(i);
					IMap materialInfoParam=BeanFactory.getClassBean("com.MaterialInfo");
					materialInfoParam.put("materialCode",materialInfo.get("materialCode"));
					List list=db.getList(materialInfoParam,null);
					if(!list.isEmpty()){
						materialInfoParam = (IMap)list.get(0);
						materialInfo.put("id",materialInfoParam.get("id"));
						HelperDB.setModifyInfo(HelperApp.getUserName(userMap),materialInfo);
						db.update(materialInfo);
					}else{
						db.insert(materialInfo);
					}
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
			result.put("attId",attId);
		}
		return result;
	}
	
}
