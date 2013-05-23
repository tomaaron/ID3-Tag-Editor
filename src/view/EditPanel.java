package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ImageHandler;
import model.MP3;

/**
 * Right panel of the main window, containing components, necessary to edit the
 * tag of a MP3 file.
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 */
@SuppressWarnings("serial")
public class EditPanel extends JPanel {
	static JLabel labelPicture = new JLabel();
	static JLabel labelTitle = new JLabel("title:");
	static JLabel labelArtist = new JLabel("artist:");
	static JLabel labelAlbum = new JLabel("album:");
	static JLabel labelYear = new JLabel("year:");
	static JLabel labelCover = new JLabel("cover:");
	static InputField textFieldTitle = new InputField();
	static InputField textFieldArtist = new InputField();
	static InputField textFieldAlbum = new InputField();
	static InputField textFieldYear = new InputField();
	static JPanel inputPanel = new JPanel();
	static JButton changeCoverButton = new JButton("change cover");
	static JButton removeCoverButton = new JButton("remove cover");

	/**
	 * Creates all GUI components, necessary to edit the tag of a MP3 file.
	 */
	public EditPanel() {
		this.setInputNames();
		this.setPreferredSize(new Dimension(Constants.INITIAL_EDITPANEL_WIDTH,
				Constants.INITIAL_WINDOW_HEIGHT));
		this.setLayout(new BorderLayout());

		labelPicture.setPreferredSize(new Dimension(1,
				Constants.MAX_FRONT_COVER_HEIGHT));

		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

		this.addVerticalSpace(inputPanel);

		this.addLabel(inputPanel, labelTitle);
		this.addInput(inputPanel, textFieldTitle);

		this.addVerticalSpace(inputPanel);

		this.addLabel(inputPanel, labelArtist);
		this.addInput(inputPanel, textFieldArtist);

		this.addVerticalSpace(inputPanel);

		this.addLabel(inputPanel, labelAlbum);
		this.addInput(inputPanel, textFieldAlbum);

		this.addVerticalSpace(inputPanel);

		this.addLabel(inputPanel, labelYear);
		this.addInput(inputPanel, textFieldYear);

		this.addVerticalSpace(inputPanel);

		this.addLabel(inputPanel, labelCover);
		this.addVerticalSpace(inputPanel);
		this.addCover(inputPanel);

		this.addVerticalSpace(inputPanel);

		this.addCoverButtons(inputPanel);

		this.addVerticalSpace(inputPanel);

		this.add(inputPanel, BorderLayout.PAGE_START);

	}

	/**
	 * Adds a vertical space to a given JPanel.
	 * 
	 * @param panel - the JPanel, the vertical space should be added to.
	 */
	private void addVerticalSpace(JPanel panel) {
		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createRigidArea(new Dimension(0,
				Constants.DEFAULT_VERTICAL_SPACE)));
		panel.add(hBox);
	}

	/**
	 * Adds the label for displaying a front cover to a given panel.
	 * The label is centered inside a horizontal box.
	 * 
	 * @param panel - the JPanel, the label should be added to.
	 */
	private void addCover(JPanel panel) {
		Box hBox = Box.createHorizontalBox();
		labelPicture.setAlignmentX(Box.CENTER_ALIGNMENT);
		hBox.add(labelPicture);
		panel.add(hBox, BorderLayout.PAGE_START);
	}

	/**
	 * Adds buttons for changing the front cover to a given panel.
	 * 
	 * @param panel - the JPanel, the buttons should be added to.
	 */
	private void addCoverButtons(JPanel panel) {
		Box hBox = Box.createHorizontalBox();
		hBox.add(changeCoverButton);
		hBox.add(Box.createRigidArea(new Dimension(
				Constants.DEFAULT_HORIZONTAL_SPACE, 0)));
		hBox.add(removeCoverButton);
		panel.add(hBox, BorderLayout.PAGE_START);
	}

	/**
	 * Adds a given label correctly formatted to a given panel. It is embedded
	 * into a horizontal box, left aligned, with space left and right.
	 * 
	 * @param panel - the JPanel, the label should be added to.
	 * @param label - the JPanel to add.
	 */
	private void addLabel(JPanel panel, JLabel label) {
		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createRigidArea(new Dimension(
				Constants.DEFAULT_HORIZONTAL_SPACE, 0)));
		hBox.add(label);
		hBox.add(Box.createHorizontalGlue());
		hBox.add(Box.createRigidArea(new Dimension(
				Constants.DEFAULT_HORIZONTAL_SPACE, 0)));
		panel.add(hBox, BorderLayout.PAGE_START);
	}

	/**
	 * Adds a given InputField correctly formatted to a given panel.
	 * It is embedded in an horizontal Box with space on the left and right.
	 * 
	 * @param panel - the JPanel, the InputField should be added to.
	 * @param input - the InputField to add.
	 */
	private void addInput(JPanel panel, InputField input) {
		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createRigidArea(new Dimension(
				Constants.DEFAULT_HORIZONTAL_SPACE, 0)));
		hBox.add(input);
		hBox.add(Box.createRigidArea(new Dimension(
				Constants.DEFAULT_HORIZONTAL_SPACE, 0)));
		panel.add(hBox, BorderLayout.PAGE_START);
	}

	/**
	 * Assigns distinct names to the InputFields.
	 */
	private void setInputNames() {
		textFieldTitle.setName("title");
		textFieldArtist.setName("artist");
		textFieldAlbum.setName("album");
		textFieldYear.setName("year");
	}

	/**
	 * Clears all InputFields.
	 */
	public void reset() {
		textFieldAlbum.setText("");
		textFieldArtist.setText("");
		textFieldTitle.setText("");
		textFieldYear.setText("");
		labelPicture.setIcon(null);
	}

	/**
	 * Modifies the editability of the InputFields.
	 * 
	 * @param enable - either true (enabled) or false (disabled)
	 */
	public void setInputsEnabled(Boolean enable) {
		textFieldAlbum.setEditable(enable);
		textFieldArtist.setEditable(enable);
		textFieldTitle.setEditable(enable);
		textFieldYear.setEditable(enable);
		changeCoverButton.setEnabled(enable);
		removeCoverButton.setEnabled(enable);
		textFieldYear.setFocusable(enable);
		textFieldArtist.setFocusable(enable);
		textFieldAlbum.setFocusable(enable);
		textFieldTitle.setFocusable(enable);
		changeCoverButton.setFocusable(enable);
		removeCoverButton.setFocusable(enable);
	}

	/**
	 * Fills the InputFields with the content of a given MP3 file.
	 * 
	 * @param mp3Node - the mp3 file, that contains the data to be added.
	 */
	public void setContent(MP3 mp3Node) {
		textFieldAlbum.setText(mp3Node.getTextValue("album"));
		textFieldArtist.setText(mp3Node.getTextValue("artist"));
		textFieldTitle.setText(mp3Node.getTextValue("title"));
		textFieldYear.setText(mp3Node.getTextValue("year"));
		ImageIcon icon = ImageHandler.createPicture(mp3Node.getPictureData());
		// if the byte array was empty, the icon-size is negative
		if (icon.getIconHeight() >= 0) {
			labelPicture.setPreferredSize(new Dimension(icon.getIconWidth(),
					icon.getIconHeight()));
		}
		labelPicture.setIcon(icon);
	}
}