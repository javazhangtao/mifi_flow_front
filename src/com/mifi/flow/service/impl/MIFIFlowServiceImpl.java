package com.mifi.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.mifi.flow.common.Dictionary;
import com.mifi.flow.common.FlowServer;
import com.mifi.flow.entity.MQFlowEntity;
import com.mifi.flow.mq.MQCode;
import com.mifi.flow.mq.MQManager;
import com.mifi.flow.service.MIFIFlowService;

@FlowServer(name="flowService")
public class MIFIFlowServiceImpl implements MIFIFlowService{
	
	@Autowired
	private MQCode mqCode;

	@Override
	public void flowAppear(String datas,String ntime , String apMac) throws Exception{
		MQFlowEntity flowEntity=new MQFlowEntity();
		if(Strings.isNullOrEmpty(datas) || Strings.isNullOrEmpty(apMac) || Strings.isNullOrEmpty(ntime))
			throw new NullPointerException("Params must be bot null");
		String _destination=Dictionary.getProperty("ap_flow_appear_destination");
		if(Strings.isNullOrEmpty(_destination))
			throw new NullPointerException(" not found ap_flow_appear_destination in properties file ");
		try {
			if(null==mqCode.getProducers().get(_destination))
				throw new NullPointerException("["+_destination+"] MQ producer is null ");
			flowEntity.setApMac(apMac);
			flowEntity.setDatas(datas);
			flowEntity.setNtime(ntime);
			MQManager mqManager=new MQManager(mqCode,mqCode.getProducers().get(_destination));
			mqManager.send(flowEntity.toString());
		} catch (Exception e) {
			throw new Exception(" flow appear fail :",e);
		}
	}

}
