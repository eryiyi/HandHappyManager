package com.liangxunwang.unimanager.push.model;

import java.util.LinkedList;
import java.util.List;
import com.baidu.yun.core.annotation.JSonPath;
import com.baidu.yun.push.model.*;
import com.baidu.yun.push.model.MsgStatInfo;

public class QueryStatisticMsgResponse extends com.baidu.yun.push.model.PushResponse {

	@JSonPath(path="response_params\\result")
	private List<com.baidu.yun.push.model.MsgStatInfo> msgStats = new LinkedList<com.baidu.yun.push.model.MsgStatInfo>();
	
	public List<MsgStatInfo> getMsgStats () {
		return msgStats;
	}
}
