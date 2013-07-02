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
		templateManager.define("DOMAIN_NAME", cfg.domain);
		templateManager.define("PACKAGE_NAME", cfg.packageName);
		templateManager.define("PACKAGE_NAME_AS_PATH",
				cfg.packageName.replace('.', '/'));

		// Project specific definitions
		templateManager.define("PRJ_ANDROID_NAME", cfg.projectName);

		// Android manifest definitions
		templateManager.define("ANDROID_MIN_SDK", cfg.androidMinSdkVersion);
		templateManager.define("ANDROID_TARGET_SDK",
				cfg.androidTargetSdkVersion);
		templateManager.define("ANDROID_USES_SDK");
		
		if (Integer.parseInt(cfg.androidTargetSdkVersion) >= 17){
			templateManager.define("IF_V_17_UP", "");
		}else {
			templateManager.define("IF_V_17_UP", "//");
		}
	}

	/**
	 * The raw structure of all projects is contained in a zip file. This method
	 * inflates this file in a temporary folder.
	 * 
	 * @throws IOException
	 */
	public void inflateProjects(boolean quietly) throws IOException {
		FileUtils.forceMkdir(tmpDst);
		FileUtils.cleanDirectory(tmpDst);

		InputStream is = getStream("blankdroid.zip");
		ZipInputStream zis = new ZipInputStream(is);

		ZipEntry entry;

		while ((entry = zis.getNextEntry()) != null) {

			if (!entry.getName().startsWith("__MACOSX")) {
				if (!quietly) {
					System.out.println("copying " + entry.getName() + " into "
							+ tmpDst.getName());
				}

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

		// because zipping stuff on my mac is weird, move everything in
		// blankdroid up, delete empty blankdroid TODO wtf is that about
		File root = new File(tmpDst, "blankdroid");
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				FileUtils.moveDirectoryToDirectory(f, root.getParentFile(),
						false);
			} else {
				FileUtils.moveFileToDirectory(f, root.getParentFile(), false);

			}
		}
		FileUtils.deleteQuietly(root);

	}

	public void addWebFiles() throws IOException {
		File src = new File(cfg.siteRoot);
		File dst = new File(tmpDst, "assets/webfiles");
		FileUtils.forceMkdir(dst);
		FileUtils.copyDirectory(src, dst);

	}

	public void postProcess() throws IOException {

		File packageDir = new File(tmpDst, "src/"
				+ cfg.packageName.replace('.', '/'));
		FileUtils.forceMkdir(packageDir);

		File main = new File(tmpDst, "src/MainActivity.java");
		File newMain = new File(packageDir, "/MainActivity.java");
		FileUtils.deleteQuietly(newMain);
		FileUtils.moveFile(main, newMain);

		File client = new File(tmpDst, "src/MKDroidClient.java");
		File newClient = new File(packageDir, "/MKDroidClient.java");
		FileUtils.deleteQuietly(newClient);
		FileUtils.moveFile(client, newClient);

		File webInterface = new File(tmpDst, "src/MKDroidJsInterface.java");
		File newWebInterface = new File(packageDir, "/MKDroidJsInterface.java");
		FileUtils.deleteQuietly(newWebInterface);
		FileUtils.moveFile(webInterface, newWebInterface);

		reviseFiles();

		File dst = new File(tmpDst, cfg.projectName);
		FileUtils.copyDirectory(tmpDst, dst);

	}

	public void copy(boolean quietly) throws IOException {
		if (!quietly) {
			printDirContents(tmpDst);
		}
		File dst = new File(cfg.destinationPath);
		File src = new File(tmpDst, cfg.projectName);
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

	private void reviseFiles() throws IOException {
		File packageDir = new File(tmpDst, "src/"
				+ cfg.packageName.replace('.', '/'));
		templateManager.processOver(new File(tmpDst, "/AndroidManifest.xml"));
		templateManager.processOver(new File(packageDir, "MainActivity.java"));
		templateManager.processOver(new File(packageDir,
				"MKDroidClient.java"));
		templateManager.processOver(new File(packageDir,
				"MKDroidJsInterface.java"));
		templateManager.processOver(new File(tmpDst, "/.project"));
		templateManager.processOver(new File(tmpDst, "/project.properties"));
		templateManager.processOver(new File(tmpDst, "/.classpath"));

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