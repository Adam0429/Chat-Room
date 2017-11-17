package 聊天室;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.security.auth.callback.TextOutputCallback;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JMenuBar;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;

public class Client {
	JFrame f;
	JTextField tf;
	JTextArea ta;
	JButton b;
	JButton b2;
	JScrollPane qScroller;
	BufferedReader br;
	PrintWriter pw;
	Socket s;
	String Serverip;
	private JTextField tf2;
	private JTextField textField_1;
	public static void main(String[] Args){
		new Client().go();
		
	}
	public Client(){
		f=new JFrame("聊天室");
		tf=new JTextField();
		ta=new JTextArea(10,20);
		b=new JButton("\u7FA4\u53D1");
		b2=new JButton("\u79C1\u804A");
		ta.setLineWrap(true);//激活自动换行功能 			
        ta.setWrapStyleWord(true);// 激活断行不断字功能	
        ta.setEditable(false);	
        qScroller = new JScrollPane(ta);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(450, 450);
		f.getContentPane().setLayout(null);
		b.setBounds(273,340,90,30);
		b2.setBounds(273,307,90,30);
		b.addActionListener(new MyButton());
		b2.addActionListener(new MyButton2());
		qScroller.setBounds(14,50,278,230);
		//ta.setBounds(50,50,300,230);	
		tf.setBounds(56,345,200,20);
		f.getContentPane().add(qScroller);
		f.getContentPane().add(tf);
		f.getContentPane().add(b);
		f.getContentPane().add(b2);
		
		tf2 = new JTextField();
		tf2.setBounds(78, 312, 178, 20);
		f.getContentPane().add(tf2);
		
		textField_1 = new JTextField();
		textField_1.setText("无用户");
		textField_1.setBounds(306, 50, 112, 238);
		f.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u79C1\u804A\u5BF9\u8C61:");
		lblNewLabel.setBounds(0, 313, 72, 18);
		f.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("\u5185\u5BB9\uFF1A");
		label.setBounds(0, 346, 72, 18);
		f.getContentPane().add(label);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		Serverip = "127.0.0.1";
		try{
			s=new Socket(Serverip, 8888);//这里是要获取与服务器端口的连接，所以要先运行服务器程序	
			InputStreamReader is=new InputStreamReader(s.getInputStream());			
			br=new BufferedReader(is);
			pw=new PrintWriter(s.getOutputStream());
			Thread readthread=new Thread(new incomingReader());
			readthread.start();
			JOptionPane.showMessageDialog(null, "连接成功！");
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
			String from;
			try {
				from = InetAddress.getLocalHost().getHostAddress();
				pw.println(from+"说:"+string);
				pw.flush();
				tf.setText("");
				tf.requestFocus();//光标进入这个控件中
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}
	}
	
	public class MyButton2 implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			String string=tf.getText();
			String to=tf2.getText();
			String from;
			try {
				from = InetAddress.getLocalHost().getHostAddress();
				pw.println(from+"aaa"+string+"aaa"+to);//到server里check一下是私聊还是广播
				pw.flush();
				tf.requestFocus();//光标进入这个控件中
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}
	}
	public class incomingReader implements Runnable{
		public void run() {
			String message;             
			try {
	             while ((message = br.readLine()) != null) { 
	            	//readline读到换行符才算读到一行,而且还需要flush()或close()。因为输入流缓冲区不满，他是不会接收到数据的
	            	//System.out.println("read " + message);
	            	if(message.contains("IPlist")){
	            		System.out.println(message);

	            		String iplist = message.replace("127.0.0.1",Serverip);
	            		textField_1.setText(iplist);
	            	}
	            	else{
	            		ta.append(message + "\n");
	                	int height=15;								//自动换行
	                	Point p = new Point();
	                	p.setLocation(0,ta.getLineCount()*height);
	                	qScroller.getViewport().setViewPosition(p);
	            	}
	             }
	           }
	           catch(Exception e) {
	        	   e.printStackTrace();
	           }
		}
		
	}
}
