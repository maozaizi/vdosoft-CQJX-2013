package com.u2a.business;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.brick.api.Service;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.u2a.framework.util.DateTimeUtil;
/**
 * 
 * 系统名称：报表业务类   
 * 类名称：ReportService   
 * 类描述：   
 * 创建人：duch 
 * 创建时间：Sep 16, 2013 3:15:42 PM
 */
public class ReportService extends Service{
	/**
	 * 8.1 上井劳务收入明细表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 10, 2013 3:21:18 PM
	 */
	public IMap<String, Object> getlabourExReportList(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
			}else{
				//所有
				in.put("orgCode", orgId);
			}
		}
		String yearD = "";
		if(in.get("endDate")!=null&&!"".equals((String)in.get("endDate"))){
			String endDate = ((String) in.get("endDate")).replace("-", "");
			in.put("endDate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			result.put("endDate", endDate);
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("endDate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		
		List<IMap> labourExReportList = db.getList(in, "getlabourExReportList", "");
		
		result.put("yearD", yearD);
		result.put("factory", factory);
		result.put("labourExReportList", labourExReportList);
		return result;
	}
	/**
	 * 8.1 导出上井劳务收入明细表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 10, 2013 3:21:18 PM
	 */
	public IMap<String, Object> importlabourExcel(IMap<String, Object> in) {
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
			in.put("endDate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("endDate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		List<IMap> labourExReportList = db.getList(in, "getlabourExReportList", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"劳务收入（明细）统计表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"劳务收入（明细）统计表" ,  0);
			
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
			//添加标题行
			ws.mergeCells(0, 0, 17, 1);
			
			//序号
			ws.mergeCells(0, 2, 0, 3);
			ws.mergeCells(1, 2, 1, 3);
			ws.mergeCells(2, 2, 2, 3);
			ws.mergeCells(3, 2, 3, 3);
			ws.mergeCells(4, 2, 4, 3);
			ws.mergeCells(5, 2, 5, 3);
			ws.mergeCells(6, 2, 6, 3);
			ws.mergeCells(7, 2, 7, 3);
			ws.mergeCells(8, 2, 17, 2);
			ws.mergeCells(18, 2, 28, 2);
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"劳务收入（明细）统计表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "摘要", format1));
			ws.addCell(new jxl.write.Label(2, 2, "单机号", format1));
			ws.addCell(new jxl.write.Label(3, 2, "修理类别", format1));
			ws.addCell(new jxl.write.Label(4, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "使用单位", format1));
			ws.addCell(new jxl.write.Label(6, 2, "项目部", format1));
			ws.addCell(new jxl.write.Label(7, 2, "井号", format1));
			ws.addCell(new jxl.write.Label(8, 2, "上井(金额)", format1));
			ws.addCell(new jxl.write.Label(8, 3, "钻修", format1));
			ws.addCell(new jxl.write.Label(9, 3, "机修", format1));
			ws.addCell(new jxl.write.Label(10, 3, "电修", format1));
			ws.addCell(new jxl.write.Label(11, 3, "机加", format1));
			ws.addCell(new jxl.write.Label(12, 3, "铆焊", format1));
			ws.addCell(new jxl.write.Label(13, 3, "前指一", format1));
			ws.addCell(new jxl.write.Label(14, 3, "前指三", format1));
			ws.addCell(new jxl.write.Label(15, 3, "前指四", format1));
			ws.addCell(new jxl.write.Label(16, 3, "前指五", format1));
			ws.addCell(new jxl.write.Label(17, 3, "小计", format1));
			ws.addCell(new jxl.write.Label(18, 2, "材料（金额）（井队带料）", format1));
			ws.addCell(new jxl.write.Label(18, 3, "钻修", format1));
			ws.addCell(new jxl.write.Label(19, 3, "机修", format1));
			ws.addCell(new jxl.write.Label(20, 3, "电修", format1));
			ws.addCell(new jxl.write.Label(21, 3, "机加", format1));
			ws.addCell(new jxl.write.Label(22, 3, "铆焊", format1));
			ws.addCell(new jxl.write.Label(23, 3, "前指一", format1));
			ws.addCell(new jxl.write.Label(24, 3, "前指三", format1));
			ws.addCell(new jxl.write.Label(25, 3, "前指四", format1));
			ws.addCell(new jxl.write.Label(26, 3, "前指五", format1));
			ws.addCell(new jxl.write.Label(27, 3, "小计", format1));
			ws.addCell(new jxl.write.Label(28, 3, "合计", format1));
			
			
			for (int i = 0; i < labourExReportList.size(); i++) {
				IMap map = labourExReportList.get(i);
				
				ws.addCell(new jxl.write.Number(0, i + 4, (Long)map.get("rownum"), wcfNint));
				ws.addCell(new jxl.write.Label(1, i+4, (String)map.get("remark"), format1));
				ws.addCell(new jxl.write.Label(2, i+4, (String)map.get("bodyCode"), format1));
				ws.addCell(new jxl.write.Label(3, i+4, (String)map.get("repairType"), format1));
				ws.addCell(new jxl.write.Label(4, i+4, (String)map.get("repairFrom"), format1));
				ws.addCell(new jxl.write.Label(5, i+4, (String)map.get("useFrom"), format1));
				ws.addCell(new jxl.write.Label(6, i+4, (String)map.get("projectDept"), format1));
				ws.addCell(new jxl.write.Label(7, i+4, (String)map.get("expatsTo"), format1));
				
				ws.addCell(new jxl.write.Number(8, i + 4, (Double)map.get("zxValue"), wcfN));
				ws.addCell(new jxl.write.Number(9, i + 4, (Double)map.get("jxValue"), wcfN));
				ws.addCell(new jxl.write.Number(10, i + 4, (Double)map.get("dxValue"), wcfN));
				ws.addCell(new jxl.write.Number(11, i + 4, (Double)map.get("jjValue"), wcfN));
				ws.addCell(new jxl.write.Number(12, i + 4, (Double)map.get("mhValue"), wcfN));
				ws.addCell(new jxl.write.Number(13, i + 4, ((BigDecimal)map.get("qz1Value")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(14, i + 4, ((BigDecimal)map.get("qz3Value")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(15, i + 4, ((BigDecimal)map.get("qz4Value")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(16, i + 4, ((BigDecimal)map.get("qz5Value")).doubleValue(), wcfN));
				ws.addCell(new Formula(17, i+4, "SUM(I" + (i+5) + ":Q" + (i+5) + ")", wcfN));
				ws.addCell(new jxl.write.Number(18, i + 4, (Double)map.get("zxamount"), wcfN));
				ws.addCell(new jxl.write.Number(19, i + 4, (Double)map.get("jxamount"), wcfN));
				ws.addCell(new jxl.write.Number(20, i + 4, (Double)map.get("dxamount"), wcfN));
				ws.addCell(new jxl.write.Number(21, i + 4, (Double)map.get("jjamount"), wcfN));
				ws.addCell(new jxl.write.Number(22, i + 4, (Double)map.get("mhamount"), wcfN));
				ws.addCell(new jxl.write.Number(23, i + 4, ((BigDecimal)map.get("qz1amount")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(24, i + 4, ((BigDecimal)map.get("qz3amount")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(25, i + 4, ((BigDecimal)map.get("qz4amount")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(26, i + 4, ((BigDecimal)map.get("qz5amount")).doubleValue(), wcfN));
				ws.addCell(new Formula(27, i+4, "SUM(S" + (i+5) + ":AA" + (i+5) + ")", wcfN));
				ws.addCell(new Formula(28, i+4, "R"+(i+5)+"+AB"+(i+5), wcfN));
			}
			if(!labourExReportList.isEmpty()){
				ws.addCell(new jxl.write.Label(0, labourExReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(1, labourExReportList.size()+4, "合计", format1));
				ws.addCell(new jxl.write.Label(2, labourExReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(3, labourExReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(4, labourExReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(5, labourExReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(6, labourExReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(7, labourExReportList.size()+4, "", format1));
				ws.addCell(new Formula(8, labourExReportList.size()+4, "SUM(I" + 5 + ":I" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(9, labourExReportList.size()+4, "SUM(j" + 5 + ":j" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(10, labourExReportList.size()+4, "SUM(k" + 5 + ":k" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(11, labourExReportList.size()+4, "SUM(l" + 5 + ":l" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(12, labourExReportList.size()+4, "SUM(m" + 5 + ":m" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(13, labourExReportList.size()+4, "SUM(n" + 5 + ":n" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(14, labourExReportList.size()+4, "SUM(o" + 5 + ":o" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(15, labourExReportList.size()+4, "SUM(p" + 5 + ":p" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(16, labourExReportList.size()+4, "SUM(q" + 5 + ":q" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(17, labourExReportList.size()+4, "SUM(r" + 5 + ":r" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(18, labourExReportList.size()+4, "SUM(s" + 5 + ":s" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(19, labourExReportList.size()+4, "SUM(t" + 5 + ":t" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(20, labourExReportList.size()+4, "SUM(u" + 5 + ":u" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(21, labourExReportList.size()+4, "SUM(v" + 5 + ":v" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(22, labourExReportList.size()+4, "SUM(w" + 5 + ":w" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(23, labourExReportList.size()+4, "SUM(x" + 5 + ":x" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(24, labourExReportList.size()+4, "SUM(y" + 5 + ":y" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(25, labourExReportList.size()+4, "SUM(z" + 5 + ":z" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(26, labourExReportList.size()+4, "SUM(aa" + 5 + ":aa" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(27, labourExReportList.size()+4, "SUM(ab" + 5 + ":ab" + (labourExReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(28, labourExReportList.size()+4, "SUM(ac" + 5 + ":ac" + (labourExReportList.size()+4) + ")", wcfN));
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
	 * @Description 8.1.2 获取设备劳务明细表
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 14, 2013 1:40:14 PM
	 */
	public IMap<String, Object> getlabourEqReportList(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
			}else{
				//所有
				in.put("orgCode", orgId);
			}
		}
		String yearD = "";
		if(in.get("endDate")!=null&&!"".equals((String)in.get("endDate"))){
			String endDate = ((String) in.get("endDate")).replace("-", "");
			in.put("endDate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			result.put("endDate", endDate);
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("endDate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		
		List<IMap> labourEqReportList = db.getList(in, "getlabourEqReportList", "");
		
		result.put("yearD", yearD);
		result.put("factory", factory);
		result.put("labourEqReportList", labourEqReportList);
		return result;
	}
	
	
	/**
	 * 8.1.2 导出设备劳务收入明细表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 10, 2013 3:21:18 PM
	 */
	public IMap<String, Object> importlabourEqExcel(IMap<String, Object> in) {
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
			in.put("endDate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			result.put("endDate", endDate);
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("endDate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		
		List<IMap> labourEqReportList = db.getList(in, "getlabourEqReportList", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"设备收入（明细）统计表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"设备收入（明细）统计表" ,  0);
			
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
			//添加标题行
			ws.mergeCells(0, 0, 33, 1);
			
			//序号
			ws.mergeCells(0, 2, 0, 3);
			ws.mergeCells(1, 2, 1, 3);
			ws.mergeCells(2, 2, 2, 3);
			ws.mergeCells(3, 2, 3, 3);
			ws.mergeCells(4, 2, 4, 3);
			ws.mergeCells(5, 2, 5, 3);
			ws.mergeCells(6, 2, 6, 3);
			ws.mergeCells(7, 2, 7, 3);
			ws.mergeCells(8, 2, 10, 2);
			ws.mergeCells(11, 2, 13, 2);
			ws.mergeCells(14, 2, 16, 2);
			ws.mergeCells(17, 2, 19, 2);
			ws.mergeCells(20, 2, 22, 2);
			ws.mergeCells(23, 2, 25, 2);
			ws.mergeCells(26, 2, 28, 2);
			ws.mergeCells(29, 2, 31, 2);
			ws.mergeCells(32, 2, 34, 2);
			ws.mergeCells(35, 2, 35, 3);
			ws.mergeCells(36, 2, 40, 2);
			//列宽
			ws.setColumnView(0, 5);
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"设备劳务收入（明细）统计表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "摘要", format1));
			ws.addCell(new jxl.write.Label(2, 2, "单机号", format1));
			ws.addCell(new jxl.write.Label(3, 2, "修理类别", format1));
			ws.addCell(new jxl.write.Label(4, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "使用单位", format1));
			ws.addCell(new jxl.write.Label(6, 2, "项目部", format1));
			ws.addCell(new jxl.write.Label(7, 2, "井号", format1));
			ws.addCell(new jxl.write.Label(8, 2, "钻修", format1));
			ws.addCell(new jxl.write.Label(8, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(9, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(10, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(11, 2, "机修", format1));
			ws.addCell(new jxl.write.Label(11, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(12, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(13, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(14, 2, "电修", format1));
			ws.addCell(new jxl.write.Label(14, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(15, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(16, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(17, 2, "机加", format1));
			ws.addCell(new jxl.write.Label(17, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(18, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(19, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(20, 2, "铆焊", format1));
			ws.addCell(new jxl.write.Label(20, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(21, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(22, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(23, 2, "前指一", format1));
			ws.addCell(new jxl.write.Label(23, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(24, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(25, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(26, 2, "前指三", format1));
			ws.addCell(new jxl.write.Label(26, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(27, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(28, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(29, 2, "前指四", format1));
			ws.addCell(new jxl.write.Label(29, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(30, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(31, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(32, 2, "前指五", format1));
			ws.addCell(new jxl.write.Label(32, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(33, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(34, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(35, 2, "小计", format1));
			ws.addCell(new jxl.write.Label(36, 2, "合计", format1));
			ws.addCell(new jxl.write.Label(36, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(37, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(38, 3, "数量（铆焊、机加、前指）", format1));
			ws.addCell(new jxl.write.Label(39, 3, "工时（铆焊、机加、前指）", format1));
			ws.addCell(new jxl.write.Label(40, 3, "金额", format1));
			
			
			for (int i = 0; i < labourEqReportList.size(); i++) {
				IMap map = labourEqReportList.get(i);
				
				ws.addCell(new jxl.write.Number(0, i + 4, (Long)map.get("rownum"), wcfNint));
				ws.addCell(new jxl.write.Label(1, i+4, (String)map.get("remark"), format1));
				ws.addCell(new jxl.write.Label(2, i+4, (String)map.get("equipmentCode"), format1));
				ws.addCell(new jxl.write.Label(3, i+4, (String)map.get("repairType"), format1));
				ws.addCell(new jxl.write.Label(4, i+4, (String)map.get("repairFrom"), format1));
				ws.addCell(new jxl.write.Label(5, i+4, (String)map.get("useFrom"), format1));
				ws.addCell(new jxl.write.Label(6, i+4, (String)map.get("projectDept"), format1));
				ws.addCell(new jxl.write.Label(7, i+4, (String)map.get("expatsTo"), format1));
				
				ws.addCell(new jxl.write.Number(8, i + 4, (Integer)map.get("zxnum"), wcfNint));
				ws.addCell(new jxl.write.Number(9, i + 4, (Integer)map.get("zxbzt"), wcfNint));
				ws.addCell(new jxl.write.Number(10, i + 4, ((BigDecimal)map.get("zxvalue")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(11, i + 4, (Integer)map.get("jxnum"), wcfNint));
				ws.addCell(new jxl.write.Number(12, i + 4, (Integer)map.get("jxbzt"), wcfNint));
				ws.addCell(new jxl.write.Number(13, i + 4, ((BigDecimal)map.get("jxvalue")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(14, i + 4, (Integer)map.get("dxnum"), wcfNint));
				ws.addCell(new jxl.write.Number(15, i + 4, (Integer)map.get("dxbzt"), wcfNint));
				ws.addCell(new jxl.write.Number(16, i + 4, ((BigDecimal)map.get("dxvalue")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(17, i + 4, (Integer)map.get("mhnum"), wcfNint));
				ws.addCell(new jxl.write.Number(18, i + 4, (Integer)map.get("mhbzt"), wcfNint));
				ws.addCell(new jxl.write.Number(19, i + 4, ((BigDecimal)map.get("mhvalue")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(20, i + 4, (Integer)map.get("jjnum"), wcfNint));
				ws.addCell(new jxl.write.Number(21, i + 4, (Integer)map.get("jjbzt"), wcfNint));
				ws.addCell(new jxl.write.Number(22, i + 4, ((BigDecimal)map.get("jjvalue")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(23, i + 4, (Integer)map.get("qz1num"), wcfNint));
				ws.addCell(new jxl.write.Number(24, i + 4, (Integer)map.get("qz1bzt"), wcfNint));
				ws.addCell(new jxl.write.Number(25, i + 4, ((BigDecimal)map.get("qz1value")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(26, i + 4, (Integer)map.get("qz3num"), wcfNint));
				ws.addCell(new jxl.write.Number(27, i + 4, (Integer)map.get("qz3bzt"), wcfNint));
				ws.addCell(new jxl.write.Number(28, i + 4, ((BigDecimal)map.get("qz3value")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(29, i + 4, (Integer)map.get("qz4num"), wcfNint));
				ws.addCell(new jxl.write.Number(30, i + 4, (Integer)map.get("qz4bzt"), wcfNint));
				ws.addCell(new jxl.write.Number(31, i + 4, ((BigDecimal)map.get("qz4value")).doubleValue(), wcfN));
				ws.addCell(new jxl.write.Number(32, i + 4, (Integer)map.get("qz5num"), wcfNint));
				ws.addCell(new jxl.write.Number(33, i + 4, (Integer)map.get("qz5bzt"), wcfNint));
				ws.addCell(new jxl.write.Number(34, i + 4, ((BigDecimal)map.get("qz5value")).doubleValue(), wcfN));
				ws.addCell(new Formula(35, i+4, "K"+(i+5)+"+N"+(i+5)+"+Q"+(i+5)+"+T"+(i+5)+"+W"+(i+5)+"+Z"+(i+5)+"+AC"+(i+5)+"+AF"+(i+5)+"+AI"+(i+5), wcfN));
				ws.addCell(new Formula(36, i+4, "I"+(i+5)+"+L"+(i+5)+"+O"+(i+5), wcfNint));
				ws.addCell(new Formula(37, i+4, "J"+(i+5)+"+M"+(i+5)+"+P"+(i+5), wcfNint));
				ws.addCell(new Formula(38, i+4, "R"+(i+5)+"+U"+(i+5)+"+X"+(i+5)+"+AA"+(i+5)+"+AD"+(i+5)+"+AG"+(i+5), wcfNint));
				ws.addCell(new Formula(39, i+4, "S"+(i+5)+"+V"+(i+5)+"+Y"+(i+5)+"+AB"+(i+5)+"+AE"+(i+5)+"+AH"+(i+5), wcfN));
				ws.addCell(new Formula(40, i+4, "AJ"+(i+5), wcfN));
			}
			if(!labourEqReportList.isEmpty()){
				ws.addCell(new jxl.write.Label(0, labourEqReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(1, labourEqReportList.size()+4, "合计", format1));
				ws.addCell(new jxl.write.Label(2, labourEqReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(3, labourEqReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(4, labourEqReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(5, labourEqReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(6, labourEqReportList.size()+4, "", format1));
				ws.addCell(new jxl.write.Label(7, labourEqReportList.size()+4, "", format1));
				ws.addCell(new Formula(8, labourEqReportList.size()+4, "SUM(I" + 5 + ":I" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(9, labourEqReportList.size()+4, "SUM(j" + 5 + ":j" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(10, labourEqReportList.size()+4, "SUM(k" + 5 + ":k" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(11, labourEqReportList.size()+4, "SUM(l" + 5 + ":l" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(12, labourEqReportList.size()+4, "SUM(m" + 5 + ":m" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(13, labourEqReportList.size()+4, "SUM(n" + 5 + ":n" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(14, labourEqReportList.size()+4, "SUM(o" + 5 + ":o" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(15, labourEqReportList.size()+4, "SUM(p" + 5 + ":p" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(16, labourEqReportList.size()+4, "SUM(q" + 5 + ":q" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(17, labourEqReportList.size()+4, "SUM(r" + 5 + ":r" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(18, labourEqReportList.size()+4, "SUM(s" + 5 + ":s" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(19, labourEqReportList.size()+4, "SUM(t" + 5 + ":t" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(20, labourEqReportList.size()+4, "SUM(u" + 5 + ":u" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(21, labourEqReportList.size()+4, "SUM(v" + 5 + ":v" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(22, labourEqReportList.size()+4, "SUM(w" + 5 + ":w" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(23, labourEqReportList.size()+4, "SUM(x" + 5 + ":x" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(24, labourEqReportList.size()+4, "SUM(y" + 5 + ":y" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(25, labourEqReportList.size()+4, "SUM(z" + 5 + ":z" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(26, labourEqReportList.size()+4, "SUM(aa" + 5 + ":aa" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(27, labourEqReportList.size()+4, "SUM(ab" + 5 + ":ab" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(28, labourEqReportList.size()+4, "SUM(ac" + 5 + ":ac" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(29, labourEqReportList.size()+4, "SUM(ad" + 5 + ":ad" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(30, labourEqReportList.size()+4, "SUM(ae" + 5 + ":ae" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(31, labourEqReportList.size()+4, "SUM(af" + 5 + ":af" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(32, labourEqReportList.size()+4, "SUM(ag" + 5 + ":ag" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(33, labourEqReportList.size()+4, "SUM(ah" + 5 + ":ah" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(34, labourEqReportList.size()+4, "SUM(ai" + 5 + ":ai" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(35, labourEqReportList.size()+4, "SUM(aj" + 5 + ":aj" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(36, labourEqReportList.size()+4, "SUM(ak" + 5 + ":ak" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(37, labourEqReportList.size()+4, "SUM(al" + 5 + ":al" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(38, labourEqReportList.size()+4, "SUM(am" + 5 + ":am" + (labourEqReportList.size()+4) + ")", wcfNint));
				ws.addCell(new Formula(39, labourEqReportList.size()+4, "SUM(an" + 5 + ":an" + (labourEqReportList.size()+4) + ")", wcfN));
				ws.addCell(new Formula(40, labourEqReportList.size()+4, "SUM(ao" + 5 + ":ao" + (labourEqReportList.size()+4) + ")", wcfN));
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
	 * @Description 导出劳务收入汇总表
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 16, 2013 12:15:01 AM
	 */
	public IMap<String, Object> importlabourTotal(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
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
			String endDate = (String) in.get("endDate");
			in.put("endDate", endDate);
			
			yearD =  endDate+"年";
			result.put("endDate", endDate);
		}else{
			yearD = DateTimeUtil.date2str("yyyy", DateTimeUtil.getLocalDate("yyyy")) ;
			
			in.put("endDate", yearD);
			yearD = yearD+"年";
		}
		
		//IMap labourTotal = db.get(in, "getlabourTotal", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"设备收入（汇总）统计表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"设备收入（汇总）统计表" ,  0);
			
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
			//添加标题行
			ws.mergeCells(0, 0, 33, 1);
			
			//序号
			ws.mergeCells(0, 2, 1, 3);
			ws.mergeCells(2, 2, 4, 2);
			ws.mergeCells(5, 2, 7, 2);
			ws.mergeCells(8, 2, 10, 2);
			ws.mergeCells(11, 2, 13, 2);
			ws.mergeCells(14, 2, 16, 2);
			ws.mergeCells(17, 2, 19, 2);
			ws.mergeCells(20, 2, 22, 2);
			ws.mergeCells(23, 2, 25, 2);
			ws.mergeCells(26, 2, 28, 2);
			ws.mergeCells(29, 2, 29, 3);
			ws.mergeCells(30, 2, 39, 2);
			ws.mergeCells(40, 2, 44, 2);
			
			ws.mergeCells(0, 4, 1, 4);
			ws.mergeCells(0, 5, 1, 5);
			ws.mergeCells(0, 6, 0, 16);
			ws.mergeCells(0, 17, 0, 27);
			ws.mergeCells(0, 28, 0, 38);
			ws.mergeCells(0, 39, 1, 39);
			ws.mergeCells(0, 40, 0, 42);
			ws.mergeCells(0, 44, 0, 46);
			ws.mergeCells(0, 48, 0, 50);
			
			
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"设备劳务收入（汇总）统计表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "项目", format1));
			ws.addCell(new jxl.write.Label(2, 2, "钻修", format1));
			ws.addCell(new jxl.write.Label(5, 2, "机修", format1));
			ws.addCell(new jxl.write.Label(8,2, "电修", format1));
			ws.addCell(new jxl.write.Label(11,2, "机加", format1));
			ws.addCell(new jxl.write.Label(14,2, "铆焊", format1));
			ws.addCell(new jxl.write.Label(17,2, "前指一", format1));
			ws.addCell(new jxl.write.Label(20,2, "前指三", format1));
			ws.addCell(new jxl.write.Label(23,2, "前指四", format1));
			ws.addCell(new jxl.write.Label(26,2, "前指五", format1));
			ws.addCell(new jxl.write.Label(29,2,"小计", format1));
			ws.addCell(new jxl.write.Label(30,2,"上井金额", format1));
			ws.addCell(new jxl.write.Label(40,2, "合计", format1));
			ws.addCell(new jxl.write.Label(2, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(3, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(4, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(5, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(6, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(7, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(8, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(9, 3, "标准台", format1));
			ws.addCell(new jxl.write.Label(10, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(11, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(12, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(13, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(14, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(15, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(16, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(17, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(18, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(19, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(20, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(21, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(22, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(23, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(24, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(25, 3, "金额", format1));
			ws.addCell(new jxl.write.Label(26, 3, "数量", format1));
			ws.addCell(new jxl.write.Label(27, 3, "工时", format1));
			ws.addCell(new jxl.write.Label(28, 3, "金额", format1));
			
			ws.addCell(new jxl.write.Label(30, 3, "钻修", format1));
			ws.addCell(new jxl.write.Label(31, 3, "机修", format1));
			ws.addCell(new jxl.write.Label(32, 3, "电修", format1));
			ws.addCell(new jxl.write.Label(33,3, "机加", format1));
			ws.addCell(new jxl.write.Label(34,3, "铆焊", format1));
			ws.addCell(new jxl.write.Label(35,3, "前指一", format1));
			ws.addCell(new jxl.write.Label(36,3, "前指三", format1));
			ws.addCell(new jxl.write.Label(37,3, "前指四", format1));
			ws.addCell(new jxl.write.Label(38,3, "前指五", format1));
			ws.addCell(new jxl.write.Label(39,3,"小计", format1));
			
			ws.addCell(new jxl.write.Label(40,3, "数量", format1));
			ws.addCell(new jxl.write.Label(41,3, "标准台", format1));
			ws.addCell(new jxl.write.Label(42,3, "数量(铆焊、机加、前指)", format1));
			ws.addCell(new jxl.write.Label(43,3, "工时(铆焊、机加、前指)", format1));
			ws.addCell(new jxl.write.Label(44,3,"金额", format1));
			
			
			ws.addCell(new jxl.write.Label(0, 4, "本年预算", format1));
			ws.addCell(new jxl.write.Label(0, 5, "同期预算", format1));
			ws.addCell(new jxl.write.Label(0, 6, "同期实际", format1));
			ws.addCell(new jxl.write.Label(1, 6, "第一项目部", format1));
			ws.addCell(new jxl.write.Label(1, 7, "第二项目部", format1));
			ws.addCell(new jxl.write.Label(1, 8, "第三项目部", format1));
			ws.addCell(new jxl.write.Label(1, 9, "第四项目部", format1));
			ws.addCell(new jxl.write.Label(1, 10, "第五项目部", format1));
			ws.addCell(new jxl.write.Label(1, 11, "装备部", format1));
			ws.addCell(new jxl.write.Label(1, 12, "专业公司", format1));
			ws.addCell(new jxl.write.Label(1, 13, "其他1", format1));
			ws.addCell(new jxl.write.Label(1, 14, "其他2", format1));
			ws.addCell(new jxl.write.Label(1, 15, "其他3", format1));
			ws.addCell(new jxl.write.Label(1, 16, "内部收入小计", format1));
			ws.addCell(new jxl.write.Label(0, 17, "应进未进", format1));
			ws.addCell(new jxl.write.Label(1, 17, "第一项目部", format1));
			ws.addCell(new jxl.write.Label(1, 18, "第二项目部", format1));
			ws.addCell(new jxl.write.Label(1, 19, "第三项目部", format1));
			ws.addCell(new jxl.write.Label(1, 20, "第四项目部", format1));
			ws.addCell(new jxl.write.Label(1, 21, "第五项目部", format1));
			ws.addCell(new jxl.write.Label(1, 22, "装备部", format1));
			ws.addCell(new jxl.write.Label(1, 23, "专业公司", format1));
			ws.addCell(new jxl.write.Label(1, 24, "其他1", format1));
			ws.addCell(new jxl.write.Label(1, 25, "其他2", format1));
			ws.addCell(new jxl.write.Label(1, 26, "其他3", format1));
			ws.addCell(new jxl.write.Label(1, 27, "内部收入小计", format1));
			ws.addCell(new jxl.write.Label(0, 28, "本年实际", format1));
			ws.addCell(new jxl.write.Label(1, 28, "第一项目部", format1));
			ws.addCell(new jxl.write.Label(1, 29, "第二项目部", format1));
			ws.addCell(new jxl.write.Label(1, 30, "第三项目部", format1));
			ws.addCell(new jxl.write.Label(1, 31, "第四项目部", format1));
			ws.addCell(new jxl.write.Label(1, 32, "第五项目部", format1));
			ws.addCell(new jxl.write.Label(1, 33, "装备部", format1));
			ws.addCell(new jxl.write.Label(1, 34, "专业公司", format1));
			ws.addCell(new jxl.write.Label(1, 35, "其他1", format1));
			ws.addCell(new jxl.write.Label(1, 36, "其他2", format1));
			ws.addCell(new jxl.write.Label(1, 37, "其他3", format1));
			ws.addCell(new jxl.write.Label(1, 38, "内部收入小计", format1));
			ws.addCell(new jxl.write.Label(0, 39, "差异（实际-预算）", format1));
			ws.addCell(new jxl.write.Label(0, 40, "对外创收（本期实际)", format1));
			ws.addCell(new jxl.write.Label(1, 40, "外部收入(社会市场)", format1));
			ws.addCell(new jxl.write.Label(1, 41, "代扣收入（合作代管)", format1));
			ws.addCell(new jxl.write.Label(1, 42, "其他", format1));
			ws.addCell(new jxl.write.Label(1, 43, "对外创收小计", format1));
			ws.addCell(new jxl.write.Label(0, 44, "对外创收（应进未进)", format1));
			ws.addCell(new jxl.write.Label(1, 44, "外部收入(社会市场)", format1));
			ws.addCell(new jxl.write.Label(1, 45, "代扣收入（合作代管)", format1));
			ws.addCell(new jxl.write.Label(1, 46, "其他", format1));
			ws.addCell(new jxl.write.Label(1, 47, "对外创收小计", format1));
			ws.addCell(new jxl.write.Label(0, 48, "对外创收（应进未进)", format1));
			ws.addCell(new jxl.write.Label(1, 48, "外部收入(社会市场)", format1));
			ws.addCell(new jxl.write.Label(1, 49, "代扣收入（合作代管)", format1));
			ws.addCell(new jxl.write.Label(1, 50, "其他", format1));
			ws.addCell(new jxl.write.Label(1, 51, "对外创收合计", format1));
			for(int i=0;i<48;i++){
				ws.addCell(new jxl.write.Number(2, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(3, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(4, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(5, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(6, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(7, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(8, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(9, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(10, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(11, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(12, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(13, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(14, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(15, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(16, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(17, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(18, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(19, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(20, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(21, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(22, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(23, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(24, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(25, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(26, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(27, i+4, 0, wcfNint));
				ws.addCell(new jxl.write.Number(28, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(29, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(30, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(31, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(32, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(33, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(34, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(35, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(36, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(37, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(38, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(39, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(40, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(41, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(42, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(43, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(44, i+4, 0, wcfN));
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
	 * 8.4 月设备材料费汇总表
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 16, 2013 1:44:25 AM
	 */
	public IMap<String, Object> importconsumeExcel(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		
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
			in.put("endDate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("endDate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		
		List<IMap> consumeReportList = db.getList(in, "getconsumeReportList", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"设备材料费汇总表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"设备材料费汇总表" ,  0);
			
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
			ws.mergeCells(0, 0, 33, 1);
			
			//序号
			ws.mergeCells(0, 2, 0, 3);
			ws.mergeCells(1, 2, 1, 3);
			ws.mergeCells(2, 2, 2, 3);
			ws.mergeCells(3, 2, 3, 3);
			ws.mergeCells(4, 2, 4, 3);
			ws.mergeCells(5, 2, 5, 3);
			ws.mergeCells(6, 2, 6, 3);
			ws.mergeCells(7, 2, 7, 3);
			ws.mergeCells(8, 2, 8, 3);
			ws.mergeCells(9, 2, 9, 3);
			ws.mergeCells(10, 2, 10, 3);
			ws.mergeCells(11, 2, 11, 3);
			ws.mergeCells(12, 2, 12, 3);
			ws.mergeCells(13, 2, 13, 3);
			ws.mergeCells(14, 2, 14, 3);
			ws.mergeCells(15, 2, 15, 3);
			ws.mergeCells(16, 2, 16, 3);
			ws.mergeCells(17, 2, 20, 2);
			ws.mergeCells(21, 2, 21, 3);
			ws.mergeCells(22, 2, 22, 3);
			ws.mergeCells(23, 2, 23, 3);
			ws.mergeCells(24, 2, 24, 3);
			ws.mergeCells(25, 2, 25, 3);
			ws.mergeCells(26, 2, 26, 3);
			ws.mergeCells(27, 2, 27, 3);
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"设备材料费汇总表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "设备名称", format1));
			ws.addCell(new jxl.write.Label(1, 2, "规格型号", format1));
			ws.addCell(new jxl.write.Label(2, 2, "设备自编号", format1));
			ws.addCell(new jxl.write.Label(3, 2, "工卡号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "修理类别", format1));
			ws.addCell(new jxl.write.Label(5, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(6, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(7, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "标准台", format1));
			ws.addCell(new jxl.write.Label(9, 2, "收费", format1));
			ws.addCell(new jxl.write.Label(10, 2, "另赠费", format1));
			ws.addCell(new jxl.write.Label(11, 2, "金额", format1));
			ws.addCell(new jxl.write.Label(12, 2, "工时", format1));
			ws.addCell(new jxl.write.Label(13, 2, "主修人", format1));
			ws.addCell(new jxl.write.Label(14, 2, "开工时间", format1));
			ws.addCell(new jxl.write.Label(15, 2, "完工时间", format1));
			ws.addCell(new jxl.write.Label(16, 2, "结算时间", format1));
			ws.addCell(new jxl.write.Label(17, 2, "直接材料费", format1));
			ws.addCell(new jxl.write.Label(17, 3, "上年材料费", format1));
			ws.addCell(new jxl.write.Label(18, 3, "常用料", format1));
			ws.addCell(new jxl.write.Label(19, 3, "大件料（10000元以上）", format1));
			ws.addCell(new jxl.write.Label(20, 3, "合计", format1));
			ws.addCell(new jxl.write.Label(21, 2, "直接材料费", format1));
			ws.addCell(new jxl.write.Label(22, 2, "油料", format1));
			ws.addCell(new jxl.write.Label(23, 2, "辅助材料", format1));
			ws.addCell(new jxl.write.Label(24, 2, "机加工件（及修复件）材料费", format1));
			ws.addCell(new jxl.write.Label(25, 2, "应进未进", format1));
			ws.addCell(new jxl.write.Label(26, 2, " 合计", format1));
			ws.addCell(new jxl.write.Label(27, 2, " 备注", format1));
			
			for (int i = 0; i < consumeReportList.size(); i++) {
				IMap map = consumeReportList.get(i);
				
				ws.addCell(new jxl.write.Label(0, i+4, (String)map.get("equipmentName"), format1));
				ws.addCell(new jxl.write.Label(1, i+4, (String)map.get("equipmentModel"), format1));
				ws.addCell(new jxl.write.Label(2, i+4, (String)map.get("selfCode"), format1));
				ws.addCell(new jxl.write.Label(3, i+4, (String)map.get("workCard"), format1));
				ws.addCell(new jxl.write.Label(4, i+4, (String)map.get("repairType"), format1));
				ws.addCell(new jxl.write.Label(5, i+4, (String)map.get("deptFrom"), format1));
				ws.addCell(new jxl.write.Label(6, i+4, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Number(7, i+4, 1, wcfNint));
				ws.addCell(new jxl.write.Number(8, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(9, i+4, (Double)map.get("eqvalue"), wcfN));
				ws.addCell(new jxl.write.Number(10, i+4, 0, wcfN));
				ws.addCell(new jxl.write.Number(11, i+4, (Double)map.get("eqvalue"), wcfN));
				ws.addCell(new jxl.write.Number(12, i+4, (Double)map.get("eqvalue"), wcfNint));
				ws.addCell(new jxl.write.Label(13, i+4, (String)map.get("mainRepair"), format1));
				ws.addCell(new jxl.write.DateTime(14, i+4, (Date)map.get("startDate"), wcfdate));
				ws.addCell(new jxl.write.DateTime(15, i+4, (Date)map.get("completeDate"), wcfdate));
				ws.addCell(new jxl.write.Label(16, i+4, "", format1));
				
				
				ws.addCell(new jxl.write.Number(17, i + 4, (Double)map.get("amount"), wcfN));
				ws.addCell(new jxl.write.Number(18, i + 4, (Double)map.get("amount"), wcfN));
				ws.addCell(new jxl.write.Number(19, i + 4, (Double)map.get("amountb"), wcfN));
				ws.addCell(new Formula(20, i+4, "S"+(i+5)+"+T"+(i+5)+"U"+(i+5), wcfN));
				ws.addCell(new jxl.write.Number(21, i + 4, 0, wcfN));
				ws.addCell(new jxl.write.Number(22, i + 4, 0, wcfN));
				ws.addCell(new jxl.write.Number(23, i + 4, 0, wcfN));
				ws.addCell(new jxl.write.Number(24, i + 4, 0, wcfN));
				ws.addCell(new Formula(25, i+4, "V"+(i+5)+"+W"+(i+5)+"X"+(i+5)+"Y"+(i+5)+"Z"+(i+5), wcfN));
				ws.addCell(new jxl.write.Label(16, i+4, "", format1));
				
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
	 * 8.5
	 * @Description 机修公司上井服务明细表
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 16, 2013 3:16:55 PM
	 */
	public IMap<String, Object> getExpatsReportList(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//判断当前登录用户权限
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
			}else{
				//所有
				in.put("orgCode", orgId);
			}
		}
		
		List<IMap> expatsList = db.getList(in, "getExpatsReportList", "");
		
		result.put("expatsList", expatsList);
		return result;
	}
	
	
	/**
	 * 8.5导出excel
	 * @param in
	 * @return
	 */
	public IMap getExpatsReportOut(IMap in) {
		IMap<String,Object> result = new DataMap<String,Object>();
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
		
		List<IMap> expatsList = db.getList(in, "getExpatsReportList", "");
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "机修公司"+factoryName+"上井服务明细表.xls";
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
			ws.mergeCells(0, 0, 15, 1);
			
			
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, "机修公司"+factoryName+"上井服务明细表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "序号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "用车单位", format1));
			ws.addCell(new jxl.write.Label(2, 2, "车型", format1));
			ws.addCell(new jxl.write.Label(3, 2, "车牌号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "承运单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "规格型号", format1));
			ws.addCell(new jxl.write.Label(6, 2, "修理内容", format1));
			ws.addCell(new jxl.write.Label(7, 2, "修理性质", format1));
			ws.addCell(new jxl.write.Label(8, 2, "完成情况", format1));
			ws.addCell(new jxl.write.Label(9, 2, "车间", format1));
			ws.addCell(new jxl.write.Label(10, 2, "项目部", format1));
			ws.addCell(new jxl.write.Label(11, 2, "队号", format1));
			ws.addCell(new jxl.write.Label(12, 2, "上井人数", format1));
			ws.addCell(new jxl.write.Label(13, 2, "出发时间", format1));
			ws.addCell(new jxl.write.Label(14, 2, "回厂时间", format1));
			ws.addCell(new jxl.write.Label(15, 2, "备注", format1));
			
			
			for (int i = 0; i < expatsList.size(); i++) {
				IMap map = expatsList.get(i);
				
				ws.addCell(new jxl.write.Number(0, i+3, (Long)map.get("rownum"), wcfNint));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("vehiclesUnit"), format1));
				ws.addCell(new jxl.write.Label(2, i+3, (String)map.get("vehiclesModel"), format1));
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("vehiclesPlate"), format1));
				ws.addCell(new jxl.write.Label(4, i+3, (String)map.get("transUnit"), format1));
				ws.addCell(new jxl.write.Label(5, i+3, (String)map.get("equipmentName")+(String)map.get("equipmentModel"), format1));
				ws.addCell(new jxl.write.Label(6, i+3, (String)map.get("taskDetail"), format1));
				ws.addCell(new jxl.write.Label(7, i+3, (String)map.get("repairType"), format1));
				ws.addCell(new jxl.write.Label(8, i+3, (String)map.get("isComplete"), format1));
				ws.addCell(new jxl.write.Label(9, i+3, (String)map.get("deptName"), format1));
				ws.addCell(new jxl.write.Label(10, i+3, (String)map.get("projectDept"), format1));
				ws.addCell(new jxl.write.Label(11, i+3, (String)map.get("expatsTo"), format1));
				ws.addCell(new jxl.write.Number(12, i+3, (Integer)map.get("expatsNum"), wcfNint));
				ws.addCell(new jxl.write.DateTime(13, i+3, (Date)map.get("departureTime"), format1));
				ws.addCell(new jxl.write.DateTime(14, i+3, (Date)map.get("backTime"), wcfdate));
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
	 * @Description 机修公司生产日报
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 16, 2013 3:16:55 PM
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
			}
		}
		
		List<IMap> expatsList = db.getList(in, "getDailyReportList", "");
		
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = noteDate+"机修公司"+factoryName+"日报.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(noteDate+"机修公司"+factoryName+"日报" ,  0);
			
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, noteDate+"机修公司"+factoryName+"日报", format2));
			ws.addCell(new jxl.write.Label(0, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(1, 2, "设备名称", format1));
			ws.addCell(new jxl.write.Label(2, 2, "规格型号", format1));
			ws.addCell(new jxl.write.Label(3, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(4, 2, "开工日期", format1));
			ws.addCell(new jxl.write.Label(5, 2, "工作进度", format1));
			ws.addCell(new jxl.write.Label(6, 2, "待料情况", format1));
			
			
			for (int i = 0; i < expatsList.size(); i++) {
				IMap map = expatsList.get(i);
				
				ws.addCell(new jxl.write.Label(0, i+3, (String)map.get("workCard"), format1));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("equipmentName"), format1));
				ws.addCell(new jxl.write.Label(2, i+3, (String)map.get("equipmentModel"), format1));
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("deptFrom"), format1));
				ws.addCell(new jxl.write.DateTime(4, i+3, (Date)map.get("startDate"), wcfN));
				ws.addCell(new jxl.write.Label(5, i+3, (String)map.get("noteContent"), format1));
				ws.addCell(new jxl.write.Label(6, i+3, (String)map.get("materialQk"), format1));
				
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
	 * 8.8
	 * @Description 生产周报
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 25, 2013 4:10:10 PM
	 */
	public IMap<String, Object> getWeekReportList(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//判断当前登录用户权限
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		//获取当前选择日期
		Date date = DateTimeUtil.str2Date("yyyy-MM-dd", (String)in.get("noteDate"));//  ;
		//获取选择日期所在的周及起始时间结束时间
		
		Date startdate =DateTimeUtil.getFirstDayOfWeek(date);
		Date enddate = DateTimeUtil.getLastDayOfWeek(date);
		int weekNum = DateTimeUtil.getWeekOfYear(date);
		
		in.put("startdate", startdate);
		in.put("enddate", enddate);
		
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			in.put("orgCode", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				in.put("orgCode", "|CQ|.|JX|.|YC|");
			}else if("2".equals(factory)){
				//庆阳
				in.put("orgCode", "|CQ|.|JX|.|QY|");
			}else{
				//所有
				in.put("orgCode", orgId);
			}
		}
		
		List<IMap> weekList = db.getList(in, "getWeekReportList", "");
		
		result.put("expatsList", weekList);
		result.put("weekNum", weekNum);
		
		return result;
	}
	/**
	 * 8.9
	 * @Description 月份完工设备产值汇总表
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 26, 2013 10:13:15 AM
	 */
	public IMap<String, Object> importCompleteReport(IMap<String, Object> in) {
		
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
			in.put("choosedate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("choosedate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		List<IMap> completeList = db.getList(in, "getMonthComReportList", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"完工设备产值汇总表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"完工设备产值汇总表" ,  0);
			
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
			ws.mergeCells(0, 0, 10, 1);
			
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"完工设备产值汇总表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "设备名称", format1));
			ws.addCell(new jxl.write.Label(1, 2, "设备型号", format1));
			ws.addCell(new jxl.write.Label(2, 2, "单机号", format1));
			ws.addCell(new jxl.write.Label(3, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(4, 2, "修理类别", format1));
			ws.addCell(new jxl.write.Label(5, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(6, 2, "设备编号", format1));
			ws.addCell(new jxl.write.Label(7, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(8, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "产值", format1));
			ws.addCell(new jxl.write.Label(9, 2, "完工时间", format1));
			ws.addCell(new jxl.write.Label(10, 2, "是否结算", format1));
			
			
			for (int i = 0; i < completeList.size(); i++) {
				IMap map = completeList.get(i);
				
				ws.addCell(new jxl.write.Label(0, i+3, (String)map.get("equipmentName"), format1));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("equipmentModel"), format1));
				ws.addCell(new jxl.write.Label(2, i+3, "", format1));
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("teamNumber"), format1));
				ws.addCell(new jxl.write.Label(4, i+3, (String)map.get("repairType"), format1));
				ws.addCell(new jxl.write.Label(5, i+3, (String)map.get("workCard"), format1));
				ws.addCell(new jxl.write.Label(6, i+3, "", format1));
				ws.addCell(new jxl.write.Label(7, i+3, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Number(8, i + 3, (Double)map.get("repairValue"), wcfN));
				ws.addCell(new jxl.write.DateTime(9, i + 3, (Date)map.get("completeDate"), wcfdate));
				ws.addCell(new jxl.write.Label(10, i + 3, "", wcfN));
				
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
	 * 导出月计划完成情况
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 16, 2013 5:04:33 AM
	 */
	public IMap<String, Object> importComstateReport(IMap<String, Object> in) {
		
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
			in.put("choosedate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("choosedate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		//List<IMap> completeList = db.getList(in, "getMonthComReportList", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"计划下达、完成情况汇总表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"计划下达、完成情况汇总表" ,  0);
			
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
			ws.mergeCells(0, 0, 14, 1);
			
			ws.mergeCells(0, 3, 0, 7);
			ws.mergeCells(0, 8, 0, 12);
			ws.mergeCells(0, 13, 0, 17);
			ws.mergeCells(0, 18, 0, 22);
			ws.mergeCells(0, 23, 0, 27);
			ws.mergeCells(0, 28, 0, 32);
			ws.mergeCells(0, 33, 0, 35);
			ws.mergeCells(0, 36, 0, 38);
			ws.mergeCells(0, 39, 0, 43);
			ws.mergeCells(0, 44, 0, 48);
			ws.mergeCells(0, 49, 0, 53);
			ws.mergeCells(0, 54, 0, 58);
			ws.mergeCells(0, 59, 0, 63);
			ws.mergeCells(0, 64, 0, 68);
			ws.mergeCells(0, 69, 0, 71);
			ws.mergeCells(0, 72, 0, 74);
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"计划下达、完成情况汇总表", format2));
			ws.addCell(new jxl.write.Label(2, 2, "3月份", format1));
			ws.addCell(new jxl.write.Label(3, 2, "4月份", format1));
			ws.addCell(new jxl.write.Label(4, 2, "5月份", format1));
			ws.addCell(new jxl.write.Label(5, 2, "6月份", format1));
			ws.addCell(new jxl.write.Label(6, 2, "7月份", format1));
			ws.addCell(new jxl.write.Label(7, 2, "8月份", format1));
			ws.addCell(new jxl.write.Label(8, 2, "9月份", format1));
			ws.addCell(new jxl.write.Label(9, 2, "10月份", format1));
			ws.addCell(new jxl.write.Label(10, 2, "11月份", format1));
			ws.addCell(new jxl.write.Label(11, 2, "12月-2014.1月份", format1));
			ws.addCell(new jxl.write.Label(12, 2, "2013年全年合计", format1));
			
			ws.addCell(new jxl.write.Label(0, 3, "公司", format1));
			ws.addCell(new jxl.write.Label(0, 8, "庆阳修造厂", format1));
			ws.addCell(new jxl.write.Label(0, 13,"庆阳钻修", format1));
			ws.addCell(new jxl.write.Label(0, 18, "庆阳机修", format1));
			ws.addCell(new jxl.write.Label(0, 23, "庆阳电修", format1));
			ws.addCell(new jxl.write.Label(0, 28, "庆阳加工", format1));
			ws.addCell(new jxl.write.Label(0, 33, "庆阳上井收入", format1));
			ws.addCell(new jxl.write.Label(0, 36, "庆阳前指收入", format1));
			ws.addCell(new jxl.write.Label(0, 39, "银川修造厂", format1));
			ws.addCell(new jxl.write.Label(0, 44, "银川钻修", format1));
			ws.addCell(new jxl.write.Label(0, 49, "银川机修", format1));
			ws.addCell(new jxl.write.Label(0, 54, "银川电修", format1));
			ws.addCell(new jxl.write.Label(0, 59, "银川加工", format1));
			ws.addCell(new jxl.write.Label(0, 64, "银川铆焊", format1));
			ws.addCell(new jxl.write.Label(0, 69, "银川上井收入", format1));
			ws.addCell(new jxl.write.Label(0, 72, "银川前指收入", format1));
			
			ws.addCell(new jxl.write.Label(1, 3, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 4, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 5, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 6, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 7, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 8, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 9, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 10, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 11, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 12, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 13, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 14, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 15, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 16, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 17, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 18, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 19, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 20, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 21, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 22, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 23, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 24, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 25, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 26, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 27, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 28, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 29, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 30, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 31, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 32, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 33, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 34, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 35, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 36, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 37, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 38, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 39, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 40, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 41, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 42, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 43, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 44, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 45, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 46, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 47, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 48, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 49, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 50, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 51, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 52, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 53, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 54, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 55, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 56, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 57, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 58, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 59, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 60, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 61, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 62, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 63, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 64, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 65, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 66, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 67, "计划台数", format1));
			ws.addCell(new jxl.write.Label(1, 68, "实际台数", format1));
			ws.addCell(new jxl.write.Label(1, 69, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 70, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 71, "完成百分比", format1));
			ws.addCell(new jxl.write.Label(1, 72, "计划", format1));
			ws.addCell(new jxl.write.Label(1, 73, "完成", format1));
			ws.addCell(new jxl.write.Label(1, 74, "完成百分比", format1));
			
			
			//暂无数据
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
	 * @Description 导出月报
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 16, 2013 5:34:21 AM
	 */
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
			in.put("choosedate", endDate);
			
			yearD =  endDate.substring(0,4)+"年"+endDate.substring(4,6)+"月";
			
		}else{
			yearD = DateTimeUtil.date2str("yyyyMM", DateTimeUtil.getLocalDate("yyyyMM")) ;
			
			in.put("choosedate", yearD);
			yearD = yearD.substring(0,4)+"年"+yearD.substring(4,6)+"月";
		}
		List<IMap> completeList = db.getList(in, "getMonthComReportList", "");
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = yearD+"机修公司"+factoryName+"生产月报.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet(yearD+"机修公司"+factoryName+"生产月报" ,  0);
			
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
			ws.mergeCells(0, 0, 11, 1);
			
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, yearD+"机修公司"+factoryName+"生产月报", format2));
			ws.addCell(new jxl.write.Label(0, 2, "修理单位", format1));
			ws.addCell(new jxl.write.Label(1, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(2, 2, "设备名称", format1));
			ws.addCell(new jxl.write.Label(3, 2, "规格型号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(6, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(7, 2, "修理性质", format1));
			ws.addCell(new jxl.write.Label(8, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "标准台", format1));
			ws.addCell(new jxl.write.Label(9, 2, "产值", format1));
			ws.addCell(new jxl.write.Label(10, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(11, 2, "备注", format1));
			
			
			
			
			
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
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Nov 27, 2013 2:54:02 PM
	 */
	public IMap<String, Object> importMetalialDetailReport(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap userMap = (IMap) in.get("userMap");
		
		
		/**生成EXCEL**/
		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "单机核算台账.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("单机核算台账" ,  0);
			
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
			ws.mergeCells(0, 0, 11, 1);
			
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
			
			
			ws.addCell(new jxl.write.Label(0, 0, "单机核算台账", format2));
			ws.addCell(new jxl.write.Label(0, 2, "z", format1));
			ws.addCell(new jxl.write.Label(1, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(2, 2, "设备名称", format1));
			ws.addCell(new jxl.write.Label(3, 2, "规格型号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "施工卡号", format1));
			ws.addCell(new jxl.write.Label(6, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(7, 2, "修理性质", format1));
			ws.addCell(new jxl.write.Label(8, 2, "数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "标准台", format1));
			ws.addCell(new jxl.write.Label(9, 2, "产值", format1));
			ws.addCell(new jxl.write.Label(10, 2, "送修单位", format1));
			ws.addCell(new jxl.write.Label(11, 2, "备注", format1));
			
			
			
			
			
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
	
	
}
