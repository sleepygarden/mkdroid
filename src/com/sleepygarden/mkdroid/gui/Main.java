package com.sleepygarden.mkdroid.gui;

/**
 * Apache 2.0 dawg~
 * @author michaelcornell | http://www.github.com/mcornell009
 */
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

	static MkDroidGUI gui;
	String path;
	public static final String alpha = "\u03B1";
	public static final String beta = "\u03B2";

	public static void main(String[] args) {

		gui = new MkDroidGUI();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui.getFrame().setVisible(true);
					gui.setVersion(alpha + "1.0");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
