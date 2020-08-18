package com.u2a.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;
import com.brick.exception.BusinessException;
import com.lowagie.text.pdf.BaseFont;
/**
 * PDF生成工具类API
 * 
 * @author 吴明强
 * 注：使用本类，必须在根目录下放置仿宋字体和黑体 fonts\simfang.ttf、simhei.ttf
 * 必须在根目录下放置样式文件 css\print.css
 */
public class PDFUtil {

	/**
	 * 生成pdf
	 * @param realPath
	 * @param content
	 * @return
	 * @throws Exception
	 */
	private static OutputStream createPDF(String realPath, String[] content)
			throws Exception {
		OutputStream os = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont(realPath + "fonts\\simsun.ttc",
				BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		fontResolver.addFont(realPath + "fonts\\simfang.ttf",
				BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		fontResolver.addFont(realPath + "fonts\\simhei.ttf",
				BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(
				renderer.getOutputDevice());
		callback.setSharedContext(renderer.getSharedContext());
		renderer.getSharedContext().setUserAgentCallback(callback);
		ByteArrayInputStream stream = new ByteArrayInputStream(content[0].getBytes("utf-8"));
		Document doc = XMLResource.load(stream).getDocument();
		renderer.setDocument(doc, "file:/"+realPath);
		renderer.layout();
		renderer.createPDF(os, false);
		for (int i = 1; i < content.length; i++) {
			stream = new ByteArrayInputStream(content[i].getBytes("utf-8"));
			doc = XMLResource.load(stream).getDocument();
			renderer.setDocument(doc, "file:/"+realPath);
			renderer.layout();
			renderer.writeNextDocument();
		}
		renderer.finishPDF();
		os.close();
		return os;

	}

	private static String checkHtml(String text,String path){
		String s=text;
		if (text.indexOf("<html")==-1){
			StringBuffer html = new StringBuffer();
			html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> ");
			html.append("<html>");
			html.append("<head>");
			html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
			html.append("<title>pdf</title>");
			//加载样式文件
			String style=getFileContext(path+"css/print.css");
			html.append("<style>");
			html.append(style);
			html.append("</style>");
			html.append("</head>");
			html.append("<body>");
			html.append(text);
			html.append("</body>");
			html.append("</html>");
			s=html.toString();
		}
		return s;
	}
	/**
	 * 将string转换为string[]
	 * 使用<!--pdfpage-->转换
	 * @param text
	 * @return
	 */
	private static String[] checkContext(String text) {
		String context = text;
		String sub = "<!--pdfpage-->";
		String[] htmls=context.split(sub);
		//为页面加头
		for(int i=0;i<htmls.length;i++){
			if (htmls[i].indexOf("<html")==-1){
				StringBuffer html = new StringBuffer();
				html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> ");
				html.append("<html>");
				html.append("<head>");
				html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
				html.append("<title>pdf</title>");
				//加载样式文件
				html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/print.css\" title=\"Style\" />");
				html.append("</head>");
				html.append("<body>");
				html.append(text);
				html.append("</body>");
				html.append("</html>");
				htmls[i]=html.toString();
			}
		}
		return htmls;

	}

	/**
	 * 生成PDF，并返回到页面，直接下载
	 * 
	 * @param fileName
	 *            下载的文件名
	 * @param Text
	 *            模板内容
	 * @param request
	 * @param response
	 */
	public static void getPdf(String fileName, String text,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			response.reset();
			ByteArrayOutputStream pdf = null;
			String path = request.getSession().getServletContext().getRealPath(
					"/");

			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			
			String agent = request.getHeader("USER-AGENT").toLowerCase();
			if (agent.indexOf("msie") != -1) {
				fileName = "\"" + URLEncoder.encode(fileName, "UTF8")
						+ "\"";
			}
			// Opera浏览器只能采用filename*
			else if (agent.indexOf("opera") != -1) {
				fileName = "=UTF-8''"
						+ URLEncoder.encode(fileName, "UTF8");
			}
			// Safari浏览器，只能采用ISO编码的中文输出
			else if (agent.indexOf("safari") != -1) {
				fileName = "\""
						+ new String(fileName.getBytes("UTF-8"), "ISO8859-1")
						+ "\"";
			}
			// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
			else if (agent.indexOf("applewebkit") != -1) {
				fileName = "\""
						+ new String(fileName.getBytes("UTF-8"), "ISO8859-1")
						+ "\"";
			}
			// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
			else if (agent.indexOf("mozilla") != -1) {
				fileName = "=?UTF-8?B?"+(new String(Base64.encodeBase64(fileName.getBytes("UTF-8"))))+"?="; 
			}
			if ("htm".equals(request.getParameter("contentType"))){
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(checkHtml(text,path));
				response.getWriter().flush();
				response.getWriter().close();
			}else{
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName+"."+request.getParameter("contentType"));
			if ("doc".equals(request.getParameter("contentType"))){
				response.setContentType("application/msword;charset=UTF-8");
				response.getWriter().write(checkHtml(text,path));
				response.getWriter().flush();
				response.getWriter().close();
			}else if("xls".equals(request.getParameter("contentType"))){
				response.setContentType("application/msexcle;charset=UTF-8");
				response.getWriter().write(checkHtml(text,path));
				response.getWriter().flush();
				response.getWriter().close();
			}else{
				response.setHeader("Content-Disposition", "attachment; filename="
						+ fileName+".pdf");
				//response.setContentType("pdf");
				String[] html = checkContext(text);
				pdf = (ByteArrayOutputStream) createPDF(path, html);

				if (pdf != null) {
					response.setContentLength(pdf.size());
					ServletOutputStream out = response.getOutputStream();
					pdf.writeTo(out);
					out.flush();
					out.close();
					pdf.close();
				} else {
					throw new BusinessException("PDF未生成！");
				}
			}
			}
		} catch (Exception e) {
			throw new BusinessException("生成PDF失败！", e);
		}
	}

