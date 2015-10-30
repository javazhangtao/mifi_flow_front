package com.mifi.flow.mq;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class MQCode implements InitializingBean{
	
	@Autowired
	PooledConnection _pooledConnection;
	private Session session ;
	
	Map<String, Boolean> _brokens; //队列   名称， 是否持久化    是：TOPIC     否：QUEUE
	
	public Map<String, Destination> destinations=new ConcurrentHashMap<String, Destination>();//对应队列名   key:队列名_TOPIC/队列名_QUEUE
	
	public Map<String, MessageProducer> producers=new ConcurrentHashMap<String, MessageProducer>();//对应队列名   
	public Map<String, MessageConsumer> consumers=new ConcurrentHashMap<String, MessageConsumer>();//对应队列名   
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(null==_pooledConnection)
			throw new IllegalAccessError(" _pooledConnection IS NULL ");
		for (Entry<String, Boolean> e:_brokens.entrySet()) {
			setSession(_pooledConnection.getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE));
			Destination dest=(e.getValue())?new ActiveMQTopic(e.getKey()):new ActiveMQQueue(e.getKey());
			if(null!=dest)
				destinations.put(e.getKey().concat((e.getValue())?"_TOPIC":"_QUEUE"), dest);
		}
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

	public Map<String, Destination> getDestinations() {
		return destinations;
	}

	public void setDestinations(Map<String, Destination> destinations) {
		this.destinations = destinations;
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
