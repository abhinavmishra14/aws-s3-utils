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
package com.github.abhinavmishra14.aws.s3.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.StringUtils;
import com.github.abhinavmishra14.aws.s3.service.impl.AwsS3IamServiceImpl;
import com.github.abhinavmishra14.aws.util.AWSUtil;

/**
 * The Class AwsS3IamServiceTest.
 *
 * @author Abhinav kumar mishra
 */
public class AwsS3IamServiceTest{

    /** The aws s3 iam service. */
	private AwsS3IamService awsS3IamService;
	
	/** The Constant AWS_S3_BUCKET. */
	private static final String AWS_S3_BUCKET = "s3-publishing";
	
	/** The Constant SAMPLE_FILE_NAME. */
	private static final String SAMPLE_FILE_NAME = "TestPutObject.txt";
	
	
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
	 * Test get bucket permissions.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetBucketPermissions() throws Exception{
		//Create bucket for test 
		awsS3IamService.createBucket(AWS_S3_BUCKET);
		List<Grant> bucketAcl = awsS3IamService.getBucketPermissions(AWS_S3_BUCKET);	
		assertEquals(true, Permission.FullControl.equals(bucketAcl.get(0).getPermission()));
	}

	/**
	 * Test contains full control permission.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testContainsFullControlPermission() throws Exception{
		//Mock grant list to test full control permission
		List<Grant> grantList = new ArrayList<>();
		grantList.add(new Grant((Grantee) new CanonicalGrantee("155545dadd"), Permission.Read));
		grantList.add(new Grant((Grantee) new CanonicalGrantee("4545dadddda"), Permission.Write));
		grantList.add(new Grant((Grantee) new CanonicalGrantee("adad44555445"), Permission.FullControl));
		assertEquals(true, awsS3IamService.containsFullControlPermission(grantList));
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createBucket(java.lang.String)}.
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
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createBucket(java.lang.String,boolean)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateBucketWithPublicAccess() throws Exception {
		Bucket bucket = awsS3IamService.createBucket(AWS_S3_BUCKET,true);
		assertNotNull(bucket);
		assertEquals(AWS_S3_BUCKET, bucket.getName());
	}
	

    /**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObject(java.lang.String, java.lang.String, java.io.InputStream,boolean)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectStringStringInputStreamObjectMetadata()
			throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult pubObjRes = uploadObjectForTest(SAMPLE_FILE_NAME);
		assertNotNull(pubObjRes);
	}

	/**
	 * Test upload object and wait for completion.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUploadObjectAndWaitForCompletion() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		boolean isDone = awsS3IamService.uploadObjectAndListenProgress(AWS_S3_BUCKET,SAMPLE_FILE_NAME,inStream,false);
		assertEquals(true,isDone);
	}
	
	/**
	 * Test upload object with public access and wait for completion.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUploadObjectWithPublicAccessAndWaitForCompletion() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		boolean isDone = awsS3IamService.uploadObjectAndListenProgress(AWS_S3_BUCKET,SAMPLE_FILE_NAME,inStream,true);
		assertEquals(true,isDone);
	}
	
	/**
	 * Test upload file async.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUploadFileAsync() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		File tempFile = AWSUtil.createTempFileFromStream(inStream);
		Upload upload = awsS3IamService.uploadFileAsync(AWS_S3_BUCKET, SAMPLE_FILE_NAME, tempFile, false);
		upload.waitForUploadResult();
		assertEquals(true,upload.isDone());
	}
	
	/**
	 * Test upload file async with public access.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUploadFileAsyncWithPublicAccess() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		File tempFile = AWSUtil.createTempFileFromStream(inStream);
		Upload upload = awsS3IamService.uploadFileAsync(AWS_S3_BUCKET, SAMPLE_FILE_NAME, tempFile, true);
		upload.waitForUploadResult();
		assertEquals(true,upload.isDone());
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.impl.AwsS3IamServiceImpl#getAllBuckets()}.
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
	 * Test check full control permission.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckFullControlPermission() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		boolean hasPermissions = awsS3IamService.checkFullControlPermission(AWS_S3_BUCKET);
		assertEquals(true, hasPermissions);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#getObject(com.amazonaws.services.s3.model.GetObjectRequest)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObject() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest(SAMPLE_FILE_NAME);// upload for test
		final GetObjectRequest getObjRequest = new GetObjectRequest(AWS_S3_BUCKET, SAMPLE_FILE_NAME);
		S3Object s3Obj = awsS3IamService.getObject(getObjRequest);
		assertNotNull(s3Obj);
		assertEquals(SAMPLE_FILE_NAME, s3Obj.getKey());
	}

	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#getObject(java.lang.String, java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObjectAsInputStream() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest(SAMPLE_FILE_NAME);// upload for test
		InputStream inStream = awsS3IamService.getObject(AWS_S3_BUCKET, SAMPLE_FILE_NAME);
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
		uploadObjectForTest(SAMPLE_FILE_NAME);// upload for test
		ObjectMetadata objMetadata = awsS3IamService.downloadObject(AWS_S3_BUCKET, SAMPLE_FILE_NAME, "test.txt");
		assertNotNull(objMetadata);
		assertEquals(true, objMetadata.getContentLength()>0);// check if content available
	}
		
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createDirectory(java.lang.String, java.lang.String,boolean)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateDirectory() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult response = awsS3IamService.createDirectory(AWS_S3_BUCKET, "test",false);
		assertNotNull(response);
		//put content in this dir
		PutObjectResult pubObjRes = uploadObjectForTest(SAMPLE_FILE_NAME);
		assertNotNull(pubObjRes);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createDirectory(java.lang.String, java.lang.String,boolean)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateDirectoryWithPublicAccess() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult response = awsS3IamService.createDirectory(AWS_S3_BUCKET, "test",true);
		assertNotNull(response);
		//put content in this dir
		PutObjectResult pubObjRes = uploadObjectForTest(SAMPLE_FILE_NAME);
		assertNotNull(pubObjRes);
	}

	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#deleteObject(java.lang.String, java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected = AmazonS3Exception.class) 
	public void testDeleteObjectStringString() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest(SAMPLE_FILE_NAME);// upload for test
		awsS3IamService.deleteObject(AWS_S3_BUCKET, SAMPLE_FILE_NAME);
		final GetObjectRequest getObjRequest = new GetObjectRequest(AWS_S3_BUCKET, SAMPLE_FILE_NAME);
		awsS3IamService.getObject(getObjRequest); // should throw AmazonS3Exception
	}

	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#deleteObjects(java.lang.String,java.util.List)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDeleteObjects() throws Exception {
		List<KeyVersion> keys = new ArrayList<KeyVersion>();
		KeyVersion key1= new KeyVersion(SAMPLE_FILE_NAME);
		KeyVersion key2= new KeyVersion("TestPutObject2.txt");
		keys.add(key1);
		keys.add(key2);
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		DeleteObjectsResult delResp = awsS3IamService.deleteObjects(AWS_S3_BUCKET,keys);
		assertNotNull(delResp);
	}
	
	/**
	 * Test is bucket exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIsBucketExists()throws Exception {
		Bucket bucket = awsS3IamService.createBucket(AWS_S3_BUCKET);
		assertNotNull(bucket);
		assertEquals(AWS_S3_BUCKET, bucket.getName());
		boolean isBucketExist = awsS3IamService.isBucketExists(AWS_S3_BUCKET);
		assertEquals(true,isBucketExist);
	}
		
	/**
	 * Test generate object url as string.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGenerateObjectUrlAsString() throws IOException {
		String url = awsS3IamService.generateObjectUrlAsString(AWS_S3_BUCKET, SAMPLE_FILE_NAME);
		assertEquals(true,!StringUtils.isNullOrEmpty(url));
		assertEquals(true,url.contains(AWS_S3_BUCKET+".s3.amazonaws.com"));
		assertEquals(true,url.contains(SAMPLE_FILE_NAME));
	}
	
	/**
	 * Test generate object url.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGenerateObjectURL() throws IOException {
		URL url = awsS3IamService.generateObjectURL(AWS_S3_BUCKET, SAMPLE_FILE_NAME);
		assertEquals(true, url != null);
		assertEquals(true,url.getHost().contains(AWS_S3_BUCKET+".s3.amazonaws.com"));
		assertEquals(true,url.getPath().contains(SAMPLE_FILE_NAME));
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
		return awsS3IamService.uploadObject(AWS_S3_BUCKET,key,inStream,false);
	}
		
	/**
	 * Tear down.
	 */
	@After
	public void tearDown(){
		try {
			// Delete test buckets
			awsS3IamService.cleanAndDeleteBucket(AWS_S3_BUCKET);
		} catch (Exception excp) {
			System.out.println("TearDown: "+excp.getMessage());
		}
	}
}
