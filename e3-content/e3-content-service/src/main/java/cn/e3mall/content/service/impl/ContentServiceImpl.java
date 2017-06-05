package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper contentMapper;
	public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//2.根据分类ID查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria= example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		//区分页结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		//创建一个easyuidategridResult对象
		EasyUIDataGridResult dataGridResult = new EasyUIDataGridResult();
		// 将信息封装进去
		dataGridResult.setTotal(total);
		dataGridResult.setRows(list);
		return dataGridResult;
	}
	
	/**
	    * @Name: addTbContent
	    * @Description: 内容管理添加
	    * @Author: lokyw
	    * @Version: V1.00 （版本号）
	    * @Create Date: 2017年6月5日上午11:38:58
	    * @Parameters: ContentServiceImpl
	 */
	public E3Result addTbContent(TbContent content) {
		Date date = new Date();
		
		content.setCreated(date);
		content.setUpdated(date);
		contentMapper.insert(content);
		return E3Result.ok();
	}

	
}
