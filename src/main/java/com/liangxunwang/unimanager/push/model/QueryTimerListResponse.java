package com.liangxunwang.unimanager.push.model;

import java.util.List;
import java.util.LinkedList;
import com.baidu.yun.core.annotation.JSonPath;
import com.baidu.yun.push.model.*;
import com.baidu.yun.push.model.TimerResultInfo;

public class QueryTimerListResponse extends com.baidu.yun.push.model.PushResponse {

	@JSonPath(path="response_params\\total_num")
	private int totalNum;
	
	@JSonPath(path="response_params\\result")
	private List<com.baidu.yun.push.model.TimerResultInfo> timerResultInfos = new LinkedList<com.baidu.yun.push.model.TimerResultInfo> ();
	
	// get
	public int getTotalNum () {
		return totalNum;
	}
	public List<TimerResultInfo> getTimerResultInfos () {
		return timerResultInfos;
	}
}
