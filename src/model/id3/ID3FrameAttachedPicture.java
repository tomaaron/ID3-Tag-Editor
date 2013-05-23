package model.id3;
/**
 * 
 * represents a mp3 attached picture frame
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ID3FrameAttachedPicture extends ID3Frame {

	private byte pictureType;
	private byte[] pictureData;
	private String mimeType;
	private byte descTextEncoding;
	private String descTextEncodingName;
	private String desc;

	public ID3FrameAttachedPicture(String id, byte[] flags,
			byte pictureType, byte[] pictureData, String mimeType, byte descTextEncoding, String descTextEncodingName, String desc) {
		super(id, flags);
		this.pictureType = pictureType;
		this.pictureData = pictureData;
		this.mimeType = mimeType;
		this.descTextEncoding = descTextEncoding;
		this.descTextEncodingName = descTextEncodingName;
		this.desc = desc;
	}

	public byte[] getPictureData() {
		return pictureData;
	}

	public void setPictureData(byte[] pictureData) {
		this.pictureData = pictureData;
	}

	public byte getPictureType() {
		return pictureType;
	}

	public void setPictureType(byte pictureType) {
		this.pictureType = pictureType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setDescTextEncoding(byte descTextEncoding) {
		this.descTextEncoding = descTextEncoding;
	}

	public byte getDescTextEncoding() {
		return descTextEncoding;
	}

	public void setDescTextEncodingName(String descTextEncodingName) {
		this.descTextEncodingName = descTextEncodingName;
	}

	public String getDescTextEncodingName() {
		return descTextEncodingName;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