	private static class ResourceLoaderUserAgent extends ITextUserAgent {
		public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
			super(outputDevice);
		}

		protected InputStream resolveAndOpenStream(String uri) {
			InputStream is = super.resolveAndOpenStream(uri);
			System.out.println("IN resolveAndOpenStream() " + uri);
			return is;
		}
	}
	/**
	 * 
	 *  描述: 获取模板内容
	 *  方法: getTempBody方法
	 *  作者: duch
	 *  时间: Mar 6, 2014
	 *  版本: 1.0
	 */
	public static String getTempBody(String tempPath) {
		String body = "";
		BufferedReader reader = null;
		//根据模板生成File类型
		File file = com.brick.util.Util.fileExist(tempPath);
		//给模板填充值
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			String tempbyte;
			while ((tempbyte = reader.readLine()) != null) {
//				System.out.println(tempbyte);
				body +=tempbyte;
			}
			reader.close();
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
			throw new BusinessException("该模板不存在！");
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("该模板不存在！");
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return body;
	}
	/**
	 * 获取模板文件
	 * @param strFileName
	 * @return
	 */
	public static String getFileContext(String strFileName) {
		StringBuffer buf = null;// the intermediary, mutable buffer
		BufferedReader breader = null;// reader for the template files
		try {
			breader = new BufferedReader(new InputStreamReader(
					new FileInputStream((strFileName)), Charset
							.forName("utf-8")));
			
			buf = new StringBuffer();
			while (breader.ready()) {
				buf.append((char) breader.read());
			}
			breader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ByteArrayOutputStream pdf = null;
			String path = "c:\\printtest\\";

			String url = "c:\\printtest\\表1建设工程消防验收基本情况记录表.html";
			String pdffile = "c:\\printtest\\表4.pdf";
			String text = getFileContext(url);
			String[] html = checkContext(text);
			pdf = (ByteArrayOutputStream) createPDF(path, html);
			if (pdf != null) {
				System.out.println("size:" + pdf.size());
				OutputStream out = new FileOutputStream(new File(pdffile));
				pdf.writeTo(out);
				out.flush();
				out.close();
				pdf.close();
			} else {
				throw new BusinessException("PDF未生成！");
			}
		} catch (Exception e) {
			throw new BusinessException("生成PDF失败！", e);
		}
	}

}
