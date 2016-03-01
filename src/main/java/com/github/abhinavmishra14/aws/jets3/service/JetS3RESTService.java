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
import java.security.NoSuchAlgorithmException;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;

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
	boolean putObject(final String fileName) throws S3ServiceException,
			NoSuchAlgorithmException, IOException ;

	/**
	 * Put object.
	 *
	 * @param fileName the file name
	 * @return true, if successful
	 * @throws S3ServiceException the s3 service exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean putObject(final File fileName) throws S3ServiceException,
			NoSuchAlgorithmException, IOException;
	/**
	 * Delete object.
	 *
	 * @param fileName the file name
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServiceException the service exception
	 */
	void deleteObject(final String fileName)
			throws NoSuchAlgorithmException, IOException, ServiceException;
	/**
	 * Delete object.
	 *
	 * @param fileName the file name
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServiceException the service exception
	 */
	void deleteObject(final File fileName)
			throws NoSuchAlgorithmException, IOException, ServiceException ;
}
