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
	static String name = "null";
	public static void main(String[] Args){
		new Client(name).go();
		
	}
	public Client(String n){
		name = n;
		f=new JFrame("聊天室");
		tf=new JTextField();
		ta=new JTextArea(10,20);
		b=new JButton("BROADCAST ");
		b2=new JButton("STATS ");
		ta.setLineWrap(true);//激活自动换行功能 			
        ta.setWrapStyleWord(true);// 激活断行不断字功能	
        ta.setEditable(false);	
        qScroller = new JScrollPane(ta);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(404, 521);
		f.getContentPane().setLayout(null);
		b.setBounds(50,338,152,30);
		b2.setBounds(50,425,310,30);
		b.addActionListener(new MyButton());
		b2.addActionListener(new MyButton2());
		qScroller.setBounds(50,50,300,230);
		//ta.setBounds(50,50,300,230);	
		tf.setBounds(50,293,287,20);
		f.getContentPane().add(qScroller);
		f.getContentPane().add(tf);
		f.getContentPane().add(b);
		f.getContentPane().add(b2);
		
		JButton btnStop = new JButton("STOP");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pw.println(name+"退出");
					pw.flush();
					s.close();
					JOptionPane.showMessageDialog(null, "断开连接");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStop.setBounds(216, 340, 121, 27);
		f.getContentPane().add(btnStop);
		
		JButton btnList = new JButton("LIST");
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
			}
		});
		btnList.setBounds(50, 381, 152, 27);
		f.getContentPane().add(btnList);
		
		JButton btnNewButton = new JButton("KICK");
		btnNewButton.setBounds(216, 380, 121, 27);
		f.getContentPane().add(btnNewButton);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		try{
			s=new Socket("localhost", 8888);//这里是要获取与服务器端口的连接，所以要先运行服务器程序	
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
			String ip;
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
				pw.println(ip+"说:"+string);
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
			String ip;
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
				pw.println(ip+"说:"+"我错了");
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
	                ta.append(message + "\n");
	                int height=15;								//自动换行
	                Point p = new Point();
	                p.setLocation(0,ta.getLineCount()*height);
	                qScroller.getViewport().setViewPosition(p);
	             }
	           }
	           catch(Exception e) {
	        	   e.printStackTrace();
	           }
		}
		
	}
}
