package ������;

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