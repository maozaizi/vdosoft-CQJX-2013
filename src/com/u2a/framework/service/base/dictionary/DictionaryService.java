package com.u2a.framework.service.base.dictionary;


import java.util.List;
import com.brick.api.Service;
import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

/**
 * 系统名称：新疆生产建设兵团消防网络化信息管理系统 类名称：DictionaryService 类描述：数据项字典管理 创建人：panghaichao
 * 创建时间：Feb 15, 2012 3:26:16 PM
 */
@SuppressWarnings("unchecked")
public class DictionaryService extends Service {

	/**
	 * @Description 获取数据项基本信息列表  
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 15, 2012 3:28:14 PM
	 */

	public IMap getDataItemBaseInfoList(IMap in) {
		IMap paramMap = (IMap) in.get("dataItemBaseInfo");
		in.put("dataItemId", "-1");
		List<IMap> dataItemBaseInfoList = db.getList(in, "dataItemBaseInfoList1", paramMap
				.getClassName());
		IMap<String, Object> result = new DataMap<String, Object>();

		// 返回时要展开的节点
		result.put("expandId", in.get("expandId") == null ? "" : (String) in
				.get("expandId"));
		result.put("dataItemBaseInfoList", dataItemBaseInfoList);
		result.put("cb", new DictionaryTreeTagCallBack());
		return result;
	}
	/**
	 * 获取字典表行政区划信息
	 * @param in
	 * @return
	 */
	public IMap findPoliticalAreaList(IMap in) {
		IMap paramMap = (IMap) in.get("dataItemBaseInfo");
		IMap map = db.get(paramMap);
		List<IMap> dataItemBaseInfoList = db.getList(paramMap, "politicalAreaList", paramMap
				.getClassName());
		IMap<String, Object> result = new DataMap<String, Object>();

		// 返回时要展开的节点
		result.put("expandId", in.get("expandId") == null ? "" : (String) in
				.get("expandId"));
		result.put("dataItemBaseInfoList", dataItemBaseInfoList);
		result.put("paId", map.get("parentId"));
		result.put("cb", new DictionaryTreeTagCallBack());
		return result;
	}
	/**
	 * @Description 添加数据字典跳转
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 17, 2012 2:12:54 PM
	 */

	public IMap toAddDataItemBaseInfo(IMap in) {
		String parentId = in.get("parentId").toString() == null ? "-1" : in
				.get("parentId").toString();

		String dataItemId = (String) in.get("dataItemId");
		// 导航名称
		StringBuffer navigation = this.getName(dataItemId,db);

		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("parentId", parentId);
		result.put("dataItemCode", in.get("dataItemCode"));
		result.put("navigation", navigation.toString());
		return result;
	}
	
	private IMap addDataItemBase(IMap in,String type) {
		IMap insertbean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		String dataItemCode = (String) in.get("dataItemCode");

		insertbean.put("dataItemId", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.DataItemBaseInfo"));
		insertbean.put("parentId", in.get("parentId"));
		insertbean.put("dataItemCode", dataItemCode);
		insertbean.put("dataItemName", in.get("dataItemName"));
		insertbean.put("dataItemValue", in.get("dataItemValue"));
		insertbean.put("type", type); // 类型：0字典项，1字典项明细
		insertbean.put("memo", in.get("memo"));
		insertbean.put("isValid", Constants.ISVALID);// 是否有效：0表示无效，1表示有效
		insertbean.put("isSys", "0"); // 是否系统 1系统，0用户添加
		insertbean.put("orderby", in.get("orderby"));
		
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap), insertbean);
		
		IMap temp = BeanFactory.getClassBean("com.DataItemBaseInfo");
		if(Util.checkNotNull(dataItemCode)){
			temp.put("dataItemCode", dataItemCode);
			temp.put("isValid", Constants.ISVALID);
			List list = db.getList(temp, null);
			if (list.size() > 0) {
				throw new BusinessException("添加失败，编码不能重复！");
			}
		}
		db.insert(insertbean);
		
