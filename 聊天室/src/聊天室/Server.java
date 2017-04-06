package 聊天室;

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
		clientOutputStreams = new ArrayList<PrintWriter>();
		try{
			ServerSocket ss=new ServerSocket(8888);//ServerSocket会监听客户端对这台机器在8888端口上的要求
			while(true){
				Socket s=ss.accept();//accept会停下来等到要求到达才会继续.
				System.out.println("hello");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				clientOutputStreams.add(w);
				//JOptionPane.showMessageDialog(null, "连接成功！");
				w.println(s.getInetAddress()+"加入群聊");
				count++;
				w.println("现在有"+count+"人在群聊");
				w.flush();
				Thread thread=new Thread(new HandleAClient(s)); 
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
		        	PrintWriter writer = (PrintWriter) it.next();//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
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