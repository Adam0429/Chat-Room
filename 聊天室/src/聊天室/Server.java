package ������;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.WriteAbortedException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.print.attribute.standard.Severity;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.omg.PortableInterceptor.USER_EXCEPTION;

public class Server {
	File file;
//	PrintWriter pw;//�����ļ��õ�
	HashMap<String, PrintWriter> clientOutputStreams;
	ArrayList<String> clients;
	int count;
	public static void main(String[] Args){
		Server server=new Server();
		//server.go();
	}
	public Server(){
		clients = new ArrayList<String>();
//		file =new File("C:/Users/wfh/Desktop/�����¼.txt");//��������ָ��ָ�������ļ�
//		try {
//			pw = new PrintWriter(new FileWriter(file));//���file�������򴴽�
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
		clientOutputStreams = new HashMap<String, PrintWriter>();
		try{
			ServerSocket ss=new ServerSocket(8888);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			while(true){
				Socket s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����.
				System.out.println("�ӽ�����һ��");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				clientOutputStreams.put(s.getInetAddress().toString().split("/")[1],w);
				//JOptionPane.showMessageDialog(null, "���ӳɹ���");
				w.println(s.getInetAddress().toString().split("/")[1]+"����Ⱥ��");
				w.flush();
				HandleAClient hc = new HandleAClient(s);				Thread thread=new Thread(hc); 
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
				while((message=br.readLine())!=null){
					/*ʵ����readLine()��һ��������������û�����ݶ�ȡʱ����һֱ���������ǣ������Ƿ���null
					readLine()ֻ���������������쳣������һ�˱�close()��ʱ���Ż᷵��nullֵ*/
					if(message.contains("aaa")){//����check�ǲ���˽��
						String from=message.split("aaa")[0];
						String strings=message.split("aaa")[1];
						String to=message.split("aaa")[2];
						System.out.println(from+"��"+to+"˵"+strings);
						tellSomeone(strings, to);
					}
					else{
						System.out.println(message);
						tellEveryone(message);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void tellSomeone(String message, String ip){
			try {
				PrintWriter writer= clientOutputStreams.get(ip);
				writer.println(message);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
		
		public void tellEveryone(String message) {
		     Iterator it = clientOutputStreams.entrySet().iterator();		     
		     while(it.hasNext()) {
		    	 try {
		             Entry entry = (Entry)it.next();
		             PrintWriter writer = new PrintWriter((PrintWriter) entry.getValue());//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
//		    		 pw.println(message);//���뵽�ļ���
//		             pw.flush();
//		             clients.add(entry.getKey().toString());
		             System.out.println(entry.getKey().toString());
					 writer.println(message);
		             writer.flush();
		         }  
		         catch(Exception ex) {
		        	 ex.printStackTrace();
		         }
//		    	 System.out.println(clients.s);
//		    	 Iterator iterator=clients.iterator();
//		    	 while (it.hasNext()) {
//		    		 System.out.println(it.next());
//		    		 
//				}
		      
		   } // end while
		       
		   // close tellEveryone
	}
}