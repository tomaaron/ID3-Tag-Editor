package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.InputProvider;
import view.View;

public class KeyHandler implements KeyListener{

	@Override
	public void keyPressed(KeyEvent e) {
		View.getInstance().setActiveInputField((InputProvider) e.getSource());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Application.getInstance().saveInputInTree();
		View.getInstance().setActiveInputField(null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
