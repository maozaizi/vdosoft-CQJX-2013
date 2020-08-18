package com.u2a.framework.commons;

import java.util.Date;

import com.brick.dao.IBaseDAO;
import com.brick.data.IMap;
import com.u2a.framework.util.HelperDB;

public class DAO extends com.brick.dao.DAO implements IBaseDAO {
	public Object update(IMap obj) {
		if (obj.getMeta().get("modifyDate")!=null){
			//自动添加modifyDate
			Date date =  new Date();
			HelperDB.setDateTime("modifyDate", obj, date);
		}
		return super.update(obj);
	}
	public Object insert(IMap obj){
		if (obj.getMeta().get("modifyDate")!=null){
			//自动添加修改时间
			Date date =  new Date();
			HelperDB.setDateTime("createDate", obj, date);
			HelperDB.setDateTime("modifyDate", obj, date);
			if (obj.getMeta().get("isValid")!=null){
				obj.put("isValid",Constants.ISVALID);
			}
		}
		return super.insert(obj);
	}
	
}
