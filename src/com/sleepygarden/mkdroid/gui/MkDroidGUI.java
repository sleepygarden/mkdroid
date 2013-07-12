package com.sleepygarden.mkdroid.gui;

/**
 * Apache 2.0 dawg~
 * @author michaelcornell | http://www.github.com/mcornell009
 */
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import com.sleepygarden.mkdroid.Configuration;
import com.sleepygarden.mkdroid.MkDroid;

public class MkDroidGUI {

	private JFrame frame;

	private JTextArea src;
	private JTextArea dst;
	private JTextArea domain;
	private JTextArea minSdk;
	private JTextArea targetSdk;
	private JTextArea projectName;

	private JButton srcFileChooser;
	private JButton dstFileChooser;
	private JButton submit;

	private JFileChooser fc;
	private static JLabel logLbl;

	public MkDroidGUI() {
		init();

	}

	public void init() {
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		Icon icon = UIManager.getIcon("FileView.directoryIcon");

		frame = new JFrame();
		frame.setBounds(0, 0, 510, 340);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("MkDroid");
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		//

		JLabel projNameLbl = new JLabel("Project Name:");
		projNameLbl.setBounds(5, 5, 500, 15);
		frame.getContentPane().add(projNameLbl);

		JLabel srcLbl = new JLabel(
				"Webroot folder: (/Users/MySelf/Sites/mysite/):");
		srcLbl.setBounds(5, 50, 500, 15);
		frame.getContentPane().add(srcLbl);

		JLabel dstLbl = new JLabel(
				"App destination folder: (/Users/MySelf/java_workspace/):");
		dstLbl.setBounds(5, 95, 500, 15);
		frame.getContentPane().add(dstLbl);

		JLabel domainLbl = new JLabel("Domain name: (www.mydomain.com):");
		domainLbl.setBounds(5, 140, 500, 15);
		frame.getContentPane().add(domainLbl);

		JLabel minSdkLbl = new JLabel("Min SDK (7 recommended):");
		minSdkLbl.setBounds(5, 185, 250, 15);
		frame.getContentPane().add(minSdkLbl);

		JLabel maxSdkLbl = new JLabel("Target SDK:");
		maxSdkLbl.setBounds(5, 230, 100, 15);
		frame.getContentPane().add(maxSdkLbl);

		// END LABELS

		//

		srcFileChooser = new JButton(icon);
		srcFileChooser.setBounds(485, 66, 20, 20);
		srcFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				src.setText(popFileChooser());
				updatePathColor(src);

			}
		});
		frame.getContentPane().add(srcFileChooser);

		dstFileChooser = new JButton(icon);
		dstFileChooser.setBounds(485, 111, 20, 20);
		dstFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dst.setText(popFileChooser());
				updatePathColor(dst);
			}
		});
		frame.getContentPane().add(dstFileChooser);

		//

		// START TEXT AREAS

		projectName = new JTextArea();
		projectName.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		JScrollPane projNameScrollPane = new JScrollPane(projectName,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		projNameScrollPane.setBounds(5, 21, 475, 21);
		frame.getContentPane().add(projNameScrollPane);

		src = new JTextArea();
		src.setForeground(new Color(0xBB0000));
		src.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				updatePathColor(src);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
		src.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		JScrollPane srcScrollPane = new JScrollPane(src,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		srcScrollPane.setBounds(5, 66, 475, 21);
		frame.getContentPane().add(srcScrollPane);

		dst = new JTextArea();
		dst.setForeground(new Color(0xBB0000));
		dst.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				updatePathColor(dst);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
		dst.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		JScrollPane dstScrollPane = new JScrollPane(dst,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dstScrollPane.setBounds(5, 111, 475, 21);
		frame.getContentPane().add(dstScrollPane);

		domain = new JTextArea();
		domain.setForeground(new Color(0xBB0000));
		domain.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				updateUrlColor(domain);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
		domain.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		JScrollPane domainScrollPane = new JScrollPane(domain,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		domainScrollPane.setBounds(5, 156, 475, 21);
		frame.getContentPane().add(domainScrollPane);

		minSdk = new JTextArea("7");
		minSdk.setForeground(new Color(0x00BB00));
		minSdk.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				updateIntColor(minSdk);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
		minSdk.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		JScrollPane minSdkScrollPane = new JScrollPane(minSdk,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		minSdkScrollPane.setBounds(5, 201, 30, 21);
		frame.getContentPane().add(minSdkScrollPane);

		targetSdk = new JTextArea("17");
		targetSdk.setForeground(new Color(0x00BB00));
		targetSdk.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				updateIntColor(targetSdk);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});
		targetSdk.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		JScrollPane maxSdkScrollPane = new JScrollPane(targetSdk,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		maxSdkScrollPane.setBounds(5, 246, 30, 21);
		frame.getContentPane().add(maxSdkScrollPane);

		logLbl = new JLabel("");
		logLbl.setBounds(5, 280, 405, 25);
		frame.getContentPane().add(logLbl);

		submit = new JButton("mkdroid->");
		submit.setBounds(415, 290, 95, 25);
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// mkdroid!
				MkDroid.config = new Configuration();
				MkDroid.config.projectName = projectName.getText();
				MkDroid.config.siteRoot = src.getText();
				MkDroid.config.destinationPath = dst.getText();
				MkDroid.config.domain = domain.getText();
				MkDroid.config.androidMinSdkVersion = minSdk.getText();
				if (targetSdk.getText() != null) {
					MkDroid.config.androidTargetSdkVersion = targetSdk
							.getText();
				}
				MkDroid.run();

			}
		});
		frame.getContentPane().add(submit);

	}

	public void setVersion(String v) {
		frame.setTitle(frame.getTitle() + " v:" + v);
	}

	public Window getFrame() {
		return frame;
	}

	public void log(String msg) {
		logLbl.setForeground(Color.BLACK);
		logLbl.setText(msg);
		System.out.println(msg);
	}

	public void errlog(String msg) {
		logLbl.setForeground(new Color(0xBB0000));
		logLbl.setText(msg);
		System.err.println(msg);
	}

	public String popFileChooser() {
		int returnVal = fc.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fpath = fc.getSelectedFile().getAbsolutePath();
			return fpath;
		}
		return null;
	}

	public void updatePathColor(JTextArea area) {
		File f = new File(area.getText());
		if (f.exists()) {
			area.setForeground(new Color(0x00BB00));
		} else {
			area.setForeground(new Color(0xBB0000));
		}
	}

	public void updateIntColor(JTextArea area) {

		if (isValidInt(area.getText())) {
			area.setForeground(new Color(0x00BB00));

		} else {
			area.setForeground(new Color(0xBB0000));

		}

	}

	private boolean isValidInt(String s) {
		int i = -1;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		if (i < 1) {
			return false;
		}
		return true;
	}

	// validate url
	public void updateUrlColor(JTextArea area) {
		// File f = new File(area.getText());
		// if (f.exists()) {
		area.setForeground(new Color(0x00BB00));
		// } else {
		// area.setForeground(new Color(0xBB0000));
		// }

	}

}
