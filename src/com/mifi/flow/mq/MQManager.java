package com.mifi.flow.mq;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class MQManager {
	Logger logger=Logger.getLogger(MQManager.class);
	
	MessageProducer _messageProducer;
	MessageConsumer _messageConsumer;
	Destination _destination;
	MQCode _mqCode;
	
	public MQManager(MQCode mqCode,String _destinationName) throws Exception{
		this._mqCode=mqCode;
		if(!StringUtils.hasText(_destinationName))
			throw new NullPointerException(" MQ NAME IS NULL ");
		this._destination=mqCode.getDestinations().get(_destinationName);
		_createConsumer(_destinationName);
	}
	
	public MQManager(MQCode mqCode,String _destinationName,Boolean _ifSend) throws Exception{
		this._mqCode=mqCode;
		if(!StringUtils.hasText(_destinationName))
			throw new NullPointerException(" MQ NAME IS NULL ");
		this._destination=mqCode.getDestinations().get(_destinationName);
		if(_ifSend){
			_createProducer(_destinationName);
		}else{
			_createConsumer(_destinationName);
		}
	}
	
	
	
	void _createProducer(String _destinationName) throws Exception{
		if(_mqCode.producers.containsKey(_destinationName)){
			_messageProducer=_mqCode.getProducers().get(_destinationName);
		}else{
			_messageProducer=_mqCode.getSession().createProducer(_destination);
			_messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			synchronized(_mqCode.getProducers()){
				_mqCode.getProducers().put(_destinationName, _messageProducer);
			}
		}
	}
	
	void _createConsumer(String _destinationName) throws Exception{
		if(_mqCode.producers.containsKey(_destinationName)){
			_messageConsumer=_mqCode.getConsumers().get(_destinationName);
		}else{
			_messageConsumer=_mqCode.getSession().createConsumer(_destination);
			synchronized(_mqCode.getConsumers()){
				_mqCode.getConsumers().put(_destinationName, _messageConsumer);
			}
		}
	}
	
	
	
	public void listener(MessageListener listener) throws Exception{
		_messageConsumer.setMessageListener(listener);
	}
	
	
	public void send(String _message) throws Exception{
		if(!StringUtils.hasText(_message))
			throw new NullPointerException(" message IS NULL ");
		TextMessage msg=null ;
		try {
			msg=_mqCode.getSession().createTextMessage(_message);
			if(null!=msg)
				_messageProducer.send(msg);
		} catch (Exception e) {
			logger.error("[MQ]: SEND ["+_message+"] MESSAGE ERROR",e);
		}
	}
}
