package cn.e3mall.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	@Value("${htmlGenPath}")
	private String htmlGenPath;
	
	
	@Override
	public void onMessage(Message message) {

		try {
			//2.从消息中获取id
			TextMessage textMessage= (TextMessage) message;
			long itemId = new Long(textMessage.getText());
			//3.根据id获取商品信息
			Thread.sleep(1000);
			TbItem tbItem = itemService.getTbItemById(itemId);
			Item item = new Item(tbItem);
			//4获取商品描述
			
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			//5.生成静态页面输出的目录,需要freemarker模板
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个数据集
			Map dataModel = new HashMap<>();
			dataModel.put("item", item);
			dataModel.put("itemDesc", itemDesc);
			Writer out = new FileWriter(htmlGenPath+itemId+".html");
			//生成文件
			template.process(dataModel, out);
			//关闭流
			out.close();
			//9.配置ngix静态页面
		} catch ( Exception e) {
			e.printStackTrace();
		}
		
				
	}

}
