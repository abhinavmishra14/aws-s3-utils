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
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.StringUtils;
import com.github.abhinavmishra14.aws.s3.service.impl.AwsS3IamServiceImpl;
import com.github.abhinavmishra14.aws.util.AWSUtil;
import com.github.abhinavmishra14.aws.util.AWSUtilConstants;

/**
 * The Class AwsS3IamServiceTest.
 *
 * @author Abhinav kumar mishra
 */
public class AwsS3IamServiceTest{

    /** The aws s3 iam service. */
	private AwsS3IamService awsS3IamService;
	
	/** The Constant AWS_S3_BUCKET. */
	private static final String AWS_S3_BUCKET = "s3-publishing-test";	
	
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
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#getBucketPermissions(java.lang.String)}.
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
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createBucket(java.lang.String,com.amazonaws.services.s3.model.CannedAccessControlList)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateBucketWithCannedACL() throws Exception {
		Bucket bucket = awsS3IamService.createBucket(AWS_S3_BUCKET,CannedAccessControlList.PublicRead);
		assertNotNull(bucket);
		assertEquals(AWS_S3_BUCKET, bucket.getName());
	}
	

    /**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObject(java.lang.String, java.lang.String, java.io.InputStream)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObject() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult pubObjRes = uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);
		assertNotNull(pubObjRes);
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObject(java.lang.String, java.lang.String, java.io.InputStream,boolean)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectWithPublicAccess() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		PutObjectResult pubObjRes =  awsS3IamService.uploadObject(AWS_S3_BUCKET,AWSUtilConstants.SAMPLE_FILE_NAME,inStream,true);
		assertNotNull(pubObjRes);
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObject(java.lang.String, java.lang.String, java.io.InputStream,com.amazonaws.services.s3.model.CannedAccessControlList)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectWithCannedACL() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class.getResourceAsStream("/sample-file/TestPutObject.txt");
		PutObjectResult pubObjRes = awsS3IamService.uploadObject(AWS_S3_BUCKET,AWSUtilConstants.SAMPLE_FILE_NAME,inStream,CannedAccessControlList.PublicRead);
		assertNotNull(pubObjRes);
	}

	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObjectAndListenProgress(java.lang.String, java.lang.String, java.io.InputStream, com.amazonaws.services.s3.model.CannedAccessControlList)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectWithACLAndWaitForCompletion() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class.getResourceAsStream("/sample-file/TestPutObject.txt");
		boolean isDone = awsS3IamService.uploadObjectAndListenProgress(AWS_S3_BUCKET,AWSUtilConstants.SAMPLE_FILE_NAME,inStream,CannedAccessControlList.PublicRead);
		assertEquals(true,isDone);
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObjectAndListenProgress(java.lang.String, java.lang.String, java.io.InputStream)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectAndWaitForCompletion() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class.getResourceAsStream("/sample-file/TestPutObject.txt");
		boolean isDone = awsS3IamService.uploadObjectAndListenProgress(AWS_S3_BUCKET,AWSUtilConstants.SAMPLE_FILE_NAME,inStream);
		assertEquals(true,isDone);
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadObjectAndListenProgress(java.lang.String, java.lang.String, java.io.InputStream,boolean)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadObjectWithPublicAccessAndWaitForCompletion() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		boolean isDone = awsS3IamService.uploadObjectAndListenProgress(AWS_S3_BUCKET,AWSUtilConstants.SAMPLE_FILE_NAME,inStream,true);
		assertEquals(true,isDone);
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadFileAsync(java.lang.String, java.lang.String, java.io.File)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadFileAsync() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		File tempFile = AWSUtil.createTempFileFromStream(inStream);
		Upload upload = awsS3IamService.uploadFileAsync(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME, tempFile);
		upload.waitForUploadResult();
		assertEquals(true,upload.isDone());
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadFileAsync(java.lang.String, java.lang.String, java.io.File,boolean)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadFileWithPublicAccessAsync() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		File tempFile = AWSUtil.createTempFileFromStream(inStream);
		Upload upload = awsS3IamService.uploadFileAsync(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME, tempFile,true);
		upload.waitForUploadResult();
		assertEquals(true,upload.isDone());
	}
	
	/**
     * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#uploadFileAsync(java.lang.String, java.lang.String, java.io.File,com.amazonaws.services.s3.model.CannedAccessControlList)}.
     *
     * @throws Exception the exception
     */
	@Test
	public void testUploadFileWithCannedACLAsync() throws Exception{
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		InputStream inStream = AwsS3IamServiceTest.class
				.getResourceAsStream("/sample-file/TestPutObject.txt");
		File tempFile = AWSUtil.createTempFileFromStream(inStream);
		Upload upload = awsS3IamService.uploadFileAsync(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME, tempFile,CannedAccessControlList.PublicRead);
		upload.waitForUploadResult();
		assertEquals(true,upload.isDone());
	}
		
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#getAllBuckets()}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetAllBuckets() throws Exception {
		List<Bucket> buckets = awsS3IamService.getAllBuckets();
		System.out.println(buckets);
		assertNotNull(buckets);
		assertEquals(true, buckets.size()>0);
	}

	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#hasFullControlPermission(java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHasFullControlPermission() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		boolean hasPermissions = awsS3IamService.hasFullControlPermission(AWS_S3_BUCKET);
		assertEquals(true, hasPermissions);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#checkBucketPermission(java.lang.String,com.amazonaws.services.s3.model.Permission)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckBucketPermission() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		boolean checkPermission = awsS3IamService.checkBucketPermission(AWS_S3_BUCKET,Permission.FullControl);
		assertEquals(true, checkPermission);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#hasWritePermissionOnBucket(java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHasWritePermissionOnBucket() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		boolean checkPermission = awsS3IamService.hasWritePermissionOnBucket(AWS_S3_BUCKET);
		assertEquals(true, checkPermission);
	}
		
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#checkObjectPermission(java.lang.String,java.lang.String,com.amazonaws.services.s3.model.Permission)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckObjectPermission() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);// upload for test
		boolean checkPermission = awsS3IamService.checkObjectPermission(AWS_S3_BUCKET,
				AWSUtilConstants.SAMPLE_FILE_NAME, Permission.FullControl);
		assertEquals(true, checkPermission);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#getObject(com.amazonaws.services.s3.model.GetObjectRequest)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObject() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);// upload for test
		final GetObjectRequest getObjRequest = new GetObjectRequest(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME);
		S3Object s3Obj = awsS3IamService.getObject(getObjRequest);
		assertNotNull(s3Obj);
		assertEquals(AWSUtilConstants.SAMPLE_FILE_NAME, s3Obj.getKey());
	}

	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#getObject(java.lang.String, java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObjectAsInputStream() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test 
		uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);// upload for test
		InputStream inStream = awsS3IamService.getObject(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME);
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
		uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);// upload for test
		ObjectMetadata objMetadata = awsS3IamService.downloadObject(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME, "test.txt");
		assertNotNull(objMetadata);
		assertEquals(true, objMetadata.getContentLength()>0);// check if content available
	}
		
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createDirectory(java.lang.String, java.lang.String)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateDirectory() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult response = awsS3IamService.createDirectory(AWS_S3_BUCKET, "test");
		assertNotNull(response);
		//put content in this dir
		PutObjectResult pubObjRes = uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);
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
		PutObjectResult pubObjRes = uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);
		assertNotNull(pubObjRes);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#createDirectory(java.lang.String, java.lang.String,com.amazonaws.services.s3.model.CannedAccessControlList)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateDirectoryWithCannedACL() throws Exception {
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		PutObjectResult response = awsS3IamService.createDirectory(AWS_S3_BUCKET, "test",CannedAccessControlList.PublicRead);
		assertNotNull(response);
		//put content in this dir
		PutObjectResult pubObjRes = uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);
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
		uploadObjectForTest(AWSUtilConstants.SAMPLE_FILE_NAME);// upload for test
		awsS3IamService.deleteObject(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME);
		final GetObjectRequest getObjRequest = new GetObjectRequest(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME);
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
		KeyVersion key1= new KeyVersion(AWSUtilConstants.SAMPLE_FILE_NAME);
		KeyVersion key2= new KeyVersion("TestPutObject2.txt");
		keys.add(key1);
		keys.add(key2);
		awsS3IamService.createBucket(AWS_S3_BUCKET);//create bucket for test
		DeleteObjectsResult delResp = awsS3IamService.deleteObjects(AWS_S3_BUCKET,keys);
		assertNotNull(delResp);
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#isBucketExists(java.lang.String)}.
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
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#generateObjectUrlAsString(java.lang.String,java.lang.String)}.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGenerateObjectUrlAsString() throws IOException {
		String url = awsS3IamService.generateObjectUrlAsString(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME);
		assertEquals(true,!StringUtils.isNullOrEmpty(url));
		assertEquals(true,url.contains(AWS_S3_BUCKET+".s3.amazonaws.com"));
		assertEquals(true,url.contains(AWSUtilConstants.SAMPLE_FILE_NAME));
	}
	
	/**
	 * Test method for {@link com.github.abhinavmishra14.aws.s3.service.AwsS3IamService#generateObjectURL(java.lang.String,java.lang.String)}.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGenerateObjectURL() throws IOException {
		URL url = awsS3IamService.generateObjectURL(AWS_S3_BUCKET, AWSUtilConstants.SAMPLE_FILE_NAME);
		assertEquals(true, url != null);
		assertEquals(true,url.getHost().contains(AWS_S3_BUCKET+".s3.amazonaws.com"));
		assertEquals(true,url.getPath().contains(AWSUtilConstants.SAMPLE_FILE_NAME));
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
		} catch (Exception excp) {
			System.out.println("TearDown: "+excp.getMessage());
		}
	}
}
