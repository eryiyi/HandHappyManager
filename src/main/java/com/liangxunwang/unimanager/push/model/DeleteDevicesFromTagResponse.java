package com.liangxunwang.unimanager.push.model;

import java.util.List;
import java.util.LinkedList;
import com.baidu.yun.core.annotation.JSonPath;
import com.baidu.yun.push.model.*;
import com.baidu.yun.push.model.DeviceInfo;

public class DeleteDevicesFromTagResponse extends com.baidu.yun.push.model.PushResponse {

	@JSonPath(path="response_params\\result")
	private List<com.baidu.yun.push.model.DeviceInfo> devicesInfoAfterDel = new LinkedList<com.baidu.yun.push.model.DeviceInfo> ();
	
	// get
	public List<DeviceInfo> getDevicesInfoAfterDel () {
		return devicesInfoAfterDel;
	}
}
