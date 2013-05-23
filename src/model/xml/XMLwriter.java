package model.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Directory;
import model.MP3;
import model.id3.ID3FrameAttachedPicture;
import model.id3.ID3FrameBytes;
import model.id3.ID3FrameText;
import model.id3.ID3Tag;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/**
 * XML Writer class.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public class XMLwriter {

	public final Document doc;
	
	public String cacheabspath;

	public XMLwriter() {
		Document doc = null;
		try {
			DocumentBuilderFactory docFact = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = docFact.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		this.doc = doc;
		this.doc.appendChild(this.doc.createElement("cache"));
		Attr tsAttr = doc.createAttribute("timestamp");
		tsAttr.setNodeValue(Long.toString(System.currentTimeMillis()));
		this.doc.getChildNodes().item(0).getAttributes().setNamedItem(tsAttr);
	}
	/**
	 * creates in the choosen directory a XML file
	 * @param dmtnode root from FileTree
	 * @param jetztigenode XML root node
	 */
	public void createXML(DefaultMutableTreeNode dmtnode, Node jetztigenode) {
		
		if (dmtnode.getUserObject() instanceof model.Directory) {
			// neue dir erstellen
			Node folder = addFolder((Directory) dmtnode.getUserObject(),
					jetztigenode);
			if (dmtnode.getChildCount() > 0) {
				for (int counter = 0; counter < dmtnode.getChildCount(); counter++) {
					createXML(
							(DefaultMutableTreeNode) dmtnode
									.getChildAt(counter),
							folder);
				}
			}
		}
		if (dmtnode.getUserObject() instanceof model.MP3) {
			MP3 mp3object = (MP3) dmtnode.getUserObject();
			Node filenode = addFile(mp3object, jetztigenode);
			Node tagnode = addTag(filenode);
			// tag holen
			ID3Tag mp3tag = mp3object.getTag();
			// frametextE holen
			ID3FrameText[] mp3frametexte = mp3tag.getTextFrames();
			// for each frametex jeweils ein frameText in XML erstellen und an
			// die tag node ranh�ngen
			for (int counter = 0; counter < mp3frametexte.length; counter++) {
				addFrameText(mp3frametexte[counter], tagnode);
			}
			ID3FrameBytes[] mp3framebytes = mp3tag.getByteFrames();
			for (int counter = 0; counter < mp3framebytes.length; counter++) {
				addByteFrame(mp3framebytes[counter], tagnode);
			}
			ID3FrameAttachedPicture[] mp3frameattachedpicture = mp3tag
					.getAttachedPictureFrames();
			for (int counter = 0; counter < mp3frameattachedpicture.length; counter++) {
				if (mp3frameattachedpicture[counter].getPictureType() != mp3object
						.getAttachedPictureMapping()
						|| !mp3object.getDeleteAttachedPicture())
					addFrameAttachedPicture(mp3frameattachedpicture[counter],
							tagnode);
			}
		}

	}
	public Node addFolder(Directory file, Node node) {
		// node erstellen
		Node folder = doc.createElement("folder");
		// attribute erstellen
		Attr name = doc.createAttribute("name");
		Attr location = doc.createAttribute("location");
		String relativepath = new File(cacheabspath).toURI().relativize(file.toURI()).getPath();
		// attribute wert zuweisen : folder name
		name.setNodeValue(file.getValue());
		location.setNodeValue(relativepath);
		// attribute dem node zuweisen
		folder.getAttributes().setNamedItem(name);
		folder.getAttributes().setNamedItem(location);
		// node hinzuf�gen
		node.appendChild(folder);
		return folder;
	}

	public Node addFile(File file, Node node) {
		// node file erstellen
		Node filenode = doc.createElement("file");
		// attributes:
		// * name�
		Attr path = doc.createAttribute("path");
		String relativepath = new File(cacheabspath).toURI().relativize(file.toURI()).getPath();
		path.setNodeValue(relativepath);
		Attr name = doc.createAttribute("name");
		name.setNodeValue(file.getName());
		filenode.getAttributes().setNamedItem(path);
		filenode.getAttributes().setNamedItem(name);
		// File hinzuf�gen innerhalb des aktuellen Ordners
		node.appendChild(filenode);
		return filenode;
	}

	public void addFrameText(ID3FrameText frameText, Node node) {
		// node file erstellen
		Node textframenode = doc.createElement("textframe");
		Node valuenode = doc.createElement("value");
		valuenode.setTextContent(frameText.getValue());
		// attributes:
		// * id
		Attr id = doc.createAttribute("id");
		id.setNodeValue(frameText.getID());
		textframenode.getAttributes().setNamedItem(id);
		// attributes:
		// * flag1
		Attr flag1 = doc.createAttribute("flag1");
		byte[] flags = frameText.getFlags();
		Byte.toString(flags[0]);
		flag1.setNodeValue(Byte.toString(flags[0]));
		textframenode.getAttributes().setNamedItem(flag1);
		// attributes:
		// * flag2
		Attr flag2 = doc.createAttribute("flag2");
		flag2.setNodeValue(Byte.toString(flags[1]));
		textframenode.getAttributes().setNamedItem(flag2);
		textframenode.getAttributes().setNamedItem(flag1);
		// attributes:
		// * encoding
		Attr encoding = doc.createAttribute("encoding");
		encoding.setNodeValue(Byte.toString(frameText.getEncoding()));
		textframenode.getAttributes().setNamedItem(encoding);
		// attributes:
		// * encodingName
		Attr encodingName = doc.createAttribute("encodingName");
		encodingName.setNodeValue(frameText.getEncodingName());
		textframenode.getAttributes().setNamedItem(encodingName);
		// File hinzuf�gen innerhalb des aktuellen Ordners
		textframenode.appendChild(valuenode);
		node.appendChild(textframenode);
	}

	public Node addTag(Node node) {
		Node tag = doc.createElement("tag");
		node.appendChild(tag);
		return tag;
	}

	public void addByteFrame(ID3FrameBytes frameBytes, Node node) {
		// node file erstellen
		Node byteframenode = doc.createElement("byteframe");
		Node bytesnode = doc.createElement("bytes");
		String encoded = new String(Base64.encodeBase64(frameBytes.getBytes()));
		bytesnode.setTextContent(encoded);
		// attributes:
		// * id
		Attr id = doc.createAttribute("id");
		id.setNodeValue(frameBytes.getID());
		byteframenode.getAttributes().setNamedItem(id);
		// attributes:
		// * flag1
		Attr flag1 = doc.createAttribute("flag1");
		byte[] flags = frameBytes.getFlags();
		Byte.toString(flags[0]);
		flag1.setNodeValue(Byte.toString(flags[0]));
		byteframenode.getAttributes().setNamedItem(flag1);
		// attributes:
		// * flag2
		Attr flag2 = doc.createAttribute("flag2");
		Byte.toString(flags[0]);
		flag2.setNodeValue(Byte.toString(flags[1]));
		byteframenode.getAttributes().setNamedItem(flag2);

		byteframenode.appendChild(bytesnode);
		node.appendChild(byteframenode);
	}

	public void addFrameAttachedPicture(
			ID3FrameAttachedPicture frameAttachedPicture, Node node) {
		// node file erstellen
		Node attachedpicnode = doc.createElement("attachedpictureframe");
		Node picdatanode = doc.createElement("picturedata");
		String encodedpicdata = new String(
				Base64.encodeBase64(frameAttachedPicture.getPictureData()));
		picdatanode.setTextContent(encodedpicdata);
		// attributes:
		// * id
		Attr id = doc.createAttribute("id");
		id.setNodeValue(frameAttachedPicture.getID());
		attachedpicnode.getAttributes().setNamedItem(id);
		// attributes:
		// * pictype
		Attr pictyp = doc.createAttribute("picturetype");
		pictyp.setNodeValue(Byte.toString(frameAttachedPicture.getPictureType()));
		attachedpicnode.getAttributes().setNamedItem(pictyp);
		// attributes:
		// * mimetype
		Attr mimetype = doc.createAttribute("mimetype");
		mimetype.setNodeValue(frameAttachedPicture.getMimeType());
		attachedpicnode.getAttributes().setNamedItem(mimetype);
		// attributes:
		// * flag1
		Attr flag1 = doc.createAttribute("flag1");
		byte[] flags = frameAttachedPicture.getFlags();
		flag1.setNodeValue(Byte.toString(flags[0]));
		attachedpicnode.getAttributes().setNamedItem(flag1);
		// attributes:
		// * flag2
		Attr flag2 = doc.createAttribute("flag2");
		flag2.setNodeValue(Byte.toString(flags[1]));
		attachedpicnode.getAttributes().setNamedItem(flag2);
		// attributes:
		// * desc
		Attr desc = doc.createAttribute("desc");
		desc.setNodeValue(frameAttachedPicture.getDesc());
		attachedpicnode.getAttributes().setNamedItem(desc);
		// attributes:
		// * descEncoding
		Attr descEncoding = doc.createAttribute("descEncoding");
		descEncoding.setNodeValue(Byte.toString(frameAttachedPicture.getDescTextEncoding()));
		attachedpicnode.getAttributes().setNamedItem(descEncoding);
		// attributes:
		// * descEncodingName
		Attr descEncodingName = doc.createAttribute("descEncodingName");
		descEncodingName.setNodeValue(frameAttachedPicture.getDescTextEncodingName());
		attachedpicnode.getAttributes().setNamedItem(descEncodingName);

		attachedpicnode.appendChild(picdatanode);
		node.appendChild(attachedpicnode);
	}
	/**
	 * writes data to XML file
	 * @param xml 
	 */
	public void writeToDisk(File xml) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(xml), "UTF-8"));
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();

			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
					"4");

			// if we'd like to use a dtd file:
			t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "cache.dtd");

			t.transform(new DOMSource(doc), new StreamResult(writer));
		} catch (TransformerException ex) {
			System.err.println("TransformerException: " + ex.getMessage());
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
