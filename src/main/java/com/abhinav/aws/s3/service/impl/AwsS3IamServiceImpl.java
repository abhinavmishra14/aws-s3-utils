/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */
package com.abhinav.aws.s3.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abhinav.aws.s3.service.AwsS3IamService;
import com.abhinav.aws.util.AWSUtil;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * The Class AwsS3IamServiceImpl.
 * 
 * @author Abhinav kumar mishra
 */
public class AwsS3IamServiceImpl implements AwsS3IamService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3IamServiceImpl.class);

	/** The access key. */
	private String accessKey;

	/** The secret key. */
	private String secretKey;

	/** The Constant SEPARATOR. */
	private static final String SEPARATOR = "/";

	/** The s3client. */
	private AmazonS3 s3client;

	/**
	 * Instantiates a new aws s3 iam service impl.<br/>
	 * Use this constructor if you have keys and dont want to use IAM roles.
	 * This Service can be used when you want to communicate with Amazon S3
	 * bucket independenly.<br/>
	 * UseCase: Assuming you are working on a developer machine and want to
	 * upload contents on S3 bucket.<br/>
	 * This service is implementation of core amazon sdk.
	 * 
	 *
	 * @param accessKey the access key
	 * @param secretKey the secret key
	 */
	public AwsS3IamServiceImpl(final String accessKey, final String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		initService(); // Initialize the AwsS3IamService
	}

	/**
	 * Inits the service.
	 */
	private void initService() {
		AWSUtil.notNull(accessKey, "AccessKey is null!");
		AWSUtil.notNull(secretKey, "SecretKey is null!");
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for
		// this example to work
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AwsS3IamService is initializing using keys..");
		}
		final AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		// create a client connection based on credentials
		s3client = new AmazonS3Client(credentials);
	}

	/**
	 * Instantiates a new aws s3 iam service impl.
	 */
	public AwsS3IamServiceImpl() {
		super();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AwsS3IamService is initializing using IAM Role..");
		}
		// create a client connection based on iam role assigned
		s3client = new AmazonS3Client();
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#getAllBuckets()
	 */
	@Override
	public List<Bucket> getAllBuckets() throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getAllBuckets invoked..");
		}
		return s3client.listBuckets();
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#createBucket(java.lang.String)
	 */
	@Override
	public Bucket createBucket(final String bucketName) throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createBucket invoked..");
		}
		return s3client.createBucket(bucketName);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#deleteBucket(java.lang.String)
	 */
	@Override
	public void deleteBucket(final String bucketName) throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteBucket invoked..");
		}
		s3client.deleteBucket(bucketName);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#uploadObject(com.amazonaws.services.s3.model.PutObjectRequest)
	 */
	@Override
	public PutObjectResult uploadObject(final PutObjectRequest putObjectRequest)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("uploadObject invoked..");
		}
		return s3client.putObject(putObjectRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abhinav.aws.s3.service.AwsS3IamService#uploadObject(java.lang.String,
	 * java.lang.String, java.io.InputStream)
	 */
	@Override
	public PutObjectResult uploadObject(final String bucketName, final String fileName, final InputStream inputStream)
			throws AmazonClientException, AmazonServiceException, IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("uploadObject invoked, bucketName: {} , fileName: {} ", bucketName, fileName);
		}
		File tempFile = null;
		PutObjectRequest putObjectRequest = null;
		PutObjectResult uploadResult = null;
		try {
			// Create temp file from stream to avoid 'out of memory' exception
			tempFile = AWSUtil.createTempFileFromStream(inputStream);
			putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			uploadResult = uploadObject(putObjectRequest);
		} finally {
			AWSUtil.deleteTempFile(tempFile); // delete temp file once uploaded
		}
		return uploadResult;
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#getObject(com.amazonaws.services.s3.model.GetObjectRequest)
	 */
	@Override
	public S3Object getObject(final GetObjectRequest getObjRequest)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getObject invoked..");
		}
		return s3client.getObject(getObjRequest);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#getObject(java.lang.String, java.lang.String)
	 */
	@Override
	public InputStream getObject(final String bucketName, final String key)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getObject invoked, bucketName: {}, key: {} ", bucketName, key);
		}
		final GetObjectRequest getObjRequest = new GetObjectRequest(bucketName, key);
		final S3Object s3Object = getObject(getObjRequest);
		return s3Object.getObjectContent();
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#downloadObject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ObjectMetadata downloadObject(final String bucketName, final String key, final String filePath)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("downloadObject invoked, bucketName: {}, key: {}, filePath: {} ", bucketName, key, filePath);
		}
		final GetObjectRequest getObjRequest = new GetObjectRequest(bucketName, key);
		return s3client.getObject(getObjRequest, new File(filePath));
	}


	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#createDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public PutObjectResult createDirectory(final String bucketName, final String dirName)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createDirectory invoked, bucketName: {}, dirName: {} ", bucketName, dirName);
		}
		final ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// Create empty content,since creating empty folder needs an empty
		// content
		final InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// Create a PutObjectRequest passing the directory name suffixed by '/'
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, dirName + SEPARATOR, emptyContent,
				metadata);
		return s3client.putObject(putObjectRequest);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#deleteObject(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteObject(final String bucketName, final String fileName)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteObject invoked, bucketName: {}, fileName: {} ", bucketName, fileName);
		}
		s3client.deleteObject(bucketName, fileName);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#deleteObjects(java.lang.String, java.util.List)
	 */
	@Override
	public DeleteObjectsResult deleteObjects(final String bucketName, final List<KeyVersion> keys)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteObjects invoked, bucketName: {}, keys: {} ", bucketName, keys);
		}
		final DeleteObjectsRequest deleteObjReq = new DeleteObjectsRequest(bucketName);
		deleteObjReq.setKeys(keys);
		return s3client.deleteObjects(deleteObjReq);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#deleteDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteDirectory(final String bucketName, final String dirName)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteDirectory invoked, bucketName: {}, dirName: {} ", bucketName, dirName);
		}
		final List<S3ObjectSummary> listOfFiles = s3client.listObjects(bucketName, dirName).getObjectSummaries();
		for (final S3ObjectSummary eachFile : listOfFiles) {
			s3client.deleteObject(bucketName, eachFile.getKey());
		}
		s3client.deleteObject(bucketName, dirName);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#cleanAndDeleteBucket(java.lang.String)
	 */
	@Override
	public void cleanAndDeleteBucket(final String bucketName) throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("cleanAndDeleteBucket invoked, bucketName: {} ", bucketName);
		}
		final List<S3ObjectSummary> listOfFiles = s3client.listObjects(bucketName).getObjectSummaries();
		for (final S3ObjectSummary eachFile : listOfFiles) {
			s3client.deleteObject(bucketName, eachFile.getKey());
		}
		s3client.deleteBucket(bucketName);
	}
}
