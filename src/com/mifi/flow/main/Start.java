package com.mifi.flow.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("spring/context.xml");
	}
}
