package model.id3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import model.MP3;
import control.Application;
/**
 * 
 * Decodes a mp3 file
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class ID3Decoder {

	public static final boolean PARSER_DEBUG = false;
	public static final boolean PARSER_INFO = false;

	private static final byte ISO_8859_1_ENCODING = 0;
	private static final byte UNICODE_ENCODING = 1;

	public static boolean decode(MP3 file) {
		try {
			RandomAccessFile raFile = new RandomAccessFile(file, "r");

			byte[] FIb = new byte[3];
			raFile.readFully(FIb);
			if (!new String(FIb).equals("ID3")) {
				if (PARSER_DEBUG)
					Application.log("File: " + file.getAbsolutePath()
							+ " does not have an ID3v2 tag");
				return false;
			}

			byte tagVersion1 = raFile.readByte();
			byte tagVersion2 = raFile.readByte();
			if (tagVersion1 != 3 || tagVersion2 != 0) {
				if (PARSER_DEBUG)
					Application.log("File: " + file.getAbsolutePath()
							+ " has unsupported tag version: ID3v2."
							+ tagVersion1 + "." + tagVersion2);
				return false;
			}

			byte flags = raFile.readByte();
			if (flags != 0) {
				if (PARSER_DEBUG)
					Application.log("File: " + file.getAbsolutePath()
							+ " has unsupported tag flags: " + flags);
				return false;
			}

			int size = raFile.readByte() << 21;
			size += raFile.readByte() << 14;
			size += raFile.readByte() << 7;
			size += raFile.readByte();

			byte[] tag = new byte[size];
			raFile.readFully(tag);
			raFile.close();
			file.setTag(decodeTag(tag));
			file.getTag().setSize(size);
			return file.getTag() != null;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ID3Tag decodeTag(byte[] bytes) {
		ID3Tag tag = new ID3Tag();
		int i = 0;
		while (i < bytes.length) {
			if (bytes[i] == 0) {
				tag.setPadding(bytes.length - i);
				break;
			}
			String ID = new String(bytes, i, 4);
			if (PARSER_INFO)
				Application.log("ID is: " + ID);
			i += 4;
			int fSize = ((bytes[i] < 0) ? bytes[i] + 256 : bytes[i]) << 24;
			i++;
			fSize += ((bytes[i] < 0) ? bytes[i] + 256 : bytes[i]) << 16;
			i++;
			fSize += ((bytes[i] < 0) ? bytes[i] + 256 : bytes[i]) << 8;
			i++;
			fSize += ((bytes[i] < 0) ? bytes[i] + 256 : bytes[i]);
			i++;
			if (PARSER_INFO)
				Application.log("ID's size is: " + fSize);
			byte[] fFlags = new byte[2];
			fFlags[0] = bytes[i];
			i++;
			fFlags[1] = bytes[i];
			i++;
			if (ID3Frame.isFlagSet(fFlags, ID3Frame.COMPRESSION)
					|| ID3Frame.isFlagSet(fFlags, ID3Frame.ENCRYPTION)) {
				return null;
			}
			byte[] fBytes = new byte[fSize];
			System.arraycopy(bytes, i, fBytes, 0, fSize);
			// ID3Frame frame = decodeFrame(ID, fFlags, fBytes);
			// tag.addFrame(frame);
			if (ID.startsWith("T") && !ID.endsWith("XXX")) {
				tag.addTextFrame(decodeTextFrame(ID, fFlags, fBytes));
			} else if (ID.equals("APIC")) {
				tag.addAttachedPictureFrame(decodeAttachedPictureFrame(ID,
						fFlags, fBytes));
			} else {
				tag.addByteFrame(new ID3FrameBytes(ID, fFlags, fBytes));
			}
			i += fSize;
		}
		return tag;
	}

	public static ID3FrameText decodeTextFrame(String ID, byte[] flags,
			byte[] bytes) {
		String value = "";
		byte encoding = bytes[0];
		String encodingName = "";
		try {
			if (encoding == ISO_8859_1_ENCODING) {
				// zero terminated ?
				if (bytes[bytes.length - 1] == 0)
					value = new String(bytes, 1, bytes.length - 2, "ISO-8859-1");
				else
					value = new String(bytes, 1, bytes.length - 1, "ISO-8859-1");
				encodingName =  "ISO-8859-1";
			} else if (encoding == UNICODE_ENCODING) {
				byte[] bom = new byte[2];
				bom[0] = bytes[1];
				bom[1] = bytes[2];
				if (bom[0] == -2 && bom[1] == -1) {
					// zero terminated ?
					if (bytes[bytes.length - 1] == 0
							&& bytes[bytes.length - 2] == 0)
						value = new String(bytes, 3, bytes.length - 5,
								"UTF-16BE");
					else
						value = new String(bytes, 3, bytes.length - 3,
								"UTF-16BE");
					encodingName =  "UTF-16BE";
				} else // if(bom[0] == -1 && bom[1] == -2) <- implicit by
						// id3v2.3.0 specification
				{
					// zero terminated ?
					if (bytes[bytes.length - 1] == 0
							&& bytes[bytes.length - 2] == 0)
						value = new String(bytes, 3, bytes.length - 5,
								"UTF-16LE");
					else
						value = new String(bytes, 3, bytes.length - 3,
								"UTF-16LE");
					encodingName =  "UTF-16LE";
				}
			} else {
				if (bytes[bytes.length - 1] == 0)
					value = new String(bytes, 1, bytes.length - 2, "ISO-8859-1");
				else
					value = new String(bytes, 1, bytes.length - 1, "ISO-8859-1");
				encodingName =  "ISO-8859-1";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ID3FrameText(ID, flags, value, encoding, encodingName);
	}

	private static ID3FrameAttachedPicture decodeAttachedPictureFrame(
			String ID, byte[] flags, byte[] bytes) {
		// setting encoding of description
		byte descEncoding = bytes[0];
		// setting mime-type of attached picture
		int i = 1;
		String mimeType = "";
		while (bytes[i] != 0 && i <= 64) {
			mimeType += (char) bytes[i];
			i++;
		}
		// setting type of attached picture
		i++;
		byte pictureType = bytes[i];
		// setting description of attached picture
		// also setting encoding name of description
		String desc = "";
		String descEncodingName = "";
		i++;
		if (descEncoding == ISO_8859_1_ENCODING) {
			descEncodingName = "ISO-8859-1";
			while (bytes[i] != 0) {
				byte[] b = {bytes[i]};
				try {
					desc += new String(b, descEncodingName);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				i++;
			}
			// skip zero termination
			i++; 
		} else /* if(encoding == UNICODE_ENCODING) <- implicit by id3.2.3 specification */{
			if(bytes[i] == -2 && bytes[i+1] == -1) {
				descEncodingName = "UTF-16BE";
			}
			else /* if(bytes[i] == -1 && bytes[i+1] == -2) <- implicit ... */ {
				descEncodingName = "UTF-16LE";
			}
			// skip bom bytes
			i+= 2;
			while (bytes[i] != 0 && bytes[i+1] != 0) {
				byte[] unicodeChar = new byte[2];
				unicodeChar[0] = bytes[i];
				unicodeChar[1] = bytes[i+1];
				try {
					desc += new String(unicodeChar, descEncodingName);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				i += 2;
			}
			// skip unicode zero termination
			i += 2;
		}
		byte[] pictureData = new byte[bytes.length - i];
		System.arraycopy(bytes, i, pictureData, 0, pictureData.length);
		return new ID3FrameAttachedPicture(ID, flags, pictureType,
				pictureData, mimeType, descEncoding, descEncodingName, desc);
	}

}
