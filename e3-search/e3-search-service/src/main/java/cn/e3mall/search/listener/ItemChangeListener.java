package cn.e3mall.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.impl.SearchItemServiceImpl;

/**
 * @author lokyw
 *接收Activemq发送的消息
 */
public class ItemChangeListener implements MessageListener{

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		//从message中获取内容
		TextMessage textMessage = (TextMessage) message;
		long itemId = 0 ;
		try {
			itemId = Long.parseLong(textMessage.getText());
			Thread.sleep(1000);
			//根据商品ID查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			// 3、使用SolrServer对象写入索引库。
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			// 5、向索引库中添加文档。
			solrServer.add(document);
			solrServer.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	
	
	
}
