/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2014-2015. Abhinav Kumar Mishra. 
 * All rights reserved.
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
		final File tempFile = File.createTempFile("tempFile", "s3Object");
		FileUtils.copyInputStreamToFile(inputStream, tempFile);    	
		return tempFile;
	}
	
	/**
	 * Delete temp file.
	 *
	 * @param tempFile the temp file
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
