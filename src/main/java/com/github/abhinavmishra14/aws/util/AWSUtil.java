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
package com.github.abhinavmishra14.aws.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

/**
 * The Class AWSUtil.
 */
public final class AWSUtil {

	/**
	 * Instantiates a new AWS util.
	 */
	private AWSUtil(){
		super();
	}
	
	
	/**
	 * Creates the temp file from stream.
	 *
	 * @param inputStream the in stream
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File createTempFileFromStream(final InputStream inputStream)
			throws IOException {
		final File tempFile = File.createTempFile(AWSUtilConstants.TEMP_FILE_PREFIX, AWSUtilConstants.TEMP_FILE_SUFFIX);
		FileUtils.copyInputStreamToFile(inputStream, tempFile);    	
		return tempFile;
	}
	
	/**
	 * Delete temp file.
	 *
	 * @param tempFile the temp file
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean deleteTempFile(final File tempFile) throws IOException {
		boolean isDeleted = false;
		if (tempFile != null && tempFile.exists()) {
			isDeleted = tempFile.delete();
		}
		return isDeleted;
	}
	
	/**
	 * Not null.
	 *
	 * @param parameterValue the parameter value
	 * @param errorMessage the error message
	 */
	public static void notNull(final Object parameterValue,
			final String errorMessage) {
		if (parameterValue == null){
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
