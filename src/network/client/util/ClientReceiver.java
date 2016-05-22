package network.client.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import network.Adapter;
import network.packet.Packet;
import network.util.NetworkTypes;

public class ClientReceiver extends Thread {
	
	private ObjectInputStream sInput;
	
	public ClientReceiver(Socket socket) throws IOException {
		this.sInput = new ObjectInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		super.setName("ClientThread-ClientReceiverThread");
		
		do {
			Packet packet = null;
			
			if(sInput != null)
				try {
					packet = getPacket();
				} catch (IOException e) {
					break;
				}
			
			if(packet == null)
				continue;
			
			Adapter.parsePacket(NetworkTypes.CLIENT, packet);
		} while (true);
	}
	
	/*
	 * returns the packet from the input stream
	 */
	protected Packet getPacket() throws IOException {
		Packet packet = null;
		try {
			packet = (Packet) sInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return packet;
	}
	
	/*
	 * closes the input stream
	 */
	public void close() {
		if(sInput != null)
			try {
				sInput.close();
			} catch (IOException e) {
				System.err.println("error closing client input stream");
			}
	}

}
