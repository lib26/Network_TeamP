package RockPaperScissors;


import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;


public class ClientThread extends Thread{

	connectingDB user= new connectingDB();

	   private HandGameClient ct_client; // ChatClient 객체
	   private Socket ct_sock; // 클라이언트 소켓
	   private DataInputStream ct_in; // 입력 스트림
	   private DataOutputStream ct_out; // 출력 스트림
	   private StringBuffer ct_buffer; // 버퍼
	   private Thread thisThread;
	   private PlayGame playroom; //게임방
	   public static String st_ID,withID;
	   private static String choose1=null;//참여자1의 버튼 결과
	   private static String choose2=null;//참여자2의 버튼 결과

	   private static final String SEPARATOR = "|";
	   private static final String DELIMETER = "`";
	   // 메시지 패킷 코드 및 데이터 정의

	   // 서버에 전송하는 메시지 코드
	   private static final int REQ_LOGON = 1001;
	   private static final int REQ_SENDWORDS = 1021;
	   private static final int REQ_LOGOUT = 1031;
	   private static final int REQ_GETRESULT = 1042;
	   private static final int REQ_QUITROOM = 1041;
	   private static final int REQ_SENDMESSAGE = 1045;
	   private static final int REQ_PLAYGAME = 1055;
	   private static final int REQ_CHAT = 1056; //챗

	   // 서버로부터 전송되는 메시지 코드
	   private static final int YES_LOGON = 2001;
	   private static final int NO_LOGON = 2002;
	   private static final int MDY_PLAYMEMBER = 2013;
	   private static final int MDY_PLAYERS = 2005;
	   private static final int YES_QUITROOM = 2042;
	   private static final int YES_LOGOUT = 2031;
	   private static final int NO_LOGOUT = 2032;
	   private static final int YES_GETRESULT = 2041;
	   private static final int MDY_MEMVER = 2004;
	   private static final int NO_PLAYGAME = 2014;
	   private static final int YES_PLAYGAME = 2018;
	   private static final int RECIEVE_MSG = 2021; //챗

	   
	   // 에러 메시지 코드
	   private static final int MSG_ALREADYUSER = 3001;
	   private static final int MSG_SERVERFULL = 3002;
	   private static final int MSG_CANNOTOPEN = 3011;
	   private static final int ERR_NOUSER = 2015;
	   private static final int ERR_REJECTION = 2019;
	   private static final int ERR_ALREADYPLAYER = 2033;

	   //승패 업뎃
	   //이겼음과 졌음을 전달받기
	   private static final int WIN=1111;
	private static final int LOSE=1110;

	   private static MessageBox msgBox, logonbox;
	 

	public ClientThread(HandGameClient client) {
	      try{
	         ct_sock = new Socket(InetAddress.getLocalHost(), 3000);
	         ct_in = new DataInputStream(ct_sock.getInputStream());
	         ct_out = new DataOutputStream(ct_sock.getOutputStream());
	         ct_buffer = new StringBuffer(4096);
	         thisThread = this;
	         ct_client = client; // 객체변수에 할당

	      }catch(IOException e){
	         MessageBoxLess msgout = new MessageBoxLess(client, "연결에러", "서버에 접속할 수 없습니다.");
	         msgout.show();
	      }
	   }

