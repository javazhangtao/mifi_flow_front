package com.mifi.flow.mq;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

public class MQCode implements InitializingBean{
	
	PooledConnection _pooledConnection;
	private Session session ;
	
	Map<String, Boolean> _brokens; //队列   名称， 是否持久化    是：TOPIC     否：QUEUE
	
	public Map<String, MessageProducer> producers=new ConcurrentHashMap<String, MessageProducer>();
	public Map<String, MessageConsumer> consumers=new ConcurrentHashMap<String, MessageConsumer>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(null==_pooledConnection)
			throw new IllegalAccessError(" _pooledConnection IS NULL ");
		for (Entry<String, Boolean> e:_brokens.entrySet()) {
			setSession(_pooledConnection.getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE));
			if(e.getValue())
				_createProducer(e.getKey());
			else
				_createConsumer(e.getKey());
		}
	}
	
	
	void _createProducer(String name) throws Exception{
		if(StringUtils.hasText(name)){
			if(!getProducers().containsKey(name)){
				Destination dest = new ActiveMQTopic(name);
				MessageProducer producer=getSession().createProducer(dest);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				synchronized(getProducers()){
					getProducers().put(name, producer);
				}
			}
		}else{
			throw new NullPointerException(" TOPIC NAME IS NULL ");
		}
	}
	
	
	void _createConsumer(String name) throws Exception{
		if(StringUtils.hasText(name)){
			if(!getConsumers().containsKey(name)){
				Destination dest = new ActiveMQQueue(name);
				synchronized(getConsumers()){
					getConsumers().put(name, getSession().createConsumer(dest));
				}
			}
		}else{
			throw new NullPointerException(" QUEUE NAME IS NULL ");
		}
	}
	
	public PooledConnection get_pooledConnection() {
		return _pooledConnection;
	}

	public void set_pooledConnection(PooledConnection _pooledConnection) {
		this._pooledConnection = _pooledConnection;
	}

	public Map<String, Boolean> get_brokens() {
		return _brokens;
	}

	public void set_brokens(Map<String, Boolean> _brokens) {
		this._brokens = _brokens;
	}


	public Session getSession() {
		return session;
	}


	public void setSession(Session session) {
		this.session = session;
	}


	public Map<String, MessageProducer> getProducers() {
		return producers;
	}


	public void setProducers(Map<String, MessageProducer> producers) {
		this.producers = producers;
	}


	public Map<String, MessageConsumer> getConsumers() {
		return consumers;
	}


	public void setConsumers(Map<String, MessageConsumer> consumers) {
		this.consumers = consumers;
	}
	
	
	

}
