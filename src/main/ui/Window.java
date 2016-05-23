package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;

import main.misc.Developer;
import main.ui.components.display.DisplayUI;
import main.ui.components.display.notification.NotificationUI;
import main.ui.components.display.status.StatusUI;
import main.ui.components.handlers.WindowHandler;
import main.ui.components.input.InputUI;
import main.ui.components.popup.PopupUI;
import sound.SoundPlayer;
import util.Action;
import util.Resources;

/**
 * This class consists exclusively of static methods that initialize the program's window.
 * 
 * @author kieransherman
 * @see #initialize(String[])
 *
 */
public class Window {	
	
	private static JFrame windowFrame;		//JFrame container
	private static JPanel windowPanel;
	
	public static JTextPane terminal;		//Pane to display output
	public static JTextPane notes;			//Pane to display notes
	public static JTextField input;			//Field for input
	
	public static DefaultStyledDocument doc;	//*
	public static StyleContext context;			//* Styled for coloring output
	public static Style style;					//*
	
	// Prevent object instantiation
	private Window() {}
	
	/**
	 * Initializes and displays the window with all components from {@code UI} classes.  Executes
	 * startup commands when finished.
	 * 
	 * @param args the startup commands.
	 * @see DisplayUI
	 * @see StatusUI
	 * @see NotificationUI
	 * @see InputUI
	 */
	public static void initialize(String[] args) {
		createWindow();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			boolean isPressed = false;
				@Override
				public boolean dispatchKeyEvent(KeyEvent e) {
					if((e.getKeyChar() == '`' || e.getKeyChar() == '~') && !isPressed && !PopupUI.popupAlreadyOpen()) {
						isPressed = true;
						PopupUI.promptInput("ENTER COMMAND", true);
						Window.input.setText("");
	
						if(PopupUI.getData() == null) {
							isPressed = false;
							return false;
						}
						
						String command = (String)PopupUI.getData()[0];
						Developer.parseCommand(command);
						isPressed = false;
					} 
					
			        return false;
				}
	    });
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				windowFrame.setVisible(true);
				
				Action load = new Action() {
					public void pre() {
						SoundPlayer.play("tapeInsert");
						SoundPlayer.play("computerBeep1");
					}
					public void execute() {
						DisplayUI.initialize();
						Window.input.setEnabled(true);
						Window.input.requestFocus();
					}
					public void post() {
						NotificationUI.queueNotification("LOGIN FINISHING", 500, null, false);
						
						for(String command : args)
							Developer.parseCommand(command);
					}
				};
				
				NotificationUI.queueNotification("AUTHORIZING", 500, load, false);
			}
		});
	}

	
	/**
	 * Initializes all components of the window
	 */
	private static void createWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				windowFrame = new JFrame(Resources.VERSION);
				windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				windowFrame.addWindowListener(new WindowHandler());
				windowFrame.setResizable(false);
				
				windowPanel = new JPanel();
				windowPanel.setBackground(new Color(15, 15, 15));
				windowPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
				windowPanel.setLayout(new BorderLayout());
				windowPanel.setPreferredSize(new Dimension(Resources.WIDTH, Resources.HEIGHT));
				
				windowPanel.add(DisplayUI.createDisplay(), BorderLayout.CENTER);
				windowPanel.add(InputUI.createInput(), BorderLayout.SOUTH);
				
				windowFrame.add(windowPanel);
				windowFrame.pack();
				windowFrame.setLocationByPlatform(true);
				windowFrame.setLocationRelativeTo(null);
				
				SoundPlayer.play("computerStartup");
				SoundPlayer.loop("computerHum");
				SoundPlayer.loop("computerHardDrive");
				SoundPlayer.loop("clock");
			}
		});
	}
	
	/**
	 * Returns the {@link JFrame} object of the window.
	 * 
	 * @return the JFrame.
	 */
	public static JFrame getFrame() {
		return windowFrame;
	}
	
	/**
	 * Returns the {@link JPanel} object of the window.
	 * 
	 * @return the JPanel.
	 */
	public static JPanel getPanel() {
		return windowPanel;
	}
	
}