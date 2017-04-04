package ������;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client {
	JFrame f;
	JTextField tf;
	JTextArea ta;
	JButton b;
	BufferedReader br;
	PrintWriter pw;
	Socket s;
	public static void main(String[] Args){
		new Client().go();
		
	}
	public Client(){
		f=new JFrame("������");
		tf=new JTextField();
		ta=new JTextArea();
		b=new JButton("����");
		ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        //JScrollPane qScroller = new JScrollPane(tf);
        //qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(450, 400);
		f.setLayout(null);
		b.setBounds(270,300,90,30);
		b.addActionListener(new MyButton());
		ta.setBounds(50,50,300,230);
		tf.setBounds(50,300,200,20);
		f.add(tf);
		f.add(ta);
		f.add(b);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		try{
			s=new Socket("127.0.0.1", 8888);//������Ҫ��ȡ��������˿ڵ����ӣ�����Ҫ�����з���������	
			InputStreamReader is=new InputStreamReader(s.getInputStream());			
			br=new BufferedReader(is);
			pw=new PrintWriter(s.getOutputStream());
			Thread readthread=new Thread(new incomingReader());
			readthread.start();
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
			pw.println(string);
			pw.flush();
			tf.setText("");
			tf.requestFocus();//����������ؼ���
		}
	}
	
	public class incomingReader implements Runnable{
		public void run() {
			String message;             
	           try {
	             while ((message = br.readLine()) != null) { 
	            	//readline�������з��������һ��,���һ���Ҫflush()��close()����Ϊ���������������������ǲ�����յ����ݵ�
	                System.out.println("read " + message);
	                ta.append(message + "\n");
	             }
	           }
	           catch(Exception e) {
	        	   e.printStackTrace();
	           }
		}
		
	}
}
