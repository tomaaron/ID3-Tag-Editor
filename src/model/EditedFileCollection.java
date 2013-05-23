package model;

import java.util.Vector;

@SuppressWarnings("serial")
public class EditedFileCollection extends Vector<MP3> {

	public EditedFileCollection() {
		super();
	}

	public void addFile(MP3 file) {
		this.add(file);
	}

	public boolean containsFile(MP3 file) {
		return this.contains(file);
	}

	public void removeFile(MP3 file) {
		this.remove(file);
	}

	public MP3[] getFiles() {
		return this.toArray(new MP3[0]);
	}

}
