package network;
import java.awt.*;

import java.awt.event.*;

import javax.swing.*;


public class GUI extends JFrame{
	connectingDB user = new connectingDB(); // 들어오자마자 서버접속
	
	private JPanel loginPanel = new JPanel(new GridLayout(3, 2));
	private JLabel idLabel = new JLabel("아이디 ");
	private JLabel pwLabel = new JLabel("비밀번호 ");
	private JTextField idText = new JTextField();
	private JPasswordField pwText = new JPasswordField();
	private JButton loginBtn = new JButton("로그인");
	private JButton idpwSearchBtn = new JButton("회원가입");
	public RESIGSTER register ;

	public GUI() {
		super("Welcome!");
		this.setContentPane(loginPanel);
		loginPanel.add(idLabel);
		loginPanel.add(pwLabel);
		loginPanel.add(idText);
		loginPanel.add(pwText);
		loginPanel.add(idpwSearchBtn);
		loginPanel.add(loginBtn);
		//라벨 가운데 정렬
		idLabel.setHorizontalAlignment(NORMAL);
		pwLabel.setHorizontalAlignment(NORMAL);

		//현재 프레임 창 가운데 정렬 setSize를 먼저 해주어야 정상적으로 프레임이 가운데 정렬이 됨!

		setSize(400,200);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//로그인 버튼을 눌렀을때

		loginBtn.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {

				//아이디 비번 확인 테스트 코드~

				String id = idText.getText().trim();
				String pw = pwText.getText().trim();

				if(id.length()==0 || pw.length()==0) {
					JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 입력 하셔야 됩니다.", "아이디나 비번을 입력!", JOptionPane.DEFAULT_OPTION);
					return;
				}


				if(user.loginCheck(id, pw).equals("null")) {
					
					JOptionPane.showMessageDialog(null, "로그인 실패", "로그인 확인!", JOptionPane.DEFAULT_OPTION);
				}

				else{
					JOptionPane.showMessageDialog(null, "로그인 성공", "로그인 확인!", JOptionPane.DEFAULT_OPTION);
					return;

					
				}

			}
		});


//회원가입하기 버튼 누르면
		register = new RESIGSTER(this);
	idpwSearchBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				register.setVisible(true);
				
			}

		});
	
	
	}


	public static void main(String[] args) {
		new GUI();
	}

}