/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */
package com.abhinav.aws.s3.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

/**
 * The Interface AwsS3IamService.<br/>
 * This service class will use the IAM Roles to perform the operations on S3
 * buckets.<br/>
 * SDK Location:
 * http://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-core/1.10.2
 * 
 * @author Abhinav kumar mishra
 */
public interface AwsS3IamService {

	/**
	 * Gets the all buckets.
	 *
	 * @return the all buckets
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	List<Bucket> getAllBuckets() throws AmazonClientException,
			AmazonServiceException;

	/**
	 * Creates the bucket.
	 *
	 * @param bucketName the bucket name
	 * @return the bucket
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	Bucket createBucket(final String bucketName) throws AmazonClientException,
			AmazonServiceException;

	/**
	 * Delete bucket.
	 *
	 * @param bucketName the bucket name
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	void deleteBucket(final String bucketName) throws AmazonClientException,
			AmazonServiceException;
	
	/**
	 * Clean and delete bucket.
	 *
	 * @param bucketName the bucket name
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	void cleanAndDeleteBucket(final String bucketName) throws AmazonClientException,
			AmazonServiceException;

	/**
	 * Upload object.
	 *
	 * @param putObjectRequest the put object request
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	PutObjectResult uploadObject(final PutObjectRequest putObjectRequest)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Upload object.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param inputStream the inputStream
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	PutObjectResult uploadObject(final String bucketName,
			final String fileName, final InputStream inputStream)
			throws AmazonClientException, AmazonServiceException, IOException;

	
	/**
	 * Gets the object.
	 *
	 * @param getObjRequest the get object request
	 * @return the object
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	S3Object getObject(final GetObjectRequest getObjRequest)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Gets the object.
	 *
	 * @param bucketName the bucket name
	 * @param key the key, it is the name of file. e.g. myFile.pdf which is
	 *            already existing in specified bucket
	 * @return the object
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	InputStream getObject(final String bucketName, final String key)
			throws AmazonClientException, AmazonServiceException;

	/**
	 * Save object to file.
	 *
	 * @param bucketName the bucket name
	 * @param key the key
	 * @param filePath the file path
	 * @return the object metadata
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	ObjectMetadata downloadObject(final String bucketName, final String key,final String filePath)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Creates the directory.
	 *
	 * @param bucketName the bucket name
	 * @param dirName the dir name
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	PutObjectResult createDirectory(final String bucketName, final String dirName)
			throws AmazonClientException, AmazonServiceException;

	
	/**
	 * Delete object.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	void deleteObject(final String bucketName, final String fileName)
			throws AmazonClientException, AmazonServiceException;

	/**
	 * Delete objects.
	 *
	 * @param bucketName the bucket name
	 * @param keys the keys
	 * @return the delete objects result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	DeleteObjectsResult deleteObjects(final String bucketName,
			final List<KeyVersion> keys) throws AmazonClientException,
			AmazonServiceException;

	/**
	 * Delete directory.
	 *
	 * @param bucketName the bucket name
	 * @param dirName the dir name
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	void deleteDirectory(final String bucketName, final String dirName)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Checks if is bucket exists.
	 *
	 * @param bucketName the bucket name
	 * @return true, if checks if is bucket exists
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	boolean isBucketExists(final String bucketName)
			throws AmazonClientException, AmazonServiceException;
}
