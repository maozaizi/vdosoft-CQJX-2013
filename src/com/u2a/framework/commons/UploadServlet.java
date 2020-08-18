package com.u2a.framework.commons;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.brick.data.IMap;
import com.brick.manager.ContextUtil;
import com.u2a.framework.util.DateTimeUtil;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	/** 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	static long offset = 0;//文件流位置,用于断点续传
	static long currTime = 0;//记录上传前时间
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
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
				java.io.File saveFile = new java.io.File(savePath+ filepath +name);
				BufferedInputStream ins;
				try {
					ins = new BufferedInputStream(item.getInputStream());
					this.offset = 0;
					if(saveFile.exists()){
						offset = saveFile.length();
					}
					if (offset == size && offset != 0) {
						System.out.println("该文件已经上传! ");
					}else {
						doUpload(ins, out, name, size, savePath+ filepath);
					}
					break;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @param out
	 * @param @param fileName
	 * @param @param maxLen
	 * @param @param upload_dir    
	 * @return void   
	 * @author duch
	 * @date May 10, 2013 10:15:01 AM
	 */
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
					out.print("<script>alert('11111');</script>");
					System.out.println("callback('正在上传文件 " + (offset + len)/1024/1024 + "M/" + maxLen/1024/1024  + "M')");}
			}
			out.print("<script>parent.callback('文件上传成功')</script>");
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
	 * @param @param path
	 * @param @return    
	 * @return boolean   
	 * @author duch
	 * @date May 10, 2013 9:49:19 AM
	 */
	private boolean createFolder(String path) {
		java.io.File file = new java.io.File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		return true;
	}
}
