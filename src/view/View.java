package view;

import java.io.File;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import model.FileTree;
import model.InputProvider;
import model.MP3;
import control.CoverChangeHandler;
import control.CoverRemoveHandler;
import control.DirectorySelectHandler;
import control.KeyHandler;
import control.SaveHandler;
import control.TreeSelectionHandler;

/**
 * View accessor class.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class View {

	private static View instance = new View();
	@SuppressWarnings("unused")
	private MainWindow mainWindow;

	/**
	 * The view remembers which InputField is focused by the user.
	 */
	private InputProvider activeInputField;

	private View() {
	}

	public static View getInstance() {
		return instance;
	}

	/**
	 * Sets default values, like font and "look and feel", paints the GUI and
	 * initializes all objects.
	 */
	public void createGUI() {
		setDefaultFont(Constants.DEFAULT_FONT);
		setLookAndFeel();
		mainWindow = new MainWindow();
		MainWindow.editPanel.setInputsEnabled(false);
		setSaveButtonEnabled(false);
	}

	/**
	 * Runs through all default settings of UI components and sets all font
	 * related settings to parameter f copied from:
	 * http://www.java-forums.org/java
	 * -tips/6522-swing-changing-component-default-font.html
	 * 
	 * @param f
	 */
	private void setDefaultFont(javax.swing.plaf.FontUIResource f) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	/**
	 * If system look and feel is not available and cross-platform look and feel
	 * fails either, the program is stopped.
	 */
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e2) {
				System.err.println("Could not set look and feel.");
				System.exit(-1);
			}
		}
	}

	public void resetEditPanel() {
		MainWindow.editPanel.reset();
	}

	public void setInputsEnabled(Boolean enable) {
		MainWindow.editPanel.setInputsEnabled(enable);
	}

	public void setEditPanelContent(MP3 mp3Node) {
		MainWindow.editPanel.setContent(mp3Node);
	}

	public void setFileTree(FileTree tree) {
		MainWindow.treePanel.setTree(tree);
	}

	public void setTreeSelectionHandler(TreeSelectionHandler handler) {
		TreePanel.jtree.addTreeSelectionListener(handler);
	}

	public void setKeyHandler(KeyHandler key) {
		EditPanel.textFieldTitle.addKeyListener(key);
		EditPanel.textFieldArtist.addKeyListener(key);
		EditPanel.textFieldAlbum.addKeyListener(key);
		EditPanel.textFieldYear.addKeyListener(key);
	}

	public void setActiveInputField(InputProvider inputField) {
		this.activeInputField = inputField;
	}

	public InputProvider getActiveInputField() {
		return activeInputField;
	}

	public void setCoverRemoveHandler(CoverRemoveHandler coverRemoveHandler) {
		EditPanel.removeCoverButton.addActionListener(coverRemoveHandler);
	}

	public void setCoverChangeHandler(CoverChangeHandler coverChangeHandler) {
		EditPanel.changeCoverButton.addActionListener(coverChangeHandler);
	}

	public void setDirectorySelectHandler(
			DirectorySelectHandler directorySelectHandler) {
		TreePanel.directorySelectButton
				.addActionListener(directorySelectHandler);
	}

	public TreePath getLastSelectedPath() {
		return TreePanel.jtree.getSelectionPath();
	}

	public void expandSelectedPath() {
		TreePanel.jtree.expandPath(TreePanel.jtree.getSelectionPath());
	}

	public File getNewDirectory() {
		FileChooser dirChooser = new FileChooser("Select directory", "dirOnly");
		dirChooser.popup();
		return dirChooser.getFile();
	}

	public File getNewPicture() {
		FileChooser picChooser = new FileChooser("Select picture", "filesOnly");
		picChooser.popup();
		return picChooser.getFile();
	}

	public void setDirectoryPathText(String absolutePath) {
		TreePanel.pathSelectLabel.setText(absolutePath);
	}

	public void setSaveHandler(SaveHandler saveHandler) {
		TreePanel.saveButton.addActionListener(saveHandler);
	}

	public void setSaveButtonEnabled(boolean enable) {
		TreePanel.saveButton.setEnabled(enable);
		TreePanel.saveButton.setFocusable(enable);
	}

}
