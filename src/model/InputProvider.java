package model;

/**
 * This interface is required by Application.saveInputInTree so that we can
 * easily change JTextFields to an editable JTable or something else without
 * needing to change the control logic
 * 
 * @author MPGI 4, WS 11/12, Tutorium 01, Gruppe 05
 * 
 */
public interface InputProvider {
	/**
	 * @return id of containing data
	 */
	public String getKey();

	/**
	 * @return containing data
	 */
	public String getValue();
}
