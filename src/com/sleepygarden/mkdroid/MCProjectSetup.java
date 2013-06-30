package com.sleepygarden.mkdroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class MCProjectSetup {
	private final ProjectSetupConfiguration cfg;
	private final File tmpDst = new File("__mkdroid_setup_tmp");
	private final TemplateManager templateManager = new TemplateManager();
	private int layer_count = -1;

	public MCProjectSetup(ProjectSetupConfiguration cfg) {
		this.cfg = cfg;

		templateManager.define("PROJECT_NAME", cfg.projectName);
		templateManager.define("MAINCLASS_NAME", cfg.mainClassName);
		templateManager.define("PACKAGE_NAME", cfg.packageName);
		templateManager.define("PACKAGE_NAME_AS_PATH",
				cfg.packageName.replace('.', '/'));

		// Project specific definitions
		templateManager.define("PRJ_ANDROID_NAME", cfg.projectName);

		// Android manifest definitions
		if (!cfg.androidMinSdkVersion.equals(""))
			templateManager.define("ANDROID_MIN_SDK", cfg.androidMinSdkVersion);
		if (!cfg.androidTargetSdkVersion.equals(""))
			templateManager.define("ANDROID_TARGET_SDK",
					cfg.androidTargetSdkVersion);
		if (!cfg.androidMaxSdkVersion.equals(""))
			templateManager.define("ANDROID_MAX_SDK", cfg.androidMaxSdkVersion);
		if (!cfg.androidMinSdkVersion.equals("")
				|| !cfg.androidTargetSdkVersion.equals("")
				|| !cfg.androidMaxSdkVersion.equals(""))
			templateManager.define("ANDROID_USES_SDK");
	}

	/**
	 * The raw structure of all projects is contained in a zip file. This method
	 * inflates this file in a temporary folder.
	 * 
	 * @throws IOException
	 */
	public void inflateProjects() throws IOException {
		FileUtils.forceMkdir(tmpDst);
		FileUtils.cleanDirectory(tmpDst);

		InputStream is = getStream("blankdroid.zip");
		ZipInputStream zis = new ZipInputStream(is);

		ZipEntry entry;

		while ((entry = zis.getNextEntry()) != null) {

			if (!entry.getName().startsWith("__MACOSX")) {
				System.out.println("copying " + entry.getName() + " into "
						+ tmpDst.getName());

				File file = new File(tmpDst, entry.getName());
				if (entry.isDirectory()) {
					FileUtils.forceMkdir(file);
				} else {
					OutputStream os = new FileOutputStream(file);
					IOUtils.copy(zis, os);
					os.close();
				}
			}
		}

		zis.close();
		File root = new File(tmpDst, "blankdroid");
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				FileUtils.moveDirectoryToDirectory(f, root.getParentFile(),
						false);
			} else {
				FileUtils.moveFileToDirectory(f, root.getParentFile(), false);

			}
		}

	}

	/**
	 * Launchers and packages are set up according to the user choices.
	 * 
	 * @throws IOException
	 */
	public void postProcess() throws IOException {
		templateManager.processOver(new File(tmpDst, "/.classpath"));

		File dst = new File(tmpDst, cfg.projectName);
		File main = new File(tmpDst, "src/MainActivity.java");
		File newMain = new File("src/" + cfg.packageName.replace('.', '/')
				+ "/MainActivity.java");
		FileUtils.deleteQuietly(newMain);
		FileUtils.moveFile(main, newMain);
		reviseManifest();
		FileUtils.copyDirectory(tmpDst, dst);

	}

	public void addWebFiles() {
		File src = new File(cfg.siteRoot);
		File dst = new File(tmpDst, "webfiles");
	}

	public void copy() throws IOException {
		printDirContents(tmpDst);
		File dst = new File(cfg.destinationPath);
		File src = new File(tmpDst, cfg.projectName);
		src.renameTo(dst);
		FileUtils.copyDirectoryToDirectory(src, dst);

	}

	public void clean() {
		FileUtils.deleteQuietly(tmpDst);
	}

	/*
	 * HELPERS
	 */

	private void printDirContents(File aFile) {
		layer_count++;
		String spcs = "";
		for (int i = 0; i < layer_count; i++)
			spcs += " ";
		if (aFile.isFile())
			System.out.println(spcs + "[FILE] " + aFile.getName());
		else if (aFile.isDirectory()) {
			System.out.println(spcs + "[DIR] " + aFile.getName());
			File[] listOfFiles = aFile.listFiles();
			if (listOfFiles != null) {
				for (int i = 0; i < listOfFiles.length; i++)
					printDirContents(listOfFiles[i]);
			} else {
				System.out.println(spcs + " [ACCESS DENIED]");
			}
		}
		layer_count--;
	}

	private void reviseManifest() throws IOException {
		File manifest = new File(tmpDst, "AndroidManifest.xml");
		if (manifest.exists()) {
			templateManager.processOver(manifest);
		}

	}

	private InputStream getStream(String path) {
		if (!path.startsWith("/")) {
			path = "/res/" + path;
		}
		InputStream is = MCProjectSetup.class.getResourceAsStream(path);
		if (is == null)
			throw new RuntimeException("File not found: " + path);
		return is;
	}
}