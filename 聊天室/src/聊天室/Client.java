package ������;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client {
	JFrame f;
	JTextField tf;
	JTextArea ta;
	JButton b;
	JButton b2;
	JScrollPane qScroller;
	BufferedReader br;
	PrintWriter pw;
	Socket s;
	String name;
	public static void main(String[] Args){
		new Client().go();
		
	}
	public Client(){
		f=new JFrame("������");
		tf=new JTextField();
		ta=new JTextArea(10,20);
		b=new JButton("BROADCAST");
		b2=new JButton("STATS");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pw.println("[STATS]:"+tf.getText());
					pw.flush();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		ta.setLineWrap(true);//�����Զ����й��� 			
        ta.setWrapStyleWord(true);// ������в����ֹ���	
        ta.setEditable(false);	
        qScroller = new JScrollPane(ta);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(404, 521);
		f.getContentPane().setLayout(null);
		b.setBounds(50,338,152,30);
		b2.setBounds(50,425,310,30);
		b.addActionListener(new MyButton());
		qScroller.setBounds(50,50,300,230);
		tf.setBounds(50,293,287,20);
		f.getContentPane().add(qScroller);
		f.getContentPane().add(tf);
		f.getContentPane().add(b);
		f.getContentPane().add(b2);
		f.setResizable(false);
		JButton btnStop = new JButton("STOP");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pw.println("[STOP]");
					pw.println(name+" close connection");
					pw.flush();
					if(!s.isClosed())
						JOptionPane.showMessageDialog(null, "close connection");
					s.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStop.setBounds(216, 340, 121, 27);
		f.getContentPane().add(btnStop);
		
		JButton btnList = new JButton("LIST");
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pw.println("[LIST]");
				pw.flush();
			}
		});
		btnList.setBounds(50, 381, 152, 27);
		f.getContentPane().add(btnList);
		
		JButton btnNewButton = new JButton("KICK");
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String client = tf.getText();
				pw.println("[KICK]:"+client);
				pw.flush();
				System.out.println("[KICK]"+client);
				
			}
			
		});
		btnNewButton.setBounds(216, 380, 121, 27);
		f.getContentPane().add(btnNewButton);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		try{
			s = new Socket("localhost", 8888);//use to connect server with port
			InputStreamReader is = new InputStreamReader(s.getInputStream());			
			br = new BufferedReader(is);
			pw = new PrintWriter(s.getOutputStream());
			Thread readthread = new Thread(new incomingReader());
			readthread.start();
			JOptionPane.showMessageDialog(null, "connect success��");
			/*
			 * �ֽ�����ȡ��λΪһ���ֽڣ��ַ�����ȡ��λΪһ���ַ� ���Զ�ȡ���ֵ�ʱ��������ֽ����ͻᵼ�¶���������
			 * �������ﲻ��stream����reader��������������stream����Ҫ��InputStreamReaderת�����ַ���
			 */
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public class MyButton implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			String string = tf.getText();
			try {
				pw.println(name+" say:"+string);
				pw.flush();
				tf.setText("");
				tf.requestFocus();//cusor enter this
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	
	public class incomingReader implements Runnable{
		public void run() {
			String message;             
	           try {
	             while ((message = br.readLine()) != null) { 
	            	//readline�������з��������һ��,���һ���Ҫflush()��close()����Ϊ���������������������ǲ�����յ����ݵ�
	            	//System.out.println("read " + message);
	            	if(message.contains("[Give name]:")){		//get its name from server 
	            		name = message.split(":")[1];
	            		ta.append("My name is:"+name+"\n");
	            	}
	            	else{
	            		ta.append(message + "\n");
	            		int height=15;								//�Զ�����
	            		Point p = new Point();
	            		p.setLocation(0,ta.getLineCount()*height);
	            		qScroller.getViewport().setViewPosition(p);
	            	}
	             }
	           }
	           catch(Exception e) {
	        	   e.printStackTrace();
	           }
		}
		
	}
}
