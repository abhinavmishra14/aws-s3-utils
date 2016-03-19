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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.Upload;

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
	List<Bucket> getAllBuckets() throws AmazonClientException, AmazonServiceException;

	/**
	 * Creates the bucket.<br/>
	 * A bucket is owned by the AWS account that created it. By default, you can
	 * create up to 100 buckets in each of your AWS accounts. If you need
	 * additional buckets, you can increase your bucket limit by submitting a
	 * service limit increase.
	 *
	 * @param bucketName
	 *            the bucket name
	 * @return the bucket
	 * @throws AmazonClientException
	 *             the amazon client exception
	 * @throws AmazonServiceException
	 *             the amazon service exception
	 */
	Bucket createBucket(final String bucketName) throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Creates the bucket.<br/>
	 * A bucket is owned by the AWS account that created it. By default, you can
	 * create up to 100 buckets in each of your AWS accounts. If you need
	 * additional buckets, you can increase your bucket limit by submitting a
	 * service limit increase.
	 *
	 * @param bucketName the bucket name
	 * @param cannedAcl the canned acl
	 * @return the bucket
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html">ACL Overview</a>
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#canned-acl">Canned ACL</a>
	 */
	Bucket createBucket(final String bucketName, final CannedAccessControlList cannedAcl)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Creates the bucket.<br/>
	 * Permission set for a Bucket does NOT automatically propagate to files stored in that Bucket.<br/>
	 * A bucket is owned by the AWS account that created it. By default, you can
	 * create up to 100 buckets in each of your AWS accounts. If you need
	 * additional buckets, you can increase your bucket limit by submitting a
	 * service limit increase.
	 *
	 * @param bucketName the bucket name
	 * @param isPublicAccessible the is public accessible
	 * @return the bucket
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	Bucket createBucket(final String bucketName,final boolean isPublicAccessible) throws AmazonClientException, AmazonServiceException;

	/**
	 * Delete bucket.
	 *
	 * @param bucketName the bucket name
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	void deleteBucket(final String bucketName) throws AmazonClientException, AmazonServiceException;

	/**
	 * Clean and delete bucket.
	 *
	 * @param bucketName the bucket name
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	void cleanAndDeleteBucket(final String bucketName) throws AmazonClientException, AmazonServiceException;

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
	 * @param inputStream the input stream
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	PutObjectResult uploadObject(final String bucketName, final String fileName, final InputStream inputStream)
			throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Upload object.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param inputStream the input stream
	 * @param cannedAcl the canned acl
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html">ACL Overview</a>
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#canned-acl">Canned ACL</a>
	 */
	PutObjectResult uploadObject(final String bucketName, final String fileName, final InputStream inputStream,
			final CannedAccessControlList cannedAcl) throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Upload object.<br/>
	 * You can pass true/false flag to allow objects pushed to S3 to be publicly accessible.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param inputStream the input stream
	 * @param isPublicAccessible the is public accessible
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	PutObjectResult uploadObject(final String bucketName, final String fileName, final InputStream inputStream,
			final boolean isPublicAccessible) throws AmazonClientException, AmazonServiceException, IOException;

	
	/**
	 * Upload object and Listen progress.<br/> 
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the file to S3 and prints the progress.<br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param inputStream the input stream
	 * @return the boolean
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean uploadObjectAndListenProgress(final String bucketName, final String fileName, final InputStream inputStream)
			throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Upload object and Listen progress.<br/> 
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the file to S3 and prints the progress.<br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param inputStream the input stream
	 * @param cannedAcl the canned acl
	 * @return the boolean
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html">ACL Overview</a>
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#canned-acl">Canned ACL</a>
	 */
	boolean uploadObjectAndListenProgress(final String bucketName, final String fileName, final InputStream inputStream,
			final CannedAccessControlList cannedAcl) throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Upload object and Listen progress.<br/> 
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the file to S3 and prints the progress.<br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.<br/>
	 * You can pass true/false flag to allow objects pushed to S3 to be publicly accessible.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param inputStream the input stream
	 * @param isPublicAccessible the is public accessible
	 * @return the boolean
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean uploadObjectAndListenProgress(final String bucketName, final String fileName, final InputStream inputStream,
			final boolean isPublicAccessible) throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Upload directory or file and Listen Progress.<br/>
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the files or directory to S3 and prints the progress.<br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.
	 *
	 * @param bucketName the bucket name
	 * @param source the source fir or directory
	 * @param virtualDirectoryKeyPrefix the key prefix of the virtual directory to upload to. Use the null or empty string to upload files to the root of the bucket.
	 * @return the true or false
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws FileNotFoundException the file not found exception
	 */
	boolean uploadDirectoryOrFileAndListenProgress(final String bucketName, final File source,
			final String virtualDirectoryKeyPrefix)
			throws AmazonClientException, AmazonServiceException, FileNotFoundException;
	
	/**
	 * Upload file async.<br/>
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the file to S3 asynchronously<br/>
	 * You can use {@link com.amazonaws.services.s3.transfer.Upload} returning object to check for progress
	 * Usage: <code>upload.getProgress().getPercentTransferred()</code><br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param fileObj the file object
	 * @return the upload
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Upload uploadFileAsync(final String bucketName, final String fileName, final File fileObj)
			throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Upload file async.<br/>
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the file to S3 asynchronously<br/>
	 * You can use {@link com.amazonaws.services.s3.transfer.Upload} returning object to check for progress
	 * Usage: <code>upload.getProgress().getPercentTransferred()</code><br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param fileObj the file object
	 * @param cannedAcl the canned acl
	 * @return the upload
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html">ACL Overview</a>
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#canned-acl">Canned ACL</a>
	 */
	Upload uploadFileAsync(final String bucketName, final String fileName, final File fileObj,
			final CannedAccessControlList cannedAcl) throws AmazonClientException, AmazonServiceException, IOException;
	
	
	/**
	 * Upload file async.<br/>
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the file to S3 asynchronously<br/>
	 * You can use {@link com.amazonaws.services.s3.transfer.Upload} returning object to check for progress
	 * Usage: <code>upload.getProgress().getPercentTransferred()</code><br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.<br/>
	 * You can pass true/false flag to allow objects pushed to S3 to be publicly accessible.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @param fileObj the file object
	 * @param isPublicAccessible the is public accessible
	 * @return the upload
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Upload uploadFileAsync(final String bucketName, final String fileName, final File fileObj,
			final boolean isPublicAccessible) throws AmazonClientException, AmazonServiceException, IOException;

	/**
	 * Upload directory or file.<br/>
	 * S3 will overwrite any existing objects that happen to have the same key,
	 * just as when uploading individual files, so use with caution.<br/>
	 * This method will upload the files or directory to S3 asynchronously<br/>
	 * You can use {@link com.amazonaws.services.s3.transfer.Transfer} returning object to check for progress,
	 * Usage: <code>transfer.getProgress().getPercentTransferred()</code><br/>
	 * It is implemented via {@link com.amazonaws.services.s3.transfer.TransferManager} <br/>
	 * TransferManager provides a simple API for uploading content to Amazon S3,
	 * and makes extensive use of Amazon S3 multipart uploads to achieve
	 * enhanced throughput, performance and reliability. <br/>When possible,
	 * TransferManager attempts to use multiple threads to upload multiple parts
	 * of a single upload at once. <br/> When dealing with large content sizes and
	 * high bandwidth, this can have a significant increase on throughput.
	 *
	 * @param bucketName the bucket name
	 * @param source the source fir or directory
	 * @param virtualDirectoryKeyPrefix the key prefix of the virtual directory to upload to. Use the null or empty string to upload files to the root of the bucket.
	 * @return the transfer
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Transfer uploadDirectoryOrFile(final String bucketName, final File source, final String virtualDirectoryKeyPrefix)
			throws AmazonClientException, AmazonServiceException, IOException;
	
	/**
	 * Gets the object.
	 *
	 * @param getObjRequest the get obj request
	 * @return the object
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	S3Object getObject(final GetObjectRequest getObjRequest) throws AmazonClientException, AmazonServiceException;

	/**
	 * Gets the object.
	 *
	 * @param bucketName the bucket name
	 * @param key the key
	 * @return the object
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	InputStream getObject(final String bucketName, final String key)
			throws AmazonClientException, AmazonServiceException;

	/**
	 * Download object.
	 *
	 * @param bucketName the bucket name
	 * @param key the key
	 * @param filePath the file path
	 * @return the object metadata
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	ObjectMetadata downloadObject(final String bucketName, final String key, final String filePath)
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
	 * Creates the directory.
	 *
	 * @param bucketName the bucket name
	 * @param dirName the dir name
	 * @param cannedAcl the canned acl
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html">ACL Overview</a>
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#canned-acl">Canned ACL</a>
	 */
	PutObjectResult createDirectory(final String bucketName, final String dirName,
			final CannedAccessControlList cannedAcl) throws AmazonClientException, AmazonServiceException;

	/**
	 * Creates the directory.<br/>
	 * You can pass true/false flag to allow directories created in S3 to be publicly accessible.
	 *
	 * @param bucketName the bucket name
	 * @param dirName the dir name
	 * @param isPublicAccessible the is public accessible
	 * @return the put object result
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	PutObjectResult createDirectory(final String bucketName, final String dirName, final boolean isPublicAccessible)
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
	DeleteObjectsResult deleteObjects(final String bucketName, final List<KeyVersion> keys)
			throws AmazonClientException, AmazonServiceException;

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
	 * @return true, if is bucket exists
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	boolean isBucketExists(final String bucketName) throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Generate object url as string.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @return the string
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	String generateObjectUrlAsString(final String bucketName, final String fileName)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Generate object url.
	 *
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @return the url
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 */
	URL generateObjectURL(final String bucketName, final String fileName)
			throws AmazonClientException, AmazonServiceException;
	
	/**
	 * Gets the bucket permissions.<br/>
	 * Returns the list of Grant objects in this access control list (ACL).<br/>
	 * The Grant object has Permission object which tell what kind of permissions are available.<br/>
	 * and it has Grantee object which tell who are grantees.<br/>
	 * Following are permissions available: <i>FullControl, Read, Write, ReadAcp, WriteAcp </i><br/>
	 * If access to the given bucket is not valid then 'AccessDenied' error will be raised.
	 *
	 * @param bucketName the bucket name
	 * @return the bucket permissions
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws AmazonS3Exception the amazon s3 exception
	 * @see com.amazonaws.services.s3.model.Grant
	 * @see com.amazonaws.services.s3.model.Permission
	 * @see com.amazonaws.services.s3.model.Grantee
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#permissions">Permissions</a>
	 */
	List<Grant> getBucketPermissions(final String bucketName)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception;
		
	/**
	 * Checks if the bucket has full control permission <br/>
	 * If access to the given bucket is not valid then 'AccessDenied' error will be raised.<br/>
	 * Read ACP permission should be available to user who is trying to check permission on bucket.
	 *
	 * @param bucketName the bucket name
	 * @return true, if successful
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws AmazonS3Exception the amazon s3 exception
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#permissions">Permissions</a>
	 */
	boolean hasFullControlPermission(final String bucketName)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception;
		
	/**
	 * Gets the bucket access control list.<br/>
	 * Provides opportunities to grant permissions and check permissions on bucket.<br/>
	 * If access to the given bucket is not valid then 'AccessDenied' error will be raised.<br/>
	 * Read ACP permission should be available to user who is trying to check permission on bucket.
	 *
	 * @param bucketName the bucket name
	 * @return the bucket access control list
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws AmazonS3Exception the amazon s3 exception
	 * @see com.amazonaws.services.s3.model.AccessControlList
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#permissions">Permissions</a>
	 */
	AccessControlList getBucketAccessControlList(final String bucketName)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception;
	
	/**
	 * Check bucket permission.<br/>
	 * If access to the given bucket is not valid then 'AccessDenied' error will be raised.<br/>
	 * Read ACP permission should be available to user who is trying to check permission on bucket.
	 *
	 * @param bucketName the bucket name
	 * @param permission the permission
	 * @return true, if successful
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws AmazonS3Exception the amazon s3 exception
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#permissions">Permissions</a>
	 */
	boolean checkBucketPermission(final String bucketName,final Permission permission)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception;
	
	/**
	 * Check object permission.<br/>
	 * If object is not available in given bucket, an exception will be thrown
	 *
	 * @param bucketName the bucket name
	 * @param key the full path of object in given bucket
	 * @param permission the permission
	 * @return true, if successful
	 * @throws AmazonClientException the amazon client exception
	 * @throws AmazonServiceException the amazon service exception
	 * @throws AmazonS3Exception the amazon s3 exception
	 * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#permissions">Permissions</a>
	 */
	boolean checkObjectPermission(final String bucketName, final String key, final Permission permission)
			throws AmazonClientException, AmazonServiceException, AmazonS3Exception;
	
	/**
	 * Checks for write permission on s3 bucket.<br/>
	 * It checks if user has write permissions on bucket or not, even if ReadACP is not granted.
	 *
	 * @param bucketName the bucket name
	 * @return true, if successful
	 */
	boolean hasWritePermissionOnBucket(final String bucketName);

}
