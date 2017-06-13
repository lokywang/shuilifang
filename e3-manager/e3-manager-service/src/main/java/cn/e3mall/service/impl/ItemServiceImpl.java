package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import redis.clients.jedis.Jedis;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;
	
	public TbItem getTbItemById(long itemId) {
		try {
			//查询缓存
			String json = jedisClient.get("ITEM_INFO" + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				//把json转换为java对象
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
	
			try {
				//把数据保存到缓存
				jedisClient.set("ITEM_INFO" + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
				//设置缓存的有效期
				jedisClient.expire("ITEM_INFO" + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}

		return tbItem;
	}

	/**
	    * @Name: getItemList
	    * @Description: 设置分页查询信息
	    * @Author: lokyw
	    * @Version: V1.00 （版本号）
	    * @Create Date: 2017年6月1日下午4:21:27
	    * @Parameters: ItemServiceImpl
	 */
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//取查询结果
		PageInfo<TbItem> info = new PageInfo<TbItem>(list);
		//返回结果
		EasyUIDataGridResult dataGridResult = new EasyUIDataGridResult();
		dataGridResult.setTotal((int)info.getTotal());
		dataGridResult.setRows(list);
		return dataGridResult;
	}

	/**
	    * @Name: addItem
	    * @Description: 添加商品信息
	    * @Author: lokyw
	    * @Version: V1.00 （版本号）
	    * @Create Date: 2017年6月4日下午8:31:56
	    * @Parameters: ItemServiceImpl
	 */
	

	
	public E3Result addItem(TbItem item, String desc) {
		//1.生成商品id
		final long itemId = IDUtils.genItemId();
		//2.补全商品信息
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		itemMapper.insert(item);
		//3.创建itemdesc对象
		TbItemDesc itDesc = new TbItemDesc();
		//4.补全信息
		itDesc.setItemId(itemId);
		itDesc.setCreated(date);
		itDesc.setUpdated(date);
		itDesc.setItemDesc(desc);	
		descMapper.insert(itDesc);
		//发送一个商品添加
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		// 7、e3Result.ok()
		return E3Result.ok();
	}

	public TbItemDesc getItemDescById(long itemId) {
		try {
			//查询缓存
			String json = jedisClient.get("ITEM_INFO" + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				//把json转换为java对象
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		try {
			//把数据保存到缓存
			jedisClient.set("ITEM_INFO" + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			//设置缓存的有效期
			jedisClient.expire("ITEM_INFO" + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}


}
