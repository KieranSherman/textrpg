package main;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.misc.Game;

/*
 * Main method creates a game and plays it
 */
public class Main {
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		UIManager.put("ProgressBar.background", new Color(40, 190, 230, 180));
		UIManager.put("ProgressBar.foreground", Color.BLACK);
		UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
		UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
		
		UIManager.put("ScrollBarUI", "main.ui.components.scrollbars.ScrollBarUI_Vertical");
		UIManager.put("ScrollBarUI", "main.ui.components.scrollbars.ScrollBarUI_Horizontal");
		
		Game g = new Game();
		g.play();
	}
	
}
