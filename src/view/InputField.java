package view;

import javax.swing.JTextField;

import model.InputProvider;

/**
 * A regular JTextField implementing the InputProvider interface
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
@SuppressWarnings("serial")
public class InputField extends JTextField implements InputProvider {

	@Override
	public String getKey() {
		return this.getName();
	}

	@Override
	public String getValue() {
		return this.getText();
	}

}
