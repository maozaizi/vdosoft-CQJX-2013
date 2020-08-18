package com.u2a.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;


/**
 * 对于文件操作的工具类
 */


public class FileUtil{

  
/*
 * 产生文件夹路径，配置路径/年/月/日
 * 
 * 格式如：配置路径/2009/5/15/
 * 
 */
  

  public static String createDatePath(){
	
//	   while(config.endsWith("/")||config.endsWith("\\")){
//		config=config.substring(0, config.length()-1);
//	   }
	   
	 return DateTimeUtil.getLocalYear()+"/"+ DateTimeUtil.getLocalMonth()+"/"+DateTimeUtil.getLocalDay();

  } 
  
    /*
	 * 产生文件名:yyyyMMddHHmmss,并在后面加n位随机数
	 * 
	 * 格式如：200905011021155489.jpg
	 */
   
    public static String createFileName(int n) {
		String date = DateTimeUtil.getLocalDateStr("yyyyMMddHHmmss");
		// 取4位随机数
		String b = String.valueOf(Math.random());
		String c = b.substring(2,n+2);
		return date + c;
	}
    /*
	 * 取文件扩展名
	 * 格式如：jpg
	 */
    public static String getSmallName(String fileName) {
		String tempName = "";
		if (fileName != null) {
			tempName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		}
		return tempName;  
	}
	/**
	 * 删除图片
	 */
	  public static boolean delete(String path){
			 
		java.io.File file=new java.io.File(path);
		if(file.exists()){
			return file.delete();
		}
		return false;
	   } 
		/**
		 * 判断文件路径是否存在，不存在则创建
		 * @Description 
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @author duch
		 * @date Mar 10, 2012 4:00:05 AM
		 */
		public static void getFilePath(String url) {
			java.io.File file = new java.io.File(url);
			if (!file.exists()) {
				file.mkdir();
			}
		}
		/**
		 * 获取PDF信息传到页面进行展示
		 * 
		 * @Description
		 * @param
		 * @param in
		 * @param
		 * @return
		 * @return IMap<String,Object>
		 * @author duch
		 * @date Mar 10, 2012 4:00:05 AM
		 */
		public static IMap<String, Object> getPDF(IMap<String, Object> in, String filePath) {
			// 基本信息ID
			// String id = (String) in.get("id");
			HttpServletResponse response = (HttpServletResponse) in.get("response");
			// String filePath = "C://demo.pdf";
			File file = new File(filePath);
			// 输入流
			InputStream is;
			try {
				is = new FileInputStream(file);
				// 输出流
				OutputStream outs = response.getOutputStream();
				// 输出类型

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline;filename=");
				// 定义一个长度
				int readLength = 0;
				// 将流传递到页面
				while ((readLength = is.read()) != -1) {
					outs.write(readLength);
				}
				// 关闭输出
				outs.close();
				// 关闭输入
				is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 传回到预览页面进行展示
			IMap<String, Object> result = new DataMap<String, Object>();
			// result.put("id", id);
			return result;
		}
	  /**
	   * 
	   * @Description 生成PDF的工具类
	   * @param @param filePath 文件的完整路径
	   * @param @param content 生成PDF的内容 如ckeditor的内同
	   * @param @param request 当前的请求
	   * @return void   
	   * @author duch
	   * @date Apr 13, 2012 4:35:34 PM
	   */
		public static void createPDF(String filePath,String content,HttpServletRequest request){
			OutputStream os = null;
			try {
				os = new FileOutputStream(new File(filePath));
			} catch (FileNotFoundException e1) {
				//请一定在这里调用Logutil中的方法打印日志 e1.printStackTrace();

			}
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver fontResolver = renderer.getFontResolver();
			String realPath = request.getSession().getServletContext().getRealPath("/"); 
			try {
				fontResolver.addFont(realPath+"fonts/simsun.ttc",
						BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				fontResolver.addFont(realPath+"fonts/simhei.ttf",
						BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			} catch (DocumentException e1) {

				//请一定在这里调用Logutil中的方法打印日志 e1.printStackTrace();

			} catch (IOException e1) {

				//请一定在这里调用Logutil中的方法打印日志 e1.printStackTrace();

			}
			StringBuffer html = new StringBuffer();
			// DOCTYPE 必需写否则类似于 这样的字符解析会出现错误  
			html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> ");
			html.append(" <html>");
			html.append("<head>");
			html.append("<style type=\"text/css\" mce_bogus=\"1\">");
			//设置字体，若不设置字体，中文会解析不到
			html.append("body {font-family: SimSun,SimHei;}");
			html.append("</style>");
			html.append(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
			html.append("<title>test</title>");
			html.append("</head>");
			html.append("<body>");
			html.append(content);
			html.append("</body>");
			html.append("</html>");
			renderer.setDocumentFromString(html.toString());
			// 解决图片的相对路径问题  
			renderer.getSharedContext().setBaseURL("file:/"+realPath+"images//");
			//System.out.println(request.getContextPath());
			renderer.layout();
			try {
				renderer.createPDF(os);
				os.close();
			} catch (DocumentException e) {
				//请一定在这里调用Logutil中的方法打印日志 e.printStackTrace();
			} catch (IOException e) {
				//请一定在这里调用Logutil中的方法打印日志 e.printStackTrace();
			}
		 }
		/**
		 * IREPORT 导出EXCEL 
		 * @param @param request
		 * @param @param response
		 * @param @param resultList 结果集LIST
		 * @param @param tempalteUrl ireport JasperReport 文件路径
		 * @param @param parameters 参数
		 * @param @return    
		 * @return IMap   
		 * @author 孟勃婷
		 * @return 
		 * @date 2013-9-26 下午05:36:17
		 */
		
		public static void getReportOut(HttpServletRequest request,HttpServletResponse response,List resultList,String tempalteUrl,Map parameters) {
			File reportFile = new File(request.getSession().getServletContext().getRealPath("/")+tempalteUrl);
			try {
					   // 构造JasperReport 文件
				        JRBeanCollectionDataSource jrbean = new JRBeanCollectionDataSource(resultList);
					   // 用数据填充JasperReport文件
					   JasperPrint jasperPrint = JasperFillManager.fillReport(
							   reportFile.getPath(), parameters, jrbean);
						JExcelApiExporter exporter = new JExcelApiExporter();
						exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
						exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
						
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE); 
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);        
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
						   // 告诉浏览器是导出操EXCEL文件操作
						response.setHeader("Content-Disposition", "attachment; filename=test.xls");
						response.setContentType("application/vnd.ms-excel");
						exporter.exportReport();
						
						response.getOutputStream().close();
					} catch (Exception e) {
						e.printStackTrace();
				}
			
		}
		
}