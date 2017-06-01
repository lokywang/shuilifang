package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	
	public TbItem getTbItemById(long itemId) {
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
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

}
