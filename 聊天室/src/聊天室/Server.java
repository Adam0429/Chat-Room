package 聊天室;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument.BranchElement;

public class Server {
	BufferedReader br;
	PrintWriter w;
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
				InputStreamReader is=new InputStreamReader(s.getInputStream());
				br=new BufferedReader(is);
				w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
				JOptionPane.showMessageDialog(null, "连接成功！");
				String string="--------已进入聊天室--------";
				w.println(string);//bufferedreader类的必须要回车和flush或close才能接受
				w.flush();
				Thread managerthread=new Thread(new manager()); 
				managerthread.run();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public class manager implements Runnable{
		String message;
		public void run() {
			try{
				while((message=br.readLine())!=null){
					System.out.println(message);
					w.write(message);
					w.flush();
				}
			}
		catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
}