package view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Lets the user choose a directory or a picture from the file system.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 */
@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {

	private File file;
	private String mode;

	/**
	 * Creates the FileChooser object and sets the mode, the FileChooser
	 * operates in.
	 * 
	 * @param name - the name of the FileChooser
	 * @param mode - either "directories only" or "files only"
	 */
	public FileChooser(String name, String mode) {
		this.setName(name);
		this.setMode(mode);
		if (this.getMode() == "dirOnly") {
			this.setFileSelectionMode(DIRECTORIES_ONLY);
			this.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "dir";
				}

			});
		} else {
			this.setFileSelectionMode(FILES_ONLY);
			this.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".jpg")
							|| f.getName().toLowerCase().endsWith(".png")
							|| f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "*.jpg, *.png";
				}

			});
		}
		this.setDialogType(OPEN_DIALOG);
	}

	/**
	 * Pops up a window representing the file system and lets the user choose a
	 * file or a directory.
	 */
	public void popup() {
		int result;
		if (this.getMode() == "dirOnly") {
			result = this.showDialog(null, "Select Directory");
		} else {
			result = this.showDialog(null, "Select Picture");
		}

		if (result == JFileChooser.APPROVE_OPTION && this.getSelectedFile().exists()) {
			setFile(this.getSelectedFile());
		} else {
			System.out.println("No File/Directory chosen.");
			setFile(null);
		}
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the file or the directory, the user has chosen.
	 * 
	 * @return the file or the directory, the user has chosen.
	 */
	public File getFile() {
		return file;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}