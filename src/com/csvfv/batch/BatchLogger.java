package com.csvfv.batch;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author yoedi.wijaya
 * @date Feb 24, 2021
 * @project BCAD-JAT
 *
 */

public class BatchLogger {
	public static final Logger BATCH_LOGGER = getLogger(readLogProperty("log4j.category.batch"),
			readLogProperty("log4j.appender.batch.layout.ConversionPattern"),
			readLogProperty("log4j.appender.stdout.layout.ConversionPattern"),
			readLogProperty("log4j.appender.stdout.Enable"), readLogProperty("log4j.appender.batch.File"),
			readLogProperty("log4j.appender.batch.MaxFileSize"),
			Integer.parseInt(readLogProperty("log4j.appender.batch.MaxBackupIndex")),
			readLogProperty("log4j.appender.batch.Threshold"));
	
	public static final Logger NOTICE_LOGGER = getLogger(readLogProperty("log4j.category.notice"),
			readLogProperty("log4j.appender.notice.layout.ConversionPattern"),
			readLogProperty("log4j.appender.stdout.layout.ConversionPattern"),
			readLogProperty("log4j.appender.stdout.Enable"), readLogProperty("log4j.appender.notice.File"),
			readLogProperty("log4j.appender.notice.MaxFileSize"),
			Integer.parseInt(readLogProperty("log4j.appender.notice.MaxBackupIndex")),
			readLogProperty("log4j.appender.notice.Threshold"));

	public synchronized static String readProperty(String propFilename, String propKey) {
		String propValue = null;
		try {
			Properties defaultProps = new Properties();
			FileInputStream in = new FileInputStream(propFilename);
			defaultProps.load(in);
			in.close();
			propValue = defaultProps.getProperty(propKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propValue;
	}

	public synchronized static String readLogProperty(String propKey) {
		return readProperty(System.getProperty("log.property.location"), propKey);
	}

	public static Logger getLogger(String category, String logFilePattern, String consolePattern, String printToConsole,
			String logFileLocation, String maxLogFileSize, int maxLogBackupIndex, String logLevel) {
		Logger logger = Logger.getLogger(category);
		PatternLayout ptLayoutAPP = new PatternLayout(logFilePattern);

		RollingFileAppender fileAppender = null;
		try {
			fileAppender = new RollingFileAppender(ptLayoutAPP, logFileLocation, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.setLevel(Level.toLevel(logLevel));
		fileAppender.setMaxFileSize(maxLogFileSize);
		fileAppender.setMaxBackupIndex(maxLogBackupIndex);
		logger.addAppender(fileAppender);

		if ("Y".equals(printToConsole)) {
			PatternLayout ptLayoutSTDOUT = new PatternLayout(consolePattern);
			ConsoleAppender consoleAppender = new ConsoleAppender(ptLayoutSTDOUT);
			logger.addAppender(consoleAppender);
		}
		return logger;
	}
}
