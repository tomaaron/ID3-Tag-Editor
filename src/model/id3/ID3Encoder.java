package model.id3;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import model.MP3;
/**
 * 
 * Encodes a mp3 file
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ID3Encoder {

	public static Byte[] encode(MP3 file) {

		Vector<Byte> result = new Vector<Byte>();
		ID3Tag tag = file.getTag();

		// Tag Header
		result.add((byte) 'I');
		result.add((byte) 'D');
		result.add((byte) '3');
		// version
		result.add((byte) 3);
		result.add((byte) 0);
		// Flags
		result.add((byte) 0);
		// remember indices 6 - 9 for tag size
		result.add((byte) 0);
		result.add((byte) 0);
		result.add((byte) 0);
		result.add((byte) 0);

		for (ID3FrameBytes frame : tag.getByteFrames()) {

			String id = frame.getID();
			addFrameID(result, id);
			byte[] bytes = frame.getBytes();
			// frame size
			int size = bytes.length;
			addFrameSize(result, size);
			for (byte b : frame.getFlags()) {
				result.add(b);
			}
			// frame data
			for (byte b : bytes) {
				result.add(b);
			}
		}
		for (ID3FrameText frame : tag.getTextFrames()) {
			// writing frame id
			String id = frame.getID();
			addFrameID(result, id);
			
			String value;
			String key;
			if ((key = file.isMappedBy(id)) != null) {
				value = file.getTextValue(key);
			} else {
				value = tag.getTextValue(id);
			}
			// converting textual information to byte array
			byte[] byteValue = null;
			try {
				byteValue = value.getBytes(frame.getEncodingName());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// calculating size
			int size = byteValue.length;
			size += 1; // encoding byte
			if(frame.getEncoding() == 1)
				size += 2; // two BOM bytes
			// writing size
			addFrameSize(result, size);
			// writing flags
			for (byte b : frame.getFlags()) {
				result.add(b);
			}
			// writing textual information
			// starting with encoding plus BOM if necessary
			result.add(frame.getEncoding());
			if(frame.getEncoding() == 1) {
				if(frame.getEncodingName() == "UTF-16BE") {
					result.add((byte) -2);
					result.add((byte) -1);
				}
				else {
					result.add((byte) -1);
					result.add((byte) -2);
				}
			}
			// writing converted textual information
			for (byte b : byteValue) {
				result.add(b);
			}
			
		}
		for (ID3FrameAttachedPicture frame : tag.getAttachedPictureFrames()) {
			
			if (!file.getDeleteAttachedPicture()) {
				// writing frame id
				addFrameID(result, frame.getID());
				// calculation frame size
				int frameSize = 1; // desc encoding
				frameSize += frame.getMimeType().length() + 1; // mime type + zero termination
				frameSize += 1;
				if(frame.getDescTextEncoding() == 0) {
					frameSize += frame.getDesc().length() + 1; // description + zero termination
				}
				else if(frame.getDescTextEncoding() == 1) {
					frameSize += 2; // unicode BOM
					frameSize += (frame.getDesc().length() + 1) * 2; // (unicode) description + zero termination
				}
				frameSize += frame.getPictureData().length;
				// writing frame size
				addFrameSize(result, frameSize);
				// writing frame flags
				for (byte b : frame.getFlags()) {
					result.add(b);
				}
				// writing desc encoding
				byte descEncoding = frame.getDescTextEncoding();
				result.add(descEncoding);
				// writing mime type
				try {
					for(byte b : frame.getMimeType().getBytes("ISO-8859-1")) {
						result.add(b);
					}
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				// adding zero termination for mime type
				result.add((byte) 0);
				// writing picture type
				result.add(frame.getPictureType());
				// writing description
				if(descEncoding == 0) {
					try {
						for(byte b : frame.getDesc().getBytes(frame.getDescTextEncodingName())) {
							result.add(b);
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// adding zero termination for iso desc
					result.add((byte) 0);
				}
				else /* if(descEncoding == 1) <- implicit by spec */ {
					// adding BOM for unicode desc
					if(frame.getDescTextEncodingName() == "UTF-16BE") {
						result.add((byte) -2);
						result.add((byte) -1);
					}
					else /* (frame.getDescTextEncodingName() == "UTF-16LE") <- implicit */ {
						result.add((byte) -1);
						result.add((byte) -2);
					}
					try {
						for(byte b : frame.getDesc().getBytes(frame.getDescTextEncodingName())) {
							result.add(b);
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// adding zero termination for unicode desc
					result.add((byte) 0);
					result.add((byte) 0);
				}
				// writing picture data
				for (byte b : file.getPictureData()) {
					result.add(b);
				}
			}
			
		}
		addTagSize(result, file);
		return result.toArray(new Byte[0]);
	}

	private static void addTagSize(Vector<Byte> result, MP3 file) {
		int size = result.size() - 10;
		if (size < file.getTag().getSize()) {
			for (; size < file.getTag().getSize(); size++) {
				result.add((byte) 0);
			}
		}
		result.set(6, (byte) (size >> 21));
		result.set(7, (byte) ((size >> 14) & 127));
		result.set(8, (byte) ((size >> 7) & 127));
		result.set(9, (byte) (size & 127));
	}

	private static void addFrameID(Vector<Byte> result, String id) {
		// frame id
		result.add((byte) id.charAt(0));
		result.add((byte) id.charAt(1));
		result.add((byte) id.charAt(2));
		result.add((byte) id.charAt(3));
	}

	public static void addFrameSize(Vector<Byte> result, int size) {
		result.add((byte) (size >> 24));
		result.add((byte) ((size >> 16) & 255)); // 00000000 00000000 00000000
													// 11111111 = 255
		result.add((byte) ((size >> 8) & 255));
		result.add((byte) (size & 255));
	}

}
