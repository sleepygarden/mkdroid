package com.sleepygarden.mkdroid;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class MkdroidMain {

	public static final String HOME = System.getProperty("user.home");

	public static void main(String[] args) throws IOException {
		String domain;
		File src = File.createTempFile("mkdroid", null);
		File dst = File.createTempFile("mkdroid", null);
		if (args.length == 0) {
			Scanner scan = new Scanner(System.in);
			System.out.println("Please enter src location:");
			String srcPath = scan.nextLine();
			System.out.println("Please enter dst location:");
			String dstPath = scan.nextLine();
			System.out
					.println("Please enter your site's domain name (ex: www.google.com):");
			domain = scan.nextLine();
			src = new File(srcPath);
			dst = new File(dstPath);
		}

		else if (args.length == 3) {
			src = new File(args[0]);
			dst = new File(args[1]);
			domain = args[3];

		} else {
			System.out
					.println("Please enter only a source, destination, and domain in that order.");
			return;
		}

		if (src != null && dst != null) {

			if (!src.exists()) {
				System.out.println("Source folder \"" + src.getAbsolutePath()
						+ "\" can not be found.");
				return;
			}
			if (!src.isDirectory()) {
				System.out.println("Source \"" + src.getAbsolutePath()
						+ "\" isn't a directory.");
				return;
			}
			if (!dst.exists()) {
				FileUtils.forceMkdir(dst);
				System.out.println("Writing destination folder...");
			}
			if (!dst.isDirectory()) {
				System.out.println("Destination isn't a directory.");
				return;
			}

		}
		//UrlValidator urlValidator = new UrlValidator();
		//if (!urlValidator.isValid(domain)) {
		//	System.out.println("Invalid domain name.");
		//	return;
		//}

		ProjectSetupConfiguration config = new ProjectSetupConfiguration();
		config.projectName = "my-mkdroid-app";
		config.destinationPath = dst.getAbsolutePath();
		config.siteRoot = src.getAbsolutePath();

		MCProjectSetup setup = new MCProjectSetup(config);

		System.out.println("Decompressing projects...");
		setup.inflateProjects(true);
		System.out.println(" done\nAdding webfiles...");
		setup.addWebFiles();
		System.out.println(" done\nPost-processing files...");
		setup.postProcess();
		System.out.println(" done\nCopying project into destination...");
		setup.copy(true);
		System.out.println(" done\nCleaning...");
		setup.clean();
		System.out.println(" done\nAll done!");
	}
}
