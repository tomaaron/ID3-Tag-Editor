package model.id3;
/**
 * 
 * represents a mp3 frame
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ID3Frame {

	public static final int TAG_ALTER_PRESERVATION = 0;
	public static final int FILE_ALTER_PRESERVATION = 1;
	public static final int READ_ONLY = 2;
	public static final int COMPRESSION = 3;
	public static final int ENCRYPTION = 4;
	public static final int GROUPING_IDENTIFY = 5;

	private String id;
	private byte[] flags;

	public ID3Frame(String id, byte[] flags) {
		this.id = id;
		this.flags = flags;
	}

	public String getID() {
		return id;
	}

	public byte[] getFlags() {
		return flags;
	}

	public static boolean isFlagSet(byte[] flags, int flag) {
		switch (flag) {
		case TAG_ALTER_PRESERVATION:
			return (flags[0] & 1 << 7) == 1 << 7;
		case FILE_ALTER_PRESERVATION:
			return (flags[0] & 1 << 6) == 1 << 6;
		case READ_ONLY:
			return (flags[0] & 1 << 5) == 1 << 5;
		case COMPRESSION:
			return (flags[1] & 1 << 7) == 1 << 7;
		case ENCRYPTION:
			return (flags[1] & 1 << 6) == 1 << 6;
		case GROUPING_IDENTIFY:
			return (flags[1] & 1 << 5) == 1 << 5;
		}
		return false;
	}

	public boolean isFlagSet(int flag) {
		return isFlagSet(flags, flag);
	}

}
