package ������;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Server {
	public static void main(String[] Args){
		new Server().go();
	}
	public Server(){
		
	}
	public void go(){
		try{
			ServerSocket ss=new ServerSocket(8887);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			while(true){
				Socket s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����
				InputStreamReader is=new InputStreamReader(new BufferedInputStream(s.getInputStream()));
				PrintWriter w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
				JOptionPane.showMessageDialog(null, "���ӳɹ���");
				w.println("���");//bufferedreader��ı���Ҫ�س���flush���ܽ���
				w.close(); //must have THIS or flush() or it never writes...
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
