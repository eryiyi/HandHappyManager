package com.liangxunwang.unimanager.push.model;

import com.baidu.yun.core.annotation.HttpParamKeyName;
import com.baidu.yun.core.annotation.R;
import com.baidu.yun.core.annotation.JSonPath;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.model.*;

public class PushBatchUniMsgResponse extends com.baidu.yun.push.model.PushResponse {

	@JSonPath(path="response_params\\msg_id")
    @HttpParamKeyName(name=BaiduPushConstants.MSG_ID, param=R.REQUIRE)
    private String msgId = null;
    
    @JSonPath(path="response_params\\send_time")
    @HttpParamKeyName(name=BaiduPushConstants.SEND_TIME, param=R.REQUIRE)
    private long sendTime;
    
    public String getMsgId () {
    	return msgId;
    }
    public long getSendTime () {
    	return sendTime;
    }
}
