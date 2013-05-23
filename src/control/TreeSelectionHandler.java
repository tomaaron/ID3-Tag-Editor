package control;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Directory;
import model.MP3;
import model.Model;
import view.View;

/**
 * 
 * Listens on events fired by the JTree component
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class TreeSelectionHandler implements TreeSelectionListener {

	/**
	 * This method is always called, when selection state of the JTree changes.
	 * We have to cover two cases: 1. The user hits the [+] or [-] buttons,
	 * which causes the tree to expand or collapse. In this case nothing will be
	 * selected. Then we have to do the same, as when the user hits a directory
	 * node: We firstly have to make sure we store data the user might have
	 * entered before and second reset and disable input fields 3. The user hits
	 * a mp3 node. Then we have to load its data into the input fields
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object source = e.getSource();
		JTree jtree = (JTree) source;
		/*
		 * We can't "if-else-likely" check, if we have a directory node or null,
		 * because we have to make a try casting the node to Directory. So we
		 * just call the Application to save, just to be sure. This is OK,
		 * because the saving method checks, if activeInputField is null
		 */
		Application.getInstance().saveInputInTree();
		View.getInstance().setActiveInputField(null);

		// might be null, if the user has hit the [+] or [-] buttons
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree
				.getLastSelectedPathComponent();
		// if node is null, we have nothing to do here
		if (node == null)
			return;
		Object obj = node.getUserObject();
		if (obj instanceof Directory) {
			View.getInstance().resetEditPanel();
			View.getInstance().setInputsEnabled(false);
		} else if (obj instanceof MP3) {
			MP3 file = (MP3) obj;
			Model.getInstance().setActiveFile(file);
			View.getInstance().setEditPanelContent(file);
			View.getInstance().setInputsEnabled(true);
		} else {
			System.err
					.println("Selected node nor was a Dir, neither a MP3 file");
			System.exit(-1);
		}
	}
}
