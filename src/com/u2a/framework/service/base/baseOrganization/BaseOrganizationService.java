package com.u2a.framework.service.base.baseOrganization;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.brick.api.Service;
import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.AppCaller;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.FileUpload;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

/**
 * 系统名称：新疆生产建设兵团消防网络化信息管理系统 类名称：BaseOrganizationService 类描述：基本组织结构维护 创建人：zhangbo
 * 创建时间：Feb 15, 2012 3:36:38 PM
 */

/**
 * @author zhangbo
 *
 */
@SuppressWarnings("unchecked")
public class BaseOrganizationService extends Service {

	/**
	 * @Description获取所有的tree节点
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Feb 15, 2012 3:38:53 PM
	 */
	public IMap getAllBaseOrganizationList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		// 设置查询条件 查询跟节点 查询所有
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("isValid", Constants.ISVALID);
		List<IMap> baseOrganizationList = db.getList(condition, null);
		
		//JSONArray a =JSONArray.fromObject(baseOrganizationList);
		result.put("baseOrganizationList", baseOrganizationList);
		
		return result;
	}

	/**
	 * @Description保存节点
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Feb 15, 2012 5:28:05 PM
	 */

	public IMap addBaseOrganization(IMap in) {
		IMap result = new DataMap();
		// 获取节点信息
		IMap baseOrganizationNode = (IMap) in.get("baseOrganization");
		//获取父节点编码
		String parentCode = (String) in.get("parentCode");
		String orgCode = (String) in.get("orgCode");
		
		String code = parentCode+".|"+orgCode+"|";
		
		// 按照.分解编号为数组
		String[] oldArray = code.replace('.', '_').split("_");
		// 获取级别
		int iLevel = oldArray.length;
		// 设置级别
		baseOrganizationNode.put("iLevel", iLevel);
		
		baseOrganizationNode.put("orgCode", code);
		//判断是否已经存在此编码，如果存在则提示存在
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("orgCode", code);
		condition.put("isValid", Constants.ISVALID);
		IMap alreadyExtist = (IMap) db.get(condition);
		if(Util.checkNotNull(alreadyExtist)){
			result.put("method.infoMsg", "添加失败，组织编号已存在!");
		}else{
			// 创建节点
				// 从session中获取用户信息
				IMap userMap = (IMap) in.get("userMap");
				// 创建主键
				baseOrganizationNode.put("id", HelperApp.getAutoIncrementPKID(
						HelperApp.getPackageName(), "com.baseorganization"));
				// 创建时间/修改时间/创建人/修改人
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),
						baseOrganizationNode);
				// 保存
				db.insert(baseOrganizationNode);
	
				result.put("method.infoMsg", OperateHelper.getAddMsg());
		}
		// 获取要跳转的也面路径
		String url = in.get("tempUrl").toString();
		result.put("method.url", url );
		return result;
	}

	public IMap toModifyBaseOrganization(IMap in) {
		IMap result = new DataMap();
		IMap baseOrganization = db.get((IMap) in.get("baseOrganization"));
		result.put("baseOrganizations", baseOrganization);
		return result;
	}

	public IMap updateBaseOrganization(IMap in) {
		IMap result = new DataMap();
		IMap baseOrganizationNode = (IMap) in.get("baseOrganization");
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		// 创建时间/修改时间/创建人/修改人
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),
				baseOrganizationNode);
		db.update(baseOrganizationNode);

		String url = in.get("tempUrl").toString();
		result.put("method.url", url);
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}

	public IMap deleteBaseOrganization(IMap in) {
		IMap result = new DataMap();
		IMap baseOrganizationNode = (IMap) in.get("baseOrganization");
		// 删除
		baseOrganizationNode.put("isValid", Constants.ISNOTVALID);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		// 创建时间/修改时间/创建人/修改人
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),
				baseOrganizationNode);
		// 更新数据
		db.update(baseOrganizationNode);
		String url = in.get("tempUrl").toString();
		result.put("method.url", url);
		result.put("method.infoMsg", OperateHelper.getDelMsg());
		return result;
	}

	public IMap updateBaseOrgInfo(IMap in) {
		IMap result = new DataMap();
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		List<IMap> orgList = db.getList(condition, null);
		int orderby = 1;
		for (IMap item : orgList) {
			updateInfo(item, orderby, db);
			orderby++;
		}
		result.put("method.infoMsg", "更新成功！");
		return result;
	}

	public IMap updateUserOrgInfo(IMap in) {
		IMap result = new DataMap();
		IMap condition = BeanFactory.getClassBean("com.UserInfo");
		List<IMap> orgList = db.getList(condition, null);
		for (IMap item : orgList) {
			String newBaseId = "";
			String oldBaseId = (String) item.get("baseOrganizationId");
			// 按照.分解编号为数组
			String[] oldArray = oldBaseId.replace('.', '_').split("_");
			for (int i = 0, j = oldArray.length; i < j; i++) {
				String splitStr = oldArray[i];
				splitStr = "{" + splitStr + "}";
				if (i == j - 1) {
					newBaseId += splitStr;
				} else {
					newBaseId += splitStr + ".";
				}
			}
			item.put("baseOrganizationId", newBaseId);
			db.update(item);
		}
		result.put("method.infoMsg", "更新成功！");
		return result;
	}

	/**
	 * 更新节点BaseID,iLevel,orderby,id,parentId字段
	 * 
	 * @param oldId
	 * @return
	 */
	public void updateInfo(IMap target, int orderby, IBaseDAO db) {
		int iLevel;
		// String id =
		// HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(),
		// target.getClassName());
		String id = (String) target.get("baseOrganizationId");
		String newBaseId = "";
		String newParentId = "";

		// 获取旧编号
		String oldBaseId = (String) target.get("baseOrganizationId");
		// 按照.分解编号为数组
		String[] oldArray = oldBaseId.replace('.', '_').split("_");
		// 获取级别
		iLevel = oldArray.length;
		// 设置级别
		target.put("iLevel", iLevel);

		// 设置主键
		target.put("id", id);
		for (int i = 0, j = iLevel; i < j; i++) {
			String splitStr = oldArray[i];
			splitStr = "{" + splitStr + "}";
			if (i == j - 1) {
				newBaseId += splitStr;
			} else {
				newBaseId += splitStr + ".";
			}
		}

		// 获取旧父编号
		String oldParentId = (String) target.get("parentId");
		// 按照.分解父编号为数组
		String[] oldParentArray = oldParentId.replace('.', '_').split("_");
		for (int i = 0, j = oldParentArray.length; i < j; i++) {
			String splitStr = oldArray[i];
			splitStr = "{" + splitStr + "}";
			if (i == j - 1) {
				newParentId += splitStr;
			} else {
				newParentId += splitStr + ".";
			}
		}
		// 设置新编号
		target.put("baseOrganizationId", newBaseId);
		target.put("parentid", newParentId);
		target.put("orderby", orderby);
//		System.out.println("旧编号为：" + oldBaseId + "===>>>>新编号：" + newBaseId
//				+ "|新父节点" + newParentId + "|节点级别:" + iLevel + "|id" + id
//				+ "|排序" + orderby);
		// 更新
		db.update(target);
	}
	

}
