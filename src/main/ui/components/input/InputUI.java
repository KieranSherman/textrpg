package main.ui.components.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.ui.Window;
import main.ui.components.Developer;
import main.ui.components.misc.PopupUI;
import network.Adapter;
import network.packet.types.Packet03Message;
import sound.SoundPlayer;
import util.Resources;

public class InputUI {
	
	private InputUI() {}
	
	public static JPanel createInput() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(new Color(15, 15, 15));
		inputPanel.setForeground(Color.WHITE);
		
		JLabel promptText = new JLabel("out:: ");
		promptText.setForeground(Color.WHITE);
		promptText.setFont(Resources.USER_INPUT);
		inputPanel.add(promptText, BorderLayout.WEST);
		
		Window.input = new JTextField();
		Window.input.setFont(Resources.USER_INPUT);
		Window.input.setBackground(new Color(15, 15, 15));
		Window.input.setForeground(Color.WHITE);
		Window.input.setCaretColor(Color.WHITE);
		Window.input.setBorder(null);
		inputPanel.add(Window.input, BorderLayout.CENTER);
		
		Border lineB = BorderFactory.createLineBorder(Color.WHITE);
		Border b = BorderFactory.createTitledBorder(lineB, "COMMS", 
				TitledBorder.CENTER, TitledBorder.TOP, Resources.DOS.deriveFont(16f), Resources.DARK_GREEN);
		Border compound = BorderFactory.createCompoundBorder(b, new EmptyBorder(0, 10, 10, 10));
		
		inputPanel.setBorder(compound);
		
		Window.input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = Window.input.getText();

				Window.appendText("> "+str);
				Window.input.setText("");
				
				if(str != null && !str.equals(""))
					Adapter.sendPacket(new Packet03Message(str));
			}
		});
		
		Window.input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					SoundPlayer.play("key"+((int)(Math.random()*10)+1));
			}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		AbstractAction command = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				PopupUI.promptInput("{ ENTER COMMAND }");
				String command = PopupUI.getData();
				Window.input.setText("");
				Developer.parseCommand(command);
			}
		};
		
		Window.input.getInputMap().put(KeyStroke.getKeyStroke('`'), "EnterCommand");
		Window.input.getActionMap().put("EnterCommand", command);
		Window.input.setEnabled(false);
		
		return inputPanel;
	}
	
}
