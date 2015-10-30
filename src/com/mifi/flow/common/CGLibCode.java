package com.mifi.flow.common;

import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import com.mifi.flow.entity.ResponceInfo;

public class CGLibCode {
	
	public ResponceInfo execute(Object targetObject,String methodName,Class<?>[] parameterTypes, Object[] parameters){
		try {
			FastClass serviceFastClass = FastClass.create(targetObject.getClass());
			FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
			return (ResponceInfo)serviceFastMethod.invoke(targetObject, parameters);
		} catch (Exception e) {
			return null ;
		}
	}

}
