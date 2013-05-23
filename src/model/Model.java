package model;

import java.io.File;
import java.sql.Date;
import java.sql.Time;

import javax.swing.tree.DefaultMutableTreeNode;

import model.xml.XMLParser;
import model.xml.XMLwriter;
import view.View;

/**
 * Model accessor class.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class Model {
	private static Model instance = new Model();

	private static FileTree fileTree = new FileTree();

	private static EditedFileCollection editedFiles = new EditedFileCollection();
	/**
	 * The Model remembers which file the user is editing at the moment
	 */
	private static MP3 activeFile = null;

	private File rootDir;

	private Model() {

	}

	public static Model getInstance() {
		return instance;
	}

	public MP3 getActiveFile() {
		return activeFile;
	}

	public void setActiveFile(MP3 node) {
		activeFile = node;
	}

	/**
	 * sets a single Frame identified by an id in the active MP3 file
	 * 
	 * @param name
	 * @param text
	 */
	public void setTextValueOfActiveFile(String name, String text) {
		activeFile.setTextValue(name, text);
	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public void updateEditStatusOfActiveFile() {
		if (activeFile.isEdited()) {
			if (!editedFiles.containsFile(activeFile)) {
				editedFiles.addFile(activeFile);
				if (editedFiles.size() == 1) {
					View.getInstance().setSaveButtonEnabled(true);
				}
				// System.out.println(activeFile.getName() + " marked");
			} else {
				// System.out.println(activeFile.getName() + " already marked");
			}
		} else {
			if (editedFiles.containsFile(activeFile)) {
				editedFiles.removeFile(activeFile);
				if (editedFiles.size() == 0) {
					View.getInstance().setSaveButtonEnabled(false);
				}
				// System.out.println(activeFile.getName() + " unmarked");
			} else {
				// System.out.println(activeFile.getName() +
				// " still not marked");
			}
		}

	}

	public MP3[] getFilesToSave() {
		return editedFiles.getFiles();
	}

	public void setRootDirectory(File dir) {
		rootDir = dir;
		// wurde dieser Ordner schon gecached?
		boolean cached = false;
		File xml = null;
		for (File file : dir.listFiles()) {
			if (file.getName().equals("cache.xml")) {
				cached = true;
				xml = file;
				break;
			}
		}
		// ist der cache noch aktuell?
		if (cached) {
			File dtd = new File("cache.dtd");
			if(!dtd.exists()){
				System.out.println("No dtd file found. Can't parse XML file, using standard parser.");
				fileTree.setRoot(FileTree.getData(dir));
				cacheFileTree();
			}else{
			XMLParser xmlParser = new XMLParser(xml);
			long cacheLastModified = xmlParser.getTimestamp() + 5000; // plus
			// 5sec
			// for
			// delay
			// when
			// dirLM
			// = LM
			// of
			// cache.xml
			long dirLastModified = getDeepLastModified(dir);
			System.out.println("dir LM: " + new Date(dirLastModified) + " "
					+ new Time(dirLastModified) + " vs. cache LM: "
					+ new Date(cacheLastModified) + " "
					+ new Time(cacheLastModified));
			if (dirLastModified > cacheLastModified) {
				cached = false;
			} else {
				// fileTree aus xml wiederherstellen
				XMLParser parser = new XMLParser(xml);
				fileTree.setRoot(parser.createFileTreeviaXML());
				System.out.println("restored via xml");
			}
		}
		}
		// erster besuch im ordner -> parsen und cachen
		if (!cached) {
			System.out.println("parsed without cache");
			fileTree.setRoot(FileTree.getData(dir));
			cacheFileTree();
		}
	}

	private long getDeepLastModified(File dir) {
		long mostRecentLastModified = dir.lastModified();
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				long deepLastModified = getDeepLastModified(file);
				if (deepLastModified > mostRecentLastModified) {
					mostRecentLastModified = deepLastModified;
				}
			} else if (file.getName().endsWith(".mp3")
					&& file.lastModified() > mostRecentLastModified)
				mostRecentLastModified = file.lastModified();
		}
		return mostRecentLastModified;
	}

	public void cacheFileTree() {

		File xml = new File(rootDir.getAbsolutePath()
				+ System.getProperty("file.separator") + "cache.xml");
		if (xml.exists()) {
			xml.delete();
		}
		XMLwriter XMLwriter = new XMLwriter();
		XMLwriter.cacheabspath = xml.getParentFile().getAbsolutePath();
		System.out.println(((Directory) ((DefaultMutableTreeNode) fileTree
				.getRoot()).getUserObject()).getAbsolutePath());
		XMLwriter.createXML((DefaultMutableTreeNode) fileTree.getRoot(),
				XMLwriter.doc.getChildNodes().item(0));
		XMLwriter.writeToDisk(xml);

	}
	
	public File getRootDir() {
		return rootDir;
	}
}
