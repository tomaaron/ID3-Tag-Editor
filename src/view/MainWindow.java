package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * Window containing the main GUI
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	public static TreePanel treePanel;
	public static EditPanel editPanel;

	/**
	 * Positions the window, creates and adds the tree- and the editPanel
	 */
	public MainWindow() {
		super(Constants.TITLE);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(Constants.INITIAL_TREEPANEL_WIDTH
				+ Constants.INITIAL_EDITPANEL_WIDTH,
				Constants.INITIAL_WINDOW_HEIGHT);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(new BorderLayout());

		treePanel = new TreePanel();
		editPanel = new EditPanel();

		this.getContentPane().add(treePanel, BorderLayout.CENTER);
		this.getContentPane().add(editPanel, BorderLayout.LINE_END);

		this.setVisible(true);

	}

}
