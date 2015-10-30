package com.mifi.flow.mq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class MQManager {
	Logger logger=Logger.getLogger(MQManager.class);
	
	static ExecutorService service=Executors.newFixedThreadPool(50);//发送消息线程池
	
	private MQCode mqCode;
	
	public MQManager(MQCode mqCode){
		this.mqCode=mqCode;
	}
	
	MessageProducer _messageProducer;
	MessageConsumer _messageConsumer;
	
	public MQManager(MQCode mqCode,MessageProducer messageProducer){
		this.mqCode=mqCode;
		this._messageProducer=messageProducer;
	}
	
	public MQManager(MQCode mqCode,MessageConsumer messageConsumer){
		this.mqCode=mqCode;
		this._messageConsumer=messageConsumer;
	}
	
	public void listener(MessageListener listener) throws Exception{
		_messageConsumer.setMessageListener(listener);
	}
	
	
	public void send(String message) throws Exception{
		if(!StringUtils.hasText(message))
			throw new NullPointerException(" message IS NULL ");
		service.execute(new ProducerSend(message));
	}
	
	
	class ProducerSend implements Runnable{
		String _message;
		
		public ProducerSend(String message){
			this._message=message;
		}
		
		@Override
		public void run() {
			TextMessage msg=null ;
			try {
				msg=mqCode.getSession().createTextMessage(_message);
				if(null!=msg)
					_messageProducer.send(msg);
			} catch (Exception e) {
				logger.error("[MQ]: SEND ["+_message+"] MESSAGE ERROR",e);
				return ;
			}
		}
	}
	
	
	

}
