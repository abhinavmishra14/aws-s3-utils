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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GrantAndPermission;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.utils.MultipartUtils;
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
		s3RESTService = new JetS3RESTServiceImpl(AWS_ACCESS_KEY, AWS_SECRET_KEY);
	}

	/**
	 * Test put object string.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testPutObjectString() throws Exception {
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		boolean putObj = s3RESTService.putObject(AWS_S3_BUCKET,JetS3RESTServiceTest.class.getResource(
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
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		boolean putObj = s3RESTService.putObject(AWS_S3_BUCKET,new File(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath()));
		assertEquals(true, putObj);		
	}
	
	/**
	 * Test get bucket acl.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetBucketAcl() throws Exception {
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		AccessControlList acl = s3RESTService.getBucketAcl(new S3Bucket(AWS_S3_BUCKET));
		GrantAndPermission[] grantAndPermissions = acl.getGrantAndPermissions();
		assertEquals(Permission.PERMISSION_FULL_CONTROL, grantAndPermissions[0].getPermission());
	}
	
	/**
	 * Test delete object.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected = org.jets3t.service.S3ServiceException.class) 
	public void testDeleteObject() throws Exception {
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		File objectKey = new File(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath());
		boolean putObj = s3RESTService.putObject(AWS_S3_BUCKET,objectKey);
		assertEquals(true, putObj);		
		s3RESTService.deleteObject(AWS_S3_BUCKET, "TestPutObject.txt");
		s3RESTService.getObject(AWS_S3_BUCKET, objectKey.getAbsolutePath());
	}
	
	/**
	 * Test upload object as multiparts.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUploadObjectAsMultiparts() throws Exception {
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		// Create a large (11 MB) file
        File file = File.createTempFile("testLargeFile", ".txt");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        int offset = 0;
        while (offset < 11 * 1024 * 1024) {
            bos.write((offset++ % 256));
        }
        bos.close();
        
        StorageObject objectKey = new StorageObject(file);
		s3RESTService.uploadObjectAsMultiparts(AWS_S3_BUCKET,objectKey ,
				MultipartUtils.MIN_PART_SIZE);
		S3Object s3Obj = s3RESTService.getObject(AWS_S3_BUCKET, objectKey.getKey());
		assertEquals(true, s3Obj!=null);
		if(file!=null){
			file.delete();//Delete when operation completes.
		}
	}
	
	/**
	 * Test get all buckets.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetAllBuckets() throws Exception{
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		List<S3Bucket> buckets = s3RESTService.getAllBuckets();
		assertEquals(false, buckets.isEmpty());
		assertEquals(true, buckets.size()>=1);
	}
	
	/**
	 * Test get object as stream.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetObjectAsStream() throws Exception{
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		File file = new File(JetS3RESTServiceTest.class.getResource(
				"/sample-file/TestPutObject.txt").getPath());
		S3Object objectKey = new S3Object(file);
		boolean putObj = s3RESTService.putObject(AWS_S3_BUCKET,objectKey);
		assertEquals(true, putObj);		
		InputStream s3ObjStream = s3RESTService.getObjectAsStream(AWS_S3_BUCKET, objectKey.getKey());
		assertEquals(true, s3ObjStream!=null);	
	}
	
	/**
	 * Test upload directory as multiparts.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUploadDirectoryAsMultiparts() throws Exception{
		s3RESTService.createBucket(AWS_S3_BUCKET);//create bucket for test
		//Keep two sample files within this directory
		final File folderPath = new File("C:\\Users\\abhinav\\uploadDir");
		s3RESTService.uploadDirectoryAsMultiparts(AWS_S3_BUCKET, folderPath);
		final List<S3Object> listOfObjects = s3RESTService.getAllObjects(AWS_S3_BUCKET);
		assertEquals(true, !listOfObjects.isEmpty());
		assertEquals(2, listOfObjects.size());
	}
	
	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
		s3RESTService.cleanAndDeleteBucket(AWS_S3_BUCKET);
	}
}
