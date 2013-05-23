package model.id3;
/**
 * 
 * represents a mp3 frame type: text
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ID3FrameText extends ID3Frame {

	private String value;
	private byte encoding;
	private String encodingName;

	public ID3FrameText(String id, byte[] flags, String value, byte encoding, String encodingName) {
		super(id, flags);
		this.value = value;
		this.encoding = encoding;
		this.encodingName = encodingName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public byte getEncoding() {
		return encoding;
	}

	public void setEncoding(byte encoding) {
		this.encoding = encoding;
	}

	public String getEncodingName() {
		return encodingName;
	}

	public void setEncodingName(String encodingName) {
		this.encodingName = encodingName;
	}

}
