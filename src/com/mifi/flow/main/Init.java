package com.mifi.flow.main;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.mifi.flow.common.Dictionary;
import com.mifi.flow.common.FlowServer;


/**
 *	初始化系统上下文静态常量
 */
@Component
public class Init implements ApplicationContextAware , InitializingBean{
	
	Logger logger=Logger.getLogger(Init.class);
	
	ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> _map=context.getBeansWithAnnotation(FlowServer.class);
		for (Entry<String,Object> e:_map.entrySet()) {
			String name=e.getKey().replace("Impl", "");
			Class<?> _clazz=e.getValue().getClass();
			FlowServer _ms=_clazz.getAnnotation(FlowServer.class);
			if(null!=_ms)
				name=_ms.name();
			Dictionary.MIFI_SERVICES.put(name,e.getValue());
		}
		logger.info("["+Dictionary.MIFI_SERVICES.size()+"] server inited ");
		
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context=context;
	}

}
