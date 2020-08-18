package com.u2a.framework.commons;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.brick.api.Service;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.ContextUtil;
import com.brick.util.Util;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.u2a.framework.util.DateTimeUtil;
import com.u2a.framework.util.FileUtil;

@SuppressWarnings("unchecked")
public class FileUpload extends Service {
	
	

	/**
	 * @Description 上传文件方法
	 * @param in
	 * @throws Exception
	 * @return IMap
	 * @author panghaichao
	 * @date Mar 15, 2012 5:02:00 PM
	 */
	@SuppressWarnings("unchecked")
	public IMap saveFileUpload(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		HttpServletResponse response = (HttpServletResponse) in.get("response");
		ServletContext serlvetContext = (ServletContext) in
				.get("serlvetContext");
		HttpSession session = (HttpSession) in.get("session");

		// 判断是否指定文件夹存放
		String AfolderName = (String) in.get("folderName");
		// 判断是否指定文件
		String AfileExt = (String) in.get("fileExt");
		// 判断是否指定文件大小
		Integer AfileSize = (Integer) in.get("fileSize");
		// 判断是否指定文件夹按年生成
		Boolean AYear = (Boolean) in.get("Year");
		// 判断是否指定文件夹按月生成
		Boolean AMonth = (Boolean) in.get("Month");
		// 判断是否指定文件夹按日生成
		Boolean ADay = (Boolean) in.get("Day");
		// 判断是否文件重命名
		Boolean ANotRename = (Boolean) in.get("NotRename");
		IMap result = new DataMap();
		try {
			SmartUpload su = new SmartUpload();
			// 初始化
			su.initialize(serlvetContext, session, request, response, null);
			// 设置文件最大量
			if (Util.checkNotNull(AfileSize)) {
				su.setMaxFileSize(AfileSize * 1024 * 1024);
				// 限制总上传数据的长度。   
				su.setTotalMaxFileSize(AfileSize * 1024 * 1024 * 2);
			} else{
				// 不指定大小，默认1M
				su.setMaxFileSize(1 * 1024 * 1024);
				}
			
			if (Util.checkNotNull(AfileExt)) {
				su.setAllowedFilesList(AfileExt);
			}
			su.setDeniedFilesList("class,jsp");
			// 上传
			su.upload();
			String filepath = "uploadfiles";
			String url = request.getSession().getServletContext().getRealPath(
					"/")
					+ filepath;
			this.createFolder(url);
			// 判断指定文件夹
			if (Util.checkNotNull(AfolderName)) {
				filepath = filepath + "/" + AfolderName;
				url = url + "/" + AfolderName;
				this.createFolder(url);
			}
			if (Util.checkNotNull(AYear) && AYear) {
				filepath = filepath + "/" + DateTimeUtil.getLocalYear();
				url = url + "/" + DateTimeUtil.getLocalYear();
				this.createFolder(url);
			}
			if (Util.checkNotNull(AMonth) && AMonth) {
				filepath = filepath + "/" + DateTimeUtil.getLocalMonth();
				url = url + "/" + DateTimeUtil.getLocalMonth();
				this.createFolder(url);
			}
			if (Util.checkNotNull(ADay) && ADay) {
				filepath = filepath + "/" + DateTimeUtil.getLocalDay();
				url = url + "/" + DateTimeUtil.getLocalDay();
				this.createFolder(url);
			}

			File file = su.getFiles().getFile(0);
			String filePathName = file.getFileName();
			if (Util.checkNotNull(ANotRename) && ANotRename) {
			} else {
				filePathName = FileUtil.createFileName(3) + "."
						+ file.getFileExt();
			}

			file.saveAs(url + "/" + filePathName);
			filepath = filepath + "/" + filePathName;

			result.put("fileName", file.getFileName());
			result.put("filePath", filepath);
			result.put("fileExt", file.getFileExt());
			result.put("fileSize", file.getSize());
			result.put("flag", "successed");
			
			result.put("error", 0);
			result.put("url", filepath);
			
			
		} catch (Exception e) {
			throw new BusinessException("上传文件失败！ ", e);
		}
		return result;
	}
	
	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @param out
	 * @param @param fileName
	 * @param @param maxLen    
	 * @return void   
	 * @author duch
	 * @date May 6, 2013 5:27:03 PM
	 */
	static long offset = 0;//文件流位置,用于断点续传
	static long currTime = 0;//记录上传前时间
	public void doUpload(BufferedInputStream in,PrintWriter out,String fileName,long maxLen,String upload_dir) {
		System.out.println("offset - " + offset);
		//"r":以只读文方式打开指定文件。如果你写的话会有IOException。
		//"rw":以读写方式打开指定文件，不存在就创建新文件。
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(upload_dir + fileName, "rw");
			
			//断点续传,跳过上传文件流offset个字节
			if (offset > 0) {
				//  要跳过的字节数
				in.skip(offset);
				// 指在文件中跳过给定数量的字节
				raf.skipBytes((int)offset);
			}
			   	
			byte[] buf = new byte[50];
			long len  = 0;
			// in.read(buf)读取出来的数据放在byte数组中，也是缓冲区
			while ((in.read(buf)) >= 0) {
				// 将每两个字节合成一个中文并写时文本
				raf.write(buf);
				len += buf.length;
				if (len % 1024*1024 == 0) {
					out.println("<script>alert('11111')</script>");
					System.out.println("callback('正在上传文件 " + (offset + len)/1024/1024 + "M/" + maxLen/1024/1024  + "M')");}
			}
			out.println("<script>parent.callback('文件上传成功')</script>");
			System.out.println("上传完成 !");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
					raf = null;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
					long now = System.currentTimeMillis();
					int min = (int)(now - currTime)/1000/60;
					int second = (int)(now - currTime)/1000%60;
					System.out.println("花费时间：" + min + ":" + second);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
	}
	
	
	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @throws IOException 
	 * @date May 6, 2013 5:26:52 PM
	 */
	public IMap<String, Object> uploadIfy(IMap<String, Object> in) throws IOException {
		IMap<String, Object> result = new DataMap<String, Object>();//输出map
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		HttpServletResponse response = (HttpServletResponse) in.get("response");
		String savePath = ContextUtil.servletContext.getRealPath("/");
		String filepath = "uploadfiles/";
		this.createFolder(savePath+filepath);
		//为路径加入年月日的文件夹
		filepath = filepath + "/" + DateTimeUtil.getLocalYear();
		this.createFolder(savePath+filepath);
		
		filepath = filepath + "/" + DateTimeUtil.getLocalMonth();
		this.createFolder(savePath+filepath);
		
		filepath = filepath + "/" + DateTimeUtil.getLocalDay();
		this.createFolder(savePath+filepath);
		filepath = filepath + "/";
		
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException ex) {
			
		}
		Iterator<FileItem> it = fileList.iterator();
		String name = "";
		String extName = "";
		List<IMap> list = new ArrayList<IMap>();
		while (it.hasNext()) {
			IMap<String, Object> map = new DataMap<String, Object>();
			FileItem item = it.next();
			if (!item.isFormField()) {
				name = item.getName();
				long size = item.getSize();
				String type = item.getContentType();
				System.out.println(size + "字节" + " " + type);
				if (name == null || name.trim().equals("")) {
					continue;
				}
				//扩展名格式：  
				if (name.lastIndexOf(".") >= 0) {
					extName = name.substring(name.lastIndexOf("."));
				}
				map.put("fileName", name);
				java.io.File file = null;
				do {
					//生成文件名：
					name = UUID.randomUUID().toString();
					file = new java.io.File(savePath + name + extName);
				} while (file.exists());
				java.io.File saveFile = new java.io.File(savePath+ filepath + name + extName);
				
				map.put("filePath", filepath + name + extName);
				map.put("fileExt", extName);
				map.put("fileSize", size);
				
				list.add(map);
				try {
					item.write(saveFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.put("fileList", list);
		}
//		try {
//			response.getWriter().print(name + extName);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		return result;
	}
	
	
	

	/**
	 * @Description 创建文件夹
	 * @param
	 * @param path
	 * @return boolean
	 * @author panghaichao
	 * @date Mar 15, 2012 11:35:38 AM
	 */
	private boolean createFolder(String path) {
		java.io.File file = new java.io.File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		return true;
	}

	/**
	 * @Description 删除文件
	 * @param
	 * @param path
	 * @return boolean
	 * @author panghaichao
	 * @date Mar 15, 2012 11:35:49 AM
	 */
	public IMap deleteFile(IMap in) {
		IMap result = new DataMap();
		String realpath = ContextUtil.servletContext.getRealPath("/")
				+ "uploadfiles";
		;
		String path = (String) in.get("path");
		String url = realpath + "/" + path;
		java.io.File file = new java.io.File(url);
		if (file.exists()) {
			file.delete();
		}
		return result;
	}
	/**
	 * 下载
	 * @param in
	 */
	  public IMap downLoad(IMap in){
		  IMap result = new DataMap();
		  HttpServletRequest request = (HttpServletRequest) in.get("request");
		  HttpServletResponse response = (HttpServletResponse) in.get("response");
		  ServletContext serlvetContext = (ServletContext) in.get("serlvetContext");
		  HttpSession session = (HttpSession) in.get("session");
		  String file=(String)in.get("file");
		  downFile(request, response,  serlvetContext, session,file);
		  return result;
	  }
	/**
	 * @Description 下载文件方法
	 * @param request
	 * @param response
	 * @param serlvetContext
	 * @param session
	 * @param filePath
	 * @return void
	 * @author panghaichao
	 * @date Mar 30, 2012 11:00:26 AM
	 */
	public static void downFile(HttpServletRequest request,
			HttpServletResponse response, ServletContext serlvetContext,
			HttpSession session, String filePath) {
			String url = request.getSession().getServletContext().getRealPath("/");
			String fileDownUrl = url + filePath;
			SmartUpload su = new SmartUpload();
			java.io.File file = new java.io.File(fileDownUrl);
			if (file.exists()) {
			try {
				su.initialize(serlvetContext, session, request, response, null);
				su.setContentDisposition(null);
				su.downloadFile(fileDownUrl);
			} catch (Exception e) {
				throw new BusinessException("下载文件失败！");
			}
			} else {
				throw new BusinessException("您要下载的文件不存在,请联系管理员！");
			}
		
	}
}