		String url = in.get("url").toString();

		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("method.url", url + "?dataItemId="
				+ insertbean.get("dataItemId") + "&isShua=1");
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}
	/**
	 * @Description 添加数据字典
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 17, 2012 2:13:34 PM
	 */

	public IMap addDataItemBaseInfo(IMap in) {
		return addDataItemBase(in,"0");
	}

	/**
	 * @Description 添加数据字典项明细
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 28, 2012 11:11:10 AM
	 */

	public IMap addDataItemBaseInfoMx(IMap in) {
		return addDataItemBase(in,"1");
	}

	/**
	 * @Description 修改数据字典跳转
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 17, 2012 2:16:15 PM
	 */

	public IMap toUpdateDataItemBaseInfo(IMap in) {
		IMap insert = (IMap) in.get("dataItemBaseInfo");
		// 导航名称
		StringBuffer navigation = this.getName((String) insert
				.get("dataItemId"),db);

		IMap result = new DataMap();
		result.put("dataItemBaseInfo", db
				.get((IMap) in.get("dataItemBaseInfo")));
		result.put("navigation", navigation.toString());
		return result;
	}

	/**
	 * @Description 修改数据字典信息
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 17, 2012 2:22:00 PM
	 */

	public IMap updateDataItemBaseInfo(IMap in) {
		// 从Session获取登录用户
		IMap userMap = (IMap) in.get("userMap");

		IMap updateBean = (IMap) in.get("dataItemBaseInfo");
		
		String dataItemCode = (String) updateBean.get("dataItemCode");
		IMap temp = BeanFactory.getClassBean("com.DataItemBaseInfo");
		IMap temp1 = BeanFactory.getClassBean("com.DataItemBaseInfo");
		
		if(Util.checkNotNull(dataItemCode)){
			temp.put("dataItemCode", dataItemCode);
			temp.put("isValid", Constants.ISVALID);
			List list = db.getList(temp, null);
			
			temp1.put("dataItemId", updateBean.get("dataItemId"));
			temp1.put("isValid", Constants.ISVALID);
			List<IMap> list1 = db.getList(temp1, null);
			String cc ="";
			if(Util.checkNotNull(list1) && list1.size()>0){
				cc = (String) list1.get(0).get("dataItemCode");
			}
			if(Util.checkNotNull(cc) && cc.equals(dataItemCode)){
				if (list.size() > 1) {
					throw new BusinessException("修改失败，编码不能重复！");
				}
			}else{
				if (list.size() > 0) {
					throw new BusinessException("修改失败，编码不能重复！");
				}
			}
		}
		
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), updateBean);
		
		db.update(updateBean);
		
		IMap<String, Object> result = new DataMap<String, Object>();
		String url = in.get("url").toString();
		result.put("method.url", url + "?dataItemId="
				+ updateBean.get("dataItemId") + "&isShua=1");
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}

	/**
	 * @Description 删除数据字典信息
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Feb 17, 2012 2:23:32 PM
	 */

	public IMap deleteDataItemBaseInfo(IMap in) {
		IMap userMap = (IMap) in.get("userMap");

		String infoString = "作废成功！";
		IMap deletebean = (IMap) in.get("dataItemBaseInfo");
		List<IMap> childNode = db.getList(deletebean, "dataItemBaseInfoList",
				deletebean.getClassName());
		deletebean = db.get(deletebean);
		if (childNode == null || childNode.size() == 0) {
			deletebean.put("isValid", Constants.ISNOTVALID);
			HelperDB.setCreateInfo(HelperApp.getUserName(userMap), deletebean);
			db.update(deletebean);
		} else {
			infoString = "要先作废子节点，才能作废父节点！";
		}

		IMap<String, Object> result = new DataMap<String, Object>();
		String url = in.get("url").toString();
		result.put("method.url", url + "?dataItemId="
				+ deletebean.get("parentId") + "&isShua=1");
		result.put("method.infoMsg", infoString);
		return result;
	}

	/**
	 * @Description 获取数据字典导航
	 * @param
	 * @param dataItemId
	 * @return StringBuffer
	 * @author panghaichao
	 * @date Apr 5, 2012 9:59:39 AM
	 */
	StringBuffer str = new StringBuffer();
	int i = 0;

	private StringBuffer getName(String dataItemId,IBaseDAO db) {
		IMap<String, Object> dataItemBaseInfo = BeanFactory
				.getClassBean("com.DataItemBaseInfo");
		if (dataItemId != null) {
			dataItemBaseInfo.put("dataItemId", dataItemId);
		} else {
			dataItemBaseInfo.put("parentId", "-1");
		}

		IMap map = db.get(dataItemBaseInfo);

		if (i == 0) {
			i++;
			this.getName(map.get("parentId").toString(),db);
			str.append(map.get("dataItemName").toString());
		} else {
			if (!dataItemId.equals("-1")) {
				this.getName(map.get("parentId").toString(),db);
				// str.append("<a
				// href='http://localhost:8080/fireNet/api/fileManage/list?folderId="+map.get("folderId").toString()+"&type=0'>"+map.get("folderName").toString()+"</a>"+"
				// >> ");
				str.append("" + map.get("dataItemName").toString() + " >> ");
			}
		}
		return str;
	}

	/**
	 * @Description 获取字典信息
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Mar 20, 2012 12:50:01 PM
	 */
	public IMap getInfo(IMap in) {
		IMap map = (IMap) in.get("dataItemBaseInfo");
		String dataItemId = (String) map.get("dataItemId");
		if (dataItemId == null || dataItemId.equals("")) {
			map.put("parentId", "-1");
		} else {
			map.put("dataItemId", map.get("dataItemId"));
		}
		map = db.get(map);

		// 导航名称
		StringBuffer navigation = this.getName(dataItemId,db);

		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("dataItemBaseInfo", map);
		result.put("navigation", navigation.toString());
		return result;
	}

	/**
	 * @Description 根据编码获取列表
	 * @param dataItemCode编码
	 * @return IMap<String,Object>
	 * @author panghaichao
	 * @date Feb 28, 2012 3:53:37 PM
	 */

	public static List<IMap> getDataDictionary(String dataItemCode,IBaseDAO db) {
		IMap tempBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		IMap listBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		tempBean.put("dataItemCode", dataItemCode);
		List<IMap> listId = db.getList(tempBean, "getIdByCode","com.DataItemBaseInfo");
		List<IMap> list = null;
		if(listId.size()>0){
			String dataItemId=(String)listId.get(0).get("dataItemId");
			listBean.put("dataItemId", dataItemId);
			list = db.getList(listBean, "getChildsById","com.DataItemBaseInfo");
		}
		
		return list;
	}
	/**
	 * 
	 * @Description 根据ID获得数据项
	 * @param @param dataItemId 
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date May 4, 2012 10:17:29 AM
	 */
	public static IMap<String, Object> getDictionary(String dataItemId,IBaseDAO db) {
		IMap insertBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		insertBean.put("dataItemId", dataItemId);
		insertBean.put("type", "1");
		insertBean.put("isValid", Constants.ISVALID);
		IMap<String, Object> map = db.get(insertBean);
		return map;
	}
	/**
	 * 
	 * @Description 根据dataItemId获取一级的子节点信息
	 * @param @param dataItemId   
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date May 4, 2012 10:17:29 AM
	 */
	public static List<IMap> getParentDictionary(String dataItemId,IBaseDAO db) {
		IMap insertBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		insertBean.put("parentId", dataItemId);
		insertBean.put("isValid", Constants.ISVALID);
		OrderBean orderBean = new OrderBean();
		orderBean.put("orderby", orderBean.ASC);
		List<IMap> list = db.getList(insertBean,orderBean);
		return list;
	}
}
