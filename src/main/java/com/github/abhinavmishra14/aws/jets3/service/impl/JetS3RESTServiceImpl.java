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
package com.github.abhinavmishra14.aws.jets3.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService;
import com.github.abhinavmishra14.aws.util.AWSUtil;
import com.github.abhinavmishra14.aws.util.AWSUtilConstants;

/**
 * The Class JetS3RESTService.<br/>
 * This class can be used to manually store and delete contents from the s3 bucket.
 *
 * @author Abhinav kumar mishra
 */
public class JetS3RESTServiceImpl implements JetS3RESTService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(JetS3RESTServiceImpl.class);

	/** The bucket name. */
	private final String bucketName;

	/** The s3Service. */
	private S3Service s3Service;

	/** The bucket. */
	private S3Bucket bucket;

	/**
	 * Instantiates a new rEST service.<br/>
	 * This Service can be used when you want to communicate with Amazon S3 bucket independenly.<br/>
	 * UseCase: Assuming you are working on a developer machine and want to upload contents on S3 bucket.
	 * This service is implementation of core amazon restful webservices.
	 * 
	 */
	public JetS3RESTServiceImpl(final String accessKey, final String secretKey, final String bucketName) {
		AWSUtil.notNull(accessKey, AWSUtilConstants.ERR_MSG_ACCESSKEY);
		AWSUtil.notNull(secretKey, AWSUtilConstants.ERR_MSG_SECRETKEY);
		AWSUtil.notNull(bucketName, AWSUtilConstants.ERR_MSG_BUCKETNAME);

		//Amazon Web Services BucketName
		this.bucketName = bucketName;
		// Instantiate S3 Service and create necessary bucket.
		try {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Initializing JetS3 service..");
			}
			s3Service = new RestS3Service(new AWSCredentials(accessKey, secretKey));
			bucket = s3Service.getOrCreateBucket(bucketName);
		} catch (S3ServiceException s3ServExcp) {
			LOGGER.error("JetS3RESTService Initialization Error ", s3ServExcp);
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
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("putObject invoked, filename: {}",fileName);
		}
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
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("putObject invoked, filename: {}", fileName);
		}
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
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("deleteObject invoked, filename: {}", fileName);
		}
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
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("deleteObject invoked, filename: {}", fileName);
		}
		s3Service.deleteObject(bucketName, new S3Object(fileName).getKey());
	}
}
