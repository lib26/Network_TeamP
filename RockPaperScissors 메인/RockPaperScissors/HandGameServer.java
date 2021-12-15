package RockPaperScissors;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import RockPaperScissors.ServerThread;

public class HandGameServer extends Frame implements ActionListener,KeyListener {
	
	public static final int port = 3000;
	public static final int maxclient = 7;

	// 클라이언트로부터 접속요청을 기다리고, 소켓을 생성한다.
	   public static void main(String args[]){
	      try{
	    	  
	          ServerSocket theSocket = new ServerSocket(port);
	          while(true){
	              Socket sock = null;
	              ServerThread client = null; //클라이언트와 통신할 서버소켓

				  try{
	                 sock = theSocket.accept(); // 클라이언트의 접속을 기다린다.
	                 client = new ServerThread(sock);
	                 client.start();
	              }catch(IOException e){
	                 System.out.println(e);
	                 try{
	                    if(sock != null)
	                       sock.close();
	                 }catch(IOException e1){
	                    System.out.println(e);
	                 }finally{
	                    sock = null;
	                 }
	              }
	           }
	        }catch(IOException e){
	           // 서버가 정상적으로 동작하지 않는 경우를 처리한다.
	        }
	     }


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}