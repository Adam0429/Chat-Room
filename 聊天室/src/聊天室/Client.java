package ������;

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
			Socket s=new Socket("localhost",8888);//������Ҫ��ȡ��������˿ڵ����ӣ�����Ҫ�����з���������
			InputStreamReader is=new InputStreamReader(s.getInputStream());
			BufferedReader b=new BufferedReader(is);
			System.out.println(b.readLine());
			b.close();
			/*
			 * �ֽ�����ȡ��λΪһ���ֽڣ��ַ�����ȡ��λΪһ���ַ� ���Զ�ȡ���ֵ�ʱ��������ֽ����ͻᵼ�¶���������
			 * �������ﲻ��stream����reader��������������stream����Ҫ��InputStreamReaderת�����ַ���
			 */
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
