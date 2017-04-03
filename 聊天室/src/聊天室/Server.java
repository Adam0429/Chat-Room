package ������;

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
			ServerSocket ss=new ServerSocket(8887);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			while(true){
				Socket s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����
				InputStreamReader is=new InputStreamReader(s.getInputStream());
				br=new BufferedReader(is);
				w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
				JOptionPane.showMessageDialog(null, "���ӳɹ���");
				String string="--------�ѽ���������--------";
				w.println(string);//bufferedreader��ı���Ҫ�س���flush��close���ܽ���
				w.flush();
				Thread managerthread=new Thread(new manager()); 
				managerthread.run();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public class manager implements Runnable{
		public void run() {
			try{
				String message;
				while((message=br.readLine())!=null){
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