package model;

import java.io.File;

/**
 * Wrapper class representing a directory.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
@SuppressWarnings("serial")
public class Directory extends File {

	private String location;
	private String value;

	public Directory(String value) {
		super(value);
		this.setValue(this.getName());
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * toString method is required by JTree to display a name
	 */
	public String toString() {
		return value;
	}

}
