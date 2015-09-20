/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2015. Abhinav Kumar Mishra. 
 * All rights reserved.
 */
package com.abhinav.aws.s3.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abhinav.aws.s3.service.AwsS3IamService;
import com.abhinav.aws.util.AWSUtil;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

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
		// create a client connection based on IAM role assigned
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
		LOGGER.info("uploadObject invoked, bucketName: {} , fileName: {} ", bucketName, fileName);
		File tempFile = null;
		PutObjectRequest putObjectRequest = null;
		PutObjectResult uploadResult = null;
		try {
			// Create temporary file from stream to avoid 'out of memory' exception
			tempFile = AWSUtil.createTempFileFromStream(inputStream);
			putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			uploadResult = uploadObject(putObjectRequest);
		} finally {
			AWSUtil.deleteTempFile(tempFile); // Delete the temporary file once uploaded
		}
		return uploadResult;
	}

	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#uploadObjectAndListenProgress(java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public boolean uploadObjectAndListenProgress(final String bucketName, final String fileName,
			final InputStream inputStream) throws AmazonClientException, AmazonServiceException, IOException {
		LOGGER.info("uploadObjectAndListenProgress invoked, bucketName: {} , fileName: {} ", bucketName, fileName);
		File tempFile = null;
		PutObjectRequest putObjectRequest = null;
		Upload upload = null;
		try {
			// Create temporary file from stream to avoid 'out of memory' exception
			tempFile = AWSUtil.createTempFileFromStream(inputStream);
			putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			final TransferManager transferMgr = new TransferManager(s3client);
			upload = transferMgr.upload(putObjectRequest);
			// You can poll your transfer's status to check its progress
			if (upload.isDone() == false) {
				LOGGER.info("Transfer: {}  - State: {} - Progress (%): {}", upload.getDescription(),
						upload.getState(), upload.getProgress().getPercentTransferred());
			}
			 
			// Add progressListener to listen asynchronous notifications about your transfer's progress
			upload.addProgressListener(new ProgressListener() {
				public void progressChanged(ProgressEvent event) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Transferred bytes: " + (long) event.getBytesTransferred());
					}
	             }
			});
		
			try {
				//Block the current thread and wait for completion
				//If the transfer fails AmazonClientException will be thrown
				upload.waitForCompletion();
			} catch (AmazonClientException | InterruptedException excp) {
				LOGGER.error("Exception occured while waiting for transfer: ",excp);
			}
			 
			//Shutdown to release the resources after completion
			transferMgr.shutdownNow();
		} finally {
			AWSUtil.deleteTempFile(tempFile); // Delete the temporary file once uploaded
		}
		LOGGER.info("Upload Status (%): "+upload.getProgress().getPercentTransferred());
		return upload.isDone();
	}
	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#uploadFileAsync(java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public Upload uploadFileAsync(final String bucketName, final String fileName, final File fileObj)
			throws AmazonClientException, AmazonServiceException, IOException {
		LOGGER.info("uploadObjectAsync invoked, bucketName: {} , fileName: {} ", bucketName, fileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, fileObj);
		final TransferManager transferMgr = new TransferManager(s3client);
		return transferMgr.upload(putObjectRequest);
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#uploadDirectoryOrFileAndWaitForCompletion(java.lang.String, java.io.File, java.lang.String)
	 */
	@Override
	public boolean uploadDirectoryOrFileAndListenProgress(final String bucketName, final File source,
			final String virtualDirectoryKeyPrefix)
					throws AmazonClientException, AmazonServiceException, FileNotFoundException {
		LOGGER.info("uploadDirectoryOrFileAndWaitForCompletion invoked, bucketName: {} , Source: {} ", bucketName,
				source.getAbsolutePath());
		Transfer transfer = null;
		final TransferManager transferMgr = new TransferManager(s3client);
		if (source.isFile()) {
			transfer = transferMgr.upload(bucketName,source.getPath(),source);
		} else if (source.isDirectory()) {
			//upload recursively
			transfer = transferMgr.uploadDirectory(bucketName, virtualDirectoryKeyPrefix, source, true);
		} else {
			throw new FileNotFoundException("File is neither a regular file nor a directory " + source);
		}
		
		// You can poll your transfer's status to check its progress
		if (transfer.isDone() == false) {
			LOGGER.info("Transfer: {}  - State: {} - Progress (%): {}", transfer.getDescription(),
					transfer.getState(), transfer.getProgress().getPercentTransferred());
		}
		 
		// Add progressListener to listen asynchronous notifications about your transfer's progress
		transfer.addProgressListener(new ProgressListener() {
			public void progressChanged(ProgressEvent event) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Transferred bytes: " + (long) event.getBytesTransferred());
				}
             }
		});
	
		try {
			//Block the current thread and wait for completion
			//If the transfer fails AmazonClientException will be thrown
			transfer.waitForCompletion();
		} catch (AmazonClientException | InterruptedException excp) {
			LOGGER.error("Exception occured while waiting for transfer: ",excp);
		}
		 
		//Shutdown to release the resources after completion
		transferMgr.shutdownNow();
		LOGGER.info("Upload Status (%): "+transfer.getProgress().getPercentTransferred());
		return transfer.isDone();
	}

	
	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#uploadDirectoryOrFile(java.lang.String, java.io.File, java.lang.String)
	 */
	@Override
	public Transfer uploadDirectoryOrFile(final String bucketName, final File source,
			final String virtualDirectoryKeyPrefix) throws AmazonClientException, AmazonServiceException, IOException {
		LOGGER.info("uploadDirectoryOrFile invoked, bucketName: {} , Source: {} ", bucketName,
				source.getAbsolutePath());
		Transfer transfer = null;
		final TransferManager trMgr = new TransferManager(s3client);
		if (source.isFile()) {
			transfer = trMgr.upload(bucketName,source.getPath(),source);
		} else if (source.isDirectory()) {
			//Upload recursively
			//virtualDirectoryKeyPrefix could be virtual directory name inside the bucket
			transfer = trMgr.uploadDirectory(bucketName, virtualDirectoryKeyPrefix, source, true);
		} else {
			throw new FileNotFoundException("Source is neither a regular file nor a directory " + source);
		}
		return transfer;
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
		LOGGER.info("getObject invoked, bucketName: {}, key: {} ", bucketName, key);
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
		LOGGER.info("downloadObject invoked, bucketName: {}, key: {}, filePath: {} ", bucketName, key, filePath);
		final GetObjectRequest getObjRequest = new GetObjectRequest(bucketName, key);
		return s3client.getObject(getObjRequest, new File(filePath));
	}


	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#createDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public PutObjectResult createDirectory(final String bucketName, final String dirName)
			throws AmazonClientException, AmazonServiceException {
		LOGGER.info("createDirectory invoked, bucketName: {}, dirName: {} ", bucketName, dirName);
		final ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// Create empty content,since creating empty folder needs an empty content
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
		LOGGER.info("deleteObject invoked, bucketName: {}, fileName: {} ", bucketName, fileName);
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
		LOGGER.info("deleteDirectory invoked, bucketName: {}, dirName: {} ", bucketName, dirName);
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
		LOGGER.info("cleanAndDeleteBucket invoked, bucketName: {} ", bucketName);
		final List<S3ObjectSummary> listOfFiles = s3client.listObjects(bucketName).getObjectSummaries();
		for (final S3ObjectSummary eachFile : listOfFiles) {
			s3client.deleteObject(bucketName, eachFile.getKey());
		}
		s3client.deleteBucket(bucketName);
	}

	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#isBucketExists(java.lang.String)
	 */
	@Override
	public boolean isBucketExists(final String bucketName) throws AmazonClientException, AmazonServiceException {
		return s3client.doesBucketExist(bucketName);
	}

	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#generateObjectUrlAsString(java.lang.String, java.lang.String)
	 */
	@Override
	public String generateObjectUrlAsString(final String bucketName, final String fileName)
			throws AmazonClientException, AmazonServiceException {
		LOGGER.info("generateObjectUrlAsString invoked, bucketName: {}, fileName: {} ", bucketName, fileName);
		return generateObjectURL(bucketName,fileName).toString();
	}

	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#generateObjectURL(java.lang.String, java.lang.String)
	 */
	@Override
	public URL generateObjectURL(final String bucketName, final String fileName)
			throws AmazonClientException, AmazonServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generateObjectURL invoked, bucketName: {}, fileName: {} ", bucketName, fileName);
		}
		final GeneratePresignedUrlRequest presignedUrlReq = new GeneratePresignedUrlRequest(bucketName, fileName);
		return s3client.generatePresignedUrl(presignedUrlReq);
	}

	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#getBucketPermissions(java.lang.String)
	 */
	@Override
	public List<Grant> getBucketPermissions(final String bucketName)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getBucketPermissions invoked, bucketName: {}", bucketName);
		}
		return getBucketAccessControlList(bucketName).getGrantsAsList();
	}

	/* (non-Javadoc)
	 * @see com.abhinav.aws.s3.service.AwsS3IamService#getBucketAccessControlList(java.lang.String)
	 */
	@Override
	public AccessControlList getBucketAccessControlList(final String bucketName)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception {
		return s3client.getBucketAcl(bucketName);
	}


	/**
	 * Checks if the grant list has full control permission <br/>
	 * If access to the given bucket is not valid then 'AccessDenied' error will be raised.
	 *
	 * @param grantList the grant list
	 * @return true, if successful
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws AmazonS3Exception the amazon s3 exception
	 */
	@Override
	public boolean containsFullControlPermission(final List<Grant> grantList)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception {
		boolean containsFullControl = false;
		for (Grant grant : grantList) {
			if(Permission.FullControl.equals(grant.getPermission())){
				containsFullControl = true;
				break;
			}
		}
		return containsFullControl;
	}
}
