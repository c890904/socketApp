package com.display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JTextField tf_ip = new JTextField();
	private JTextField tf_port = new JTextField();
	private JTextArea ta_request = new JTextArea();
	private JTextArea ta_response = new JTextArea();
	
	private JButton btn_connect = new JButton("Connect");
	private JButton btn_disconnect = new JButton("Disconnect");
	private JButton btn_send = new JButton("Send Message");
	
	private Socket socket;
	private Thread msr;
	
	private byte[] receivedData;
	
	public Main() {
		//App Title
		super("TCPIP Socket");
		
		//Button Action Setting
		btn_connect.addActionListener(this);
		btn_disconnect.addActionListener(this);
		btn_send.addActionListener(this);
		
		//Top Area
		JPanel pnl1 = new JPanel();
		GridLayout gl1 = new GridLayout(1, 6);
		pnl1.setLayout(gl1);
		pnl1.add(new JLabel("IP"));
		pnl1.add(tf_ip);
		pnl1.add(new JLabel("PORT"));
		pnl1.add(tf_port);
		pnl1.add(btn_connect);
		btn_disconnect.setEnabled(false);
		pnl1.add(btn_disconnect);
		
		//Middle Area
		//Request
		GridBagConstraints gbc_request = new GridBagConstraints();
		gbc_request.fill = GridBagConstraints.BOTH;
		gbc_request.insets = new Insets(0, 0, 5, 0);
		gbc_request.gridx = 0;
		gbc_request.gridy = 0;
		ta_request.setFont(new Font("Monospaced", Font.PLAIN, 16));
		
		//Button
		GridBagConstraints gbc_btn = new GridBagConstraints();
		gbc_btn.anchor = GridBagConstraints.NORTH;
		gbc_btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn.insets = new Insets(0, 0, 5, 0);
		gbc_btn.gridx = 0;
		gbc_btn.gridy = 1;
		btn_send.setEnabled(false);
		
		//Response
		GridBagConstraints gbc_response = new GridBagConstraints();
		gbc_response.fill = GridBagConstraints.BOTH;
		gbc_response.gridx = 0;
		gbc_response.gridy = 2;
		ta_request.setLineWrap(true);
		ta_response.setFont(new Font("Monospaced", Font.PLAIN, 16));
		ta_response.setLineWrap(true);
		ta_response.setEditable(false);
		ta_request.setEditable(false);
		
		//Set
		JPanel pnl2 = new JPanel();
		GridBagLayout gbl_pnl2 = new GridBagLayout();
		gbl_pnl2.columnWidths = new int[] {684, 0};
		gbl_pnl2.rowHeights = new int[] {300, 20, 300, 0};
		gbl_pnl2.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl_pnl2.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnl2.setLayout(gbl_pnl2);
		
		pnl2.add(ta_request, gbc_request);
		pnl2.add(btn_send, gbc_btn);
		pnl2.add(ta_response, gbc_response);
		
		//Set General View
		Container contentPane = this.getContentPane();
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPane.add(pnl1, BorderLayout.NORTH);
		contentPane.add(pnl2, BorderLayout.CENTER);
		
		this.setLocation(500, 400);
		this.setPreferredSize(new Dimension(700, 500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new Main();
	}
	
	public void actionPerformed(ActionEvent ae) {
		try {
			//Connect
			if(ae.getSource().equals(btn_connect)) {
				socket = new Socket();
				socket.connect(new InetSocketAddress(tf_ip.getText(), Integer.parseInt(tf_port.getText())));
				
				msr = new Server_Thread(new DataInputStream(socket.getInputStream()));
				msr.start();
				
				changeView(true);
			//Send
			}else if(ae.getSource().equals(btn_send)) {
				ta_response.setText("");
				
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				byte[] bar = new byte[ta_request.getText().length() / 2];
				
				for(int i = 0; i < bar.length; i++) {
					bar[i] = (byte)Integer.parseInt(ta_request.getText().substring(i * 2, (i + 1) * 2), 16);
				}
				
				dos.write(bar);
				
			//Disconnect
			}else if(ae.getSource().equals(btn_disconnect)) {
				msr = null;
				socket.close();
				
				changeView(false);
			}
		}catch(Exception e) {
			if(!socket.isClosed()) {
				try {
					socket.close();
				} catch(Exception e1) {}
				changeView(false);
				e.printStackTrace();
				ta_response.setText(e.toString());
			}
		}
	}
	
	private void changeView(boolean state) {
		//Connected
		if(state) {
			tf_ip.setEditable(false);
			tf_port.setEditable(false);
			btn_connect.setEnabled(false);
			
			ta_request.setEditable(true);
			btn_disconnect.setEnabled(true);
			btn_send.setEnabled(true);

		//Disconnected
		} else {
			tf_ip.setEditable(true);
			tf_port.setEditable(true);
			btn_connect.setEnabled(true);
			
			ta_request.setEditable(false);
			btn_disconnect.setEnabled(false);
			btn_send.setEnabled(false);
			
			ta_request.setText("");
		}
		
		//Common
		ta_response.setText("");
	}
	
	class Server_Thread extends Thread {
		DataInputStream dis;
		FileOutputStream fos;
		
		public Server_Thread(DataInputStream dis) {
			this.dis = dis;
		}
		
		@Override
		public void run() {
			try {
				ArrayList<Byte> arr = new ArrayList<Byte>();
				
				while(true) {
					int int_first = dis.read();
					if(int_first == -1) return;
					
					ta_response.setText(ta_response.getText() + String.format("%02X", int_first & 0xff));
					arr.add((byte)int_first);
					
					if(dis.available() <= 0) {
						System.out.println("End Receive");
						receivedData = new byte[arr.size()];
						for(int i = 0; i < arr.size(); i++) {
							receivedData[i] = arr.get(i);
						}
						arr.clear();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
