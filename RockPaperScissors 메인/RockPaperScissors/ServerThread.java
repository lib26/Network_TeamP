package RockPaperScissors;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import RockPaperScissors.HandGameServer;
import RockPaperScissors.ServerThread;


public class ServerThread extends Thread {
	  private Socket st_sock;
	   private DataInputStream st_in;
	   private DataOutputStream st_out;
	   private StringBuffer st_buffer;

	   /* 로그온된 사용자 저장 */
	   private static Hashtable<String,ServerThread> logonHash; 
	   private static Vector<String> logonVector;
	   private static Set<DataOutputStream> ChatHash; // 챗
	   
	   /* 게임방 리스트 저장 */
	   private static Hashtable<String,ServerThread> playerHash; 
	   private static Vector<String> playerVector;

	   private static int isOpenRoom = 0; // 대화방이 개설안됨(초기값)
	   private HandGameClient st_client;
	   private static final String SEPARATOR = "|"; // 메시지간 구분자
	   private static final String DELIMETER = "`"; // 소메시지간 구분자

	   public String st_ID; 			// ID 저장
	   private static String choose1=null;
	   private static String choose2=null;
	   private static ServerThread client1 = null;
	   private static ServerThread client2 = null;
	   private static int chooseInt1=0;
	   private static int chooseInt2=0;
	   private PlayGame gameroom;

	   // 클라이언트로부터 전달되는 메시지 코드
	   private static final int REQ_LOGON = 1001;
	   private static final int REQ_SENDWORDS = 1021;
	   private static final int REQ_LOGOUT = 1031;
	   private static final int REQ_GETRESULT = 1042;
	   private static final int REQ_QUITROOM = 1041;
	   private static final int REQ_SENDMESSAGE = 1045;
	   private static final int REQ_PLAYGAME = 1055;
	   private static final int REQ_CHAT = 1056; //챗
 
	   
	   // 클라이언트에 전송하는 메시지 코드
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
	   private static final int SEND_MSG = 2021; // 챗
	   
	   
	   // 에러 메시지 코드
	   private static final int MSG_ALREADYUSER = 3001;
	   private static final int MSG_SERVERFULL = 3002;
	   private static final int MSG_CANNOTOPEN = 3011;
	   private static final int ERR_NOUSER = 2015;
	   private static final int ERR_REJECITON = 2019;
	   private static final int ERR_ALREADYPLAYER = 2033;

	   //승패 업뎃
	   //이겼음과 졌음을 전달받기
	   private static final int WIN=1111;
		private static final int LOSE=1110;
	   
	   static{	
		      logonHash = new Hashtable<String,ServerThread>(HandGameServer.maxclient);
		      logonVector = new Vector<String>(HandGameServer.maxclient); 
		      playerHash = new Hashtable<String,ServerThread>(HandGameServer.maxclient);
		      playerVector = new Vector<String>(HandGameServer.maxclient);

			  ChatHash = new HashSet<>();
		   }
	
	public ServerThread(Socket sock){
		
	      try{
	         st_sock = sock; //연결이 된 클라이언트와 연결 소켓	        
	         st_in = new DataInputStream(sock.getInputStream()); 
	         st_out = new DataOutputStream(sock.getOutputStream());
	         st_buffer = new StringBuffer(2048);
	         
	      }catch(IOException e){
	         System.out.println(e);
	      }
	   }

