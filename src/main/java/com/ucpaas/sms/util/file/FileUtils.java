package com.ucpaas.sms.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 文件工具类
 * 
 * @author xiejiaan
 */
public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 查看文件
	 * 
	 * @param path
	 *            文件路径
	 */
	public static void view(String path) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(path);
			out = new BufferedOutputStream(StrutsUtils.getResponse().getOutputStream());
			byte[] buffer = new byte[16 * 1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();

		} catch (FileNotFoundException e) {
			logger.debug("查看文件【文件不存在】：path=" + path);
		} catch (IOException e) {
			logger.error("查看文件【失败】：path=" + path, e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件【失败】：path=" + path, e);
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param path
	 *            文件路径
	 */
	public static void download(String path) {
		String os = System.getProperty("os.name");
		int index = path.lastIndexOf("$$$");

		String fileName = path.substring(path.lastIndexOf("/") + 1);
		if(os.toLowerCase().startsWith("win") && path.indexOf("\\") != -1){
			if (index >= 0){
				//截取文件名称
				fileName = path.substring(path.lastIndexOf("\\") + 1,path.lastIndexOf("$$$"));
			}else {
				fileName = path.substring(path.lastIndexOf("\\") + 1);
			}
		}else{
			if (index >= 0){
				fileName = path.substring(path.lastIndexOf("/") + 1,path.lastIndexOf("$$$"));
			}else {
				fileName = path.substring(path.lastIndexOf("/") + 1);
			}
		}

		InputStream in = null;
		OutputStream out = null;
		try {
			logger.debug("下载文件【开始】：path=" + path);
			in = new FileInputStream(path); // 文件流
			// 设置response的Header
			HttpServletResponse response = StrutsUtils.getResponse();
			response.reset();
			response.setCharacterEncoding("GBK");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(fileName.getBytes("GBK"), "ISO-8859-1"));
			response.setContentType(FileContentTypes.getContentType(fileName));
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[16 * 1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();
			logger.debug("下载文件【成功】：path=" + path);

		} catch (FileNotFoundException e) {
			logger.debug("下载文件【文件不存在】：path=" + path);
		} catch (Throwable e) {
			logger.error("下载文件【失败】：path=" + path, e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件【失败】：path=" + path, e);
			}
		}
	}
	
	public static void upload(String path, String fileName, File uploadFile){
		OutputStream out = null;
		InputStream in = null;
		File saveFile = new File(path,fileName);
		String saveAbsPath = path + "\\"+ fileName;
		
		try {
			out = new FileOutputStream(saveFile);  
	        in = new FileInputStream(uploadFile);
	        
	        byte[] buffer = new byte[1024];  
	        int len = 0 ;  
	          
	        while((len = in.read(buffer)) > 0) 
	        {  
	            out.write(buffer, 0, len);  
	        } 
	        
	        logger.debug("Excel上传【成功】：path=" + saveAbsPath);
	        
		} catch ( IOException e) {
			logger.error("Excel上传【失败】：path=" + saveAbsPath , e);
		} finally{
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件【失败】：path=" + saveAbsPath, e);
			}
			
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件路径
	 */
	public static void delete(String path) {
		new File(path).delete();
	}

	/**
	 * 创建文件夹目录
	 * 
	 * @param path
	 */
	public static void makeDir(String path) {
		int last = path.lastIndexOf("/");
		if (last > 0) {
			File file = new File(path.substring(0, last));
			if (!file.isDirectory()) {
				file.mkdirs();
			}
		}
	}
	

}
