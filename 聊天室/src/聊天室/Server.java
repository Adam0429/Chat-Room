package ������;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.print.attribute.standard.Severity;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Server {
	ArrayList<PrintWriter> clientOutputStreams;
	int count;
	public static void main(String[] Args){
		Server server=new Server();
	}
	public Server(){
		JFrame jf=new JFrame("Server");
		JPanel panel=new JPanel();
		JPanel panel2=new JPanel();
		JButton b=new JButton("����");//��Ϊ����ִ�е����߳�,�̻߳�һֱ��,���԰�ť�Ῠס
		JButton b2=new JButton("ֹͣ");//��Ϊ�����Ῠ��������ֹͣҲ�������ˣ�������װ����
		b.addActionListener(new blistener());
		panel.add(BorderLayout.CENTER,b);
		panel.add(BorderLayout.CENTER,b2);
		jf.getContentPane().add(BorderLayout.CENTER,panel);
		jf.getContentPane().add(BorderLayout.EAST, panel2);
		jf.setVisible(true);
		jf.setSize(400, 80);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		count=0;
	}
	public void go(){
		clientOutputStreams = new ArrayList<PrintWriter>();
		try{
			ServerSocket ss=new ServerSocket(8888);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			while(true){
				Socket s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����.
				System.out.println("hello");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				clientOutputStreams.add(w);
				//JOptionPane.showMessageDialog(null, "���ӳɹ���");
				w.println(s.getInetAddress()+"����Ⱥ��");
				count++;
				w.println("������"+count+"����Ⱥ��");
				w.flush();
				Thread thread=new Thread(new HandleAClient(s)); 
				thread.start();//������µ�socket�ӽ�������Ϊ��࿪һ���߳�
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public class blistener implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			go();
			
		}
		
	}
	
	public class blistener2 implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//	
		}
		
	}
	public class HandleAClient implements Runnable{
		Socket s;
		BufferedReader br;
		public HandleAClient(Socket socket){
			try{
				s=socket;
				InputStreamReader is=new InputStreamReader(s.getInputStream());
				br=new BufferedReader(is);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void run() {
			String message;
			try{
				//bufferedreader��ı���Ҫ�س���flush��close���ܽ���
				;
				while((message=br.readLine())!=null){
					System.out.println(message);
					tellEveryone(message);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		 public void tellEveryone(String message) {
		      Iterator it = clientOutputStreams.iterator();
		      while(it.hasNext()) {
		        try {
		        	PrintWriter writer = (PrintWriter) it.next();//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
		            writer.println(message);
		            writer.flush();
		        }  
		        catch(Exception ex) {
		        	ex.printStackTrace();
		         }
		      
		       } // end while
		       
		   } // close tellEveryone
	}
}