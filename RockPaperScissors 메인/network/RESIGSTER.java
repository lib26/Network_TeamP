package network;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RESIGSTER extends JDialog{
	connectingDB user = new connectingDB();
	
	private JPanel signUpPanel = new JPanel(new GridLayout(20, 0));
	
	 private JTextField idText = new JTextField("아이디");
	 private JPasswordField pwText = new JPasswordField();
	 //----아이디 중복확인 기능 추가---------------------------
	 private JButton idcheckBtn = new JButton("아이디 중복 확인");
	 //------------------------------------------------
	 
	 private JPasswordField pwCheckText = new JPasswordField();
	 private JTextField nameText = new JTextField("이름");
	 private JTextField nicknameText = new JTextField("닉네임");
	
	 private JTextField emailText = new JTextField("E-mail");
	 private JTextField SNSText = new JTextField("SNS");

	 private JButton signUpbtn = new JButton("회원가입");
//---------LABEL-------------------------------------------------------
	 private JLabel idlabel = new JLabel("아이디");
	 private JLabel pwlabel = new JLabel("비밀번호");
	 private JLabel pwChecklabel = new JLabel("비밀번호 확인");
	 private JLabel namelabel = new JLabel("이름");
	 private JLabel nicknamelabel = new JLabel("닉네임");
	 private JLabel emaillabel = new JLabel("E-mail");
	 private JLabel SNSlabel = new JLabel("SNS");

	 private JLabel nonelabel = new JLabel("");

	 private boolean membershipProgress = false;
	 private boolean idcanbeused =false;


	 public RESIGSTER (Frame frame) {

		super(frame, true);
		this.setTitle("회원가입");
		
		this.signUpPanel.add(idlabel);
		this.signUpPanel.add(idText);
		this.signUpPanel.add(idcheckBtn);
		this.signUpPanel.add(pwlabel);
		this.signUpPanel.add(pwText);
		this.signUpPanel.add(pwChecklabel);
		this.signUpPanel.add(pwCheckText);
		
		this.signUpPanel.add(nonelabel);
		this.signUpPanel.add(namelabel);
		this.signUpPanel.add(nameText);
		this.signUpPanel.add(nicknamelabel);
		this.signUpPanel.add(nicknameText);
		this.signUpPanel.add(emaillabel);
		this.signUpPanel.add(emailText);
		this.signUpPanel.add(SNSlabel);
		this.signUpPanel.add(SNSText);
		
		this.signUpPanel.add(nonelabel);
		this.signUpPanel.add(signUpbtn);

		this.setContentPane(signUpPanel);
		this.setSize(500,500);				
		this.setLocationRelativeTo(null);
		FocusEvent();
		checkID();
		checkValue();
		//데이터베이스에 집어넣기

	}
	 
	 //텍스트 필드에 있는 값을 체크하고 지우기 위한 메소드
	 private void FocusEvent() {

			idText.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {				
					if(idText.getText().trim().length()==0) {
						idText.setText("아이디");
					}
				}

				public void focusGained(FocusEvent e) {				
					if(idText.getText().trim().equals("아이디")) {
						idText.setText("");
					}				
				}
			});

			

			nameText.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if(nameText.getText().trim().length()==0) {
						nameText.setText("이름");
					}
				}

				

				public void focusGained(FocusEvent e) {			
					if(nameText.getText().trim().equals("이름")) {
						nameText.setText("");

					}

				}

			});

			nicknameText.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if(nicknameText.getText().trim().length()==0) {
						nicknameText.setText("닉네임");
					}
				}

				

				public void focusGained(FocusEvent e) {			
					if(nicknameText.getText().trim().equals("닉네임")) {
						nicknameText.setText("");

					}

				}

			});
			emailText.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if(emailText.getText().trim().length()==0) {
						emailText.setText("E-mail");
					}
				}



				public void focusGained(FocusEvent e) {
					if(emailText.getText().trim().equals("E-mail")) {
						emailText.setText("");
					}
				}
			});	
			SNSText.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if(SNSText.getText().trim().length()==0) {
						SNSText.setText("SNS");
					}
				}



				public void focusGained(FocusEvent e) {
					if(SNSText.getText().trim().equals("SNS")) {
						SNSText.setText("");
					}
				}
			});	
	 }
	//아이디 중복확인 버튼 누르면
	 private void checkID() {
		
		 idcheckBtn.addActionListener(new ActionListener() {
			 boolean idischecked;
				public void actionPerformed(ActionEvent e) {
					if(idText.getText().trim().length()==0 || idText.getText().trim().equals("아이디")) {
						JOptionPane.showMessageDialog(null, "아이디를 입력해 주세요.", "아이디 입력", JOptionPane.WARNING_MESSAGE);
						idText.grabFocus();
						return;
					}
					
				 idischecked=user.getIdByCheck(getIdText())	;
				 if(!idischecked) 	{
					 JOptionPane.showMessageDialog(null, "사용할 수 없는 아이디입니다.", "아이디 재입력", JOptionPane.WARNING_MESSAGE);
					 idcanbeused =false;
					 return;
				 }
				 else {
					 JOptionPane.showMessageDialog(null, "사용가능한 아이디입니다.", "현재아이디 사용가능", JOptionPane.WARNING_MESSAGE);
					 idcheckBtn.setEnabled(false);
					 idcanbeused =true;
					 return;
				 }
				}

			});
	 }
	 //회원 가입할때 모든 값이 입력되었는지 체크하기 위한 메소드
	 private void checkValue(){
		
		 signUpbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(idText.getText().trim().length()==0 || idText.getText().trim().equals("아이디")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해 주세요.", "아이디 입력", JOptionPane.WARNING_MESSAGE);
					idText.grabFocus();
					return;
				}
				if (!idcanbeused) {
					JOptionPane.showMessageDialog(null, "아이디 중복확인이 필요합니다.", "아이디 중복확인", JOptionPane.WARNING_MESSAGE);
					idcheckBtn.grabFocus();
					return;
				}
				
				if(pwText.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해 주세요.", "비밀번호 입력", JOptionPane.WARNING_MESSAGE);
					pwText.grabFocus();
					return;
				}

				

				if(pwCheckText.getText().trim().length()==0) {
					JOptionPane.showMessageDialog(null, "비밀번호 확인을 입력해 주세요.", "비밀번호 확인 입력", JOptionPane.WARNING_MESSAGE);
					pwCheckText.grabFocus();
					return;

				}

				

				if(!(pwText.getText().trim().equals(pwCheckText.getText().trim()))) {
					JOptionPane.showMessageDialog(null, "비밀번호가 같지 않습니다.!!", "비밀번호 확인", JOptionPane.WARNING_MESSAGE);
					return;

				}

				

				if(nameText.getText().trim().length()==0 || nameText.getText().trim().equals("이름")) {
					JOptionPane.showMessageDialog(null, "이름을 입력해 주세요.", "이름 입력", JOptionPane.WARNING_MESSAGE);
					nameText.grabFocus();
					return;

				}

				if(nicknameText.getText().trim().length()==0 || nicknameText.getText().trim().equals("닉네임")) {
					JOptionPane.showMessageDialog(null, "닉네임을 입력해 주세요.", "닉네임 입력", JOptionPane.WARNING_MESSAGE);
					nicknameText.grabFocus();
					return;

				}

				if(emailText.getText().trim().length()==0 || emailText.getText().trim().equals("E-mail")) {
					JOptionPane.showMessageDialog(null, "E-mail을 입력해 주세요.", "E-mail 입력", JOptionPane.WARNING_MESSAGE);
					emailText.grabFocus();

					return;

				}

				if(SNSText.getText().trim().length()==0 || SNSText.getText().trim().equals("SNS")) {
					JOptionPane.showMessageDialog(null, "SNS를 입력해 주세요.", "SNS 입력", JOptionPane.WARNING_MESSAGE);
					SNSText.grabFocus();

					return;

				}

				//여기까지 왔다면 모든 값을 입력하고 선택하는 것이 완료되었으니 회원가입 과정이 완료.
				membershipProgress = true;
				setVisible(false);

			}

		});

	 }

	

	 //메인 클래스에서 다이얼로그 회원가입 창 데이터를 가져오기 위한 get 메소드 선언

	public String getIdText() {
		return this.idText.getText().trim();

	}

	public String getPwText() {
		return this.pwText.getText().trim();

	}


	public String getPwCheckText() {
		return this.pwCheckText.getText().trim();

	}

	public String getNameText() {
		return this.nameText.getText().trim();

	}

	public String getNickNameText() {
		return this.nicknameText.getText().trim();

	}

	public String getEmailText() {
		return this.emailText.getText().trim();

	}
	public String getSNSText() {
		return this.SNSText.getText().trim();

	}

	public boolean memberCheck() {
		return membershipProgress;

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
