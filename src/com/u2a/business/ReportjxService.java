package com.u2a.business;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.brick.api.Service;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.WebFunction;
import com.u2a.framework.util.DateTimeUtil;
/**
 * 
 * 系统名称：报表业务类   
 * 类名称：ReportjxService   
 * 类描述：   
 * 创建人：mengbt
 * 创建时间：Jul 1, 2014 10:00:34 AM
 */
public class ReportjxService extends Service{
	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author mengbt
	 * @date Jul 1, 2014 10:01:20 AM
	 */
	public IMap<String, Object> importDailyReport(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//判断当前登录用户权限
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		String factoryName = "";
		String noteDate = (String) in.get("noteDate");
		if("".equals(noteDate)||noteDate==null){
			noteDate = DateTimeUtil.date2str("yyyy-MM-dd", DateTimeUtil.getLocalDate());
			in.put("noteDate", noteDate);
		}
		noteDate = noteDate.substring(0,4)+"年"+noteDate.substring(5,7)+"月"+noteDate.substring(8,10)+"日";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				in.put("orgCode", orgId);
				factoryName="机修公司";
			}
		}
		
		List<IMap> expatsList = db.getList(in, "getDailyReportList", "");
		String dateStr = (String)in.get("noteDate");
		//上月25日
		Date date =DateTimeUtil.getBeforeMonth(new Date(),25);
		Calendar cb = Calendar.getInstance();
		cb.setTime(date);
		//当前时间
		Calendar cn = Calendar.getInstance();
		cn.setTime(DateTimeUtil.str2Date("yyyy-MM-dd", (String)in.get("noteDate")));
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = noteDate+"机修公司"+factoryName+"日报.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(factoryName+"日报" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			//添加标题行
		    ws.mergeCells(0, 0, 13,0);
		    //单位
		    ws.mergeCells(0, 1, 2,1);
		    //日期
			ws.mergeCells(12, 1, 13,1);
			
			//列宽
			ws.setColumnView(0, 5);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 15);
			ws.setColumnView(9, 15);
			ws.setColumnView(10, 15);
			ws.setColumnView(11, 15);
			ws.setColumnView(12, 15);
			ws.setColumnView(13, 15);
			ws.setColumnView(14, 15);
			ws.setColumnView(15, 15);
			ws.setColumnView(16, 15);
			ws.setColumnView(17, 15);
			ws.setColumnView(18, 15);
			ws.setColumnView(19, 15);
			ws.setColumnView(20, 15);
			ws.setColumnView(21, 15);
			ws.setColumnView(22, 15);
			ws.setColumnView(23, 15);
			ws.setColumnView(24, 15);
			ws.setColumnView(25, 15);
			ws.setColumnView(26, 15);
			ws.setColumnView(27, 15);
			ws.setColumnView(28, 15);
			
			
			ws.addCell(new jxl.write.Label(0, 0, factoryName+"生产日报", format2));
			ws.addCell(new jxl.write.Label(0, 1,"单位："+factoryName, format1));
			ws.addCell(new jxl.write.Label(3, 1,"", format1));
			ws.addCell(new jxl.write.Label(4, 1,"", format1));
			ws.addCell(new jxl.write.Label(5, 1,"", format1));
			ws.addCell(new jxl.write.Label(6, 1,"", format1));
			ws.addCell(new jxl.write.Label(7, 1,"", format1));
			ws.addCell(new jxl.write.Label(8, 1,"", format1));
			ws.addCell(new jxl.write.Label(9, 1,"", format1));
			ws.addCell(new jxl.write.Label(10, 1,"", format1));
			ws.addCell(new jxl.write.Label(11, 1,"", format1));
			ws.addCell(new jxl.write.Label(12, 1,noteDate, format1));
			ws.mergeCells(0,2,0,3);
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.mergeCells(1,2,1,3);
			ws.addCell(new jxl.write.Label(1, 2, "进厂设备/工卡号", format1));
			ws.mergeCells(2,2,2,3);
			ws.addCell(new jxl.write.Label(2, 2, "送修单位", format1));
			ws.mergeCells(3,2,3,3);
			ws.addCell(new jxl.write.Label(3, 2, "出厂设备/工卡号", format1));
			ws.mergeCells(4,2,4,3);
			ws.addCell(new jxl.write.Label(4, 2, "使用单位", format1));
			ws.mergeCells(5,2,9,2);
			ws.addCell(new jxl.write.Label(5, 2, "在修设备", format1));
			ws.addCell(new jxl.write.Label(5, 3, "设备编号", format1));
			ws.addCell(new jxl.write.Label(6, 3, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(7, 3, "设备规格名称", format1));
			ws.addCell(new jxl.write.Label(8, 3, "送修单位", format1));
			ws.addCell(new jxl.write.Label(9, 3, "开工日期", format1));
			ws.mergeCells(10,2,10,3);
			ws.addCell(new jxl.write.Label(10, 2, "工作进度", format1));
			ws.mergeCells(11,2,11,3);
			ws.addCell(new jxl.write.Label(11, 2, "待料情况", format1));
			ws.mergeCells(12,2,12,3);
			ws.addCell(new jxl.write.Label(12, 2, "上井服务情况", format1));
			ws.mergeCells(13,2,13,3);
			ws.addCell(new jxl.write.Label(13, 2, "备注", format1));
//			int days=0;
//			String dates[]  = new String[31];
//			String startDate = DateTimeUtil.date2str("yyyy-MM-dd", cb.getTime());
//			for(;cb.before(cn);days++){
//				ws.addCell(new jxl.write.Label(7+days, 2,DateTimeUtil.date2str("yyyy-MM-dd", cb.getTime()), format1));
//				cb.add(Calendar.DAY_OF_YEAR,1);
//				dates[days] = DateTimeUtil.date2str("yyyy-MM-dd", cb.getTime());
//			}
			in.put("dateColumn", "t1.in_date");
			in.put("startdate", (String)in.get("noteDate"));
			in.put("enddate", (String)in.get("noteDate"));
			List inList = db.getList(in,"getWeekReportList",null);
			in.put("dateColumn", "t.factory_date");
			List outList = db.getList(in,"getWeekReportList",null);
			int count = 0;
			count = outList.size()>inList.size()?outList.size():inList.size();
			count = count > expatsList.size()?count:expatsList.size();
			//当日进厂设备
			for (int i = 0; i < count; i++){
				ws.addCell(new jxl.write.Label(0, i+4,(i+1)+"", format1));
				if(i<inList.size()){
					IMap map = (IMap) inList.get(i);
					ws.addCell(new jxl.write.Label(1, i+4, (String)map.get("equipment_name")+"/"+(String)map.get("work_card"), format1));
					ws.addCell(new jxl.write.Label(2, i+4, (String)map.get("dept_from"), format1));
				}else{
					ws.addCell(new jxl.write.Label(1, i+4, "", format1));
					ws.addCell(new jxl.write.Label(2, i+4, "", format1));
				}
			}
			//当日出厂设备
			for (int i = 0; i <count; i++) {
				if(i<outList.size()){
					IMap map = (IMap) outList.get(i);
					ws.addCell(new jxl.write.Label(3, i+4, (String)map.get("equipment_name")+"/"+(String)map.get("work_card"), format1));
					ws.addCell(new jxl.write.Label(4, i+4, "", format1));
				}else{
					ws.addCell(new jxl.write.Label(3, i+4, "", format1));
					ws.addCell(new jxl.write.Label(4, i+4, "", format1));
				}
			}
			for (int i = 0; i <count; i++) {
				if( i<expatsList.size()){
					IMap map = expatsList.get(i);
					ws.addCell(new jxl.write.Label(5, i+4, (String)map.get("equipmentModel"), format1));
					ws.addCell(new jxl.write.Label(6, i+4, (String)map.get("workCard"), format1));
					ws.addCell(new jxl.write.Label(7, i+4, (String)map.get("equipmentName"), format1));
					ws.addCell(new jxl.write.Label(8, i+4, (String)map.get("deptFrom"), format1));
					ws.addCell(new jxl.write.Label(9, i+4, DateTimeUtil.date2str("yyyy-MM-dd", (Date)map.get("startDate")), format1));
					ws.addCell(new jxl.write.Label(10, i+4, (String)map.get("noteContent"), format1));
					ws.addCell(new jxl.write.Label(11, i+4, (String)map.get("materialQk"), format1));
					ws.addCell(new jxl.write.Label(12, i+4, "", format1));
					ws.addCell(new jxl.write.Label(13, i+4, "", format1));
				}else{
					ws.addCell(new jxl.write.Label(5, i+4, "", format1));
					ws.addCell(new jxl.write.Label(6, i+4, "", format1));
					ws.addCell(new jxl.write.Label(7, i+4, "", format1));
					ws.addCell(new jxl.write.Label(8, i+4, "", format1));
					ws.addCell(new jxl.write.Label(9, i+4, "", format1));
					ws.addCell(new jxl.write.Label(10, i+4, "", format1));
					ws.addCell(new jxl.write.Label(11, i+4, "", format1));
					ws.addCell(new jxl.write.Label(12, i+4, "", format1));
					ws.addCell(new jxl.write.Label(13, i+4, "", format1));
				}
				//查询其他日期日报信息
//				IMap condition = new DataMap();
//				condition.put("workCard", (String)map.get("workCard"));
//				condition.put("startDate", startDate);
//				condition.put("endDate", (String)in.get("noteDate"));
//				List<IMap> noteList = db.getList(condition, "getDailyReportList", "");
//				if(dates!=null){
//					for(int j=0;j<days;j++){
//						for(int k=0;k<noteList.size();k++){
//							IMap noteMap  = (IMap)noteList.get(k);
//							String notedate = DateTimeUtil.date2str("yyyy-MM-dd", (Date)map.get("noteDate"));
//							if(notedate.equals(dates[j])){
//								ws.addCell(new jxl.write.Label(6+j, i+3, (String)map.get("noteContent"), format1));
//							}
//						}
//					}
//				}
//				ws.addCell(new jxl.write.Label(6, i+3, (String)map.get("materialQk"), format1));
			}
			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	/**
	 * 公司材料消耗汇总表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 8, 2014 2:20:44 PM
	 */
	public IMap<String, Object> importOutReport(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//判断当前登录用户权限
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		String factoryName = "";
		String inDate = (String) in.get("inDate");
		if("".equals(inDate)||inDate==null){
			inDate = DateTimeUtil.date2str("yyyy-MM", DateTimeUtil.getLocalDate());
			in.put("inDate", inDate);
		}
		//否则根据查询条件不同，值不同
		if("1".equals(factory)){
			//银川
			in.put("orgCode", "|CQ|.|JX|.|YC|");
			factoryName="银川厂";
		}else if("2".equals(factory)){
			//庆阳
			in.put("orgCode", "|CQ|.|JX|.|QY|");
			factoryName="庆阳厂";
		}else{
			//所有
			in.put("orgCode", orgId);
		}
		inDate = inDate.substring(0,4)+"年"+inDate.substring(5,7)+"月";
		//单位部门信息
		List<IMap> orgList = db.getList(in, "getOrgInfoList", "");
		String dateStr = (String)in.get("inDate");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = inDate+"机修公司"+factoryName+"材料统计表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			WritableSheet ws  =  book.createSheet(inDate+"机修公司"+factoryName+"材料消耗汇总表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			//添加标题行
			ws.mergeCells(0, 0, 6, 1);
			
			//列宽
			ws.setColumnView(0, 5);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 15);
			ws.setColumnView(9, 15);
			ws.setColumnView(10, 15);
			ws.setColumnView(11, 15);
			ws.setColumnView(12, 15);
			ws.setColumnView(13, 15);
			ws.setColumnView(14, 15);
			ws.setColumnView(15, 15);
			ws.setColumnView(16, 15);
			ws.setColumnView(17, 15);
			ws.setColumnView(18, 15);
			ws.setColumnView(19, 15);
			ws.setColumnView(20, 15);
			ws.setColumnView(21, 15);
			ws.setColumnView(22, 15);
			ws.setColumnView(23, 15);
			ws.setColumnView(24, 15);
			ws.setColumnView(25, 15);
			ws.setColumnView(26, 15);
			ws.setColumnView(27, 15);
			ws.setColumnView(28, 15);
			
			
			ws.addCell(new jxl.write.Label(0, 0, inDate+"机修公司材料消耗汇总表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "单位名称", format1));
			ws.addCell(new jxl.write.Label(2, 2, "材料1", format1));
			ws.addCell(new jxl.write.Label(3, 2, "材料2", format1));
			ws.addCell(new jxl.write.Label(4, 2, "材料3", format1));
			ws.addCell(new jxl.write.Label(5, 2, "材料4", format1));
			ws.addCell(new jxl.write.Label(6, 2, "材料5", format1));
			ws.addCell(new jxl.write.Label(7, 2, "材料小计", format1));
			
			for (int i = 0; i < orgList.size(); i++) {
				IMap map = orgList.get(i);
				ws.addCell(new jxl.write.Label(0, i+3, i+1+"", format1));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("pOrgName")+"-"+(String)map.get("OrgName"), format1));
				//查询消耗材料 
				//小计金额
				double totalPrice = 0;
				//出库查询条件
				IMap condition = new DataMap();
				condition.put("orgId", (String)map.get("orgId"));
				condition.put("inDate", inDate);
				for(int j=1;j<=5;j++){
					condition.put("outType", j);
					List m1 = db.getList(condition, "getOutStorePrice","");
					if(m1.size()>0){
						IMap outMap = (IMap)m1.get(0);
						double outprice =(Double)outMap.get("outPrice");
						ws.addCell(new jxl.write.Number(j+1, i+3, outprice, format1));
						totalPrice += outprice;
					}
				}
				ws.addCell(new jxl.write.Number(7, i+3, totalPrice, format1));
			}
			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	/**
	 * 上井服务明细导出
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 17, 2014 10:22:59 AM
	 */
	public IMap<String, Object> importExpatsExcel(IMap<String, Object> in){
		IMap<String, Object> result  = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				in.put("orgCode", orgId);
				factoryName="机修公司";
			}
		}
		
		List<IMap> expatsList = db.getList(in, "getExpatsReportList", "");
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "机修公司上井服务明细表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("机修公司"+factoryName+"上井服务明细表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setAlignment(jxl.format.Alignment.CENTRE);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			format1.setWrap(true);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			
			//添加标题行
			ws.mergeCells(0,0,16,0);
			
			
			//列宽
			ws.setColumnView(0, 16);
			ws.setColumnView(1, 16);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 10);
			ws.setColumnView(9, 10);
			ws.setColumnView(10, 10);
			ws.setColumnView(11, 10);
			ws.setColumnView(12, 10);
			ws.setColumnView(13, 10);
			ws.setColumnView(14, 10);
			ws.setColumnView(15, 10);
			ws.setColumnView(16, 10);
			ws.setColumnView(17, 10);
			ws.setColumnView(18, 10);
			ws.setColumnView(19, 10);
			ws.setColumnView(20, 10);
			ws.setColumnView(21, 10);
			ws.setColumnView(22, 10);
			ws.setColumnView(23, 10);
			ws.setColumnView(24, 10);
			ws.setColumnView(25, 10);
			ws.setColumnView(26, 10);
			ws.setColumnView(27, 10);
			ws.setColumnView(28, 10);
			ws.setColumnView(38, 23);
			ws.setColumnView(39, 23);
			
			
			ws.addCell(new jxl.write.Label(0, 0, "机修公司"+factoryName+"上井服务明细表", format2));
			ws.addCell(new jxl.write.Label(0, 1, "单位："+factoryName, format2));
			ws.addCell(new jxl.write.Label(14, 1, "时间："+(String)in.get("beginInDate")+"/"+(String)in.get("endInDate"), format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "修理单位", format1));
			ws.addCell(new jxl.write.Label(2, 2, "项目部", format1));
			ws.addCell(new jxl.write.Label(3, 2, "队号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "出发时间", format1));
			ws.addCell(new jxl.write.Label(5, 2, "回厂时间", format1));
			ws.addCell(new jxl.write.Label(6, 2, "设备名称", format1));
			ws.addCell(new jxl.write.Label(7, 2, "规格型号", format1));
			ws.addCell(new jxl.write.Label(8, 2, "修理内容", format1));
			ws.addCell(new jxl.write.Label(9, 2, "修理性质", format1));
			ws.addCell(new jxl.write.Label(10, 2, "修理大类", format1));
			ws.addCell(new jxl.write.Label(11, 2, "工作任务完成情况", format1));
			ws.addCell(new jxl.write.Label(12, 2, "上井人数", format1));
			ws.addCell(new jxl.write.Label(13, 2, "上井维修费", format1));
			ws.addCell(new jxl.write.Label(14, 2, "上井发生材料", format1));
			ws.addCell(new jxl.write.Label(15, 2, "车号", format1));
			ws.addCell(new jxl.write.Label(16, 2, "备注", format1));
			
			
			for (int i = 0; i < expatsList.size(); i++) {
				IMap map = expatsList.get(i);
				
				ws.addCell(new jxl.write.Number(0, i+3, i+1, wcfNint));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("deptName"), format1));
				List list = WebFunction.getDataItem("pro_dept");
				for(int j=0;j<list.size();j++){
					IMap projectDeptMap = (IMap)list.get(j);
					String value = (String)projectDeptMap.get("dataItemValue");
					if(value.equals((String)map.get("projectDept"))){
						ws.addCell(new jxl.write.Label(2, i+3, (String)projectDeptMap.get("dataItemName"), format1));
						break;
					}
				}
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("expatsTo"), format1));
				if((Date)map.get("departureTime")!=null){
					ws.addCell(new jxl.write.Label(4, i+3, DateTimeUtil.date2str("yyyy-MM-dd", (Date)map.get("departureTime")), format1));
				}
				if((Date)map.get("backTime")!=null){
					ws.addCell(new jxl.write.Label(5, i+3, DateTimeUtil.date2str("yyyy-MM-dd", (Date)map.get("backTime")), format1));
				}else{
					ws.addCell(new jxl.write.Label(5, i+3, "", format1));
				}
				ws.addCell(new jxl.write.Label(6, i+3, (String)map.get("equipmentName"), format1));
				ws.addCell(new jxl.write.Label(7, i+3, (String)map.get("equipmentModel"), format1));
				//修理内容
				ws.addCell(new jxl.write.Label(8, i+3, (String)map.get("vehiclesModel"), format1));
				ws.addCell(new jxl.write.Label(9, i+3, (String)map.get("repairType"), format1));
				ws.addCell(new jxl.write.Label(10, i+3, "", format1));
				if("1".equals( (String)map.get("isComplete"))){
					ws.addCell(new jxl.write.Label(11, i+3, "完成", format1));
				}
				if("0".equals( (String)map.get("isComplete"))){
					ws.addCell(new jxl.write.Label(11, i+3, "未完成", format1));
				}
				ws.addCell(new jxl.write.Number(12, i+3, (Integer)map.get("expatsNum"), wcfNint));
				ws.addCell(new jxl.write.Label(13, i+3, "", format1));
				ws.addCell(new jxl.write.Label(14, i+3, "", format1));
				ws.addCell(new jxl.write.Label(15, i+3, (String)map.get("vehiclesPlate"), format1));
				ws.addCell(new jxl.write.Label(16, i+3, (String)map.get("remark"), format1));
			}

			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 17, 2014 3:05:38 PM
	 */
	public IMap importMaterialDjReport(IMap in){
		IMap result = new DataMap();

		result.put("searchDate",in.get("searchDate"));
		String orgName = "";

		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		IMap con = new DataMap();
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			con.put("orgId", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				con.put("orgId", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				con.put("orgId", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				con.put("orgId", orgId);
				factoryName="机修公司";
			}
		}
		
		
		con.put("searchDate",((String)in.get("searchDate")).replace("-",""));
		
		List<IMap> materialWfReportList = db.getList(con,"queryMaterialDjReport",null);
		

		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "机修公司大件审批表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(factoryName+"大件审批表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setAlignment(jxl.format.Alignment.CENTRE);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			format1.setWrap(true);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			
			//添加标题行
			ws.mergeCells(0, 0, 10, 1);
			ws.mergeCells(0, 2, 1,2);
			ws.mergeCells(2,2, 8,2);
			ws.mergeCells(9,2, 10,2);
			//列宽
			ws.setColumnView(0, 15);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 10);
			ws.setColumnView(9, 10);
			ws.setColumnView(10, 10);
			ws.setColumnView(11, 10);
			ws.setColumnView(12, 10);
			ws.setColumnView(13, 10);
			ws.setColumnView(14, 10);
			ws.setColumnView(15, 10);
			ws.setColumnView(16, 10);
			ws.setColumnView(17, 10);
			ws.setColumnView(18, 10);
			ws.setColumnView(19, 10);
			ws.setColumnView(20, 10);
			ws.setColumnView(21, 10);
			ws.setColumnView(22, 10);
			ws.setColumnView(23, 10);
			ws.setColumnView(24, 10);
			ws.setColumnView(25, 10);
			ws.setColumnView(26, 10);
			ws.setColumnView(27, 10);
			ws.setColumnView(28, 10);
			ws.setColumnView(38, 23);
			ws.setColumnView(39, 23);
			
			
			ws.addCell(new jxl.write.Label(0, 0, "大件审批表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "单位："+factoryName, format1));
			ws.addCell(new jxl.write.Label(8, 2, "", format1));
			ws.addCell(new jxl.write.Label(9, 2, "时间："+in.get("searchDate"), format1));
			ws.addCell(new jxl.write.Label(0, 3, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 3, "物资编码", format1));
			ws.addCell(new jxl.write.Label(2, 3, "规格名称及型号", format1));
			ws.addCell(new jxl.write.Label(3, 3, "单位", format1));
			ws.addCell(new jxl.write.Label(4, 3, "单价", format1));
			ws.addCell(new jxl.write.Label(5, 3, "需要数量", format1));
			ws.addCell(new jxl.write.Label(6, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(7, 3, "计划时间", format1));
			ws.addCell(new jxl.write.Label(8, 3, "要求到货时间", format1));
			ws.addCell(new jxl.write.Label(9, 3, "申请单位", format1));
			ws.addCell(new jxl.write.Label(10, 3, "备注", format1));
			
			
			for (int i = 0; i < materialWfReportList.size(); i++) {
				IMap<Object, Object> map =  materialWfReportList.get(i);
				
				//ws.addCell(new jxl.write.Label(0, i+3, (String)map.get("parentOrgName"), format1));
				//ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("orgName"), format1));
				ws.addCell(new jxl.write.Label(0, i+4, (i+1)+"", format1));
				ws.addCell(new jxl.write.Label(1, i+4, (String)map.get("materialCode"), format1));
				ws.addCell(new jxl.write.Label(2, i+4, (String)map.get("materialDetail"), format1));
				ws.addCell(new jxl.write.Label(3, i+4, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Number(4, i+4, (Double)map.get("estimatePrice"), wcfN));
				if(map.get("realNum")!=null){
					ws.addCell(new jxl.write.Number(5, i+4, (Double)map.get("realNum"), wcfNint));
				}else{
					ws.addCell(new jxl.write.Number(5, i+4, (Double)map.get("materialNum"), wcfNint));
				}
				if(map.get("totalPrice")!=null){
					ws.addCell(new jxl.write.Number(6, i+4, (Double)map.get("totalPrice"), wcfN));
				}else{
					ws.addCell(new jxl.write.Label(6, i+4, "", format1));
				}
				ws.addCell(new jxl.write.Label(7, i+4, "", format1));
				ws.addCell(new jxl.write.Label(8, i+4, "", format1));
				ws.addCell(new jxl.write.Label(9, i+4, (String)map.get("parentOrgName")+"-"+(String)map.get("orgName"), format1));
				ws.addCell(new jxl.write.Label(10, i+4, (String)map.get("remark"), format1));
			}
			if(materialWfReportList.isEmpty()){
				ws.addCell(new jxl.write.Label(0,4, "", format1));
				ws.addCell(new jxl.write.Label(1,4, "", format1));
				ws.addCell(new jxl.write.Label(2,4, "", format1));
				ws.addCell(new jxl.write.Label(3,4, "", format1));
				ws.addCell(new jxl.write.Label(4,4, "", format1));
				ws.addCell(new jxl.write.Label(5,4, "", format1));
				ws.addCell(new jxl.write.Label(6,4, "", format1));
				ws.addCell(new jxl.write.Label(7,4, "", format1));
				ws.addCell(new jxl.write.Label(8,4, "", format1));
				ws.addCell(new jxl.write.Label(9,4, "", format1));
				ws.addCell(new jxl.write.Label(10,4, "", format1));
			}

			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	public IMap<String, Object> importMonthReport(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				in.put("orgCode", orgId);
			}
		}
		String yearD = "";
		if(in.get("endDate")!=null&&!"".equals((String)in.get("endDate"))){
			String endDate = ((String) in.get("endDate")).replace("-", "");
			in.put("inDate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("inDate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"生产月报.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"生产月报" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			//添加标题行
			ws.mergeCells(0, 0, 14, 0);
			ws.mergeCells(1, 1, 3, 0);
			ws.mergeCells(13, 1, 14,1);
			//列宽
			ws.setColumnView(0, 15);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 15);
			ws.setColumnView(9, 15);
			ws.setColumnView(10, 15);
			ws.setColumnView(11, 15);
			ws.setColumnView(12, 15);
			ws.setColumnView(13, 15);
			ws.setColumnView(14, 15);
			ws.setColumnView(15, 15);
			ws.setColumnView(16, 15);
			ws.setColumnView(17, 15);
			ws.setColumnView(18, 15);
			ws.setColumnView(19, 15);
			ws.setColumnView(20, 15);
			ws.setColumnView(21, 15);
			ws.setColumnView(22, 15);
			ws.setColumnView(23, 15);
			ws.setColumnView(24, 15);
			ws.setColumnView(25, 15);
			ws.setColumnView(26, 15);
			ws.setColumnView(27, 15);
			ws.setColumnView(28, 15);
			
			
			ws.addCell(new jxl.write.Label(0, 0, "完工产品汇总表", format2));
			ws.addCell(new jxl.write.Label(1, 1, "单位："+factoryName, format2));
			ws.addCell(new jxl.write.Label(13,1 , yearD, format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "设备名称及规格型号", format1));
			ws.addCell(new jxl.write.Label(2, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(3, 2, "修理类别", format1));
			ws.addCell(new jxl.write.Label(4, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(5, 2, "设备编号", format1));
			ws.addCell(new jxl.write.Label(6, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(7, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "标准台", format1));
			ws.addCell(new jxl.write.Label(9, 2, "产值", format1));
			ws.addCell(new jxl.write.Label(10, 2, "工时", format1));
			ws.addCell(new jxl.write.Label(11, 2, "完成时间", format1));
			ws.addCell(new jxl.write.Label(12, 2, "承修单位", format1));
			ws.addCell(new jxl.write.Label(13, 2, "是否结算", format1));
			ws.addCell(new jxl.write.Label(14, 2, "备注", format1));
			//钻修
			in.put("tableName", "bus_drilling");
			List reportList = db.getList(in,"getMonthReport",null);
			for (int i = 0; i < reportList.size(); i++) {
				IMap map =  (IMap) reportList.get(i);
				ws.addCell(new jxl.write.Label(0, i+3, (i+1)+"", format1));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("equipment_name")+"-"+(String)map.get("equipment_code"), format1));
				ws.addCell(new jxl.write.Label(2, i+3, (String)map.get("dept_from"), format1));
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Label(4, i+3, (String)map.get("work_card"), format1));
				ws.addCell(new jxl.write.Label(5, i+3, (String)map.get("self_code"), wcfNint));
				ws.addCell(new jxl.write.Label(6, i+3, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Label(7, i+3, "", format1));
				ws.addCell(new jxl.write.Label(8, i+3, (String)map.get("standard_num"), format1));
				ws.addCell(new jxl.write.Label(9, i+3, (String)map.get("output_value"), format1));
				ws.addCell(new jxl.write.Label(10, i+3, (String)map.get("quota_hour"), format1));
				ws.addCell(new jxl.write.Label(11, i+3, (String)map.get("parentOrgName")+"-"+(String)map.get("orgName"), format1));
				ws.addCell(new jxl.write.Label(12, i+3, (String)map.get("complete_date"), format1));
				ws.addCell(new jxl.write.Label(13, i+3, "", format1));
				ws.addCell(new jxl.write.Label(14, i+3, (String)map.get("remark"), format1));
			}
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 22, 2014 10:26:38 AM
	 */
	public IMap importWeekReport(IMap in){
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				in.put("orgCode", orgId);
				factoryName="机修公司";
			}
		}
		//获取当前选择日期
		Date date = DateTimeUtil.str2Date("yyyy-MM-dd", (String)in.get("noteDate"));//  ;
		//获取选择日期所在的周及起始时间结束时间
		
		Date startdate =DateTimeUtil.getFirstDayOfWeek(date);
		Date enddate = DateTimeUtil.getLastDayOfWeek(date);
		int weekNum = DateTimeUtil.getWeekOfYear(date);
		
		in.put("startdate", DateTimeUtil.date2str("yyyy-MM-dd", startdate));
		in.put("enddate", DateTimeUtil.date2str("yyyy-MM-dd", enddate));
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = factoryName+"生产周报.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("周报表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			//添加标题行
			ws.mergeCells(0, 0, 15, 0);
			//ws.mergeCells(11, 1, 3, 0);
			//列宽
			ws.setColumnView(0, 15);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 15);
			ws.setColumnView(9, 15);
			ws.setColumnView(10, 15);
			ws.setColumnView(11, 15);
			ws.setColumnView(12, 15);
			ws.setColumnView(13, 15);
			ws.setColumnView(14, 15);
			ws.setColumnView(15, 15);
			ws.setColumnView(16, 15);
			ws.setColumnView(17, 15);
			ws.setColumnView(18, 15);
			ws.setColumnView(19, 15);
			ws.setColumnView(20, 15);
			ws.setColumnView(21, 15);
			ws.setColumnView(22, 15);
			ws.setColumnView(23, 15);
			ws.setColumnView(24, 15);
			ws.setColumnView(25, 15);
			ws.setColumnView(26, 15);
			ws.setColumnView(27, 15);
			ws.setColumnView(28, 15);
			
			
			ws.addCell(new jxl.write.Label(0, 0, factoryName+"设备修理明细表（周报表）", format2));
			ws.addCell(new jxl.write.Label(0, 1, "单位："+factoryName, format2));
			ws.addCell(new jxl.write.Label(14, 1, "日期："+in.get("startdate")+"-"+in.get("enddate"), format2));
			ws.mergeCells(0,2,15, 0);//进厂设备标题
			ws.addCell(new jxl.write.Label(0, 2, "进厂设备", format1));
			ws.addCell(new jxl.write.Label(0, 3, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 3, "设备名称及型号", format1));
			ws.addCell(new jxl.write.Label(2, 3, "工卡号", format1));
			ws.addCell(new jxl.write.Label(3, 3, "设备编号", format1));
			ws.addCell(new jxl.write.Label(4, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(5, 3, "修理类别", format1));
			ws.addCell(new jxl.write.Label(6, 3, "进厂日期", format1));
			ws.addCell(new jxl.write.Label(7, 3, "送修车号", format1));
			ws.addCell(new jxl.write.Label(8, 3, "送修单位", format1));
			ws.addCell(new jxl.write.Label(9, 3, "送修项目部", format1));
			ws.addCell(new jxl.write.Label(10, 3, "完工日期", format1));
			ws.addCell(new jxl.write.Label(11, 3, "出厂日期", format1));
			ws.addCell(new jxl.write.Label(12, 3, "出厂车号", format1));
			ws.addCell(new jxl.write.Label(13, 3, "使用单位", format1));
			ws.addCell(new jxl.write.Label(14, 3, "项目部", format1));
			ws.addCell(new jxl.write.Label(15, 3, "备注", format1));
			int hrNum = 4;
			//进厂设备
			in.put("dateColumn", "t1.in_date");
			List reportList = db.getList(in,"getWeekReportList",null);
			for (int i = 0; i < reportList.size(); i++) {
				IMap map =  (IMap) reportList.get(i);
				ws.addCell(new jxl.write.Label(0, i+hrNum, (i+1)+"", format1));
				ws.addCell(new jxl.write.Label(1, i+hrNum, (String)map.get("equipment_name")+"-"+(String)map.get("equipment_model"), format1));
				ws.addCell(new jxl.write.Label(2, i+hrNum, (String)map.get("work_card"), format1));
				ws.addCell(new jxl.write.Label(3, i+hrNum, (String)map.get("self_code"), format1));
				ws.addCell(new jxl.write.Label(4, i+hrNum, "1", format1));
				ws.addCell(new jxl.write.Label(5, i+hrNum, (String)map.get("repair_type"), format1));
				if((Date)map.get("in_date")!=null){
					ws.addCell(new jxl.write.Label(6, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("in_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(6, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(7, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(8, i+hrNum, (String)map.get("dept_from"), format1));
				ws.addCell(new jxl.write.Label(9, i+hrNum, (String)map.get("project_dept"), format1));
				if((Date)map.get("complete_date")!=null){
					ws.addCell(new jxl.write.Label(10, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("complete_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(10, i+hrNum,"", format1));
				}
				if((Date)map.get("factory_date")!=null){
					ws.addCell(new jxl.write.Label(11, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("factory_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(11, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(12, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(13, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(14, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(15, i+hrNum, "", format1));
			}
			hrNum += reportList.size();
			if(reportList.isEmpty()){
				for(int i = 0 ;i<16;i++){
					ws.addCell(new jxl.write.Label(i, hrNum, "", format1));
				}
				hrNum += 1;
			}
			//完工设备
			in.put("dateColumn", "t.complete_date");
			hrNum += 1;
			ws.mergeCells(0,hrNum,15, 0);//完工设备标题
			ws.addCell(new jxl.write.Label(0, hrNum, "完工设备", format1));
			hrNum += 1;
			ws.addCell(new jxl.write.Label(0, hrNum, "序号", format1));
			ws.addCell(new jxl.write.Label(1, hrNum, "设备名称及型号", format1));
			ws.addCell(new jxl.write.Label(2, hrNum, "工卡号", format1));
			ws.addCell(new jxl.write.Label(3, hrNum, "设备编号", format1));
			ws.addCell(new jxl.write.Label(4, hrNum, "数量", format1));
			ws.addCell(new jxl.write.Label(5, hrNum, "修理类别", format1));
			ws.addCell(new jxl.write.Label(6, hrNum, "进厂日期", format1));
			ws.addCell(new jxl.write.Label(7, hrNum, "送修车号", format1));
			ws.addCell(new jxl.write.Label(8, hrNum, "送修单位", format1));
			ws.addCell(new jxl.write.Label(9, hrNum, "送修项目部", format1));
			ws.addCell(new jxl.write.Label(10, hrNum, "完工日期", format1));
			ws.addCell(new jxl.write.Label(11, hrNum, "出厂日期", format1));
			ws.addCell(new jxl.write.Label(12, hrNum, "出厂车号", format1));
			ws.addCell(new jxl.write.Label(13, hrNum, "使用单位", format1));
			ws.addCell(new jxl.write.Label(14, hrNum, "项目部", format1));
			ws.addCell(new jxl.write.Label(15, hrNum, "备注", format1));
			reportList = db.getList(in,"getWeekReportList",null);
			hrNum += 1;
			for (int i = 0; i < reportList.size(); i++) {
				IMap map =  (IMap) reportList.get(i);
				ws.addCell(new jxl.write.Label(0, i+hrNum, (i+1)+"", format1));
				ws.addCell(new jxl.write.Label(1, i+hrNum, (String)map.get("equipment_name")+"-"+(String)map.get("equipment_code"), format1));
				ws.addCell(new jxl.write.Label(2, i+hrNum, (String)map.get("work_card"), format1));
				ws.addCell(new jxl.write.Label(3, i+hrNum, (String)map.get("self_code"), format1));
				ws.addCell(new jxl.write.Label(4, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(5, i+hrNum, (String)map.get("repair_type"), format1));
				if((Date)map.get("in_date")!=null){
					ws.addCell(new jxl.write.Label(6, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("in_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(6, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(7, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(8, i+hrNum, (String)map.get("dept_from"), format1));
				ws.addCell(new jxl.write.Label(9, i+hrNum, (String)map.get("project_dept"), format1));
				if((Date)map.get("complete_date")!=null){
					ws.addCell(new jxl.write.Label(10, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("complete_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(10, i+hrNum,"", format1));
				}
				if((Date)map.get("factory_date")!=null){
					ws.addCell(new jxl.write.Label(11, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("factory_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(11, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(12, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(13, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(14, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(15, i+hrNum, "", format1));
			}
			hrNum += reportList.size();
			if(reportList.isEmpty()){
				for(int i = 0 ;i<16;i++){
					ws.addCell(new jxl.write.Label(i, hrNum, "", format1));
				}
				hrNum += 1;
			}
			//出厂设备
			in.put("dateColumn", "t.factory_date");
			hrNum += 1;
			ws.mergeCells(0,hrNum,15, 0);//出厂设备标题
			ws.addCell(new jxl.write.Label(0, hrNum, "出厂设备", format1));
			hrNum += 1;
			ws.addCell(new jxl.write.Label(0, hrNum, "序号", format1));
			ws.addCell(new jxl.write.Label(1, hrNum, "设备名称及型号", format1));
			ws.addCell(new jxl.write.Label(2, hrNum, "工卡号", format1));
			ws.addCell(new jxl.write.Label(3, hrNum, "设备编号", format1));
			ws.addCell(new jxl.write.Label(4, hrNum, "数量", format1));
			ws.addCell(new jxl.write.Label(5, hrNum, "修理类别", format1));
			ws.addCell(new jxl.write.Label(6, hrNum, "进厂日期", format1));
			ws.addCell(new jxl.write.Label(7, hrNum, "送修车号", format1));
			ws.addCell(new jxl.write.Label(8, hrNum, "送修单位", format1));
			ws.addCell(new jxl.write.Label(9, hrNum, "送修项目部", format1));
			ws.addCell(new jxl.write.Label(10, hrNum, "完工日期", format1));
			ws.addCell(new jxl.write.Label(11, hrNum, "出厂日期", format1));
			ws.addCell(new jxl.write.Label(12, hrNum, "出厂车号", format1));
			ws.addCell(new jxl.write.Label(13, hrNum, "使用单位", format1));
			ws.addCell(new jxl.write.Label(14, hrNum, "项目部", format1));
			ws.addCell(new jxl.write.Label(15, hrNum, "备注", format1));
			reportList = db.getList(in,"getWeekReportList",null);
			hrNum += 1;
			for (int i = 0; i < reportList.size(); i++) {
				IMap map =  (IMap) reportList.get(i);
				ws.addCell(new jxl.write.Label(0, i+hrNum, (i+1)+"", format1));
				ws.addCell(new jxl.write.Label(1, i+hrNum, (String)map.get("equipment_name")+"-"+(String)map.get("equipment_code"), format1));
				ws.addCell(new jxl.write.Label(2, i+hrNum, (String)map.get("work_card"), format1));
				ws.addCell(new jxl.write.Label(3, i+hrNum, (String)map.get("self_code"), format1));
				ws.addCell(new jxl.write.Label(4, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(5, i+hrNum, (String)map.get("repair_type"), format1));
				if((Date)map.get("in_date")!=null){
					ws.addCell(new jxl.write.Label(6, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("in_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(6, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(7, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(8, i+hrNum, (String)map.get("dept_from"), format1));
				ws.addCell(new jxl.write.Label(9, i+hrNum, (String)map.get("project_dept"), format1));
				if((Date)map.get("complete_date")!=null){
					ws.addCell(new jxl.write.Label(10, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("complete_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(10, i+hrNum,"", format1));
				}
				if((Date)map.get("factory_date")!=null){
					ws.addCell(new jxl.write.Label(11, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("factory_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(11, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(12, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(13, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(14, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Label(15, i+hrNum, "", format1));
			}
			hrNum += reportList.size();
			if(reportList.isEmpty()){
				for(int i = 0 ;i<16;i++){
					ws.addCell(new jxl.write.Label(i, hrNum, "", format1));
				}
				hrNum += 1;
			}
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 *  描述: 生成机修公司劳务（收入）汇总表
	 *  方法: importLwhzReport方法
	 *  作者: duch
	 *  时间: Jul 21, 2014
	 *  版本: 1.0
	 */
	public IMap importLwhzReport(IMap in){
		IMap result = new DataMap();
		
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		IMap con = new DataMap();
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			con.put("orgId", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				con.put("orgId", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				con.put("orgId", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				con.put("orgId", orgId);
			}
		}
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "机修公司劳务（收入）汇总表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("汇总表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			wcfN.setWrap(true);
			
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setAlignment(jxl.format.Alignment.CENTRE);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			format1.setWrap(true);
			//设置表头
			WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		    jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			
		    //添加标题行
			ws.mergeCells(0, 0, 66, 1);
			
			ws.mergeCells(0, 2, 1, 2);
			ws.mergeCells(2, 2, 4, 2);
			ws.mergeCells(63, 2, 64, 2);
			ws.mergeCells(65, 2, 66, 2);
			
			ws.mergeCells(0, 3, 0, 5);
			ws.mergeCells(1, 3, 3, 5);
			
			ws.mergeCells(4, 3, 36, 3);
			ws.mergeCells(4, 4, 7, 4);
			ws.mergeCells(8, 4, 11, 4);
			ws.mergeCells(12, 4, 15, 4);
			ws.mergeCells(16, 4, 18, 4);
			ws.mergeCells(19, 4, 21, 4);
			ws.mergeCells(22, 4, 24, 4);
			ws.mergeCells(25, 4, 27, 4);
			ws.mergeCells(28, 4, 30, 4);
			ws.mergeCells(31, 4, 33, 4);
			ws.mergeCells(34, 4, 36, 4);
			
			ws.mergeCells(37, 3, 40, 4);
			
			ws.mergeCells(41, 3, 64, 3);
			ws.mergeCells(41, 4, 41, 5);
			
			ws.mergeCells(42, 4, 43, 4);
			ws.mergeCells(44, 4, 45, 4);
			ws.mergeCells(46, 4, 47, 4);
			ws.mergeCells(48, 4, 49, 4);
			ws.mergeCells(50, 4, 51, 4);
			ws.mergeCells(52, 4, 53, 4);
			ws.mergeCells(54, 4, 55, 4);
			ws.mergeCells(56, 4, 57, 4);
			ws.mergeCells(58, 4, 59, 4);
			ws.mergeCells(60, 4, 61, 4);
			ws.mergeCells(62, 4, 64, 4);
			
			ws.mergeCells(65, 3, 66, 4);
			
			//列宽
//			ws.setColumnView(0, 15);
//			ws.setColumnView(1, 15);
//			ws.setColumnView(2, 10);
			ws.setColumnView(3, 15);
//			ws.setColumnView(4, 10);
//			ws.setColumnView(5, 10);
//			ws.setColumnView(6, 10);
//			ws.setColumnView(7, 10);
//			ws.setColumnView(8, 15);
//			ws.setColumnView(9, 15);
//			ws.setColumnView(10, 15);
//			ws.setColumnView(11, 15);
//			ws.setColumnView(12, 15);
//			ws.setColumnView(13, 15);
//			ws.setColumnView(14, 15);
//			ws.setColumnView(15, 15);
//			ws.setColumnView(16, 15);
//			ws.setColumnView(17, 15);
//			ws.setColumnView(18, 15);
//			ws.setColumnView(19, 15);
//			ws.setColumnView(20, 15);
//			ws.setColumnView(21, 15);
//			ws.setColumnView(22, 15);
//			ws.setColumnView(23, 15);
//			ws.setColumnView(24, 15);
//			ws.setColumnView(25, 15);
//			ws.setColumnView(26, 15);
//			ws.setColumnView(27, 15);
//			ws.setColumnView(28, 15);
			
			ws.addCell(new jxl.write.Label(0, 0, "机修公司劳务（收入）汇总表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "单位:", format3));
			ws.addCell(new jxl.write.Label(63,2, "日期:", format3));
			ws.addCell(new jxl.write.Label(0, 3, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 3, "项目", format1));
			ws.addCell(new jxl.write.Label(4, 3, "设备修理", format1));
			ws.addCell(new jxl.write.Label(4, 4, "钻修", format1));
			ws.addCell(new jxl.write.Label(8, 4, "机修", format1));
			ws.addCell(new jxl.write.Label(12, 4, "电修", format1));
			ws.addCell(new jxl.write.Label(16, 4, "机加", format1));
			ws.addCell(new jxl.write.Label(19, 4, "铆焊", format1));
			ws.addCell(new jxl.write.Label(22, 4, "前指一", format1));
			ws.addCell(new jxl.write.Label(25, 4, "前指二", format1));
			ws.addCell(new jxl.write.Label(28, 4, "前指三", format1));
			ws.addCell(new jxl.write.Label(31, 4, "前指四", format1));
			ws.addCell(new jxl.write.Label(34, 4, "前指五", format1));
			
			ws.addCell(new jxl.write.Label(41, 3, "上井劳务", format1));
			ws.addCell(new jxl.write.Label(37, 3, "设备修理小计", format1));
			ws.addCell(new jxl.write.Label(41, 4, "井次", format1));
			
			ws.addCell(new jxl.write.Label(42, 4, "钻修", format1));
			ws.addCell(new jxl.write.Label(44, 4, "机修", format1));
			ws.addCell(new jxl.write.Label(46, 4, "电修", format1));
			ws.addCell(new jxl.write.Label(48, 4, "机加", format1));
			ws.addCell(new jxl.write.Label(50, 4, "铆焊", format1));
			ws.addCell(new jxl.write.Label(52, 4, "前指一", format1));
			ws.addCell(new jxl.write.Label(54, 4, "前指二", format1));
			ws.addCell(new jxl.write.Label(56, 4, "前指三", format1));
			ws.addCell(new jxl.write.Label(58, 4, "前指四", format1));
			ws.addCell(new jxl.write.Label(60, 4, "前指五", format1));
			ws.addCell(new jxl.write.Label(62, 4, "上井劳务小计", format1));
			ws.addCell(new jxl.write.Label(65, 3, "合计", format1));
			
			ws.addCell(new jxl.write.Label(4, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(5, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(6, 5, "标准台", format1));
			ws.addCell(new jxl.write.Label(7, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(8, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(9, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(10, 5, "标准台", format1));
			ws.addCell(new jxl.write.Label(11, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(12, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(13, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(14, 5, "标准台", format1));
			ws.addCell(new jxl.write.Label(15, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(16, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(17, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(18, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(19, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(20, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(21, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(22, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(23, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(24, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(25, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(26, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(27, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(28, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(29, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(30, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(31, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(32, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(33, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(34, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(35, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(36, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(37, 5, "数量", format1));
			ws.addCell(new jxl.write.Label(38, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(39, 5, "标准台", format1));
			ws.addCell(new jxl.write.Label(40, 5, "金额", format1));
			
			ws.addCell(new jxl.write.Label(42, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(43, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(44, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(45, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(46, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(47, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(48, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(49, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(50, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(51, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(52, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(53, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(54, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(55, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(56, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(57, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(58, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(59, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(60, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(61, 5, "金额", format1));
			ws.addCell(new jxl.write.Label(62, 5, "井次", format1));
			ws.addCell(new jxl.write.Label(63, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(64, 5, "金额", format1));
		    
			ws.addCell(new jxl.write.Label(65, 5, "工时", format1));
			ws.addCell(new jxl.write.Label(66, 5, "金额", format1));
			
			
			//获取所有项目部的信息
			IMap tempBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
			IMap listBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
			tempBean.put("dataItemCode", "pro_dept");
			List<IMap> listId = db.getList(tempBean, "getIdByCode","com.DataItemBaseInfo");
			List<IMap> list = null;
			if(listId.size()>0){
				String dataItemId=(String)listId.get(0).get("dataItemId");
				listBean.put("dataItemId", dataItemId);
				list = db.getList(listBean, "getChildsById","com.DataItemBaseInfo");
			}
			//以list的size为行数
			int lnum = list.size();
			for(int i=6;i<lnum+6;i++){
				IMap proBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
				proBean = list.get(i-6);
				ws.addCell(new jxl.write.Label(3, i, (String)proBean.get("dataItemName"), wcfN));
			}
			ws.addCell(new jxl.write.Label(3, lnum+6, "内部收入小计", format1));
			ws.mergeCells(2, 6, 2, lnum+6);
			ws.addCell(new jxl.write.Label(2, 6, "同期实际", format1));
			
			for(int j=lnum+1+6;j<lnum+lnum+6+1;j++){
				IMap proBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
				proBean = list.get(j-lnum-7);
				ws.addCell(new jxl.write.Label(3, j, (String)proBean.get("dataItemName"), wcfN));
			}
			
			ws.addCell(new jxl.write.Label(3, lnum+lnum+6+1, "内部收入应进未进小计", format1));
			ws.mergeCells(2, lnum+1+6, 2, lnum+lnum+6+1);
			ws.addCell(new jxl.write.Label(2, lnum+1+6, "应进未进", format1));
			
			
			for(int k=(2*lnum)+2+6;k<(3*lnum)+6+2;k++){
				IMap proBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
				proBean = list.get(k-(2*lnum)-8);
				ws.addCell(new jxl.write.Label(3, k, (String)proBean.get("dataItemName"), wcfN));
			}
			
			ws.addCell(new jxl.write.Label(3, (3*lnum)+6+2, "内部收入合计", format1));
			ws.mergeCells(2, (2*lnum)+2+6, 2, (3*lnum)+6+2);
			ws.addCell(new jxl.write.Label(2, (2*lnum)+2+6, "本年实际", format1));
			
			ws.mergeCells(1, 6, 1, (3*lnum)+6+2);
			ws.addCell(new jxl.write.Label(1, 6, "内部劳务", format1));
			
			ws.mergeCells(2, (3*lnum)+9, 2, (3*lnum)+9+3);
			ws.addCell(new jxl.write.Label(2, (3*lnum)+9, "同期实际", format1));
			
			ws.addCell(new jxl.write.Label(3, (3*lnum)+9, "对外创收1", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+10, "对外创收2", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+11, "对外创收3", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+12, "对外创收小计", format1));
			
			ws.mergeCells(2, (3*lnum)+13, 2, (3*lnum)+13+3);
			ws.addCell(new jxl.write.Label(2, (3*lnum)+13, "应进未进", format1));
			
			ws.addCell(new jxl.write.Label(3, (3*lnum)+13, "对外创收1", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+14, "对外创收2", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+15, "对外创收3", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+16, "对外创收应进未进小计", format1));
			
			ws.mergeCells(2, (3*lnum)+17, 2, (3*lnum)+17+3);
			ws.addCell(new jxl.write.Label(2, (3*lnum)+17, "本年实际", format1));
			
			ws.addCell(new jxl.write.Label(3, (3*lnum)+17, "对外创收1", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+18, "对外创收2", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+19, "对外创收3", format1));
			ws.addCell(new jxl.write.Label(3, (3*lnum)+20, "对外创收合计", format1));
			
			
			ws.mergeCells(1, (3*lnum)+9, 1, (3*lnum)+17+3);
			ws.addCell(new jxl.write.Label(1, (3*lnum)+9, "对外创收", format1));
			
			ws.mergeCells(1, (3*lnum)+21, 3, (3*lnum)+21);
			ws.addCell(new jxl.write.Label(1, (3*lnum)+21, "劳务总计", format1));
			//序号
			for(int p=0;p<(3*lnum)+17;p++){
				ws.addCell(new jxl.write.Number(0, p+5, p, format1));
			}
			//对各个车间的劳务情况进行汇总统计
			
			
			
			
			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (WriteException e) {
			e.printStackTrace();
		}
		return result;
	}
		@SuppressWarnings("unchecked")
	public IMap<Object, Object> importCompleteMonthReport(IMap<Object, Object> in){
		IMap<Object, Object> result  = new DataMap<Object, Object>();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				in.put("orgCode", orgId);
				factoryName="机修公司";
			}
		}
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = factoryName+"生产月报（完工产品明细表）.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("生产月报" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			//添加标题行
			ws.mergeCells(0, 0, 15, 0);
			//ws.mergeCells(11, 1, 3, 0);
			//列宽
			ws.setColumnView(0, 15);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 15);
			ws.setColumnView(9, 15);
			ws.setColumnView(10, 15);
			ws.setColumnView(11, 15);
			ws.setColumnView(12, 15);
			ws.setColumnView(13, 15);
			ws.setColumnView(14, 15);
			ws.setColumnView(15, 15);
			ws.setColumnView(16, 15);
			ws.setColumnView(17, 15);
			ws.setColumnView(18, 15);
			ws.setColumnView(19, 15);
			ws.setColumnView(20, 15);
			ws.setColumnView(21, 15);
			ws.setColumnView(22, 15);
			ws.setColumnView(23, 15);
			ws.setColumnView(24, 15);
			ws.setColumnView(25, 15);
			ws.setColumnView(26, 15);
			ws.setColumnView(27, 15);
			ws.setColumnView(28, 15);
			
			
			ws.addCell(new jxl.write.Label(0, 0, factoryName+"月报（完工产品明细表）", format2));
			ws.addCell(new jxl.write.Label(0, 1, "单位："+factoryName, format2));
			ws.addCell(new jxl.write.Label(14, 1, "日期："+in.get("inDate"), format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "修理单位", format1));
			ws.addCell(new jxl.write.Label(2, 2, "设备名称及型号", format1));
			ws.addCell(new jxl.write.Label(3, 2, "工卡号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "设备编号", format1));
			ws.addCell(new jxl.write.Label(5, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(6, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(7, 2, "工时", format1));
			ws.addCell(new jxl.write.Label(8, 2, "产值", format1));
			ws.addCell(new jxl.write.Label(9, 2, "修理类别", format1));
			ws.addCell(new jxl.write.Label(10, 2, "进厂日期", format1));
			ws.addCell(new jxl.write.Label(11, 2, "送修车号", format1));
			ws.addCell(new jxl.write.Label(12, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(13, 2, "送修项目部", format1));
			ws.addCell(new jxl.write.Label(14, 2, "完工日期", format1));
			ws.addCell(new jxl.write.Label(15, 2, "出厂车号", format1));
			ws.addCell(new jxl.write.Label(16, 2, "使用单位", format1));
			ws.addCell(new jxl.write.Label(17, 2, "项目部", format1));
			ws.addCell(new jxl.write.Label(18, 2, "是否外包", format1));
			ws.addCell(new jxl.write.Label(19, 2, "结算费用", format1));
			ws.addCell(new jxl.write.Label(20, 2, "备注", format1));
			int hrNum = 3;
			//进厂设备
			String indate = (String)in.get("indate");
			in.put("indate",indate.replace("-",""));
			List reportList = db.getList(in,"getMonthReportList",null);
			for (int i = 0; i < reportList.size(); i++) {
				IMap map =  (IMap) reportList.get(i);
				ws.addCell(new jxl.write.Label(0, i+hrNum, (i+1)+"", format1));
				ws.addCell(new jxl.write.Label(1, i+hrNum, (String)map.get("dept_name"), format1));
				ws.addCell(new jxl.write.Label(2, i+hrNum, (String)map.get("equipment_name")+"-"+(String)map.get("equipment_code"), format1));
				ws.addCell(new jxl.write.Label(3, i+hrNum, (String)map.get("work_card"), format1));
				ws.addCell(new jxl.write.Label(4, i+hrNum, (String)map.get("self_code"), format1));
				ws.addCell(new jxl.write.Label(5, i+hrNum, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Label(6, i+hrNum, "1", format1));
				//工时
				ws.addCell(new jxl.write.Label(7, i+hrNum, "", format1));
				ws.addCell(new jxl.write.Number(8, i+hrNum, (Integer)map.get("output_value"), format1));
				ws.addCell(new jxl.write.Label(9, i+hrNum, (String)map.get("repair_type"), format1));
				if((Date)map.get("in_date")!=null){
					ws.addCell(new jxl.write.Label(10, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("in_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(10, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(11, i+hrNum, (String)map.get("incarno"), format1));
				ws.addCell(new jxl.write.Label(12, i+hrNum, (String)map.get("dept_from"), format1));
				ws.addCell(new jxl.write.Label(12, i+hrNum, (String)map.get("dept_from"), format1));
				ws.addCell(new jxl.write.Label(13, i+hrNum, "", format1));
				List list = WebFunction.getDataItem("pro_dept");
				for(int j=0;j<list.size();j++){
					IMap projectDeptMap = (IMap)list.get(j);
					String value = (String)projectDeptMap.get("dataItemValue");
					if(value.equals((String)map.get("project_dept"))){
						ws.addCell(new jxl.write.Label(13, i+hrNum, (String)projectDeptMap.get("dataItemName"), format1));
						break;
					}
				}
				if((Date)map.get("complete_date")!=null){
					ws.addCell(new jxl.write.Label(14, i+hrNum, DateTimeUtil.date2str("yyyy-MM-dd",(Date)map.get("complete_date")), format1));
				}else{
					ws.addCell(new jxl.write.Label(14, i+hrNum,"", format1));
				}
				ws.addCell(new jxl.write.Label(15, i+hrNum,"", format1));
				ws.addCell(new jxl.write.Label(16, i+hrNum,"", format1));
				ws.addCell(new jxl.write.Label(17, i+hrNum,"", format1));
				ws.addCell(new jxl.write.Label(18, i+hrNum,"", format1));
				ws.addCell(new jxl.write.Label(19, i+hrNum,"", format1));
				ws.addCell(new jxl.write.Label(20, i+hrNum,"", format1));
			}
			hrNum += reportList.size();
			if(reportList.isEmpty()){
				for(int i = 0 ;i<16;i++){
					ws.addCell(new jxl.write.Label(i, hrNum, "", format1));
				}
				hrNum += 1;
			}
			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
