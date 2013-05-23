package model.id3;
/**
 * 
 * represents a mp3 frame type: bytes
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ID3FrameBytes extends ID3Frame {

	byte[] bytes;

	public ID3FrameBytes(String id, byte[] flags, byte[] bytes) {
		super(id, flags);
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