	   public void run(){
		   try{
		         Thread currThread = Thread.currentThread();
		         while(currThread == thisThread){ // 종료는 LOG_OFF에서 thisThread=null;에 의하여
		            String recvData = ct_in.readUTF();
		            StringTokenizer st = new StringTokenizer(recvData, SEPARATOR);
		            int command = Integer.parseInt(st.nextToken());
		            switch(command){

						case RECIEVE_MSG:{
							ct_client.messageArea.append(st.nextToken() + "\n");
							break;
						}

		               case YES_LOGON :{
		            	  // this.ct_client.LogOn_Button.setEnabled(false);
		                  logonbox.dispose();
		                  
		                  //이제부터 상대방을 선택하여 게임을 참여할 수 있다.
		                  ct_client.Text_Status.setText("로그인 성공! 원하는 게임 대상자를 골라주세요!");
		                  String ids = st.nextToken(); 
		                  StringTokenizer users = new StringTokenizer(ids, DELIMETER);
		                  ct_client.TextField_ID.setEnabled(false);
		                  
		                  while(users.hasMoreTokens()){
		                     ct_client.LogOn_Member.add(users.nextToken());
		                  }
		                  break;    
		              }
		               //로그인 오류
		               case NO_LOGON:{
		            	   st_ID = "";
		                  int errcode = Integer.parseInt(st.nextToken());
		                  if(errcode == MSG_ALREADYUSER){
		                     logonbox.dispose();
		                     msgBox = new MessageBox(ct_client, "로그온", "이미 다른 사용자가 있습니다.");
		                     msgBox.show();
		                  }else if(errcode == MSG_SERVERFULL){
		                     logonbox.dispose();
		                     msgBox = new MessageBox(ct_client, "로그온", "대화방이 만원입니다.");
		                     msgBox.show();
		                  }
		                  break;
		               }
		              //로그인 참여시 로그인 목록 갱신
		              case MDY_MEMVER:{ 
		            	 ct_client.LogOn_Member.clear();
		            	 String ids = st.nextToken(); 	            	   
		                 StringTokenizer users = new StringTokenizer(ids, DELIMETER);
		                 while(users.hasMoreTokens()){
		                	 ct_client.LogOn_Member.add(users.nextToken());
		                 }
		                 break;  
   
		             }
		              //게임 참여자 목록 갱신
		              case MDY_PLAYERS:{
		            	  String players = st.nextToken();
		            	  if(players.equals("참여자 없음")) {  //players에서 null예외 막기위해
		            		  ct_client.cc_lstPlayer.clear();
		            	  }
		            	  else{
		            		 ct_client.cc_lstPlayer.clear();
		            		 StringTokenizer users = new StringTokenizer(players, DELIMETER);
			                 while(users.hasMoreTokens()){
			                	 ct_client.cc_lstPlayer.add(users.nextToken());
			                 }
		            	  } 
			                 break; 
		            	  
		              }
		              
		              // LOGOUT 메시지 처리 
		             case YES_LOGOUT:{    
		            	 this.ct_client.LogOn_Button.setEnabled(true);
		                 ct_client.Text_Status.setText("게임을 시작하려면  ID를 입력하십시오");

		                 ct_client.TextField_ID.setEnabled(true);
		                 ct_client.TextField_ID.setText("");
		                  ct_client.LogOn_Member.removeAll();
		                    
		                break;
		             }
		             case REQ_PLAYGAME :{
		            	   String id = st.nextToken();
		            	   String idTo = st.nextToken();
		            	   String message = id +"로 부터 게임요청을 수락하시겠습니까?";
		            	   int value = JOptionPane.showConfirmDialog(null,message,"게임 요청",JOptionPane.YES_NO_OPTION);
		            	  
		            	   if(value == 1) {
		            		   try {   //상대방이 게임을 거부했을 때
		            			   ct_buffer.setLength(0);  
		            		       ct_buffer.append(NO_PLAYGAME);
		            		       ct_buffer.append(SEPARATOR);
		            		       ct_buffer.append(id);
		            		       ct_buffer.append(SEPARATOR);
		            		       ct_buffer.append(ERR_REJECTION);
		            		       send(ct_buffer.toString());   
		            			   
		            		   }catch(IOException e) {
		            			   System.out.println(e);
		            		   }
		            	   } else {  //상대방이 게임을 수락 했을때		            		   
		            		   try {
		            			   ct_buffer.setLength(0);
		            			   ct_buffer.append(YES_PLAYGAME);
		            		       ct_buffer.append(SEPARATOR);
		            		       ct_buffer.append(id);
		            		       ct_buffer.append(SEPARATOR);
		            		       ct_buffer.append(idTo);
		            		       send(ct_buffer.toString());
		            		      
		            		   }catch(IOException e) {
		            			   System.out.println(e);
		            		   }
		            		   ct_client.dispose(); // 로그온 창을 지운다.  -> 수락 받은 id 게임방창
		            		   playroom = new PlayGame(this, "게임방",id,idTo);
		            		   playroom.pack();
			                   playroom.show(); // 게임방 창을 출력한다.
		            	   }
		            	   break;
		               } 
		               //게임 거부시 
		               case NO_PLAYGAME :{
		            	   int code = Integer.parseInt(st.nextToken());
		            	   String id = st.nextToken();
		            	   
		            	   
		            	   if(code==ERR_REJECTION) {
		            		   String message = id +"님이 게임을 거부하였습니다.";
		            		   JOptionPane.showConfirmDialog(null,message,"게임 요청",JOptionPane.ERROR_MESSAGE);
		            		   break;
		            	   }
		            	   else if(code==ERR_NOUSER) {
		            		   String message = id +"님의 방에 존재하지 않습니다.";
		            		   JOptionPane.showConfirmDialog(null,message,"게임 요청",JOptionPane.ERROR_MESSAGE);
		            		   break;   
		            	   }
		            	   //상대방이 이미 게임에 참여중일때
		            	   else if(code==ERR_ALREADYPLAYER) {
		            		   String message = id +"님은 지금 게임에 참여중입니다.";
		            		   JOptionPane.showConfirmDialog(null,message,"게임 요청",JOptionPane.ERROR_MESSAGE);
		            		   break;
		            		   
		            	   }
		               }
					   //게임 수락시
						case YES_PLAYGAME:{
							//-->게임 요청한 id의 게임방창
							ct_client.dispose(); // 로그온 창을 지운다.
							String idTo = st.nextToken();
							playroom = new PlayGame(this, "게임방",st_ID,idTo);
							playroom.pack();
							playroom.show(); // 대화방 창을 출력한다.
							break;
						}

						case YES_GETRESULT:{

							String result = st.nextToken(); // 대화말 전송자의 ID를 구한다.
							int code = Integer.parseInt(st.nextToken());

							if(code==WIN) {
								user.inputwin(st_ID);

							}
							else if(code==LOSE){
								user.inputlose(st_ID);
							}
							System.out.println(st_ID);
							try{
								playroom.lb_status.setText(result);
							}catch(NoSuchElementException e){}

							break;

						}
		               

		               //게임방을 나갈시
		               case YES_QUITROOM:{
		            	   playroom.dispose();
		            	   ct_client.show();
		                  break;
		               }
		               
     
		          } // switch 종료

		          Thread.sleep(200);

		       } // while 종료(스레드 종료)

	   
		   }catch(InterruptedException e){
	         System.out.println(e);
	         release();

	      }catch(IOException e){
	         System.out.println(e);
	         release();
	      }
	   }
		   
