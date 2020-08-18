package com.u2a.framework.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.brick.util.MapUtils;
import com.u2a.framework.util.Util;

/**
 * 邮件计划limit限制
 * 
 * @author 王冲
 * @date 2010-9-8 10:34:25
 */
@SuppressWarnings("unchecked")
public class AuthLimit implements ILimitAuth {

	public Boolean check(HttpServletRequest request,
			HttpServletResponse response, String codeName) {
//		return true;
		System.out.println("codeName:"+codeName);
		//从当前用户中取得带结构的用户权限
		List<IMap> allUserAuthFrame = (List<IMap>) request.getSession()
				.getAttribute("authList");
		//把用户权限反编译成线性结构
		List<IMap> allUserAuth = decompile(allUserAuthFrame);
		for (IMap auth : allUserAuth) {
			
			if ("1".equals(auth.get("authType"))
					|| "2".equals(auth.get("authType"))) {
				String authCode = (String) auth.get("authCode");
				if (Util.checkNull(authCode))
					continue;
				System.out.println("authCode:"+authCode);
				if (authCode.equals(codeName)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public List<IMap> decompile(List<IMap> authUserList) {
		List<IMap> authList = new ArrayList<IMap>();
		for (IMap authParam : authUserList) {
			IMap auth = BeanFactory.getClassBean("com.AuthInfo");
			MapUtils.copyMapRemoveField(authParam, "childList", auth);
			authList.add(auth);
			if (authParam.get("childList") != null) {
				List<IMap> decopileList = decompile((List<IMap>) authParam
						.get("childList"));
				authList.addAll(decopileList);
			}
		}
		return authList;
	}

	/**
	 * 得到两个列表中相同的元素
	 * 
	 * @param userAuthGroup
	 *            第一集合小集合
	 * @param allAuthGroup
	 *            第二集合大集合
	 * @param compareField
	 *            使用这个这字判进行比较
	 * @param isSelectOne
	 *            true 取第一集合的值，false取第二集合的值
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<IMap> getSameAllAuthGroup(List<IMap> userAuthGroup,
			List<IMap> allAuthGroup, String compareField, Boolean isSelectOne) {
		
		List<IMap> result = new ArrayList<IMap>();
		if ((userAuthGroup == null || userAuthGroup.size() <= 0)
				|| Util.checkNull(compareField)) {
			return result;
		}
		// 比较集合
		List<IMap> compareList = userAuthGroup;
		// 被比较集合
		List<IMap> beComparedList = allAuthGroup;

		// 遍历
		for (IMap theOne : compareList) {
			for (IMap theTwo : beComparedList) {
				String theOneCompareField = (String) theOne.get(compareField);
				String theTwoCompareField = (String) theTwo.get(compareField);
				if (theTwoCompareField != null && theTwoCompareField != null
						&& theOneCompareField.equals(theTwoCompareField)) {
					if (isSelectOne) {
						result.add(theOne);
					} else {
						result.add(theTwo);
					}
					break;
				}
			}
		}
		return result;
	}

}
