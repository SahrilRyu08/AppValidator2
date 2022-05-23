package com.csvfv.batch;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class CSVFileValidatorExecutor {
	private static Logger bLog = null;
	private static List<File> files = new ArrayList<>();
	public static void main(String[] args) {
		args = new String[5];

		args[0] = ""; // company code
		args[1] = ""; // production code
		args[2] = ""; // no application
		args[3] = "v0.01"; // version
		args[4] = "/home/lenovo/Documents/BCADProject/AppValidator/passed/";
//		System.out.println(Connec.checkDBRealisasiforNoApplikasi(args));

		System.setProperty("log.property.location",
				System.getProperty("batch.csvfv.log.properties"));
		bLog = BatchLogger.BATCH_LOGGER;
				String userChoice = "";
			do {
				Scanner userInput = new Scanner(System.in); // Create a Scanner object
				System.out.println("==============================================================");
				System.out.println("                   CSV FILE VALIDATOR "
						+ args[3]
						+ "                   ");
				System.out
						.println("==============================================================");
				System.out
						.println("Choose number that represent the CSV file you want to validate");
				System.out
						.println("==============================================================");
				System.out.println("1. ALL");
				System.out.println("2. UPLOAD TO FILE SERVER");
				System.out.println("3. EXIT");
				System.out.print("Your choice: ");
				userChoice = userInput.nextLine();
				String dirName = "srcfiles";
				File file = new File(dirName);
				File[] myfiles = doListing(file);
				List<String> stringList = new ArrayList<>();
				switch (userChoice) {

				case "1":
					Connec.addfield("OS_REAFILE");
//					Connec.addfield("OS_PENGURUS");
//					Connec.addfield("OS_REPAYMENT");
					for (File myfile : myfiles) {
						stringList.add(String.valueOf(myfile));
					}

					for (String s : stringList) {
						String[] s1 = s.split("_");
						args[0] = s1[1]; //company code
						args[1] = s1[2]; //product code
						if (s.contains("PENGURUS")) {
							args[2] = s1[3];
							System.out.println(args[0] + " " + args[1] + " " + args[2]);
						} else {
							System.out.println(args[0] + " " + args[1]);
						}
						start();

						String pathfile = s1[0] + "_";
						String exportFile = "";
						if (pathfile.contains("REAFILE")) {
							exportFile = "REAFILE_";
//						} else if (pathfile.contains("PENGURUS")) {
//							exportFile = "PENGURUS_";
//						} else if (pathfile.contains("REPAYMENT")) {
//							exportFile = "REPAYMENT_";
						}
						CSVService.writeFile(pathfile, args, exportFile);
						end();

//			System.out.println(s1[1]);
//			System.out.println(s1[2]);
//			args[3] = "Z:\\Temp\\"; //lokasi file server
//			System.out.println(Arrays.toString(args));
					}
					break;
				case "2":
					for (File myfile : myfiles) {
						CSVService.uploadToFileServer(myfile.getName()
										.replace("srcfiles/", ""), args[4]);}
					break;
				case "3":
					break;
				default:
					bLog.error("Choose the number provided");
					break;
				}
			} while (!"3".equals(userChoice));
//	  	long end = System.currentTimeMillis();
//		System.out.println((end - start) + " ms");
	}

	public static void start() {
		System.setProperty("log.property.location",
				System.getProperty("batch.csvfv.log.properties"));
		bLog = BatchLogger.BATCH_LOGGER;
		bLog.info("FiVaExecutor - Start");
	}

	public static void end() {
		System.setProperty("log.property.location",
				System.getProperty("batch.csvfv.log.properties"));
		bLog = BatchLogger.BATCH_LOGGER;
		bLog.info("FiVaExecutor - END\n");
	}
	public static File[] doListing(File dirName) {
		FilenameFilter filter = new FilenameFilter() {

			public boolean accept(File f, String name)
			{
				return name.endsWith(".csv");
			}
		};
		File[] fileList = dirName.listFiles(filter);
		return fileList;
	}
}
