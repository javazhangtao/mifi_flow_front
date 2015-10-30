package com.mifi.flow.service.impl;


import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.mifi.flow.common.Dictionary;
import com.mifi.flow.common.FlowServer;
import com.mifi.flow.entity.MQFlowEntity;
import com.mifi.flow.entity.ResponceInfo;
import com.mifi.flow.mq.MQCode;
import com.mifi.flow.mq.MQManager;
import com.mifi.flow.service.MIFIFlowService;

@FlowServer(name="flowService")
public class MIFIFlowServiceImpl implements MIFIFlowService{
	
	@Autowired
	private MQCode mqCode;

	@Override
	public ResponceInfo flowAppear(String datas,String ntime , String apMac){
		ResponceInfo responceInfo=new ResponceInfo();
		try {
			MQFlowEntity flowEntity=new MQFlowEntity();
			if(Strings.isNullOrEmpty(datas) || Strings.isNullOrEmpty(apMac) || Strings.isNullOrEmpty(ntime)){
				responceInfo.setCode(Dictionary.CODE_PARAM_ILLEGAL);
				responceInfo.setMessage("Params is illegal");
				return responceInfo;
			}
			String _destination_name=Dictionary.getProperty("ap_flow_appear_destination");
			if(Strings.isNullOrEmpty(_destination_name)){
				responceInfo.setCode(Dictionary.CODE_PARAM_NULL);
				responceInfo.setMessage(" not found ap_flow_appear_destination in properties file ");
				return responceInfo;
			}
			flowEntity.setApMac(apMac);
			flowEntity.setDatas(datas);
			flowEntity.setNtime(ntime);
			MQManager mqManager=new MQManager(mqCode,_destination_name,true);
			mqManager.send(flowEntity.toString());
			responceInfo.setCode(Dictionary.CODE_SUCCESS);
			responceInfo.setMessage("SUCCESS");
			return responceInfo;
		} catch (Exception e) {
			responceInfo.setCode(Dictionary.CODE_SYSTEM_ERROT);
			responceInfo.setMessage(e.getMessage());
			return responceInfo;
		}
	}

}
