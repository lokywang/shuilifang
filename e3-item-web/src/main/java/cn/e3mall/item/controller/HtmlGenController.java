package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@RequestMapping("/htmlgen")
	@ResponseBody
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfig.getConfiguration();
		//使用configuration对象获得temolate对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建数据集
		Map dataModel =new HashMap();
		dataModel.put("hello", "1000");
		//创建输出文件writer对象
		Writer out  = new FileWriter(new File("H:/temp/hel.html"));
		//调用模板对象生成process生成文件
		template.process(dataModel, out);
		//关闭流
		out.close();
		return "loky";
		
	}
}
