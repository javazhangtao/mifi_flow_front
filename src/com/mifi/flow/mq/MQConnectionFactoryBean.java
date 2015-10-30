package com.mifi.flow.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

public class MQConnectionFactoryBean implements FactoryBean<PooledConnection>{
	
	Logger logger=Logger.getLogger(MQConnectionFactoryBean.class);
	
	private final String URL_PREFIX="failover:(tcp://";//URL前缀
	private final String URL_SUFFIX=")";//URL后缀
	
	private String host ; 
	private Integer port=61616;
	private String username;
	private String password;
	
	PooledConnection _pooledConnection=null;
	

	@Override
	public PooledConnection getObject() throws Exception {
		if(null==_pooledConnection)
			_createPooledConnection();
		return _pooledConnection;
	}

	@Override
	public Class<?> getObjectType() {
		return PooledConnection.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	
	void _createPooledConnection() throws Exception{
		if(!StringUtils.hasText(host))
			throw new NullPointerException("MQ: HOST IS NULL ");
		ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory(URL_PREFIX.concat(host)+":"+port+URL_SUFFIX);
		PooledConnectionFactory poolFactory = new PooledConnectionFactory(connectionFactory);
		_pooledConnection=(PooledConnection) poolFactory.createConnection(username,password);  
		_pooledConnection.start();
		logger.info("[MQ]: "+host+":"+port +" CONNECTIONED");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
