package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
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

	/**
	    * @Name: addItem
	    * @Description: 添加商品信息
	    * @Author: lokyw
	    * @Version: V1.00 （版本号）
	    * @Create Date: 2017年6月4日下午8:31:56
	    * @Parameters: ItemServiceImpl
	 */
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	
	public E3Result addItem(TbItem item, String desc) {
		//1.生成商品id
		long itemId = IDUtils.genItemId();
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
		return E3Result.ok();
	}

}
