package com.web.capture.controller;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * WebEvidAction Tester.
 *
 * @author <Andrew Lee>
 * @version 1.0
 * @since <pre>$DATE $TIME</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = SpringBootSampleApplication.class)
@WebAppConfiguration
public class WebEvidActionTest {

	@Before
	public void before() throws Exception {
	}

	@After
	public void after() throws Exception {
	}

	/**
	 * Method: doSnapshot(String webUrl, String userId)
	 */
	@Test
	public void testDoSnapshot() throws Exception {


		MockMvc mvc = MockMvcBuilders.standaloneSetup(new WebEvidAction()).build();
		RequestBuilder request = null;

		// 1、get查一下user列表，应该为空
//	request = get("/users/");
//	request = post("/users/")
//			.param("id", "1")
//			.param("name", "测试大师")
//			.param("age", "20");
//	mvc.perform(request)
//			.andExpect(content().string(equalTo("success")));
		String url = "http://localhost:8089/api/snapshot.action";
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("webUrl", "http://www.baidu.com");
		map.add("userId", "222");
		RestTemplate template = new RestTemplate();
		String result = template.postForObject(url, map, String.class);
		System.out.println(result);
//		assertNotNull(result);
	}

	/**
	 * Method: forceMkdir(File directory)
	 */
	@Test
	public void testForceMkdir() throws Exception {
//TODO: Test goes here... 
	}

	/**
	 * Method: generateFileName(String prefix, String suffix)
	 */
	@Test
	public void testGenerateFileName() throws Exception {
//TODO: Test goes here... 
	}

	/**
	 * Method: generatorStorageNo()
	 */
	@Test
	public void testGeneratorStorageNo() throws Exception {
//TODO: Test goes here... 
	}

	/**
	 * Method: dateToString(Date date, String pattern)
	 */
	@Test
	public void testDateToString() throws Exception {
//TODO: Test goes here... 
	}


	/**
	 * Method: getWebIp(String url)
	 */
	@Test
	public void testGetWebIp() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = WebEvidAction.getClass().getMethod("getWebIp", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
	}

} 
