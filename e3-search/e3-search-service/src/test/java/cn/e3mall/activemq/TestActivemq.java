package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActivemq {

	@Test
	public void testQueueConsumer() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//等待
		System.out.println("系统启动成功");
		System.in.read();
	}

}
