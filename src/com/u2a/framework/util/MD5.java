package com.u2a.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * <p> MD5的实现  </p>
 * @author waitbin
 * @see 2007年8月8日11:26:47
 * @since 1.0
 */
public class MD5 {
	private MessageDigest md5;
//	十六进制下数字到字符的映射数组 
	private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"}; 
	private static MD5 Instance;
	private final static char[] hexDigitchar = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
	
	public synchronized static MD5 getMD5Instance() {
		if (Instance == null)
			Instance = new MD5();
		return Instance;
	}
	private MD5() {
		try {
			this.md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	public String compute(String inStr) {
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = this.md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	
    /**
     * 下面是第二个MD5加密的算法（zhaofei）
     */
    
	/**把inputString加密*/ 
	public static String createPassword(String inputString){ 
	return encodeByMD5(inputString); 
	} 

	/** 
	* 验证输入的密码是否正确 
	* @param password 真正的密码（加密后的真密码） 
	* @param inputString 输入的字符串 
	* @return 验证结果，boolean类型 
	*/ 
	public static boolean authenticatePassword(String password,String inputString){ 
	if(password.equals(encodeByMD5(inputString))){ 
	return true; 
	}else{ 
	return false; 
	} 
	} 
	/**
	 * ================================MD5文件操作=============================================
	 */
	public static String getFileMD5String(File file) throws IOException{
		  FileInputStream in = new FileInputStream(file);
		  MessageDigest md;
		try {
			  md = MessageDigest.getInstance("MD5");
		
			  FileChannel ch =in.getChannel();
			  MappedByteBuffer byteBuffer =ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			  md.update(byteBuffer);
		  
		  	return bufferToHex(md.digest());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
		}
	private static String bufferToHex(byte bytes[]){
		  return bufferToHex(bytes, 0,bytes.length);
		}
	private static String bufferToHex(byte bytes[], int m, int n){
		  StringBuffer stringbuffer =new StringBuffer(2 * n);
		  int k = m + n;
		  for (int l = m; l< k; l++) {
		  appendHexPair(bytes[l], stringbuffer);
		  }
		  return stringbuffer.toString();
		}
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
	  char c0 = hexDigitchar[(bt& 0xf0) >> 4];
	  char c1 = hexDigitchar[bt& 0xf];
	  stringbuffer.append(c0);
	  stringbuffer.append(c1);
	}
	/**
	 * ================================MD5文件操作=============================================
	 */
	
	
	/**对字符串进行MD5编码*/ 
	private static String encodeByMD5(String originString){ 
	if(originString != null){ 
	try{ 
//	创建具有指定算法名称的信息摘要 
	MessageDigest md = MessageDigest.getInstance("MD5"); 
//	使用指定的字节数组对摘要进行最后更新，然后完成摘要计算 
	byte[] results = md.digest(originString.getBytes()); 
//	将得到的字节数组变成字符串返回 
	String resultString = byteArrayToHexString(results); 
	return resultString.toUpperCase(); 
	}catch(Exception ex){ 
	ex.printStackTrace(); 
	} 
	} 
	return null; 
	} 

	/** 
	* 轮换字节数组为十六进制字符串 
	* @param b 字节数组 
	* @return 十六进制字符串 
	*/ 
	private static String byteArrayToHexString(byte[] b){ 
	StringBuffer resultSb = new StringBuffer(); 
	for(int i=0;i<b.length;i++){ 
	resultSb.append(byteToHexString(b[i])); 
	} 
	return resultSb.toString(); 
	} 

	/** 
	* 将一个字节转化成十六进制形式的字符串 
	*/ 
	private static String byteToHexString(byte b){ 
	int n = b; 
	if(n<0) 
	n=256+n; 
	int d1 = n/16; 
	int d2 = n%16; 
	return hexDigits[d1] + hexDigits[d2]; 
	}
	
	
	public static void main(String[] args) {
		//System.out.println(createPassword("123456"));
		String sql = "select * form person_info t where t.creatuser like '%user%' order by t.createdate";
		String ordersql = "";
		//try {
        String selectsql = "";
        //判断如果有order by 子句  则将order by 子句放到Row_Number() over ()中
        if(sql.indexOf("order")>0){
        	selectsql = sql.substring(0, sql.indexOf("order"));
        	ordersql = sql.substring(sql.indexOf("order"),sql.length());
        }
        	System.out.print(ordersql+"======="+selectsql);
			//System.out.println(getFileMD5String(new File("C:/Upload/基础练习4.xls")));
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
	}
	
}