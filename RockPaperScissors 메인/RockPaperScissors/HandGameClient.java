package RockPaperScissors;

import network.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class HandGameClient extends Frame implements MouseListener{

		connectingDB user = new connectingDB();

	   public TextField TextField_ID; // 로그온 입력 텍스트 필드
	   public Button LogOn_Button; // 로그온 실행 버튼
	   private Button cc_btEnter; // 게임요청 버튼
	   private Button cc_btLogout; // 로그아웃 버튼
		private Button Stat_Button; // 로그아웃 버튼
	   public TextField Text_Status; // 로그온 개설 안내
	   public List LogOn_Member,cc_lstPlayer; //  로그온 참가자

	   public static ClientThread cc_thread;
	   public static HandGameClient client;
	   public String myID ="";
	   String targetID=null;  //게임 대상자 선택

	private static final int WIN = 1111;
	private static final int LOSE = 1110;

	  //챗
	  String setnick=null;
	JPanel textingPanel = new JPanel();
	JTextField textField = new JTextField(50);
	JTextArea messageArea = new JTextArea(16, 50);


		public void creat(HandGameClient client){
			cc_thread = new ClientThread(client); // 로컬 호스트용 생성자
			cc_thread.start(); // 클라이언트의 스레드를 시작한다.
			cc_thread.requestLogon(setnick);
		}

	   public HandGameClient(String str,String nick){

			super(str);
		   this.setnick=nick;
			setLayout(new BorderLayout());

		  ////////

	      Panel north_panel = new Panel();
	      north_panel.setLayout(new FlowLayout());
	      Text_Status = new TextField("게임을 시작하려면  ID를 입력하십시오.",43);
	      Text_Status.setEditable(false);
	      north_panel.add(Text_Status);
		  add("North", north_panel);

		  //////////////////

		   Panel center_panel = new Panel();
	      center_panel.setLayout(new BorderLayout());
	      Panel label = new Panel();
	      label.add(new Label("로그온 중 참여자"),BorderLayout.EAST) ;
	      label.add(new Label("게임참여자"),BorderLayout.WEST) ;
		  center_panel.add(label,BorderLayout.NORTH);


	      Panel board = new Panel();
	      LogOn_Member = new List(10);
	      board.add(LogOn_Member,BorderLayout.EAST);
	      cc_lstPlayer = new List(10);
	      LogOn_Member.addMouseListener(this);
	      board.add(cc_lstPlayer,BorderLayout.WEST);
		  center_panel.add(board,BorderLayout.CENTER);


		  /////////////// 전적 이벤트 함수 추가하기/////////////
		  Stat_Button =new Button("전적검색");
		  center_panel.add(Stat_Button,BorderLayout.SOUTH);
		   add("Center",center_panel);

		  ////////////

		   Panel south_panel = new Panel();
		   south_panel.setLayout(new FlowLayout());

		   TextField_ID = new TextField(nick,10);
		   south_panel.add(TextField_ID);

		   cc_btEnter = new Button("게임요청");
		   south_panel.add(cc_btEnter);

		   cc_btLogout = new Button("로그아웃");
		   south_panel.add(cc_btLogout);

		   add("South", south_panel);

		   /////////챗
		   textField.setEditable(true);
		   messageArea.setEditable(false);
		   textingPanel.setLayout(new BorderLayout());
		   textingPanel.add(textField,BorderLayout.SOUTH);
		   textingPanel.add(new JScrollPane(messageArea),BorderLayout.CENTER);

		   add("East",textingPanel);

	      addWindowListener(new WinListener());

		  /* 엑션들 */
		   textField.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) { // enter 키가 눌리면

				   cc_thread.chat(textField.getText()); // 메세지를 보내고
				   textField.setText(""); // text field 비움.
			   } //만약에 텍스트필드에 입력하면 생기는 일들을 액션 리스너로 정의
		   });

		   cc_btEnter.addActionListener(new ActionListener() { //게임참여
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   cc_thread.requestPlayGame(targetID);
			   }
		   });

		   cc_btLogout.addActionListener(new ActionListener() { //로그아웃
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   //
				   user.inputLastloginT(myID);// 닉네임을 받아와 종료시각 저장

				   setVisible(false);

			   }
		   });
		   Stat_Button.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   String stat_info = user.viewInfo(targetID);
				   String[] splitted = stat_info.split("//");
				   JFrame jframe = new JFrame();
				   JOptionPane.showMessageDialog(jframe, "닉네임 : " + splitted[0] + "\n승리횟수 : " + splitted[1] + "\n패배 횟수 : " + splitted[2], "전적조회", -1);
			   }
		   });


		}

	class WinListener extends WindowAdapter {
		WinListener() {
		}
		public void windowClosing(WindowEvent we) {
			if (HandGameClient.this.myID == ClientThread.st_ID) {
				HandGameClient.cc_thread.requestLogout(HandGameClient.this.myID);
			}
			System.exit(0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//게임하고 싶은 대상을 선택한다.
		targetID = LogOn_Member.getSelectedItem().toString();
		System.out.println(targetID);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


   	   
}
