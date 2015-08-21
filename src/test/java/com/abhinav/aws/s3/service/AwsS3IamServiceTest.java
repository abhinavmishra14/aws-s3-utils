/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */

package com.abhinav.aws.s3.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.abhinav.aws.s3.service.impl.AwsS3IamServiceImpl;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

/**
 * The Class AwsS3IamServiceTest.
 *
 * @author Abhinav kumar mishra
 */
public class AwsS3IamServiceTest{

    /** The aws s3 iam service. */
    private AwsS3IamService awsS3IamService=null;
	
	/** The Constant AWS_S3_BUCKET. */
	private static final String AWS_S3_BUCKET = "s3-publishing";
	
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		
		/*For testing IAM services anywhere else use the parameterized
		  constructor call to create instance of AwsS3IamService.*/
		
		//awsS3IamService = new AwsS3IamServiceImpl("accessKey","secretKey");
		
		/* For testing IAM services on EC2 instance which is already mapped with
		  IAM role, use the default constructor call to create instance of
		  AwsS3IamService. */
		
		awsS3IamService = new AwsS3IamServiceImpl();  
	}

	/**
	 * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#createBucket(java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateBucket() throws Exception {
		Bucket bucket = awsS3IamService.createBucket(AWS_S3_BUCKET);
		assertNotNull(bucket);
		assertEquals(AWS_S3_BUCKET, bucket.getName());
	}
	

    /**
     * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#uploadObject(java.lang.String, java.lang.String, java.io.InputStream, com.amazonaws.services.s3.model.ObjectMetadata)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectStringStringInputStreamObjectMetadata()
			throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult pubObjRes = uploadObjectForTest("TestPutObject.txt");
		assertNotNull(pubObjRes);
	}

	
	/**
	 * Test method for {@link com.abhinav.aws.s3.service.impl.AwsS3IamServiceImpl#getAllBuckets()}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetAllBuckets() throws Exception {
		List<Bucket> buckets = awsS3IamService.getAllBuckets();
		assertNotNull(buckets);
		assertEquals(true, buckets.size()>0);
	}

	/**
	 * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#getObject(com.amazonaws.services.s3.model.GetObjectRequest)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObject() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest("TestPutObject.txt");// upload for test
		final GetObjectRequest getObjRequest = new GetObjectRequest(AWS_S3_BUCKET, "TestPutObject.txt");
		S3Object s3Obj = awsS3IamService.getObject(getObjRequest);
		assertNotNull(s3Obj);
		assertEquals("TestPutObject.txt", s3Obj.getKey());
	}

	/**
	 * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#getObject(String, String))}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObjectAsInputStream() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest("TestPutObject.txt");// upload for test
		InputStream inStream = awsS3IamService.getObject(AWS_S3_BUCKET, "TestPutObject.txt");
		assertNotNull(inStream);
		assertEquals(true, inStream.available()>0);// check if content available in stream
	}
	
	/**
	 * Test download object.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDownloadObject() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest("TestPutObject.txt");// upload for test
		ObjectMetadata objMetadata = awsS3IamService.downloadObject(AWS_S3_BUCKET, "TestPutObject.txt", "test.txt");
		assertNotNull(objMetadata);
		assertEquals(true, objMetadata.getContentLength()>0);// check if content available
	}
		
	/**
	 * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#createDirectory(com.amazonaws.services.s3.model.PutObjectRequest)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateDirectory() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult response = awsS3IamService.createDirectory(AWS_S3_BUCKET, "test");
		assertNotNull(response);
		//put content in this dir
		PutObjectResult pubObjRes = uploadObjectForTest("TestPutObject.txt");
		assertNotNull(pubObjRes);
	}

	/**
	 * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#deleteObject(java.lang.String, java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected = AmazonS3Exception.class) 
	public void testDeleteObjectStringString() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest("TestPutObject.txt");// upload for test
		awsS3IamService.deleteObject(AWS_S3_BUCKET, "TestPutObject.txt");
		final GetObjectRequest getObjRequest = new GetObjectRequest(AWS_S3_BUCKET, "TestPutObject.txt");
		awsS3IamService.getObject(getObjRequest); // should throw AmazonS3Exception
	}

	/**
	 * Test method for {@link com.abhinav.aws.s3.service.AwsS3IamService#deleteObjects(java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDeleteObjects() throws Exception {
		List<KeyVersion> keys = new ArrayList<KeyVersion>();
		KeyVersion key1= new KeyVersion("TestPutObject.txt");
		KeyVersion key2= new KeyVersion("TestPutObject2.txt");
		keys.add(key1);
		keys.add(key2);
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		DeleteObjectsResult delResp = awsS3IamService.deleteObjects(AWS_S3_BUCKET,keys);
		assertNotNull(delResp);
	}
	
	@Test
	public void isBucketExistsTest()throws Exception {
		boolean isBucketExist = awsS3IamService.isBucketExists(AWS_S3_BUCKET);
		assertEquals(true,isBucketExist);
	}
	
	/**
	 * Upload object for test.
	 *
	 * @param key the key
	 * @return the put object result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private PutObjectResult uploadObjectForTest(String key) throws IOException {
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		return awsS3IamService.uploadObject(AWS_S3_BUCKET,key,inStream);
	}
	
	
	/**
	 * Tear down.
	 */
	@After
	public void tearDown(){
		try {
			// Delete test buckets
			awsS3IamService.cleanAndDeleteBucket(AWS_S3_BUCKET);
		} catch (Exception ex) {
			System.out.println("[INFO] TearDown: "+ex.getMessage());
		}
	}
}
