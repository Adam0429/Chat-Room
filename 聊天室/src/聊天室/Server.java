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
				JOptionPane.showMessageDialog(null, "���ӳɹ���");
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
				w=new PrintWriter(s.getOutputStream());//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void run() {
			try{
				w.println(s.getInetAddress()+"--------�ѽ���������--------");//bufferedreader��ı���Ҫ�س���flush��close���ܽ���
				w.flush();
				String message;
				while((message=br.readLine())!=null){	//�ر�client���б��� java.net.SocketException: Connection reset
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