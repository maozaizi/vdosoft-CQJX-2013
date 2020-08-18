package com.u2a.framework.service.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.service.sys.AuthGroupService;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
@SuppressWarnings("unchecked")
public class AuthService extends Service {

	private IBaseDAO baseDAO;
	
	public IBaseDAO getBaseDAO() {
		baseDAO = (IBaseDAO) super.db;
		return baseDAO;
	}
	
	/**
	 * 加载所有权限
	 */
	public IMap getAllAuth(IMap in) {
		IMap result = new DataMap();
		
		// 查询所有权限
//		List<IMap> allAuth = getBaseDAO().getList(
//				null, "queryAllAuthList", "com.AuthInfo");
		List<IMap> allAuth = null;
		result.put("allAuth", allAuth);
		
		return result;
	}
	
	/**
	 * 加载所有权限,权限树
	 * @param in
	 * @return
	 */
	public IMap getAuthTree(IMap in) {
		IMap result = new DataMap();
		IMap paramMap = new DataMap();
		paramMap.put("authId", "-1");
		
		// 查询所有权限
		List<IMap> authList = getBaseDAO().getList(
				paramMap, "queryAuthList", "com.AuthInfo");
		
		authList = getAuthList(authList, "com.AuthInfo");
		
		result.put("authTree", authList);
		
		return result;
	}
	
	public IMap getListAll(IMap in) {
		IMap result = new DataMap();
		
		IMap paramMap = (IMap) in.get("auth");
		String className = paramMap.getClassName();
		in.put("authId", "-1");
		// 查询所有权限
		List<IMap> authList = getBaseDAO().getList(
				in, "queryAuthList", className);
		
		authList = getAuthList(authList, className);
		
		// 返回时要展开的节点
		result.put("expandId", in.get("expandId") == null 
				? "" : (String) in.get("expandId"));
		result.put("authList", authList);
		return result;
	}
	
	/**
	 * 递归查询子权限
	 * @param parentList
	 * @return
	 */
	private List<IMap> getAuthList(List<IMap> parentList, String className) {
		
		for (IMap map : parentList) {
			
			List<IMap> authList = getBaseDAO().getList(
					map, "queryAuthList", className);
			
			if (authList.size() > 0) {
				map.put("childList", authList);
				getAuthList(authList, className);
			}
		}
		
		return parentList;
	}
	
	/**
	 * 跳转至添加权限页面，并传递参数
	 * @param in 参数集合，包含了上级权限ID和权限级别
	 * @return
	 */
	public IMap goAddAuthPage(IMap in) {
		IMap result = new DataMap();
		// 上级权限ID，如果参数中parentAuthId为null，表示添加的是顶级权限，顶级权限的上级ID为-1
		String parentAuthId = in.get("parentAuthId") == null 
						? "-1" : (String) in.get("parentAuthId");
		// 权限级别，如果参数中authGrade为null，表示添加的是一级权限，否则将当前权限+1
		String authGrade = in.get("authGrade") == null ? "1" 
				: (Integer.parseInt(in.get("authGrade").toString()) + 1) +"";
		String rootAuthId = in.get("rootAuthId") == null ? "" 
				: in.get("rootAuthId").toString();
		
		result.put("parentAuthId", parentAuthId);
		result.put("authGrade", authGrade);
		result.put("rootAuthId", rootAuthId);
		
		return result;
	}
	
	/**
	 * 保存权限信息
	 * @param in 权限信息集合，包括有classname和权限的各个信息
	 * @return
	 */
	
