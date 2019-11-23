package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Main extends JFrame implements ActionListener{
	JTextField tf_ip, tf_port;
	JTextArea ta_request, ta_response;
	JButton b_connect, b_disconnect, b_send;
	Socket socket;
	Thread msr;
	
	public Main() {
		b_connect.addActionListener(this);
		b_disconnect.addActionListener(this);
		b_send.addActionListener(this);
		
		JPanel panel1 = new JPanel();
		GridLayout gl1 = new GridLayout(1, 6);
		panel1.setLayout(gl1);
	
		JPanel panel2 = new JPanel();
		GridBagLayout gbl2 = new GridBagLayout();
		gbl2.columnWidths = new int[] {684, 0};
		gbl2.rowHeights = new int[] {300, 20, 300, 0};
		gbl2.columnWeights = new double[] {0.0, Double.MIN_VALUE};
		gbl2.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl2);
		
		GridBagConstraints gbc_ta_request = new GridBagConstraints();
		gbc_ta_request.fill = GridBagConstraints.BOTH;
		gbc_ta_request.insets = new Insets(0, 0, 5, 0);
		gbc_ta_request.gridx = 0;
		gbc_ta_request.gridy = 0;
		
		GridBagConstraints gbc_b_send = new GridBagConstraints();
		gbc_b_send.anchor = GridBagConstraints.NORTH;
		gbc_b_send.fill = GridBagConstraints.HORIZONTAL;
		gbc_b_send.insets = new Insets(0, 0, 5, 0);
		gbc_b_send.gridx = 0;
		gbc_b_send.gridy = 1;
		
		GridBagConstraints gbc_ta_response = new GridBagConstraints();
		gbc_ta_response.fill = GridBagConstraints.BOTH;
		gbc_ta_response.gridx = 0;
		gbc_ta_response.gridy = 2;
		
		ta_request.setLineWrap(true);
		
		panel2.add(ta_request, gbc_ta_request);
		panel2.add(b_send, gbc_b_send);
		panel2.add(ta_response, gbc_ta_response);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel1, BorderLayout.NORTH);
		contentPane.add(panel2, BorderLayout.CENTER);
		
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
			if(ae.getSource().equals(b_send)) {
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