	   ///
	connectingDB update = new connectingDB();
	   ///
	   //연결된 여러개의 클라이언트 쓰레드
	   public void run(){
		   try{
		         while(true){
		            String recvData = st_in.readUTF();
		            StringTokenizer st = new StringTokenizer(recvData, SEPARATOR);
		            int command = Integer.parseInt(st.nextToken());
		            switch(command){

						//챗
						case REQ_CHAT:{
							String msg = st_ID +" : " + st.nextToken(); //메세지 내용을 받고

							for (DataOutputStream writer : ChatHash) {
								writer.writeUTF(SEND_MSG + SEPARATOR + msg);
								writer.flush();
							}
							break;
						}

		               // 로그온 시도 메시지 PACKET : REQ_LOGON|ID
		               case REQ_LOGON:{
		                  int result;
		                  String id = st.nextToken(); // 클라이언트의 ID를 얻는다.
		                  result = addUser(id, this, st_out);
		                  st_buffer.setLength(0);
		                  if(result ==0){  // 접속을 허용한 상태	                	 
		                     st_buffer.append(YES_LOGON);                      					
		                     st_buffer.append(SEPARATOR);
		                     st_buffer.append(SEPARATOR);
		                     String userIDs = getUsers(); //로그인 참여 사용자ID를 구한다
		                     st_buffer.append(userIDs);
		                     send(st_buffer.toString());
		                     
		                     st_buffer.setLength(0);   //모든 클라이언트의 로그온 사용자 수정
		                     st_buffer.append(MDY_MEMVER);
		                     st_buffer.append(SEPARATOR);
		                     st_buffer.append(userIDs);    
		                     broadcast_log(st_buffer.toString());
		                     
		                     
		                     String playerIDs = getPlayers();
		                     if(!playerIDs.equals("")) {
		                    	 st_buffer.setLength(0);   //모든 클라이언트의 게임참여자 label 수정
		                    	 st_buffer.append(MDY_PLAYERS);
		                    	 st_buffer.append(SEPARATOR);
		                    	 st_buffer.append(playerIDs);    
		                    	 broadcast_log(st_buffer.toString());
		                     }
		                     
		                     
		                  }else{  // 접속불가 상태
		                     st_buffer.append(NO_LOGON);  // NO_LOGON|errCode
		                     st_buffer.append(SEPARATOR);
		                     st_buffer.append(result); // 접속불가 원인코드 전송
		                     send(st_buffer.toString());
		                  }
		                  break;
		               }
		               case REQ_LOGOUT:{
		            	   st_buffer.setLength(0);
		                   String id = st.nextToken(); // 그로인 참여자의 ID를 얻는다.
		            	   
		                   logonVector.removeElement(id);  // 사용자 ID 제거
		                   logonHash.remove(id, this); //사용자 ID 및 클라이언트와 통신할  스레드 제거

		                   st_buffer.append(YES_LOGOUT); 
		                   send(st_buffer.toString()); // YES_ENTERROOM 패킷을 전송한다.

		                   
		                   st_buffer.setLength(0);
		                   st_buffer.append(MDY_MEMVER);
		                   st_buffer.append(SEPARATOR);
		                   String userIDs = getUsers(); // 대화방 참여 사용자 ID를 구한다
		                   st_buffer.append(userIDs);
		                   broadcast_log(st_buffer.toString()); // 로그인 사용자 들의 label수정
		                  
		                  break;
		               }
		               //게임 요청
		               case REQ_PLAYGAME :{
		            	   String id = st.nextToken();
		            	   String idTo = st.nextToken();
		            	   
		            	   ServerThread client = null;
		            	   ServerThread client_p = null;
		            	   
		            	   //게임을 요청받은 사용자를 hash로 뽑아내 요청 메세지 보내기
		            	   if((client = (ServerThread) logonHash.get(idTo))!=null){
		            		   //만약 게임방에 있는 사용자 일때
		            		   if((client_p = (ServerThread) playerHash.get(idTo))!=null){
		            			   st_buffer.setLength(0);
			            		   st_buffer.append(NO_PLAYGAME);
			            		   st_buffer.append(SEPARATOR);
			            		   st_buffer.append(ERR_ALREADYPLAYER);
			            		   st_buffer.append(SEPARATOR);
			            		   st_buffer.append(idTo);
			            		   send(st_buffer.toString()); 
			            		   break;     
		            		   }
		            		   st_buffer.setLength(0);
		            		   st_buffer.append(REQ_PLAYGAME);
		            		   st_buffer.append(SEPARATOR);
		            		   st_buffer.append(id);
		            		   st_buffer.append(SEPARATOR);
		            		   st_buffer.append(idTo);
		            		   client.send(st_buffer.toString()); 
		            		   break;
		            	   }
		            	   else { //사용자를 선택하지 않았을때
		            		   st_buffer.setLength(0);
		            		   st_buffer.append(NO_PLAYGAME);
		            		   st_buffer.append(SEPARATOR);
		            		   st_buffer.append(ERR_NOUSER);
		            		   st_buffer.append(idTo);
		            		   send(st_buffer.toString()); 
		            		   break;   
		            	   }
		            	      
		               }
		               //상대방이 수락을 하여 게임을 시작한다.
		               case YES_PLAYGAME :{
		            	   String id = st.nextToken();
		            	   String idTo = st.nextToken();
		            	   
		            	   playerVector.addElement(id);  //참여자들 벡터와 해쉬테이블에 추가
		                   playerHash.put(id, this); 
		                   playerVector.addElement(idTo);  
		                   playerHash.put(idTo, this); 
		                   	
		                   //요청한 아이디에게 상대방이 게임을 수락하였다고 알려줌.
		            	   ServerThread client = null;
		            	   client = (ServerThread) logonHash.get(id);
		            	   st_buffer.setLength(0);
		        		   st_buffer.append(YES_PLAYGAME);
		        		   st_buffer.append(SEPARATOR);
		        		   st_buffer.append(idTo);	        		   
		        		   client.send(st_buffer.toString()); 
		        		   
		        		   String playerIDs = getPlayers(); //게임참여자목록 수정
		        		   st_buffer.setLength(0);
		        		   st_buffer.append(MDY_PLAYERS);
		        		   st_buffer.append(SEPARATOR);
		        		   st_buffer.append(playerIDs);		
		        		   broadcast_log(st_buffer.toString()); 
		        		    
		        		   break;
		 
		               }
		               
		               //상대방이 게임을 거부한다.
		               case NO_PLAYGAME :{
		            	   String id = st.nextToken();
		            	   ServerThread client = null;
		            	   client = (ServerThread) logonHash.get(id);
		            	   st_buffer.setLength(0);
		        		   st_buffer.append(NO_PLAYGAME);
		        		   st_buffer.append(SEPARATOR);
		        		   st_buffer.append(ERR_REJECITON);
		        		   st_buffer.append(SEPARATOR);
		        		   st_buffer.append(id);

		        		   client.send(st_buffer.toString()); 
		        		   break;
		 
		               }
		               
		               //게임 결과 계산하기
		               case REQ_GETRESULT:{
		            	   
		            	   String id = st.nextToken();
		            	   st_buffer.setLength(0);
		                   st_buffer.append(YES_GETRESULT);
		                   st_buffer.append(SEPARATOR);
		                   
		                   if(client1 == null) {  //참여자1 hashtable에서 찾기
		            		   client1 = (ServerThread) logonHash.get(id);
		            	   }
		            	   else{  //참여자2 hashtable에서 찾기
		            		   client2 = (ServerThread) logonHash.get(id);
		            	   }
		            	   
		            	   if(choose1 == null) {  //참여자1의 버튼결과
		            		    choose1 = st.nextToken();
		            		    chooseInt1 = Integer.parseInt(choose1);
		            		    
		            	   }
		            	   else{				//참여자1의 버튼결과
		            		    choose2 = st.nextToken();
		            		    chooseInt2 = Integer.parseInt(choose2);
		            	   }
		            	   
		            	   if(choose1!=null&&choose2!=null) {  //두 버튼이 모두 눌려지면 결과 계산하기
		            		   switch(chooseInt1) {
		            		   case 0 :   //가위
		            			   if(chooseInt2 == 0) {
		            				   st_buffer.append("상대방과의 게임에서 비겼습니다!");
		            				   client1.send(st_buffer.toString()); 
		            				   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			  ////////////////////////////////////////////////////////////////////////////////////
		            			   }else if(chooseInt2 == 1) {  
		            				   st_buffer.append("상대방과의 게임에서 졌습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(LOSE);


									   client1.send(st_buffer.toString());
		            				   System.out.println(st_buffer.toString()+"\n");
		            				   st_buffer.delete( st_buffer.length()-21, st_buffer.length()-1);

		            				   st_buffer.append("상대방과의 게임에서 이겼습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(WIN);


									   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			   
		            			   }else if(chooseInt2 == 2) {
		            				   st_buffer.append("상대방과의 게임에서 이겼습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(WIN);

									   client1.send(st_buffer.toString());
		            				   st_buffer.delete( st_buffer.length()-22, st_buffer.length()-1);

		            				   st_buffer.append("상대방과의 게임에서 졌습니다.");
									   st_buffer.append(SEPARATOR);
									   st_buffer.append(LOSE);

									   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			   
		            			   }
		            			 
		            		   case 1 :  //바위
		            		   
		            			   if(chooseInt2 == 0) {
		            				   st_buffer.append("상대방과의 게임에서 이겼습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(WIN);


									   client1.send(st_buffer.toString());
		            				   st_buffer.delete( st_buffer.length()-22, st_buffer.length()-1);

		            				   st_buffer.append("상대방과의 게임에서 졌습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(LOSE);


									   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			  
		            			   }else if(chooseInt2 == 1) {
		            				   st_buffer.append("상대방과의 게임에서 비겼습니다!");
		            				   client1.send(st_buffer.toString());
		            				   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			  
		            			   }else if(chooseInt2 == 2) {
		            				   st_buffer.append("상대방과의 게임에서 졌습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(LOSE);

									   client1.send(st_buffer.toString());
		            				   
		            				   st_buffer.delete( st_buffer.length()-21, st_buffer.length()-1);
		            				   st_buffer.append("상대방과의 게임에서 이겼습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(WIN);

									   client2.send(st_buffer.toString());

		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			   
		            			   }
		            		   case 2 :  //보
		            		   
		            			   if(chooseInt2 == 0) {
		            				   st_buffer.append("상대방과의 게임에서 졌습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(LOSE);

									   client1.send(st_buffer.toString());
		            				   st_buffer.delete( st_buffer.length()-21, st_buffer.length()-1);
		            				   st_buffer.append("상대방과의 게임에서 이겼습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(WIN);

									   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			  
		            			   }else if(chooseInt2 == 1) {
		            				   st_buffer.append("상대방과의 게임에서 이겼습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(WIN);

									   client1.send(st_buffer.toString());
		            				   st_buffer.delete( st_buffer.length()-22, st_buffer.length()-1);
		            				   st_buffer.append("상대방과의 게임에서 졌습니다.");

									   st_buffer.append(SEPARATOR);
									   st_buffer.append(LOSE);

									   client2.send(st_buffer.toString());
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			   
		            			   }else if(chooseInt2 == 2) {
		            				   st_buffer.append("상대방과의 게임에서 비겼습니다!");
		            				   client1.send(st_buffer.toString());
		            				   client2.send(st_buffer.toString()); 
		            				   
		            				   choose1 = choose2 = null;
		            				   client1 = client2 = null;
		            				   break;
		            			   }
		            		 

		            		   }
		            	   }
		            	   break; 
 	   
		               }
		               //게임방을 나갈때
		               case REQ_QUITROOM:{
		            	   st_buffer.setLength(0);
		                   String id = st.nextToken();  
		                  
		            	   playerVector.removeElement(id); //게임참여자 리스트에서 제거 
		                   playerHash.remove(id, this); 

		                   st_buffer.append(YES_QUITROOM); 
		                   send(st_buffer.toString()); 
		                   String PlayerIDs = getPlayers();
 
		                   st_buffer.setLength(0);
		                   st_buffer.append(MDY_PLAYERS);
		                   st_buffer.append(SEPARATOR);  
		                   //PlayerIDs의 null예외를 막기위해
		                   if(PlayerIDs.equals("")) PlayerIDs = "참여자 없음";
		                   st_buffer.append(PlayerIDs);
		                   broadcast_log(st_buffer.toString());
		                   break;
		               }
		               
		            } // switch 종료

		            Thread.sleep(100);
		         } //while 종료

		      }catch(NullPointerException e){ // 로그아웃시 st_in이 이 예외를 발생하므로
		      }catch(InterruptedException e){
		      }catch(IOException e){
		      }
  }
	   

	   private static synchronized int addUser(String id, ServerThread client, DataOutputStream st_out){
		      if(checkUserID(id) != null){
		         return MSG_ALREADYUSER;
		      }  
		      if(logonHash.size() >= HandGameServer.maxclient){
		         return MSG_SERVERFULL;
		      }
		      logonVector.addElement(id);  // 사용자 ID 추가
		      logonHash.put(id, client); // 사용자 ID 및 클라이언트와 통신할 스레드를 저장한다.
		      ChatHash.add(st_out); // 챗
		      client.st_ID = id;
		      return 0; // 클라이언트와 성공적으로 접속하고, 대화방이 이미 개설된 상태.
		   }

		   /*ID 중복 확인
		           반환값이 null이라면 요구한 ID로 게임방 입장이 가능함. */
		   private static ServerThread checkUserID(String id){
		      ServerThread alreadyClient = null;
		      alreadyClient = (ServerThread) logonHash.get(id);
		      return alreadyClient;
		   }
		   
		   // 로그온에 참여한 사용자 ID를 구한다.
		   private String getUsers(){
		      StringBuffer id = new StringBuffer();
		      String ids;
		      Enumeration<String> enu = logonVector.elements();
		      while(enu.hasMoreElements()){
		         id.append(enu.nextElement());
		         id.append(DELIMETER); 
		      }
		      try{
		         ids = new String(id);  // 문자열로 변환한다.
		         ids = ids.substring(0, ids.length()-1); // 마지막 "`"를 삭제한다.
		      }catch(StringIndexOutOfBoundsException e){
		         return "";
		      }
		      return ids;
		   }

		   // 게임방에 참여한 사용자 ID를 구한다.
		   private String getPlayers(){
		      StringBuffer id = new StringBuffer();
		      String ids;
		      Enumeration<String> enu = playerVector.elements();
		      while(enu.hasMoreElements()){
		         id.append(enu.nextElement());
		         id.append(DELIMETER); 
		      }
		      try{
		         ids = new String(id);
		         ids = ids.substring(0, ids.length()-1); // 마지막 "`"를 삭제한다.
		      }catch(StringIndexOutOfBoundsException e){
		         return "";
		      }
		      return ids;
		   }
		   //로그인한 사용자들에게 (브로드케스팅) 데이터를 전송한다.
		   public synchronized void broadcast_log(String sendData) throws IOException{
			   ServerThread client;
			      Enumeration<String> enu = logonVector.elements();
			      while(enu.hasMoreElements()){	  
			         client = (ServerThread) logonHash.get(enu.nextElement());
			         client.send(sendData);
			      }
		   }
		   
		   // 게임방에 참여한 모든 사용자(브로드케스팅)에게 데이터를 전송한다.
		   public synchronized void broadcast(String sendData) throws IOException{
		      ServerThread client;
		      Enumeration<String> enu = playerVector.elements();
		      while(enu.hasMoreElements()){	  
		         client = (ServerThread) playerHash.get(enu.nextElement());
		         client.send(sendData);
		      }
		   }

		   // 데이터를 전송한다.
		   public void send(String sendData) throws IOException{
		      synchronized(st_out){
		         st_out.writeUTF(sendData);
		         st_out.flush();
		      }
		   }
}
