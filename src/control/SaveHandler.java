package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import model.MP3;
import model.Model;
import model.id3.ID3Encoder;

public class SaveHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {

		Application.getInstance().saveInputInTree();

		MP3[] filesToSave = Model.getInstance().getFilesToSave();
		for (MP3 file : filesToSave) {
			file.createMissingFrames();
			file.setFramesFromValues();
			Byte[] newTag = ID3Encoder.encode(file);

			if (newTag.length - 10 == file.getTag().getSize()) {
				System.out.println("writing...");
				try {
					RandomAccessFile ra = new RandomAccessFile(file, "rw");
					for (byte b : newTag) {
						ra.write(b);
					}
					ra.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("done");

			} else {
				try {
					int oldTagSize = file.getTag().getSize() + 10;
					long mp3DataSize = file.length() - oldTagSize;
					int newFileSize = (int) (newTag.length + mp3DataSize);
					byte[] newFile = new byte[newFileSize];

					int i;
					for (i = 0; i < newTag.length; i++) {
						newFile[i] = newTag[i];
					}
					
					RandomAccessFile ra = new RandomAccessFile(file, "r");
					ra.skipBytes(oldTagSize);
					ra.readFully(newFile, newTag.length, (int) mp3DataSize);
					ra.close();

					FileOutputStream fos = new FileOutputStream(file);

					BufferedOutputStream bos = new BufferedOutputStream(fos);

					System.out.println("schreiben");

					bos.write(newFile);
					bos.flush();
					fos.flush();
					bos.close();

					fos.close();

					System.out.println("feddisch");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Model.getInstance().cacheFileTree();
		Model.getInstance().updateEditStatusOfActiveFile();
	}
}