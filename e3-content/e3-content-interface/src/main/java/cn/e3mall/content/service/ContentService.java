package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	EasyUIDataGridResult getContentList(long categoryId,int page,int rows);
	E3Result addTbContent(TbContent content);
	List<TbContent> getContentListByCid(long categoryId);
}
