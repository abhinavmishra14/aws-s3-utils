/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */
package com.abhinav.aws.jets3.service;

import static org.junit.Assert.*;

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

	private JetS3RESTService s3RESTService=null;
	private static final String AWS_S3_BUCKET = "abhinavmishra";
	private static final String AWS_ACCESS_KEY = "xxxx";
	private static final String AWS_SECRET_KEY = "xxxx";

	@Before
	public void setUp() throws Exception {
		s3RESTService = new JetS3RESTServiceImpl(AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_S3_BUCKET);
	}

	@Test
	public void testPutObjectString() throws Exception {
		boolean putObj = s3RESTService.putObject(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath());
		assertEquals(true, putObj);		
	}

	@Test
	public void testPutObjectFile() throws Exception {
		boolean putObj = s3RESTService.putObject(new File(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath()));
		assertEquals(true, putObj);		
	}

	@After
	public void tearDown() throws Exception {
		s3RESTService.deleteObject(JetS3RESTServiceTest.class.getResource("/sample-file/TestPutObject.txt").getPath());	
	}
}
