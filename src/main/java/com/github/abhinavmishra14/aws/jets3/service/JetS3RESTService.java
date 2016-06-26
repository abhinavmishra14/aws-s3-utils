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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.model.MultipleDeleteResult;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;

/**
 * The Class JetS3RESTService.<br/>
 * This class can be used to manually store and delete contents from the s3 bucket.
 *
 * @author Abhinav kumar mishra
 */
public interface JetS3RESTService {

	/**
	 * Put object.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 * @throws S3ServiceException the s3 service exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean putObject(final String bucketName, final String fileName) throws S3ServiceException, NoSuchAlgorithmException, IOException;

	/**
	 * Put object.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 * @throws S3ServiceException the s3 service exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean putObject(final String bucketName, final File fileName) throws S3ServiceException, NoSuchAlgorithmException, IOException;
	
	/**
	 * Put object.
	 *
	 * @param bucketName the bucket name
	 * @param s3Object the s3 object
	 * @return true, if successful
	 * @throws S3ServiceException the s3 service exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean putObject(final String bucketName, final S3Object s3Object) throws S3ServiceException, NoSuchAlgorithmException, IOException;

	
	/**
	 * Delete object.
	 *
	 * @param fileName the file name
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServiceException the service exception
	 */
	void deleteObject(final String bucketName, final String fileName) throws NoSuchAlgorithmException, IOException, ServiceException;
	
	/**
	 * Creates the bucket.
	 *
	 * @param bucketName the bucket name
	 * @return the s3 bucket
	 * @throws ServiceException the service exception
	 */
	S3Bucket createBucket(final String bucketName) throws ServiceException;
	
	/**
	 * Clean and delete bucket.
	 *
	 * @param bucketName the bucket name
	 * @throws ServiceException the service exception
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	void cleanAndDeleteBucket(final String bucketName) throws ServiceException, NoSuchAlgorithmException, IOException;
	
	/**
	 * Delete bucket.
	 *
	 * @param bucketName the bucket name
	 * @throws ServiceException the service exception
	 */
	void deleteBucket(final String bucketName) throws ServiceException;	
	
	/**
	 * Gets the all buckets.
	 *
	 * @return the all buckets
	 * @throws S3ServiceException the s3 service exception
	 */
	List<S3Bucket> getAllBuckets() throws S3ServiceException;
	
	/**
	 * Gets the object.
	 *
	 * @param bucketName the bucket name
	 * @param objectKey the object key
	 * @return the object
	 * @throws S3ServiceException the s3 service exception
	 */
	S3Object getObject(final String bucketName, final String objectKey) throws S3ServiceException;	
	
	/**
	 * Gets the all objects.
	 *
	 * @param bucketName the bucket name
	 * @return the all objects
	 * @throws S3ServiceException the s3 service exception
	 */
	List<S3Object> getAllObjects(final String bucketName) throws S3ServiceException;
	
	/**
	 * Delete objects.
	 *
	 * @param bucketName the bucket name
	 * @param objectKeys the object keys
	 * @return the multiple delete result
	 * @throws S3ServiceException the s3 service exception
	 */
	MultipleDeleteResult deleteObjects(final String bucketName, final String[] objectKeys) throws S3ServiceException;
	
	/**
	 * Gets the object acl.
	 *
	 * @param bucketName the bucket name
	 * @param objectKey the object key
	 * @return the object acl
	 * @throws S3ServiceException the s3 service exception
	 */
	AccessControlList getObjectAcl(final S3Bucket bucketName, final String objectKey) throws S3ServiceException;
	
	/**
	 * Gets the bucket acl.
	 *
	 * @param bucketName the bucket name
	 * @return the bucket acl
	 * @throws S3ServiceException the s3 service exception
	 */
	AccessControlList getBucketAcl(final S3Bucket bucketName) throws S3ServiceException;

	/**
	 * Gets the object as stream.
	 *
	 * @param bucketName the bucket name
	 * @param objectKey the object key
	 * @return the object as stream
	 * @throws S3ServiceException the s3 service exception
	 * @throws ServiceException 
	 */
	InputStream getObjectAsStream(final String bucketName, final String objectKey)
			throws S3ServiceException, ServiceException;

	/**
	 * Upload object as multiparts.
	 *
	 * @param bucketName the bucket name
	 * @param object the object
	 * @param maxPartSize the max part size
	 * @throws ServiceException the service exception
	 */
	void uploadObjectAsMultiparts(final String bucketName, final StorageObject object, final long maxPartSize)
			throws ServiceException;

	/**
	 * Upload objects as multiparts.
	 *
	 * @param bucketName the bucket name
	 * @param objects the objects
	 * @throws ServiceException the service exception
	 * @throws Exception the exception
	 */
	void uploadObjectsAsMultiparts(final String bucketName, final List<StorageObject> objects)
			throws ServiceException, Exception;
	
	/**
	 * Upload directory as multiparts.
	 *
	 * @param bucketName the bucket name
	 * @param folderPath the folder path
	 * @throws ServiceException the service exception
	 * @throws Exception the exception
	 */
	void uploadDirectoryAsMultiparts(final String bucketName, final File folderPath) throws ServiceException, Exception;

	/**
	 * Sets the object acl.<br/>
	 * AccessControlList could be: REST_CANNED_AUTHENTICATED_READ,
	 * REST_CANNED_PUBLIC_READ, REST_CANNED_PRIVATE and
	 * REST_CANNED_PUBLIC_READ_WRITE
	 *
	 * @param bucketName the bucket name
	 * @param objectKey  the object key
	 * @param acl the acl
	 * @throws ServiceException the service exception
	 */
	void setObjectAcl(final String bucketName, final String objectKey, final AccessControlList acl)
			throws ServiceException;

	/**
	 * Sets the bucket acl.<br/>
	 * AccessControlList could be: REST_CANNED_AUTHENTICATED_READ,
	 * REST_CANNED_PUBLIC_READ, REST_CANNED_PRIVATE and
	 * REST_CANNED_PUBLIC_READ_WRITE
	 *
	 * @param bucketName the bucket name
	 * @param acl the acl
	 * @throws ServiceException the service exception
	 */
	void setBucketAcl(final String bucketName, final AccessControlList acl) throws ServiceException;
}
