package ������;

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
			ServerSocket ss=new ServerSocket(8888);//ServerSocket������ͻ��˶���̨������8888�˿��ϵ�Ҫ��
			while(true){
				Socket s=ss.accept();//accept��ͣ�����ȵ�Ҫ�󵽴�Ż����.
				System.out.println("hello");
				PrintWriter w=new PrintWriter(s.getOutputStream());
				clientOutputStreams.add(w);
				JOptionPane.showMessageDialog(null, "���ӳɹ���");
				Thread thread=new Thread(new HandleAClient(s)); 
				thread.start();//������µ�socket�ӽ�������Ϊ��࿪һ���߳�
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
				//bufferedreader��ı���Ҫ�س���flush��close���ܽ���
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
		           PrintWriter writer = (PrintWriter) it.next();//Java.io.PrintWriter ���ӡ��ʽ������ı�ʾ���ı��������
		           writer.println(message);
		           writer.flush();
		         } catch(Exception ex) {
		              ex.printStackTrace();
		         }
		      
		       } // end while
		       
		   } // close tellEveryone
	}
}