package model.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.Directory;
import model.MP3;
import model.Model;
import model.id3.ID3FrameAttachedPicture;
import model.id3.ID3FrameBytes;
import model.id3.ID3FrameText;
import model.id3.ID3Tag;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * XML Parser class.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class XMLParser {
	private Document doc;
	private String xmlLocation;
	public XMLParser(File xml) {
		FileInputStream is = null;
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setValidating(true);
			fact.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			is = new FileInputStream(xml);
			xmlLocation=Model.getInstance().getRootDir().getAbsolutePath();
			this.doc = builder.parse(is);
			createFileTreeviaXML();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public long getTimestamp() {
		String value = doc.getChildNodes().item(1).getAttributes()
				.getNamedItem("timestamp").getNodeValue();
		return Long.parseLong(value);
	}
/**
 * constructs via XML a FileTree and returns the root if it
 * @return root of FileTree
 */
	public DefaultMutableTreeNode createFileTreeviaXML() {
		NodeList dataList = doc.getElementsByTagName("cache");
		NodeList cacheList = dataList.item(0).getChildNodes();
		Directory rootDir = new Directory(Model.getInstance().getRootDir().getAbsolutePath());
		DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(
				rootDir);
		NodeList cacheList2 = cacheList.item(0).getChildNodes();
		XMLtoNode(cacheList2, rootTreeNode);
		return rootTreeNode;
	}
/**
 * help function of createFileTreeviaXML, recursivly through the XML 
 * @param list NodeList
 * @param treeNode parentTreeNode
 */
	public void XMLtoNode(NodeList list, DefaultMutableTreeNode treeNode) {
		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (item.getNodeName().equals("folder")) {
				Directory dir = new Directory(xmlLocation + System.getProperty("file.separator") + item.getAttributes()
						.getNamedItem("location").getTextContent());
				DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(
						dir);
				treeNode.add(newTreeNode);
				XMLtoNode(item.getChildNodes(), newTreeNode);
			}
			if (item.getNodeName().equals("file")) {
				MP3 newmp3 = new MP3(xmlLocation + System.getProperty("file.separator") +item.getAttributes().getNamedItem("path")
						.getTextContent());
				newmp3.setMapping();
				ID3Tag id3Tag = new ID3Tag();
				if (item.hasChildNodes()) {
					NodeList tag = item.getChildNodes();
					NodeList tagNodes = tag.item(0).getChildNodes();
					for (int c = 0; c < tagNodes.getLength(); c++) {
						// text frames
						if (tagNodes.item(c).getNodeName().equals("textframe")) {
							Node textFrame = tagNodes.item(c);
							byte[] flags = new byte[2];
							flags[0] = Byte.parseByte(textFrame.getAttributes()
									.getNamedItem("flag1").getTextContent());
							flags[1] = Byte.parseByte(textFrame.getAttributes()
									.getNamedItem("flag2").getTextContent());
							String id = textFrame.getAttributes()
									.getNamedItem("id").getTextContent();
							byte encoding = Byte.parseByte(textFrame.getAttributes()
									.getNamedItem("encoding").getTextContent());
							String encodingName = textFrame.getAttributes()
									.getNamedItem("encodingName").getTextContent();
							String value = textFrame.getChildNodes().item(0)
									.getTextContent();
							ID3FrameText mp3textframe = new ID3FrameText(id,
									flags, value, encoding, encodingName);
							id3Tag.addTextFrame(mp3textframe);
						}
						if (tagNodes.item(c).getNodeName().equals("byteframe")) {
							Node byteFrame = tagNodes.item(c);
							byte[] flags = new byte[2];
							flags[0] = Byte.parseByte(byteFrame.getAttributes()
									.getNamedItem("flag1").getTextContent());
							flags[1] = Byte.parseByte(byteFrame.getAttributes()
									.getNamedItem("flag2").getTextContent());
							byte[] bytes = Base64.decodeBase64(byteFrame
									.getChildNodes().item(0).getTextContent());
							ID3FrameBytes mp3byteframe = new ID3FrameBytes(
									byteFrame.getAttributes()
											.getNamedItem("id")
											.getTextContent(), flags, bytes);
							id3Tag.addByteFrame(mp3byteframe);

						}
						if (tagNodes.item(c).getNodeName()
								.equals("attachedpictureframe")) {
							Node attachedPicFrame = tagNodes.item(c);
							byte[] flags = new byte[2];
							flags[0] = Byte.parseByte(attachedPicFrame
									.getAttributes().getNamedItem("flag1")
									.getTextContent());
							flags[1] = Byte.parseByte(attachedPicFrame
									.getAttributes().getNamedItem("flag2")
									.getTextContent());
							byte[] picbytes = Base64
									.decodeBase64(attachedPicFrame
											.getChildNodes().item(0)
											.getTextContent());
							String picid = attachedPicFrame.getAttributes()
									.getNamedItem("id").getTextContent();
							String pictype = attachedPicFrame
									.getAttributes()
									.getNamedItem("picturetype")
									.getTextContent();
							String mimetype = attachedPicFrame
									.getAttributes().getNamedItem("mimetype")
									.getTextContent();
							String desc = attachedPicFrame
									.getAttributes().getNamedItem("desc")
									.getTextContent();
							byte descEncoding = Byte.parseByte(attachedPicFrame
									.getAttributes().getNamedItem("descEncoding")
									.getTextContent());
							String descEncodingName = attachedPicFrame
									.getAttributes().getNamedItem("descEncodingName")
									.getTextContent();
							ID3FrameAttachedPicture mp3apicframe = new ID3FrameAttachedPicture(
									picid, flags, 
									Byte.parseByte(pictype), picbytes, mimetype, descEncoding, descEncodingName, desc);
							id3Tag.addAttachedPictureFrame(mp3apicframe);
						}
					}
				}
				newmp3.setTag(id3Tag);
				DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(
						newmp3);
				treeNode.add(newTreeNode);
			}
		}
	}

}