	   public void release(){ };

		//챗
		public void chat(String msg) {
			try {
				ct_buffer.setLength(0);
				ct_buffer.append(REQ_CHAT);
				ct_buffer.append(SEPARATOR);
				ct_buffer.append(msg);
				send(ct_buffer.toString());
			}catch(IOException e){
				System.out.println(e);
			}
		}

	   public void requestLogon(String id) {
		      try{
		    	 st_ID = id;
		         logonbox = new MessageBox(ct_client, "로그온", "서버에 로그온 중입니다.");
		         logonbox.show();
		    	 System.out.println(st_ID);
		         ct_buffer.setLength(0);   
		         ct_buffer.append(REQ_LOGON);
		         ct_buffer.append(SEPARATOR);
		         ct_buffer.append(id);
		         send(ct_buffer.toString());

		      }catch(IOException e){
		         System.out.println(e);
		      }
		   }
	   
	// Logout 패킷(REQ_LOGOUT|ID)을 생성하고 전송한다.
	   public void requestLogout(String id) {
		      try{
		    	 st_ID = "";
		         logonbox = new MessageBox(ct_client, "로그아웃", "서버에서 로그아웃 되었습니다.");
		         logonbox.show();
		         ct_buffer.setLength(0); 
		         ct_buffer.append(REQ_LOGOUT);
		         ct_buffer.append(SEPARATOR);
		         ct_buffer.append(id);
		         send(ct_buffer.toString());  
		      }catch(IOException e){
		         System.out.println(e);
		      }
		   }

	//상대방을 선택한 후 게임을 요청한다.
	 public void requestPlayGame(String idTo) {
		 withID = idTo;
		   try {
			   	ct_buffer.setLength(0);  
		         ct_buffer.append(REQ_PLAYGAME);
		         ct_buffer.append(SEPARATOR);
		         ct_buffer.append(st_ID);
		         ct_buffer.append(SEPARATOR);
		         ct_buffer.append(idTo);
		         send(ct_buffer.toString()); 

		   }catch(IOException e) {
			   System.out.println(e);
		   }
	   }
	 
	 //누른 버튼을 결과를 내기위해 서버스레드쪽으로 보낸다.
	 public void requestSendResult(String choose) {
		 try {

			   	ct_buffer.setLength(0);  
		         ct_buffer.append(REQ_GETRESULT);
		         ct_buffer.append(SEPARATOR);
		         ct_buffer.append(st_ID);
		         ct_buffer.append(SEPARATOR);
		         ct_buffer.append(choose);
		         ct_buffer.append(SEPARATOR);
		         send(ct_buffer.toString());    

		   }catch(IOException e) {
			   System.out.println(e);
		   }
		 
	 }
	 //게임방을 나가기 원할때
	 public void requestQuiterRoom(String id) {
		
	      try{
	         ct_buffer.setLength(0);   
	         ct_buffer.append(REQ_QUITROOM);
	         ct_buffer.append(SEPARATOR);
	         ct_buffer.append(id);
	         send(ct_buffer.toString());   
	         withID = "";
	      }catch(IOException e){       
	         System.out.println(e);
	      }
	   }

	  //메세지 전송
	 private void send(String sendData) throws IOException {
		    ct_out.writeUTF(sendData);
		    ct_out.flush();
	 }	      

}
