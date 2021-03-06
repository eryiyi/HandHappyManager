package com.liangxunwang.unimanager.push.model;

import com.baidu.yun.core.annotation.HttpParamKeyName;
import com.baidu.yun.core.annotation.R;
import com.baidu.yun.core.annotation.RangeRestrict;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.model.*;

public class QueryStatisticTopicRequest extends com.baidu.yun.push.model.PushRequest {

	@HttpParamKeyName(name=BaiduPushConstants.TOPIC_ID, param=R.REQUIRE)
	@RangeRestrict(minLength=1, maxLength=128)
	private String topicId = null;
	
	// get
	public String getTopicId () {
		return topicId;
	}
	// set
	public void setTopicId (String topicId) {
		this.topicId = topicId;
	}
	// add
	public QueryStatisticTopicRequest addTopicId (String topicId) {
		this.topicId = topicId;
		return this;
	}
    public QueryStatisticTopicRequest addDeviceType (Integer deviceType) {
    	this.deviceType = deviceType;
    	return this;
    }
	public QueryStatisticTopicRequest addExpires(Long requestTimeOut) {
		this.expires = requestTimeOut;
		return this;
	}
}
