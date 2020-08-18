package com.u2a.datadict;

import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
/**
 * 行业代码与名称对象查询类
 * @author 王冲
 * @date 2010-8-27 17:32:04
 */
@SuppressWarnings("unchecked")
public class TradeData {
	
	private static IMap mapData = new DataMap(); //存储行业数据的code-name键值对
	private static TradeData tradedata = new TradeData(); //单例
	private TradeData(){}
	public static TradeData getTradeData(){
		return tradedata;
	}
	/**
	 * 通过行业code得到行业名称
	 * @param code
	 * @return
	 */
	public String getNameByCode(String code){
		//第一次或者mapData没有code，通过查询获得name
		if(mapData.size()<=0 || (mapData.get(code)==null)){
			return selectName(code);
		}else{//如果存在，直接从mapData取出
			return (String) mapData.get(code);
		}
	}
	/**
	 * 从数据库中通过code取name
	 * @param code
	 * @return
	 */
	private String selectName(String code){
		IBaseDAO dao = (IBaseDAO) ContextUtil.getSpringBean("brickDao");
		IMap tradeParam = BeanFactory.getClassBean("com.Ttrade");
		tradeParam.put("tradCode", code);
		IMap tradeMap = dao.get(tradeParam);
		String tradName = (String) tradeMap.get("tradName");
		mapData.put(code, tradName);
		return tradName;
	}
}
