package com.liangxunwang.unimanager.push.model;

import java.util.List;
import java.util.LinkedList;
import com.baidu.yun.core.annotation.JSonPath;
import com.baidu.yun.push.model.*;
import com.baidu.yun.push.model.TagInfo;

public class QueryTagsResponse extends com.baidu.yun.push.model.PushResponse {

	@JSonPath(path="response_params\\total_num")
	private int totalNum;
	
	@JSonPath(path="response_params\\result")
	private List<com.baidu.yun.push.model.TagInfo> tagsInfo = new LinkedList<com.baidu.yun.push.model.TagInfo> ();
	
	// get
	public int getTotalNum () {
		return totalNum;
	}
	public List<TagInfo> getTagsInfo () {
		return tagsInfo;
	}
}
