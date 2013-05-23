package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Model;
import view.View;

/**
 * Listens on events fired by the "remove the cover"-button
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class CoverRemoveHandler implements ActionListener {

	/**
	 * When the user removes the cover picture an empty picture is generated an
	 * assigned to the active mp3 file
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		byte[] empty = {};
		Model.getInstance().getActiveFile().setPictureData(empty);
		Model.getInstance().getActiveFile().setDeleteAttachedPicture(true);
		Model.getInstance().updateEditStatusOfActiveFile();
		View.getInstance().setEditPanelContent(
				Model.getInstance().getActiveFile());
	}

}
