package 聊天室;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.print.attribute.standard.Severity;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import 聊天室.Server.Clientinfo;
import javax.swing.JLabel;

public class Server {
	HashMap<String,Clientinfo> clients;				//Use to manage the clients,also can use Arraylist,btu hashmap is more easily to get cilents'information
	String IPlist;	//the list store Clients 
	int number;	//clients index
	class Clientinfo{//it is not a client class , beacuse if i use client class,every time i new a client ,it will use the main method in client.
		String name;
		String Stats;		//store stats
		PrintWriter pw;		
		Socket socket; //use to check who are you
		public Clientinfo(String n ,String st,PrintWriter p,Socket s){
			name = n;
			Stats = st;
			pw = p;
			socket = s;
		}

	}
	
	public static void main(String[] Args){
		Server server = new Server();
	}
	public Server(){
		JFrame jf = new JFrame("Server");
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		jf.getContentPane().add(BorderLayout.CENTER,panel);
		
		JLabel lblServerRunning = new JLabel("Server Running");
		panel.add(lblServerRunning);
		jf.getContentPane().add(BorderLayout.EAST, panel2);
		jf.setVisible(true);
		jf.setSize(400, 80);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		IPlist = "";
		number = 1;
		go();
	}
	public void go(){
		clients = new HashMap<String,Clientinfo>();
		try{
			ServerSocket ss = new ServerSocket(8888);//ServerSocket will listen to the require in port 8888
			while(true){
				Socket s = ss.accept();//accept会停下来等到要求到达才会继续.
				Clientinfo client = new Clientinfo("Client"+number,"",new PrintWriter(s.getOutputStream()),s);
				clients.put("Client"+number,client);
				updateiplist();
				tellEveryone("Client"+number+" come in! ");
				givename(client);
				Thread thread = new Thread(new HandleAClient(client)); // if has new client , make a thread to listen to it
				thread.start();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void givename(Clientinfo c){	
		PrintWriter pw = c.pw;
		pw.println("[Give name]:"+"Client"+number);
		number++;
		pw.flush();
	}
	public void updateiplist(){
 		Iterator it = clients.entrySet().iterator();
		    IPlist  = "";
		    while(it.hasNext()) {
		        try {
		        	
		        	Entry entry = (Entry) it.next();
		        	String name = ((Clientinfo)entry.getValue()).name;//Java.io.PrintWriter 类打印格式化对象的表示到文本输出流。
		            IPlist = IPlist + name +'\n';
		            
		        }  
		        catch(Exception ex) {
		        	ex.printStackTrace();
		         }
		    }
	}

//	public String from(Socket s){
//		Iterator it = clients.entrySet().iterator();
//		    
//		    while(it.hasNext()) {
//		        try {
//		        	
//		        	Entry entry = (Entry) it.next();
//		        	Socket socket = ((Clientinfo)entry.getValue()).socket;
//		            if(socket == s)
//		            	return ((Clientinfo)entry.getValue()).name;	            
//		        }  
//		        catch(Exception ex) {
//		        	ex.printStackTrace();
//		         }
//		    }
//			return null;
//
//		}
	
	public void tellEveryone(String message) {		//tell all clients
	      Iterator it = clients.entrySet().iterator();
	      while(it.hasNext()) {
	        try {
	        	Entry entry = (Entry) it.next();
	        	PrintWriter writer = ((Clientinfo) entry.getValue()).pw;
	            writer.println(message);//buffer need '\n' and flush(), or it can not really receive message
	            writer.flush();
	        }  
	        catch(Exception ex) {
	        	ex.printStackTrace();
	         }
	      
	       }
	       
	   } 
	
	public class HandleAClient implements Runnable{
		Socket s;//use this to find the client 
		BufferedReader br;
		Clientinfo client;
		PrintWriter pw;
		public HandleAClient(Clientinfo c){
			try{
				client = c;
				s = client.socket;
				pw = client.pw;
				InputStreamReader is = new InputStreamReader(s.getInputStream());
				br = new BufferedReader(is);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void run() {				//deal with the message and command
			String message;
			try{

				while((message=br.readLine())!=null){
					if(message.contains("[STOP]")){
						clients.remove(client.name);
						updateiplist();
						client.Stats = client.Stats+message+"\n";
					}
					else if(message.contains("[LIST]")){
						pw.println("IPlist:\n"+IPlist);
						pw.flush();
						client.Stats = client.Stats+message+"\n";

					}
					else if(message.contains("[KICK]")){
						try{
							pw.println(message);
							String kickcl = message.split(":")[1];
							Clientinfo kickclient = clients.get(kickcl);
							clients.remove(kickcl);
							updateiplist();

							kickclient.pw.println("kicked by "+client.name);
							kickclient.pw.flush();
							kickclient.pw.close();
							pw.println(client.name+" kicked "+kickclient.name);
							pw.flush();
							client.Stats = client.Stats+message+"\n";	
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					}
					else if(message.contains("[STATS]")){
						try {
							String clname = message.split(":")[1];
							Clientinfo cl = clients.get(clname);
							pw.println(clname+" stats:\n"+cl.Stats);
							pw.flush();
							client.Stats = client.Stats+message+"\n";
						} catch (Exception e) {
							// TODO: handle exception
						}
					

					}
					else{
						System.out.println(message);
						tellEveryone(message);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
		 
}
