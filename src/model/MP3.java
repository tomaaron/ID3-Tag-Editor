package model;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import model.id3.ID3FrameAttachedPicture;
import model.id3.ID3FrameText;
import model.id3.ID3Tag;

/**
 * 
 * represents a mp3 file
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
@SuppressWarnings("serial")
public class MP3 extends File {

	private Hashtable<String, String> newTextFrameValues;
	private byte[] newAttachedPictureData;

	private Hashtable<String, String> textFrameMapping;
	private byte attachedPictureMapping;
	private String newMimeType;

	private ID3Tag tag;
	private boolean deleteAttachedPictureFrame;

	/**
	 * constructs a MP3 object with just specifying the filename, which will be
	 * displayed in the tree
	 * 
	 * @param filename
	 */
	public MP3(String pathToFile) {
		super(pathToFile);
		newTextFrameValues = new Hashtable<String, String>();
	}

	public void setTag(ID3Tag tag) {
		this.tag = tag;
		this.setMapping();
		this.setValuesFromTag();
	}

	public void setMapping() {
		this.textFrameMapping = new Hashtable<String, String>();
		textFrameMapping.put("title", "TIT2");
		textFrameMapping.put("artist", "TPE1");
		textFrameMapping.put("album", "TALB");
		textFrameMapping.put("year", "TYER");
		attachedPictureMapping = 3;
	}

	public String getTextMappingOf(String key) {
		return textFrameMapping.get(key);
	}

	public byte getAttachedPictureMapping() {
		return attachedPictureMapping;
	}

	public String isMappedBy(String id) {
		String result = null;
		if (textFrameMapping.contains(id)) {
			for (Enumeration<String> e = textFrameMapping.keys(); e
					.hasMoreElements();) {
				String key = e.nextElement();
				if (textFrameMapping.get(key).equals(id)) {
					result = key;
					break;
				}
			}
		}
		return result;
	}

	public void setValuesFromTag() {
		newTextFrameValues.put("title",
				tag.getTextValue(getTextMappingOf("title")));
		newTextFrameValues.put("artist",
				tag.getTextValue(getTextMappingOf("artist")));
		newTextFrameValues.put("album",
				tag.getTextValue(getTextMappingOf("album")));
		newTextFrameValues.put("year",
				tag.getTextValue(getTextMappingOf("year")));
		newAttachedPictureData = tag.getAttachedPicture(attachedPictureMapping);
		newMimeType = tag.getMimeTypeOfAttachedPicture(attachedPictureMapping);
	}

	public void setFramesFromValues() {
		String[] keys = { "title", "artist", "album", "year" };
		for (String key : keys) {
			if (isTextValueEdited(key)) {
				tag.setTextValue(getTextMappingOf(key),
						newTextFrameValues.get(key));
			}
		}
		if (isAttachedPictureEdited()) {
			tag.setAttachedPicture(attachedPictureMapping,
					newAttachedPictureData, newMimeType);
			
		}
	}

	public ID3Tag getTag() {
		return tag;
	}

	public String getTextValue(String key) {
		String result = null;
		if (textFrameMapping.containsKey(key)) {
			result = newTextFrameValues.get(key);
		}
		return result;
	}

	public boolean setTextValue(String key, String value) {
		boolean result = false;

		if (textFrameMapping.containsKey(key)) {
			newTextFrameValues.put(key, value);
			result = true;
		}
		return result;
	}

	public byte[] getPictureData() {
		return newAttachedPictureData;
	}

	public void setPictureData(byte[] pictureData) {
		newAttachedPictureData = pictureData;
	}

	private boolean isTextValueEdited(String key) {
		return !newTextFrameValues.get(key).equals(
				tag.getTextValue(textFrameMapping.get(key)));
	}

	private boolean isAttachedPictureEdited() {
		System.out.println(!Arrays.equals(newAttachedPictureData,
				tag.getAttachedPicture(attachedPictureMapping)));
		return !Arrays.equals(newAttachedPictureData,
				tag.getAttachedPicture(attachedPictureMapping));
	}

	public boolean isEdited() {
		boolean coverEdited = !newAttachedPictureData.equals(tag
				.getAttachedPicture(attachedPictureMapping));
		return isTextValueEdited("title") || isTextValueEdited("album")
				|| isTextValueEdited("artist") || isTextValueEdited("year")
				|| coverEdited;
	}

	public void createMissingFrames() {
		byte[] flags = { 0, 0 };
		if (isTextValueEdited("title")
				&& !tag.existsTextFrame(textFrameMapping.get("title")))
			tag.addTextFrame(new ID3FrameText(textFrameMapping.get("title"),
					flags, "", (byte) 1, "UTF-16LE"));
		if (isTextValueEdited("album")
				&& !tag.existsTextFrame(textFrameMapping.get("album")))
			tag.addTextFrame(new ID3FrameText(textFrameMapping.get("album"),
					flags, "", (byte) 1, "UTF-16LE"));
		if (isTextValueEdited("artist")
				&& !tag.existsTextFrame(textFrameMapping.get("artist")))
			tag.addTextFrame(new ID3FrameText(textFrameMapping.get("artist"),
					flags, "", (byte) 1, "UTF-16LE"));
		if (isTextValueEdited("year")
				&& !tag.existsTextFrame(textFrameMapping.get("year")))
			tag.addTextFrame(new ID3FrameText(textFrameMapping.get("year"),
					flags, "", (byte) 1, "UTF-16LE"));
		if (isAttachedPictureEdited()
				&& !tag.existsAttachedPictureFrame(attachedPictureMapping)) {
			tag.addAttachedPictureFrame(new ID3FrameAttachedPicture("APIC",
					flags, attachedPictureMapping, new byte[0],
					"image/jpeg", (byte) 0, "ISO-8859-1", ""));
		}

	}

	public void setAttachedPictureMimeType(String mimeType) {
		newMimeType = mimeType;
	}

	public String getAttachedPictureMimeType() {
		return newMimeType;
	}

	public String toString() {
		return this.getName();
	}

	public void setDeleteAttachedPicture(boolean delete) {
		this.deleteAttachedPictureFrame = delete;
	}

	public boolean getDeleteAttachedPicture() {
		return this.deleteAttachedPictureFrame;
	}

}
