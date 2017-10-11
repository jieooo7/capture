package com.web.capture.controller;

import com.web.capture.service.ISnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;


/**
 * 网页存证（部署到Windows服务器）
 *
 * @author lwh
 * @since 2014-6-12 下午4:30:44
 */
@Controller
public class WebEvidAction {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISnapshotService snapshotService;


	/**
	 * 网页截图
	 *
	 * @param webUrl 网址0
	 * @param userId 用户ID
	 */
	@RequestMapping("/api/snapshot.action")
	@ResponseBody
	public DeferredResult<String> snapshot(String webUrl, String userId) {
		DeferredResult<String> deferredResult = new DeferredResult<String>();
		snapshotService.doSnapshot(webUrl, userId,deferredResult);
		logger.info("请求线程>>>>>=="+Thread.currentThread().getName());
		return deferredResult;

	}



}
