package 聊天室;

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
import java.util.PrimitiveIterator.OfDouble;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.text.GapContent;
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
		b=new JButton("启动");
		b.setBounds(42, 43, 63, 27);
		b.addActionListener(new blistener2());
		panel.setLayout(null);
		panel.add(b);
		jf.getContentPane().add(BorderLayout.CENTER,panel);
		JButton btnNewButton = new JButton("Kick");
		btnNewButton.setBounds(31, 201, 139, 27);
		btnNewButton.addActionListener(new blistener());
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
		go();

	}
	
	public void updatelist(){
		IPlist = "";
		Iterator<String> i = clients.iterator();

		while(i.hasNext()){
		
			IPlist = IPlist + i.next()+'\n';
	
		}
		textArea.setText(IPlist);
		
	}
	
	public void go(){

		clientOutputStreams = new HashMap<String, PrintWriter>();			
		try{
			ServerSocket ss=new ServerSocket(8888);//ServerSocket会监听客户端对这台机器在8888端口上的要求
			Socket s;
			while(true){
				s=ss.accept();//accept会停下来等到要求到达才会继续.
				
				System.out.println("加进来了一个");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				String ip = s.getInetAddress().toString().split("/")[1];
				if(clients.contains(ip)){
					System.out.println("已连入相同ip");	
				}
				else{
				clientOutputStreams.put(ip,w);
				//JOptionPane.showMessageDialog(null, "连接成功！");
				tellEveryone(s.getInetAddress().toString().split("/")[1]+"加入群聊");
				w.flush();
				HandleAClient hc = new HandleAClient(s);
				clients.add(s.getInetAddress().toString().split("/")[1]);
				updatelist();
				tellonlinelist(IPlist);
				tellEveryone("IPlist:"+IPlist);
				Thread thread=new Thread(hc); 
				thread.start();//如果有新的socket加进来，就为其多开一个线程
				}
				
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public class blistener implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			String ip = textField_1.getText();
			PrintWriter p=clientOutputStreams.get(ip);
			tellEveryone(ip+"被踢了");
			p.close();
			clientOutputStreams.remove(ip);
			clients.remove(ip);
			updatelist();
			textArea.setText(IPlist);
//			System.out.println(clients.size());
			tellonlinelist(IPlist);
			
		}
		
	}
	
	public class blistener2 implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			tellEveryone("dsdadsa");
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
				//bufferedreader类的必须要回车和flush或close才能接受
				while((message=br.readLine())!=null){
					/*实际上readLine()是一个阻塞函数，当没有数据读取时，就一直会阻塞在那，而不是返回null
					readLine()只有在数据流发生异常或者另一端被close()掉时，才会返回null值*/
					if(message.contains("aaa")){//用来check是不是私聊
						String from=message.split("aaa")[0];
						String strings=message.split("aaa")[1];
						String to=message.split("aaa")[2];
						System.out.println(from+"对"+to+"说"+strings);
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
		             PrintWriter writer = new PrintWriter((PrintWriter) entry.getValue());//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
					 writer.println(message);
		             writer.flush();
		         }  
		         catch(Exception ex) {
		        	 ex.printStackTrace();
		         }
		     }
		      
		   } // end while
		       
		public void tellonlinelist(String iplist){
			  Iterator it = clientOutputStreams.entrySet().iterator();		     
			     while(it.hasNext()) {
			    	 try {
			             Entry entry = (Entry)it.next();
			             PrintWriter writer = new PrintWriter((PrintWriter) entry.getValue());//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
						 writer.println("IPlist:"+IPlist);
			             writer.flush();
			         }  
			         catch(Exception ex) {
			        	 ex.printStackTrace();
			         }
			
		}
		   // close tellEveryone
	}
}