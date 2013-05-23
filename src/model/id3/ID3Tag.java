package model.id3;
/**
 * 
 * represents the whole mp3 tag
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
import java.util.Hashtable;

public class ID3Tag {

	private int padding;
	private int size;
	private Hashtable<String, ID3FrameText> textFrames;
	private Hashtable<Byte, ID3FrameAttachedPicture> attachedPictureFrames;
	private Hashtable<String, ID3FrameBytes> byteFrames;

	public ID3Tag() {
		this.textFrames = new Hashtable<String, ID3FrameText>();
		this.attachedPictureFrames = new Hashtable<Byte, ID3FrameAttachedPicture>();
		this.byteFrames = new Hashtable<String, ID3FrameBytes>();

	}

	public void addTextFrame(ID3FrameText frame) {
		textFrames.put(frame.getID(), frame);
	}

	public void addAttachedPictureFrame(ID3FrameAttachedPicture frame) {
		attachedPictureFrames.put(frame.getPictureType(), frame);
	}

	public void addByteFrame(ID3FrameBytes frame) {
		byteFrames.put(frame.getID(), frame);
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public int getPadding() {
		return this.padding;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

	public boolean existsTextFrame(String id) {
		return textFrames.containsKey(id);
	}

	public boolean existsAttachedPictureFrame(int type) {
		return attachedPictureFrames.containsKey(type);
	}

	public String getTextValue(String id) {
		String result = "";
		if (textFrames.containsKey(id)) {
			result = textFrames.get(id).getValue();
		}
		return result;
	}

	public boolean setTextValue(String id, String value) {
		boolean result = false;
		if (textFrames.containsKey(id)) {
			ID3FrameText frame = textFrames.get(id);
			frame.setValue(value);
			result = true;
		}
		return result;
	}

	public byte[] getAttachedPicture(byte pictureType) {
		byte[] result = new byte[0];
		if (attachedPictureFrames.containsKey(pictureType)) {
			result = attachedPictureFrames.get(pictureType).getPictureData();
		}
		return result;
	}
	
	public boolean setAttachedPicture(byte pictureType, byte[] pictureData, String mimeType) {
		boolean result = false;
		if (attachedPictureFrames.containsKey(pictureType)) {
			ID3FrameAttachedPicture frame = attachedPictureFrames
					.get(pictureType);
			frame.setPictureData(pictureData);
			frame.setMimeType(mimeType);
			result = true;
		}
		return result;
	}

	public boolean setAttachedPicture(byte pictureType, byte[] pictureData) {
		boolean result = false;
		if (attachedPictureFrames.containsKey(pictureType)) {
			ID3FrameAttachedPicture frame = attachedPictureFrames
					.get(pictureType);
			frame.setPictureData(pictureData);
			result = true;
		}
		return result;
	}

	public ID3FrameText[] getTextFrames() {
		return textFrames.values().toArray(new ID3FrameText[0]);
	}

	public ID3FrameAttachedPicture[] getAttachedPictureFrames() {
		return attachedPictureFrames.values().toArray(
				new ID3FrameAttachedPicture[0]);
	}

	public ID3FrameBytes[] getByteFrames() {
		return byteFrames.values().toArray(new ID3FrameBytes[0]);
	}

	public Hashtable<Byte, ID3FrameAttachedPicture> getAttachedPicutreFrames() {
		return attachedPictureFrames;
	}

	public String getMimeTypeOfAttachedPicture(int type) {
		String result = "";
		if (attachedPictureFrames.get(type) != null) {
			result = attachedPictureFrames.get(type).getMimeType();
		}
		return result;
	}

}
