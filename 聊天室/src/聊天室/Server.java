package 聊天室;

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
//	PrintWriter pw;//输入文件用的
	HashMap<String, PrintWriter> clientOutputStreams;
	ArrayList<String> clients;
	int count;
	public static void main(String[] Args){
		Server server=new Server();
		//server.go();
	}
	public Server(){
		clients = new ArrayList<String>();
//		file =new File("C:/Users/wfh/Desktop/聊天记录.txt");//不是这条指令指定创建文件
//		try {
//			pw = new PrintWriter(new FileWriter(file));//如果file不存在则创建
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		JFrame jf=new JFrame("Server");
		JPanel panel=new JPanel();
		JPanel panel2=new JPanel();
		JButton b=new JButton("启动");//因为启动执行的是线程,线程会一直跑,所以按钮会卡住
		JButton b2=new JButton("停止");//因为启动会卡主，所以停止也不能用了，放这里装样子
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
			ServerSocket ss=new ServerSocket(8888);//ServerSocket会监听客户端对这台机器在8888端口上的要求
			while(true){
				Socket s=ss.accept();//accept会停下来等到要求到达才会继续.
				System.out.println("加进来了一个");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				clientOutputStreams.put(s.getInetAddress().toString().split("/")[1],w);
				//JOptionPane.showMessageDialog(null, "连接成功！");
				w.println(s.getInetAddress().toString().split("/")[1]+"加入群聊");
				w.flush();
				HandleAClient hc = new HandleAClient(s);				Thread thread=new Thread(hc); 
				thread.start();//如果有新的socket加进来，就为其多开一个线程
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
				//bufferedreader类的必须要回车和flush或close才能接受
				while((message=br.readLine())!=null){
					/*实际上readLine()是一个阻塞函数，当没有数据读取时，就一直会阻塞在那，而不是返回null
					readLine()只有在数据流发生异常或者另一端被close()掉时，才会返回null值*/
					if(message.contains("aaa")){//用来check是不是私聊
						String from=message.split("aaa")[0];
						String strings=message.split("aaa")[1];
						String to=message.split("aaa")[2];
						System.out.println(from+"对"+to+"说"+strings);
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
		             PrintWriter writer = new PrintWriter((PrintWriter) entry.getValue());//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
//		    		 pw.println(message);//输入到文件里
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