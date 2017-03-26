package 聊天室;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	public static void main(String[] Args){
		new Client().go();
	}
	public Client(){
	
	}
	public void go(){
		try{
			Socket s=new Socket("localhost",8888);//这里是要获取与服务器端口的连接，所以要先运行服务器程序
			InputStreamReader is=new InputStreamReader(s.getInputStream());
			BufferedReader b=new BufferedReader(is);
			System.out.println(b.readLine());
			b.close();
			/*
			 * 字节流读取单位为一个字节，字符流读取单位为一个字符 所以读取汉字的时候，如果用字节流就会导致读出来乱码
			 * 所以这里不用stream而用reader。但读进来的是stream所以要用InputStreamReader转换成字符型
			 */
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
