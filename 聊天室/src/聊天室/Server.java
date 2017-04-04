package 聊天室;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class Server {
	ArrayList<PrintWriter> clientOutputStreams;
	public static void main(String[] Args){
		new Server().go();
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
				JOptionPane.showMessageDialog(null, "连接成功！");
				Thread thread=new Thread(new HandleAClient(s)); 
				thread.start();//如果有新的socket加进来，就为其多开一个线程
				System.out.println("here");
			}
		}
		catch(Exception e){
			e.printStackTrace();
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
		         } catch(Exception ex) {
		              ex.printStackTrace();
		         }
		      
		       } // end while
		       
		   } // close tellEveryone
	}
}