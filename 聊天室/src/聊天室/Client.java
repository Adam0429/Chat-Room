package 聊天室;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client {
	JFrame f;
	JTextField tf;
	JTextArea ta;
	JButton b;
	BufferedReader br;
	PrintWriter pw;
	Socket s;
	public static void main(String[] Args){
		new Client().go();
		
	}
	public Client(){
		f=new JFrame("聊天室");
		tf=new JTextField();
		ta=new JTextArea();
		b=new JButton("发送");
		ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        //JScrollPane qScroller = new JScrollPane(tf);
        //qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(450, 400);
		f.setLayout(null);
		b.setBounds(270,300,90,30);
		b.addActionListener(new MyButton());
		ta.setBounds(50,50,300,230);
		tf.setBounds(50,300,200,20);
		f.add(tf);
		f.add(ta);
		f.add(b);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		try{
			s=new Socket("127.0.0.1", 8888);//这里是要获取与服务器端口的连接，所以要先运行服务器程序	
			InputStreamReader is=new InputStreamReader(s.getInputStream());			
			br=new BufferedReader(is);
			pw=new PrintWriter(s.getOutputStream());
			Thread readthread=new Thread(new incomingReader());
			readthread.start();
			/*
			 * 字节流读取单位为一个字节，字符流读取单位为一个字符 所以读取汉字的时候，如果用字节流就会导致读出来乱码
			 * 所以这里不用stream而用reader。但读进来的是stream所以要用InputStreamReader转换成字符型
			 */
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public class MyButton implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			String string=tf.getText();
			pw.println(string);
			pw.flush();
			tf.setText("");
			tf.requestFocus();//光标进入这个控件中
		}
	}
	
	public class incomingReader implements Runnable{
		public void run() {
			String message;             
	           try {
	             while ((message = br.readLine()) != null) { 
	            	//readline读到换行符才算读到一行,而且还需要flush()或close()。因为输入流缓冲区不满，他是不会接收到数据的
	                System.out.println("read " + message);
	                ta.append(message + "\n");
	             }
	           }
	           catch(Exception e) {
	        	   e.printStackTrace();
	           }
		}
		
	}
}