	public IMap saveAuth(IMap in) {
		IMap result = new DataMap();
		// 获取request
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		// 从session中获取用户信息
		IMap userMap = (IMap) request.getSession().getAttribute("userMap");
		String authId = HelperApp.getAutoIncrementPKID(
				HelperApp.getPackageName(), "com.AuthInfo"); 
		// 获取参数信息
		IMap paramMap = (IMap) in.get("auth");
		paramMap.put("authId", authId);
		// 如果rootAuthId为空，则为一级权限
		if (paramMap.get("rootAuthId") == null 
				|| "".equals(paramMap.get("rootAuthId").toString())) {
			paramMap.put("rootAuthId", authId);
		}
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), paramMap);
		getBaseDAO().insert(paramMap);
		
		result.put("method.infoMsg", "权限添加成功！");
		
		if (paramMap.get("authGrade").equals("1")) {
			result.put("method.url", in.get("actionUrl"));
		} else {
			result.put("method.url", 
					in.get("actionUrl") + "?expandId=" + authId);
		}
		
		
		// 添加后重新加载权限信息
		List<IMap> allAuth = (List<IMap>) this.getAllAuth(in).get("allAuth");
		List<IMap> authTree = (List<IMap>) this.getAuthTree(in).get("authTree");
		
		request.getSession().getServletContext()
								.setAttribute("allAuth", allAuth);
		request.getSession().getServletContext()
								.setAttribute("authTree", authTree);
		
		return result;
	}
	
	public IMap goModifyAuth(IMap in) {
		IMap result = new DataMap();
		
		IMap paramMap = (IMap) in.get("auth");
		
		IMap authMap = getBaseDAO().get(paramMap);
		
		result.put("auth", authMap);
		
		return result;
	}
	
	public IMap modifyAuth(IMap in) {
		IMap result = new DataMap();
		
		try {
			// 获取request
			HttpServletRequest request = (HttpServletRequest) in.get("request");
			IMap paramMap = (IMap) in.get("auth");
			IMap userMap = (IMap) in.get("userMap");
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap), paramMap);
			getBaseDAO().update(paramMap);
			
			result.put("method.infoMsg", "权限修改成功！");
			
			if (paramMap.get("authGrade").equals("1")) {
				result.put("method.url", in.get("actionUrl"));
			} else {
				result.put("method.url", in.get("actionUrl") 
						+ "?expandId=" + paramMap.get("authId"));
			}
			
			
			// 修改后重新加载权限信息
			List<IMap> allAuth = (List<IMap>) this.getAllAuth(in).get("allAuth");
			List<IMap> authTree = (List<IMap>) this.getAuthTree(in).get("authTree");
			request.getSession().getServletContext()
									.setAttribute("allAuth", allAuth);
			request.getSession().getServletContext()
									.setAttribute("authTree", authTree);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("权限修改失败！");
		}
		
		return result;
	}
	
	
	public IMap deleteAuth(IMap in) {
		IMap result = new DataMap();
		// 获取request
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		
		IMap paramMap = (IMap) in.get("auth");
		
		List<IMap> childList = getBaseDAO().getList(paramMap, "queryChildAuth",
				paramMap.getClassName());
		
		// 加载权限信息
		IMap authMap = db.get(paramMap);
		if (childList == null || childList.isEmpty()) {
			List<IMap> memberList = AuthGroupService.getMemberList(paramMap,db);
			// 删除权限组成员
			getBaseDAO().update(paramMap, "deleteAuthGroupAuth");
			IMap userMap = (IMap)in.get("userMap");
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap), paramMap);
			paramMap.put("ISVALID",Constants.ISNOTVALID);
			db.update(paramMap);
			//db.delete(paramMap);
			
			// 设置要展开的节点ID
			result.put("expandId", authMap.get("parentAuthId"));
			result.put("method.infoMsg", "权限删除成功！");
			
			if (authMap.get("authGrade").equals("1")) {
				result.put("method.url", in.get("actionUrl"));
			} else {
				result.put("method.url", in.get("actionUrl") 
						+ "?expandId=" + authMap.get("parentAuthId"));
			}
			
			
			// 修改后重新加载权限信息
			List<IMap> allAuth = (List<IMap>) this.getAllAuth(in).get("allAuth");
			List<IMap> authTree = (List<IMap>) this.getAuthTree(in).get("authTree");
			request.getSession().getServletContext()
									.setAttribute("allAuth", allAuth);
			request.getSession().getServletContext()
									.setAttribute("authTree", authTree);
		} else {
			result.put("method.infoMsg", "该权限被使用，不能删除！");
			result.put("method.url", in.get("actionUrl") + "?expandId=" + paramMap.get("authId"));
		}
		return result;
	}
}
