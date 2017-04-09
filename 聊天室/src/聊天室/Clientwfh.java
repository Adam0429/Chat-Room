package 聊天室;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

public class Clientwfh {
	JFrame f;
	JTextField tf;
	JTextArea ta;
	JButton b;
	JButton b2;
	JScrollPane qScroller;
	BufferedReader br;
	PrintWriter pw;
	Socket s;
	public static void main(String[] Args){
		new Client().go();
		
	}
	public Clientwfh(){
		f=new JFrame("鸿哥专属聊天室");
		tf=new JTextField();
		ta=new JTextArea(10,20);
		b=new JButton("发送");
		b2=new JButton("气她");
		ta.setLineWrap(true);//激活自动换行功能 			
        ta.setWrapStyleWord(true);// 激活断行不断字功能	
        ta.setEditable(false);	
        ta.setBackground(Color.white);
        tf.addKeyListener(new Send());
        qScroller = new JScrollPane(ta);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		f.setSize(450, 450);
		f.setLayout(null);
		b.setBounds(270,300,90,30);
		b2.setBounds(270,340,90,30);
		b.addActionListener(new MyButton());
		b2.addActionListener(new MyButton2());
		qScroller.setBounds(50,50,300,230);
		//ta.setBounds(50,50,300,230);	
		tf.setBounds(50,300,200,20);
		f.addWindowListener(new logout());
		f.add(qScroller);
		f.add(tf);
		f.add(b);
		f.add(b2);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void go(){
		try{
			s=new Socket("139.199.94.77", 8888);//这里是要获取与服务器端口的连接，所以要先运行服务器程序	
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
				pw.println("鸿鸿"+"说:"+string);
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
				pw.println("鸿鸿"+"说:"+"要吃笛笛小奶奶");
				pw.flush();
				tf.requestFocus();//光标进入这个控件中
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}
	}
	public class Send implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String string=tf.getText();
				try {
					pw.println("鸿鸿"+"说:"+string);
					pw.flush();
					tf.setText("");
					tf.requestFocus();//光标进入这个控件中
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub		
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
	}
	
	public class logout implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			pw.println("鸿鸿"+"下线了");
			pw.flush();
			tf.requestFocus();//光标进入这个控件中
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
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
