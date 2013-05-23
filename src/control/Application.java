package control;

import java.io.File;

import model.InputProvider;
import model.Model;
import view.View;

/**
 * Application control class.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 */
public class Application {

	private static Application instance = new Application();

	/**
	 * gathers debug information
	 * 
	 * @param msg
	 */
	public static void log(String msg) {
		System.out.println(msg);
	}

	public static Application getInstance() {
		return instance;
	}

	/**
	 * tries to get edited input from View and sends it to Model to save it, if
	 * there is any.
	 */
	public void saveInputInTree() {
		InputProvider inputField = View.getInstance().getActiveInputField();
		/*
		 * This method is always called, when a directory was selected, too.
		 * This is because, when a user enters some input and then changes the
		 * directory, we want to save the input. But when we switch from one
		 * directory to another, there is no active inputField, what we have to
		 * check.
		 */
		if (inputField != null) {
			Model model = Model.getInstance();
			model.setTextValueOfActiveFile(inputField.getKey(),
					inputField.getValue());
			model.updateEditStatusOfActiveFile();
		}
	}

	/**
	 * paints the GUI, calls initializing methods on the Model, registers
	 * Listeners and takes care that all is done in the correct order
	 */
	public void run() {
		View.getInstance().createGUI();
		View.getInstance().setTreeSelectionHandler(new TreeSelectionHandler());
		View.getInstance().setKeyHandler(new KeyHandler());
		View.getInstance().setCoverRemoveHandler(new CoverRemoveHandler());
		View.getInstance().setCoverChangeHandler(new CoverChangeHandler());
		View.getInstance().setDirectorySelectHandler(
				new DirectorySelectHandler());
		View.getInstance().setSaveHandler(new SaveHandler());
		File newDir = View.getInstance().getNewDirectory();
		if (newDir != null) {
			Model.getInstance().setRootDirectory(newDir);
			View.getInstance().setDirectoryPathText(newDir.getAbsolutePath());
			View.getInstance().setFileTree(Model.getInstance().getFileTree());
		}
	}
}
