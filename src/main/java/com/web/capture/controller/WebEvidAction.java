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
//		WebAsyncTask
		DeferredResult<String> deferredResult = new DeferredResult<String>();
		deferredResult.setResult(snapshotService.doSnapshot(webUrl, userId));
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				deferredResult.setResult(snapshotService.doSnapshot(webUrl,userId));
//			}
//		}).start();
		logger.info("请求线程>>>>>"+Thread.currentThread().getName());
		return deferredResult;

	}

	@RequestMapping("/api/snapshot")
	@ResponseBody
	public WebAsyncTask snapshotTest(String webUrl, String userId) {
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				return snapshotService.doSnapshot(webUrl, userId);
			}
		};

		logger.info("请求线程>>>>>"+Thread.currentThread().getName());
		return new WebAsyncTask(callable);

	}


}
