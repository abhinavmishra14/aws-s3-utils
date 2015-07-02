/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */
package com.abhinav.aws.jets3.service.impl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.springframework.util.Assert;

import com.abhinav.aws.jets3.service.JetS3RESTService;

/**
 * The Class JetS3RESTService.<br/>
 * This class can be used to manually store and delete contents from the s3 bucket.
 *
 * @author Abhinav kumar mishra
 */
public class JetS3RESTServiceImpl implements JetS3RESTService{

	/** The bucket name. */
	private final String bucketName;

	/** The s3Service. */
	private S3Service s3Service;

	/** The bucket. */
	private S3Bucket bucket;

	/**
	 * Instantiates a new rEST service.
	 */
	public JetS3RESTServiceImpl(final String accessKey, final String secretKey, final String bucketName) {
		Assert.notNull(accessKey, "AccessKey is null!");
		Assert.notNull(secretKey, "SecretKey is null!");
		Assert.notNull(bucketName, "BucketName is null!");

		//Amazon Web Services BucketName
		this.bucketName = bucketName;
		// Instantiate S3 Service and create necessary bucket.
		try {
			s3Service = new RestS3Service(new AWSCredentials(accessKey, secretKey));
			bucket = s3Service.getOrCreateBucket(bucketName);
		} catch (S3ServiceException s3ServExcp) {
			System.err.println("JetS3RESTService Initialization Error: "
					+ s3ServExcp);
		}
	} 

	/**
	 * Put object.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 * @throws S3ServiceException the s3 service exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public boolean putObject(final String fileName) throws S3ServiceException,
			NoSuchAlgorithmException, IOException {
		final S3Object obj = s3Service.putObject(bucket, new S3Object(new File(fileName)));
		return obj.getDataInputFile().exists();
	}

	/**
	 * Put object.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 * @throws S3ServiceException the s3 service exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public boolean putObject(final File fileName) throws S3ServiceException,
			NoSuchAlgorithmException, IOException {
		final S3Object obj = s3Service.putObject(bucket, new S3Object(fileName));
		return obj.getDataInputFile().exists();
	}

	/**
	 * Delete object.
	 *
	 * @param fileName the file name
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServiceException the service exception
	 */
	@Override
	public void deleteObject(final String fileName)
			throws NoSuchAlgorithmException, IOException, ServiceException {
		s3Service.deleteObject(bucketName,new S3Object(new File(fileName)).getKey());
	}

	/**
	 * Delete object.
	 *
	 * @param fileName the file name
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServiceException the service exception
	 */
	@Override
	public void deleteObject(final File fileName)
			throws NoSuchAlgorithmException, IOException, ServiceException {
		s3Service.deleteObject(bucketName, new S3Object(fileName).getKey());
	}
}
