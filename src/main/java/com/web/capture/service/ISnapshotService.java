package com.web.capture.service;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * @Author Andrew Lee
 * @CreateTime 2017/9/28 10:02
 */
public interface ISnapshotService {
	void doSnapshot(String webUrl, String userId,DeferredResult<String> deferredResult);
}
