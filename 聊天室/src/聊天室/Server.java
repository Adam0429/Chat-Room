package 聊天室;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Server {
	BufferedReader br;
	PrintWriter w;
	ArrayList<PrintWriter> clientOutputStreams;
	public static void main(String[] Args){
		new Server().go();
	}
	public Server(){
		
	}
	public void go(){
		try{
			ServerSocket ss=new ServerSocket(8887);//ServerSocket会监听客户端对这台机器在8888端口上的要求
			while(true){
				Socket s=ss.accept();//accept会停下来等到要求到达才会继续
				JOptionPane.showMessageDialog(null, "连接成功！");
				Thread thread=new Thread(new HandleAClient(s)); 
				thread.run();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public class HandleAClient implements Runnable{
		Socket s;
		public HandleAClient(Socket socket){
			this.s=socket;
			try{
				InputStreamReader is=new InputStreamReader(s.getInputStream());
				br=new BufferedReader(is);
				w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void run() {
			try{
				w.println(s.getInetAddress()+"--------已进入聊天室--------");//bufferedreader类的必须要回车和flush或close才能接受
				w.flush();
				String message;
				while((message=br.readLine())!=null){	//关闭client会有报错 java.net.SocketException: Connection reset
					System.out.println(message);
					w.println(message);
					w.flush();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}