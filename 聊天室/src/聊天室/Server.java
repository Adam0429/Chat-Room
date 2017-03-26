package 聊天室;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] Args){
		new Server().go();
	}
	public Server(){
		
	}
	public void go(){
		try{
			ServerSocket ss=new ServerSocket(8888);//ServerSocket会监听客户端对这台机器在8888端口上的要求
			while(true){
				Socket s=ss.accept();//accept会停下来等到要求到达才会继续
				PrintWriter w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
				w.println("你好");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
