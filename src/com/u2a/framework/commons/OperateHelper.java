package com.u2a.framework.commons;

/**      
 * 系统名称：新疆生产建设兵团消防网络化信息管理系统   
 * 类名称：OperateHelper   
 * 类描述：操作提示
 * 创建人：panghaichao 
 * 创建时间：Apr 19, 2012 10:38:57 PM   
 */

public class OperateHelper {

	/** 
	 * @Description 添加
	 * @return String   
	 * @author panghaichao
	 * @date Apr 19, 2012 10:38:29 PM
	 */

	public static String getAddMsg() {
		String addMsg = "添加成功!";
		return addMsg;
	}

	/** 
	 * @Description 修改
	 * @return String   
	 * @author panghaichao
	 * @date Apr 19, 2012 10:38:39 PM
	 */

	public static String getUpdateMsg() {
		String updateMsg = "修改成功!";
		return updateMsg;
	}

	/** 
	 * @Description 作废
	 * @return String   
	 * @author panghaichao
	 * @date Apr 19, 2012 10:38:46 PM
	 */

	public static String getDelMsg() {
		String delMsg = "作废成功!";
		return delMsg;
	}
	public static String getNotUpdateMsg() {
		return "数据已被更新,请刷新后重试!";
	}
	public static String getSaveMsg() {
		return "已保存!";
	}
	
	/** 
	 * @Description 删除
	 * @return String   
	 * @author panghaichao
	 * @date Apr 20, 2012 10:09:09 PM
	 */ 
		
	public static String getDeleteMsg() {
		String deleteMsg = "删除成功!";
		return deleteMsg;
	}
}
