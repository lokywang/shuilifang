package cn.e3mall.jedis;

import static org.junit.Assert.*;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testJedis()throws Exception{
		//创建一个jedis对象,需要制定服务端的ip及端口
		Jedis jedis = new Jedis("192.168.25.138",6379);
		//使用jedis对象操作数据库,每个redis命令对应一个方法
		jedis.set("test", "100");
		String string = jedis.get("test");
		//打印结果
		System.out.println(string );
		jedis.close();
	}
	
	@Test
	public void testJedisPool() throws Exception {
		//创建连接池
		JedisPool jedisPool = new JedisPool("192.168.25.138",6379);
		//获取jedis
		Jedis jedis = jedisPool.getResource();
		//使用jedis对象操作数据库,每个redis命令对应一个方法
		jedis.set("test1", "100");
		String string = jedis.get("test1");
		System.out.println(string);
		//关闭jedis 
		jedis.close();
		//关闭jedis连接池
		jedisPool.close();
		
	}
}
