package com.u2a.framework.tag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.brick.data.IMap;

public class TreeTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String input; // 控件类型，当input为checkbox时为树加长复选框
	private String position; // 树的位置，left为左侧菜单树，right为右侧的页面数
	private List<IMap> usedList; // 已非配的权限集合
	private List<IMap> list;

	public List<IMap> getUsedList() {
		return usedList;
	}

	public void setUsedList(List<IMap> usedList) {
		this.usedList = usedList;
	}

	public List<IMap> getList() {
		return list;
	}

	public void setList(List<IMap> list) {
		this.list = list;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		JspWriter out = pageContext.getOut();

		try {
			List<IMap> parameter = null;
			// 所有权限集合
//			if ("left".equalsIgnoreCase(getPosition())) {
//				parameter = (List<IMap>) request.getSession().getAttribute(
//						"authList");
//			} else {
//				parameter = (List<IMap>) request.getSession()
//						.getServletContext().getAttribute("authTree");
//			}

			//System.out.println(getList().size());
			// 如果权限集合不为nul，开始绘制树
			if (getList() != null && getList().size() > 0) {
				buildTree(getList(), out, request);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	/**
	 * 绘制权限树一级权限
	 * 
	 * @param authList
	 * @param out
	 * @param request
	 */
	private void buildTree(List<IMap> authList, JspWriter out,
			HttpServletRequest request) {
		try {
			// 获取需要展开的节点Id
			String expandId = request.getAttribute("expandId") == null ?
					"" : (String) request.getAttribute("expandId");
			
			out.println("<script language=\"javascript\">");
			out.println("var tree = new MzTreeView(\"tree\");");

			out.println("tree.setIconPath(\"" + request.getContextPath()
					+ "/images/MzTreeView12/\");");

			out.println("tree.N[\"0_1\"] = \"\";");
			// 遍历权限集合
			for (IMap authMap : authList) {
				if ("left".equalsIgnoreCase(getPosition())) {
					// 判断权限类型是否为非控件权限，左侧菜单只显示非控件权限。
					// 0，模块权限；1，页面权限；2，控件权限
					if (!"2".equals(authMap.get("authType"))) {
						// 判断权限是否有url，有则为其加上url
						if (authMap.get("url") != null
								&& authMap.get("url").toString().trim()
										.length() > 0) {

							out.println("tree.N[\"1_" + authMap.get("authId")
									+ "\"] = \"T:" + authMap.get("authName")
									+ ";target:rightFrame;url:"
									+ request.getContextPath()
									+ authMap.get("url") + "\";");

						} else { // 如果没有url，只显示权限名称

							out.println("tree.N[\"1_" + authMap.get("authId")
									+ "\"] = \"T:" + authMap.get("authName")
									+ "\";");
						}
					}
				} else { // 绘制右侧权限树
					// 判断是否需要在末级权限前加上checkbox，以此判断是授权还是管理界面
					if ("checkbox".equalsIgnoreCase(getInput())) {
						String checked = "0";
						// 将用户已分配的权限前的复选框选中
						if (getUsedList() != null && getUsedList().size() > 0) {
							for (IMap userAuthMap : getUsedList()) {
								if (userAuthMap.get("authId").equals(
										authMap.get("authId"))) {
									checked = "1";
								}
							}
						}

						out.println("tree.N[\"1_" + authMap.get("authId")
								+ "\"] = \"ctrl:authId;checked:" + checked
								+ ";T:" + authMap.get("authName") + "\";");
						
					} else if (!"checkbox".equalsIgnoreCase(getInput())) {
						// 权限名称
						out.println("tree.N[\"1_" + authMap.get("authId")
								+ "\"] = \"T:" + authMap.get("authName")
								+ ";C:setVal('" + authMap.get("authId")
								+ "', '" + authMap.get("authGrade") + "', '"
								+ authMap.get("rootAuthId") + "', '"
								+ authMap.get("authType") + "')\"");

					}
				}

				if (authMap.get("childList") != null) {
					bulidChildTree(authMap, out, request);
				}
			}
			
			out.println("document.getElementById(\"treeviewarea\").innerHTML "
					+ "= tree.toString();");
			
			// 设置展开节点
			if (!"".equals(expandId)) {
				out.println("tree.focus(\"" + expandId + "\")");
			}
			
			// 当需要加入checkbox时，所有节点全部展开
			if (getInput() != null && !"".equals(getInput())
					&& getInput().equalsIgnoreCase("checkbox")) {
				out.println("tree.expandAll();");
			}
			out.println("</script>");
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 绘制子权限
	 * 
	 * @param parentMap
	 *            父权限信息
	 * @param out
	 * @param request
	 */
	private void bulidChildTree(IMap parentMap, JspWriter out,
			HttpServletRequest request) {

		try {
			
			List<IMap> childList = (List<IMap>) parentMap.get("childList");
			// 判断子权限中是否包含非控件权限，默认为false，不包含
			boolean isNotControlAuth = false;
			// 如果包含非控件权限将isNotControlAuth置为true
			for (IMap childMap : childList) {
				if (!"2".equals(childMap.get("authType"))) {
					isNotControlAuth = true;
				}
			}
			// 绘制左侧菜单树子权限
			if ("left".equalsIgnoreCase(getPosition())) {
				// 当子权限中包含非控件权限时才绘制子菜单
				if (isNotControlAuth) {
					for (IMap childMap : childList) {
						// 判断权限类型是否为非控件权限，左侧菜单只显示非控件权限，
						// 并判断该权限是否有链接，如果有则为其加上链接。
						// 0，模块权限；1，页面权限；2，控件权限
						if (!"2".equals(childMap.get("authType"))) {
							if (childMap.get("url") != null
									&& childMap.get("url").toString().trim()
											.length() > 0) {
								// 判断权限是否有url，有则为其加上url
								out.println("tree.N[\""
										+ childMap.get("parentAuthId") + "_"
										+ childMap.get("authId") + "\"] = \"T:"
										+ childMap.get("authName")
										+ ";target:rightFrame;url:"
										+ request.getContextPath()
										+ childMap.get("url") + "\";");
							} else {
								out.println("tree.N[\""
										+ childMap.get("parentAuthId") + "_"
										+ childMap.get("authId") + "\"] = \"T:"
										+ childMap.get("authName") + "\";");
							}
						}

						if (childMap.get("childList") != null) {
							bulidChildTree(childMap, out, request);
						}
					}
				}
			} else {
				for (IMap childMap : childList) {
					// 判断是否需要在末级权限前加上checkbox，以此判断是授权还是管理界面
					if ("checkbox".equalsIgnoreCase(getInput())) {
						String checked = "0";
						// 将用户已分配的权限前的复选框选中
						if (getUsedList() != null && getUsedList().size() > 0) {
							for (IMap userAuthMap : getUsedList()) {
								if (userAuthMap.get("authId").equals(
										childMap.get("authId"))) {
									checked = "1";
								}
							}
						}
						out.println("tree.N[\"" + childMap.get("parentAuthId")
								+ "_" + childMap.get("authId")
								+ "\"] = \"ctrl:authId;checked:" + checked
								+ ";T:" + childMap.get("authName") + "\";");
					} else if (!"checkbox".equalsIgnoreCase(getInput())) {
						if (childMap.get("authType").equals("2")) {
							// 权限名称
							out.println("tree.N[\"" + childMap.get("parentAuthId")
									+ "_" + childMap.get("authId") + "\"] = \"T:"
									+ childMap.get("authName") 
									+ "[" 
									+ childMap.get("authCode") +"];C:setVal('"
									+ childMap.get("authId") + "', '"
									+ childMap.get("authGrade") + "', '"
									+ childMap.get("rootAuthId") + "','"
									+ childMap.get("authType") + "')\"");
						} else {
							// 权限名称
							out.println("tree.N[\"" + childMap.get("parentAuthId")
									+ "_" + childMap.get("authId") + "\"] = \"T:"
									+ childMap.get("authName") +";C:setVal('"
									+ childMap.get("authId") + "', '"
									+ childMap.get("authGrade") + "', '"
									+ childMap.get("rootAuthId") + "','"
									+ childMap.get("authType") + "')\"");
						}
						
					}

					if (childMap.get("childList") != null) {
						bulidChildTree(childMap, out, request);
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
