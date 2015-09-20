/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */
package com.abhinav.aws.jets3.service;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.abhinav.aws.jets3.service.impl.JetS3RESTServiceImpl;

/**
 * The Class JetS3RESTServiceTest.
 * 
 * @author Abhinav kumar mishra
 */
public class JetS3RESTServiceTest {

	/** The s3 rest service. */
	private JetS3RESTService s3RESTService=null;
	
	/** The Constant AWS_S3_BUCKET. */
	private static final String AWS_S3_BUCKET = "s3-publishing";
	
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
