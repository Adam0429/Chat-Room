package ������;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Clientwfh {
	JFrame f;
	JTextField tf;
	JTextArea ta;
	JButton b;
	JButton b2;
	JScrollPane qScroller;
	BufferedReader br;
	PrintWriter pw;
	Socket s;
	public static void main(String[] Args){
		new Client().go();
		
	}
	public Clientwfh(){
		f=new JFrame("���ר��������");
		tf=new JTextField();
		ta=new JTextArea(10,20);
		b=new JButton("����");
		b2=new JButton("����");
		ta.setLineWrap(true);//�����Զ����й��� 			
        ta.setWrapStyleWord(true);// ������в����ֹ���	
        ta.setEditable(false);	
        ta.setBackground(Color.white);
        tf.addKeyListener(new Send());
        qScroller = new JScrollPane(ta);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(450, 450);
		f.setLayout(null);
		b.setBounds(270,300,90,30);
		b2.setBounds(270,340,90,30);
		b.addActionListener(new MyButton());
		b2.addActionListener(new MyButton2());
		qScroller.setBounds(50,50,300,230);
		//ta.setBounds(50,50,300,230);	
		tf.setBounds(50,300,200,20);
		f.addWindowListener(new logout());
		f.add(qScroller);
		f.add(tf);
		f.add(b);
		f.add(b2);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		try{
			s=new Socket("139.199.94.77", 8888);//������Ҫ��ȡ��������˿ڵ����ӣ�����Ҫ�����з���������	
			InputStreamReader is=new InputStreamReader(s.getInputStream());			
			br=new BufferedReader(is);
			pw=new PrintWriter(s.getOutputStream());
			Thread readthread=new Thread(new incomingReader());
			readthread.start();
			JOptionPane.showMessageDialog(null, "���ӳɹ���");
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
			String string=tf.getText();
			String ip;
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
				pw.println("���"+"˵:"+string);
				pw.flush();
				tf.setText("");
				tf.requestFocus();//����������ؼ���
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class MyButton2 implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			String ip;
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
				pw.println("���"+"˵:"+"Ҫ�Եѵ�С����");
				pw.flush();
				tf.requestFocus();//����������ؼ���
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}
	}
	public class Send implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String string=tf.getText();
				try {
					pw.println("���"+"˵:"+string);
					pw.flush();
					tf.setText("");
					tf.requestFocus();//����������ؼ���
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub		
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
	}
	
	public class logout implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			pw.println("���"+"������");
			pw.flush();
			tf.requestFocus();//����������ؼ���
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public class incomingReader implements Runnable{
		public void run() {
			String message;             
	           try {
	             while ((message = br.readLine()) != null) { 
	            	//readline�������з��������һ��,���һ���Ҫflush()��close()����Ϊ���������������������ǲ�����յ����ݵ�
	            	//System.out.println("read " + message);
	                ta.append(message + "\n");
	                int height=15;								//�Զ�����
	                Point p = new Point();
	                p.setLocation(0,ta.getLineCount()*height);
	                qScroller.getViewport().setViewPosition(p);
	             }
	           }
	           catch(Exception e) {
	        	   e.printStackTrace();
	           }
		}
		
	}
}
