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
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.MultipleDeleteResult;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.MultipartUtils;
import org.jets3t.service.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService;
import com.github.abhinavmishra14.aws.util.AWSUtil;
import com.github.abhinavmishra14.aws.util.AWSUtilConstants;
import com.github.abhinavmishra14.aws.util.DirectoryTraverser;

/**
 * The Class JetS3RESTService.<br/>
 * This class can be used to manually store and delete contents from the s3 bucket.
 *
 * @author Abhinav kumar mishra
 */
public class JetS3RESTServiceImpl implements JetS3RESTService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(JetS3RESTServiceImpl.class);

	/** The s3Service. */
	private S3Service s3Service;

	/**
	 * Instantiates a new rEST service.<br/>
	 * This Service can be used when you want to communicate with Amazon S3 bucket independently.<br/>
	 * UseCase: Assuming you are working on a developer machine and want to upload contents on S3 bucket.
	 * This service is implementation of core amazon restful webservices.
	 *
	 * @param accessKey the access key
	 * @param secretKey the secret key
	 */
	public JetS3RESTServiceImpl(final String accessKey, final String secretKey) {
		AWSUtil.notNull(accessKey, AWSUtilConstants.ERR_MSG_ACCESSKEY);
		AWSUtil.notNull(secretKey, AWSUtilConstants.ERR_MSG_SECRETKEY);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Initializing JetS3 service..");
		}
		s3Service = new RestS3Service(new AWSCredentials(accessKey, secretKey));
	} 

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#putObject(java.lang.String)
	 */
	@Override
	public boolean putObject(final String bucketName,final String fileName) throws S3ServiceException,
			NoSuchAlgorithmException, IOException {
		LOGGER.info("putObject invoked, filename: {}",fileName);
		return putObject(bucketName,new File(fileName));
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#putObject(java.io.File)
	 */
	@Override
	public boolean putObject(final String bucketName,final File fileName) throws S3ServiceException,
			NoSuchAlgorithmException, IOException {
		LOGGER.info("putObject invoked, filename: {}", fileName);
		return putObject(bucketName, new S3Object(fileName));
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#putObject(java.lang.String, org.jets3t.service.model.S3Object)
	 */
	@Override
	public boolean putObject(final String bucketName, final S3Object s3Object)
			throws S3ServiceException, NoSuchAlgorithmException, IOException {
		LOGGER.info("putObject invoked, bucketName: {}", bucketName);
		final S3Object obj = s3Service.putObject(bucketName, s3Object);
		return obj.getDataInputFile().exists();
	}
	
	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#deleteObject(java.lang.String)
	 */
	@Override
	public void deleteObject(final String bucketName,final String fileName)
			throws NoSuchAlgorithmException, IOException, ServiceException {
		LOGGER.info("deleteObject invoked, filename: {}", fileName);
		s3Service.deleteObject(bucketName, fileName);
	}
		
	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#createBucket(java.lang.String)
	 */
	@Override
	public S3Bucket createBucket(final String bucketName) throws ServiceException {
		LOGGER.info("createBucket invoked, bucketName: {}", bucketName);
		return s3Service.createBucket(bucketName);		
	}
	
	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#deleteBucket(java.lang.String)
	 */
	@Override
	public void deleteBucket(final String bucketName) throws ServiceException {
	  LOGGER.info("deleteBucket invoked, bucketName: {}", bucketName);
      s3Service.deleteBucket(bucketName);		
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#uploadObjectAsMultiparts(java.lang.String, org.jets3t.service.model.StorageObject, long)
	 */
	@Override
	public void uploadObjectAsMultiparts(final String bucketName, final StorageObject object, final long maxPartSize)
			throws ServiceException {
		LOGGER.info("putObjectAsMultipart invoked, bucketName: {}, maxPartSize: {}", bucketName, maxPartSize);
		s3Service.putObjectMaybeAsMultipart(bucketName, object, maxPartSize);
		 
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#getAllBuckets()
	 */
	@Override
	public List<S3Bucket> getAllBuckets() throws S3ServiceException {
		LOGGER.info("getAllBucjets invoked");	
		return Arrays.asList(s3Service.listAllBuckets());
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#getObject(java.lang.String, java.lang.String)
	 */
	@Override
	public S3Object getObject(final String bucketName, final String objectKey) throws S3ServiceException {
		LOGGER.info("getObject invoked");	
		return s3Service.getObject(bucketName, objectKey);
	}
	
	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#getObjectAsStream(java.lang.String, java.lang.String)
	 */
	@Override
	public InputStream getObjectAsStream(final String bucketName, final String objectKey)
			throws S3ServiceException, ServiceException {
		LOGGER.info("getObject invoked");	
		return getObject(bucketName, objectKey).getDataInputStream();
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#getAllObjects(java.lang.String)
	 */
	@Override
	public List<S3Object> getAllObjects(final String bucketName) throws S3ServiceException {
		LOGGER.info("getAllObjects invoked, bucketName: {}",bucketName);	
		return Arrays.asList(s3Service.listObjects(bucketName));
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#deleteObjects(java.lang.String, java.lang.String[])
	 */
	@Override
	public MultipleDeleteResult deleteObjects(final String bucketName, final String[] objectKeys)
			throws S3ServiceException {
		LOGGER.info("deleteObjects invoked, bucketName: {}", bucketName);
		return s3Service.deleteMultipleObjects(bucketName, objectKeys);
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#getObjectAcl(org.jets3t.service.model.S3Bucket, java.lang.String)
	 */
	@Override
	public AccessControlList getObjectAcl(final S3Bucket bucketName, final String objectKey) throws S3ServiceException {
		LOGGER.info("getObjectAcl invoked, bucketName: {}", bucketName);
		return s3Service.getObjectAcl(bucketName,objectKey);
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#getBucketAcl(org.jets3t.service.model.S3Bucket)
	 */
	@Override
	public AccessControlList getBucketAcl(final S3Bucket bucketName) throws S3ServiceException {
		LOGGER.info("getBucketAcl invoked, bucketName: {}", bucketName);
		return s3Service.getBucketAcl(bucketName);
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#cleanAndDeleteBucket(java.lang.String)
	 */
	@Override
	public void cleanAndDeleteBucket(final String bucketName)
			throws ServiceException, NoSuchAlgorithmException, IOException {
		LOGGER.info("cleanAndDeleteBucket invoked, bucketName: {}", bucketName);
		final List<S3Object> s3Objects = getAllObjects(bucketName);
		for (final S3Object s3Object : s3Objects) {
			deleteObject(bucketName, s3Object.getKey());
		}
		deleteBucket(bucketName);
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#uploadObjectsAsMultiparts(java.lang.String, java.util.List)
	 */
	@Override
	public void uploadObjectsAsMultiparts(final String bucketName, final List<StorageObject> objects)
			throws ServiceException, Exception {
		LOGGER.info("putObjectsAsMultiparts invoked, bucketName: {}", bucketName);
		final MultipartUtils multipartUtils = new MultipartUtils();
        multipartUtils.uploadObjects(bucketName, s3Service, objects, null);	
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#uploadDirectoryAsMultiparts(java.lang.String, java.io.File)
	 */
	@Override
	public void uploadDirectoryAsMultiparts(final String bucketName, final File dirPath)
			throws ServiceException, Exception {
		LOGGER.info("uploadDirectoryAsMultiparts invoked, bucketName: {} and dirPath: {}", bucketName, dirPath);
		final Set<File> setOfFilePath = DirectoryTraverser.getFileUris(dirPath);
		final List<StorageObject> objects = new ArrayList<StorageObject>();
		for (final File file : setOfFilePath) {
			objects.add(ObjectUtils.createObjectForUpload(file.getName(), file, null, // encryptionUtil
					false // gzipFile
			));
		}
		
		final MultipartUtils multipartUtils = new MultipartUtils();
		multipartUtils.uploadObjects(bucketName, s3Service, objects, null);
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#setObjectAcl(java.lang.String, java.lang.String, org.jets3t.service.acl.AccessControlList)
	 */
	@Override
	public void setObjectAcl(final String bucketName, final String objectKey, final AccessControlList acl)
			throws ServiceException {
		LOGGER.info("setObjectAcl invoked, bucketName: {} and object: {}", bucketName, objectKey);
		s3Service.putObjectAcl(bucketName, objectKey, acl);
	}

	/* (non-Javadoc)
	 * @see com.github.abhinavmishra14.aws.jets3.service.JetS3RESTService#setBucketAcl(java.lang.String, org.jets3t.service.acl.AccessControlList)
	 */
	@Override
	public void setBucketAcl(final String bucketName, final AccessControlList acl) throws ServiceException {
		LOGGER.info("setObjectAcl invoked, bucketName: {}", bucketName);
		s3Service.putBucketAcl(bucketName, acl);
	}
}
