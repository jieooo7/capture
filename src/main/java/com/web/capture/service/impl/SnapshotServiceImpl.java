package com.web.capture.service.impl;

import com.web.capture.controller.WebEvidAction;
import com.web.capture.service.ISnapshotService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @Author Andrew Lee
 * @CreateTime 2017/9/28 10:03
 */
@Service
//@Transactional
public class SnapshotServiceImpl implements ISnapshotService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String UTF8 = "UTF-8";

	@Value("${capture.path}")
	private String basePath;
	@Value("${capture.baseUrl}")
	private String baseUrl;
	@Value("${capture.imageWidth}")
	private String imageWidth;
	@Value("${capture.delay}")
	private String delay;
	@Value("${capture.timeout}")
	private String timeout;
	@Value("${capture.renderWebpageTime}")
	private String renderTime;

	private final static ExecutorService executor;
	private final static CompletionService<String> completionService;

	static {
		executor = Executors.newFixedThreadPool(20);
		completionService = new ExecutorCompletionService<String>(executor);
	}


	@Override
	public String doSnapshot(String webUrl, String userId) {

		Callable<String> myCallable = new Callable<String>(){

			@Override
			public String call() throws Exception {
				String res = doSnapshotActual(webUrl,userId);
				logger.info(Thread.currentThread().getName());
				return res;
			}
		};


		Future<String> future = executor.submit(myCallable);
//		completionService.submit(myCallable);
		String result="2";

		try {
//			while (!future.isDone()){
//				Thread.currentThread().sleep(5000);
//			}
			result=future.get();

//			result=completionService.poll().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		logger.info("处理线程>>>>>"+Thread.currentThread().getName());
		return result;
	}


	public String doSnapshotActual(String webUrl, String userId) {
		logger.info("starttime:"+new Date().getTime());
		logger.info("webUrl=" + webUrl);
		String webUrlOri;
		try {
			webUrlOri = URLDecoder.decode(webUrl, UTF8);
//			webUrlOri = new String(Base64.decodeBase64(URLDecoder.decode(webUrl, UTF8)), UTF8);
			logger.info("webUrlOri=" + webUrlOri);
			String webIp = getWebIp(webUrlOri);
			logger.info("webIp=" + webIp);
		} catch (Exception e) {
			logger.error("网址无法访问", e);
			return "1";
		}
		try {
			// 网页存证文件夹
			String relativePath = "WEB-INF/upload/client/" + userId + '/' + generateFileName("WY", "jpg");
			String filePath = basePath + relativePath;
			File webEvidFile = new File(filePath);
			forceMkdir(webEvidFile.getParentFile());
			// filePath 生成快照的图片文件名字和配置图片放置位置
			logger.info("filePath=" + filePath);
			StringBuffer command = new StringBuffer();
			command.append(baseUrl).append("IEWPCapt.exe");
			command.append(" -url=").append(webUrlOri);
			command.append(" -image=").append(filePath);
			command.append(" -imageWidth=").append(imageWidth);
			command.append(" -delay=").append(delay);
			command.append(" -timeout=").append(timeout);
			command.append(" -show");
			command.append(" -renderWebpageTime=").append(renderTime);
			command.append(" -maxtimeout=").append("9000000");
			logger.info(command.toString());
			// 执行IECapt程序
			Process process = Runtime.getRuntime().exec(command.toString());
			try {
				// 获取进程的标准输入流
				final InputStream is1 = process.getInputStream();
				// 获取进城的错误流
				final InputStream is2 = process.getErrorStream();
				// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
				new Thread() {
					public void run() {
						BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
						try {
							String line1 = null;
							while ((line1 = br1.readLine()) != null) {
								if (line1 != null) {
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								is1.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
				new Thread() {
					public void run() {
						BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
						try {
							String line2 = null;
							while ((line2 = br2.readLine()) != null) {
								if (line2 != null) {
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								is2.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();

				process.waitFor();
				logger.info("webUrl=" + webUrlOri + ";iecapt截取返回值:" + process.exitValue());
//				process.destroy();
				logger.info("endtime:"+new Date().getTime());
			} catch (Exception e) {
				try {
					process.getErrorStream().close();
					process.getInputStream().close();
					process.getOutputStream().close();
				} catch (Exception ee) {
					logger.error(e.getMessage(), e);
					return "2";
				}
			}
			if (!webEvidFile.exists()) {
				// 文件没有生成
				logger.info("webUrl=" + webUrlOri + ";Image file does not exist. 网页快照文件未生成");
				return "2";
			}
			return relativePath;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "2";
		}
	}

	/**
	 * 获取网址对应的IP
	 */
	private String getWebIp(String url) throws MalformedURLException, UnknownHostException {
		URL u = new URL(url);
		InetAddress address = InetAddress.getByName(u.getHost());
		return address.getHostAddress();
	}


	private static void forceMkdir(File directory) throws IOException {
		String message;
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else if (!directory.mkdirs() && !directory.isDirectory()) {
			message = "Unable to create directory " + directory;
			throw new IOException(message);
		}

	}

	private String generateFileName(String prefix, String suffix)
	{
		StringBuilder result = new StringBuilder();
		if (StringUtils.isNotBlank(prefix))
		{
			result.append(prefix);
		}
		result.append(generatorStorageNo());
		if (StringUtils.isNotBlank(suffix))
		{
			result.append('.').append(suffix);
		}
		return result.toString();
	}


	private String generatorStorageNo()
	{
		String date = dateToString(new Date(), "yyyyMMddHHmmss");
		String rand = String.valueOf(RandomUtils.nextInt(999999));
		return date + StringUtils.leftPad(rand, 6, '0');
	}



	private String dateToString(Date date, String pattern) {
		if (date == null) {
			return "";
		} else {
			if (pattern == null) {
				pattern = "yyyy-MM-dd";
			}

			String dateString = "";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);

			try {
				dateString = sdf.format(date);
			} catch (Exception var5) {
				logger.error(var5.toString(), var5);
			}

			return dateString;
		}
	}

	@PreDestroy
	public void destroy(){
		logger.info("stopping");
		executor.shutdownNow();
	}

	@PostConstruct
	public void init() throws Exception {
		logger.info("init start");
	}

}
