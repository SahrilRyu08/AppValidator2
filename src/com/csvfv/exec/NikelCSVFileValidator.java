package com.csvfv.exec;

import com.csvfv.batch.CSVFileValidatorExecutor;
import com.csvfv.batch.Connec;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NikelCSVFileValidator {

	public static void main(String[] args) throws SQLException {
		System.setProperty("batch.csvfv.log.properties", "config/batch.csvfv.log.properties");
		CSVFileValidatorExecutor.main(args);
	}
}
