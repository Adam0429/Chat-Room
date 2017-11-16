package ������;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.xml.soap.Text;

import org.omg.CORBA.PUBLIC_MEMBER;

import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import java.awt.List;
import java.awt.TextArea;


public class Server {
	File file;
	HashMap<String, PrintWriter> clientOutputStreams;
	ArrayList<String> clients;
	int count;
	JTextField textField_1;
    TextArea textArea;
	JFrame jf;
	JPanel panel;
	String IPlist;
	JButton b;
	
	public static void main(String[] Args){
		Server server=new Server();
		//server.go();
	}
	public Server(){
		IPlist ="";
		clients = new ArrayList<String>();

		jf=new JFrame("Server");
		panel=new JPanel();
		b=new JButton("����");
		b.setBounds(42, 43, 63, 27);
		b.addActionListener(new blistener());
		panel.setLayout(null);
		panel.add(b);
		jf.getContentPane().add(BorderLayout.CENTER,panel);
		JButton btnNewButton = new JButton("Kick");
		btnNewButton.setBounds(31, 201, 139, 27);
		panel.add(btnNewButton);
		
		textField_1 = new JTextField();
		textField_1.setBounds(31, 151, 139, 24);
		textArea = new TextArea();
		textArea.setBounds(212, 33, 140, 252);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("IP online");
		lblNewLabel.setBounds(216, 13, 72, 18);
		panel.add(lblNewLabel);
		
		
		panel.add(textArea);
		jf.setVisible(true);
		jf.setSize(400, 342);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		count=0;
		

	}
	
	public void updatelist(){
		Iterator<String> i = clients.iterator();

		while(i.hasNext()){
		
			IPlist = IPlist + i.next()+'\n';
	
		}
		System.out.println(IPlist);
	}
	
	public void go(){

		clientOutputStreams = new HashMap<String, PrintWriter>();			
		try{
			ServerSocket ss=new ServerSocket(8888);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			Socket s;
			while(true){
				s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����.
				
				
				System.out.println("�ӽ�����һ��");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				clientOutputStreams.put(s.getInetAddress().toString().split("/")[1],w);
				//JOptionPane.showMessageDialog(null, "���ӳɹ���");
				w.println(s.getInetAddress().toString().split("/")[1]+"����Ⱥ��");
				w.flush();
				HandleAClient hc = new HandleAClient(s);
				clients.add(s.getInetAddress().toString().split("/")[1]);
				updatelist();

				textArea.setText(IPlist);
				Thread thread=new Thread(hc); 
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
						tellSomeone(strings,from ,to);
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
		
		public void tellSomeone(String message, String from, String to){
			try {
				PrintWriter fromwriter=clientOutputStreams.get(from);
				PrintWriter towriter= clientOutputStreams.get(to);
				
				towriter.println("from say:"+message);
				
				towriter.flush();
				
				fromwriter.println("say to "+to+" "+message);
				fromwriter.flush();
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