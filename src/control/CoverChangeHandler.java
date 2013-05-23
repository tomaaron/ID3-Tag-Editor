package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import model.ImageHandler;
import model.MP3;
import model.Model;
import view.View;

/**
 * Listens to events fired by the "change-picture"-button
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class CoverChangeHandler implements ActionListener {

	/**
	 * When the user wants to change the picture, for now just the other picture
	 * is loaded from the TestImages library
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File newPic = View.getInstance().getNewPicture();
		if (newPic != null) {
			try {
				byte[] newPicture = null;
				newPicture = ImageHandler.createByteArray(newPic);
				MP3 activeFile = Model.getInstance().getActiveFile();
				activeFile.setDeleteAttachedPicture(false);
				if (newPic.getName().toLowerCase().endsWith("jpg")
						|| newPic.getName().toLowerCase().endsWith("jpeg")) {
					activeFile.setAttachedPictureMimeType("image/jpeg");
				} else {
					activeFile.setAttachedPictureMimeType("image/png");
				}
				System.out.println(new MimetypesFileTypeMap().getContentType(newPic));
				Model.getInstance().getActiveFile().setPictureData(newPicture);
				View.getInstance().setEditPanelContent(activeFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Model.getInstance().updateEditStatusOfActiveFile();
	}
}