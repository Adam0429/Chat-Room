package ������;

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
			ServerSocket ss=new ServerSocket(8888);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			while(true){
				Socket s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����
				PrintWriter w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
				w.println("���");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
