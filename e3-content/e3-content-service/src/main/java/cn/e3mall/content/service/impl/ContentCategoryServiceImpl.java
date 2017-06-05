package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;


@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	
	@Autowired
	private TbContentCategoryMapper categoryMapper;
	
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		///得到List<TbContentCategory>
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		
		//把列表转换成List<EasyUITreeNode>ub
		List<EasyUITreeNode> resultList = new ArrayList<>();
		
		for (TbContentCategory TbContentCategory : list) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
		}
		return null;
	}

}
