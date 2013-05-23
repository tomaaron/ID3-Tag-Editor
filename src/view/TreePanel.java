package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import model.FileTree;

/**
 * Left panel of the main window, containing the FileTree-representation and the
 * GUI elements for selecting a new root directory.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 */
@SuppressWarnings("serial")
public class TreePanel extends JPanel {

	static TreePanel instance;
	static JTree jtree;
	private JScrollPane treeView;
	private FileTree tree;
	private JPanel directorySelectPanel = new JPanel();
	private JPanel directorySelectButtonPanel = new JPanel();
	static JButton directorySelectButton = new JButton("change directory");
	static JButton saveButton = new JButton("save edited tags");
	static JLabel pathSelectLabel = new JLabel();

	/**
	 * Creates and adds the tree and the GUI elements for selecting the root
	 * directory.
	 */
	public TreePanel() {
		this.setLayout(new BorderLayout());
		this.addButtons();
		this.addDirectorySelect();
		this.addJTree();
		this.setSize(Constants.INITIAL_TREEPANEL_WIDTH,
				Constants.INITIAL_WINDOW_HEIGHT);
	}

	/** 
	 * Creates and adds a JTree and a JScrollPane.
	 */
	private void addJTree() {
		jtree = new JTree(tree);
		jtree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeView = new JScrollPane(jtree);
		this.add(treeView);
	}

	/**
	 * Adds the buttons for saving and selecting a directory.
	 */
	private void addButtons() {
		directorySelectButtonPanel.setLayout(new BoxLayout(
				directorySelectButtonPanel, BoxLayout.X_AXIS));
		directorySelectButtonPanel.add(directorySelectButton);
		directorySelectButtonPanel.add(saveButton);
		directorySelectButtonPanel.setBorder(BorderFactory.createMatteBorder(0,
				0, 0, 1, Color.BLACK));
		this.add(directorySelectButtonPanel, BorderLayout.NORTH);
	}

	private void addDirectorySelect() {
		pathSelectLabel.setText("");
		directorySelectPanel.setLayout(new BoxLayout(directorySelectPanel,
				BoxLayout.X_AXIS));
		directorySelectPanel.add(pathSelectLabel);
		directorySelectPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0,
				1, Color.BLACK));
		this.add(directorySelectPanel, BorderLayout.PAGE_END);
	}

	/**
	 * Connects a TreeModel to the JTree.
	 * 
	 * @param tree - the tree to be connected
	 */
	public void setTree(TreeModel tree) {
		jtree.setModel(tree);
	}
}
