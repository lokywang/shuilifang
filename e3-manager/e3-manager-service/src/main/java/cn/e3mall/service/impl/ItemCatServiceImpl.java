package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper catMapper;
	public List<EasyUITreeNode> getCatList(long parentId) {
		//设置查询条件
		TbItemCatExample itemCatExample = new TbItemCatExample();
		 Criteria criteria = itemCatExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询,
		List<TbItemCat> list = catMapper.selectByExample(itemCatExample);
		//转换成easyUITreeNode的列表
		List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
		for(TbItemCat tbItemCat : list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			node.setText(tbItemCat.getName());
			//添加列表
			resultList.add(node);
		}
 		return resultList;
	}

}
