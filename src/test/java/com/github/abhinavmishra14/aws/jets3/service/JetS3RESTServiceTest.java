/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.abhinavmishra14.aws.jets3.service;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.abhinavmishra14.aws.jets3.service.impl.JetS3RESTServiceImpl;

/**
 * The Class JetS3RESTServiceTest.
 * 
 * @author Abhinav kumar mishra
 */
public class JetS3RESTServiceTest {

	/** The s3 rest service. */
	private JetS3RESTService s3RESTService=null;
	
	/** The Constant AWS_S3_BUCKET. */
	private static final String AWS_S3_BUCKET = "s3-publishing-test";
	
	/** The Constant AWS_ACCESS_KEY. */
	private static final String AWS_ACCESS_KEY = "accessKey";
	
	/** The Constant AWS_SECRET_KEY. */
	private static final String AWS_SECRET_KEY = "secretKey";

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		s3RESTService = new JetS3RESTServiceImpl(AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_S3_BUCKET);
	}

	/**
	 * Test put object string.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testPutObjectString() throws Exception {
		boolean putObj = s3RESTService.putObject(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath());
		assertEquals(true, putObj);		
	}

	/**
	 * Test put object file.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testPutObjectFile() throws Exception {
		boolean putObj = s3RESTService.putObject(new File(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath()));
		assertEquals(true, putObj);		
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
		s3RESTService.deleteObject(JetS3RESTServiceTest.class.getResource("/sample-file/TestPutObject.txt").getPath());	
	}
}
