package com.mifi.flow.entity;

import java.util.UUID;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.mifi.flow.utils.PropertiesUtil;

public class MQFlowEntity {

	private String redisKey;
	private String datas;
	private String apMac;
	private String ntime;
	
	public String getRedisKey() {
		return redisKey;
	}
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	public String getDatas() {
		return datas;
	}
	public void setDatas(String datas) {
		this.datas = datas;
	}
	public String getApMac() {
		return apMac;
	}
	public void setApMac(String apMac) {
		this.apMac = apMac;
	}
	public String getNtime() {
		return ntime;
	}
	public void setNtime(String ntime) {
		this.ntime = ntime;
	}
	
	@Override
	public String toString() {
		Gson gson=new Gson();
		if(Strings.isNullOrEmpty(this.getRedisKey())){
			UUID uuid=UUID.randomUUID();
			this.setRedisKey(PropertiesUtil.getProperty("REDIS_KEY_PRIFIX").concat(uuid.toString()));
		}
		return gson.toJson(this);
	}
	
	public MQFlowEntity _obj2Bean(String _jsonObject){
		if(Strings.isNullOrEmpty(_jsonObject))
			return null ;
		Gson gson=new Gson();
		return gson.fromJson(_jsonObject, MQFlowEntity.class);
	}
	
	
}
