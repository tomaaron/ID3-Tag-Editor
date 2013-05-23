package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import model.Model;
import view.View;

/**
 * Listens on events fired by the "directory-select"-button
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class DirectorySelectHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		File newDir = View.getInstance().getNewDirectory();
		if (newDir != null) {
			Model.getInstance().setRootDirectory(newDir);
			View.getInstance().setDirectoryPathText(newDir.getAbsolutePath());
			View.getInstance().setFileTree(Model.getInstance().getFileTree());
		}
	}

}
