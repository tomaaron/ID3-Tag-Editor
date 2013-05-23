package model;

import java.io.File;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import model.id3.ID3Decoder;

/**
 * Wrapper class representing a tree. Extends from DefaultTreeModel to benefit
 * from its event system: Each time we change the nodes, this class fires an
 * event, which the JTree listens to and updates itself without us doing
 * anything.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 */
@SuppressWarnings("serial")
public class FileTree extends DefaultTreeModel {

	/**
	 * initializes the tree with null as root.
	 */
	public FileTree() {
		super(null);
	}

	public static DefaultMutableTreeNode getData(File dir) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Directory(
				dir.getAbsolutePath()));
		getData(dir, root);
		return root;
	}

	public static void getData(File dir, DefaultMutableTreeNode currentParent) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String currentPath = files[i].getAbsolutePath();
				if (files[i].isDirectory()) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
							new Directory(currentPath));
					currentParent.add(newNode);
					getData(files[i], newNode);
				} else if (currentPath.endsWith("mp3")) {
					MP3 newMP3 = new MP3(currentPath);
					if (ID3Decoder.decode(newMP3)) {
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
								newMP3);
						currentParent.add(newNode);
					}
				}
			}
		}
	}

	/**
	 * inserts a new node, at the given path
	 * 
	 * slightly modified version of add method from here:
	 * http://www.java-forum.org
	 * /bilder-gui-damit-zusammenhaengt/23427-jtree-teil
	 * -4-veraenderliche-daten.html just for testing, until fileIO works
	 * 
	 * @param parent
	 * @param child
	 */
	public void add(TreePath parent, MutableTreeNode child) {
		// Den Knoten einbauen
		int index = getChildCount(parent.getLastPathComponent());
		((MutableTreeNode) parent.getLastPathComponent()).insert(child, index);

		// Die Listener unterrichten
		TreeModelEvent event = new TreeModelEvent(this, // Quelle des Events
				parent, // Pfad zum Vater des ver�nderten Knoten
				new int[] { index }, // Index des ver�nderten Knotens
				new Object[] { child }); // Der neue Knoten

		for (TreeModelListener listener : this.getTreeModelListeners())
			listener.treeNodesInserted(event);
	}

}
