//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package RockPaperScissors;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RESIGSTER extends JDialog {
	connectingDB user = new connectingDB();
	private JPanel signUpPanel = new JPanel(new GridLayout(20, 0));
	private JTextField idText = new JTextField("아이디");
	private JPasswordField pwText = new JPasswordField();
	private JButton idcheckBtn = new JButton("아이디 중복 확인");
	private JPasswordField pwCheckText = new JPasswordField();
	private JTextField nameText = new JTextField("이름");
	private JTextField nicknameText = new JTextField("닉네임");
	private JTextField emailText = new JTextField("E-mail");
	private JTextField SNSText = new JTextField("SNS");
	private JButton signUpbtn = new JButton("회원가입");
	private JLabel idlabel = new JLabel("아이디");
	private JLabel pwlabel = new JLabel("비밀번호");
	private JLabel pwChecklabel = new JLabel("비밀번호 확인");
	private JLabel namelabel = new JLabel("이름");
	private JLabel nicknamelabel = new JLabel("닉네임");
	private JButton nncheckBtn = new JButton("닉네임 중복 확인");
	private JLabel emaillabel = new JLabel("E-mail");
	private JLabel SNSlabel = new JLabel("SNS");
	private JLabel nonelabel = new JLabel("");
	private boolean membershipProgress = false;
	private boolean idcanbeused = false;
	private boolean nncanbeused = false;

	public RESIGSTER(Frame frame) {
		super(frame, true);
		this.setTitle("회원가입");
		this.signUpPanel.add(this.idlabel);
		this.signUpPanel.add(this.idText);
		this.signUpPanel.add(this.idcheckBtn);
		this.signUpPanel.add(this.pwlabel);
		this.signUpPanel.add(this.pwText);
		this.signUpPanel.add(this.pwChecklabel);
		this.signUpPanel.add(this.pwCheckText);
		this.signUpPanel.add(this.nonelabel);
		this.signUpPanel.add(this.namelabel);
		this.signUpPanel.add(this.nameText);
		this.signUpPanel.add(this.nicknamelabel);
		this.signUpPanel.add(this.nicknameText);
		this.signUpPanel.add(this.nncheckBtn);
		this.signUpPanel.add(this.emaillabel);
		this.signUpPanel.add(this.emailText);
		this.signUpPanel.add(this.SNSlabel);
		this.signUpPanel.add(this.SNSText);
		this.signUpPanel.add(this.nonelabel);
		this.signUpPanel.add(this.signUpbtn);
		this.setContentPane(this.signUpPanel);
		this.setSize(500, 500);
		this.setLocationRelativeTo((Component)null);
		this.FocusEvent();
		this.checkID();
		this.checkValue();
	}

	private void FocusEvent() {
		this.idText.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (RESIGSTER.this.idText.getText().trim().length() == 0) {
					RESIGSTER.this.idText.setText("아이디");
				}

			}

			public void focusGained(FocusEvent e) {
				if (RESIGSTER.this.idText.getText().trim().equals("아이디")) {
					RESIGSTER.this.idText.setText("");
				}

			}
		});
		this.nameText.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (RESIGSTER.this.nameText.getText().trim().length() == 0) {
					RESIGSTER.this.nameText.setText("이름");
				}

			}

			public void focusGained(FocusEvent e) {
				if (RESIGSTER.this.nameText.getText().trim().equals("이름")) {
					RESIGSTER.this.nameText.setText("");
				}

			}
		});
		this.nicknameText.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (RESIGSTER.this.nicknameText.getText().trim().length() == 0) {
					RESIGSTER.this.nicknameText.setText("닉네임");
				}

			}

			public void focusGained(FocusEvent e) {
				if (RESIGSTER.this.nicknameText.getText().trim().equals("닉네임")) {
					RESIGSTER.this.nicknameText.setText("");
				}

			}
		});
		this.emailText.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (RESIGSTER.this.emailText.getText().trim().length() == 0) {
					RESIGSTER.this.emailText.setText("E-mail");
				}

			}

			public void focusGained(FocusEvent e) {
				if (RESIGSTER.this.emailText.getText().trim().equals("E-mail")) {
					RESIGSTER.this.emailText.setText("");
				}

			}
		});
		this.SNSText.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (RESIGSTER.this.SNSText.getText().trim().length() == 0) {
					RESIGSTER.this.SNSText.setText("SNS");
				}

			}

			public void focusGained(FocusEvent e) {
				if (RESIGSTER.this.SNSText.getText().trim().equals("SNS")) {
					RESIGSTER.this.SNSText.setText("");
				}

			}
		});
	}

	private void checkID() {
		this.idcheckBtn.addActionListener(new ActionListener() {
			boolean idischecked;

			public void actionPerformed(ActionEvent e) {
				if (RESIGSTER.this.idText.getText().trim().length() != 0 && !RESIGSTER.this.idText.getText().trim().equals("아이디")) {
					this.idischecked = RESIGSTER.this.user.getIdByCheck(RESIGSTER.this.getIdText());
					if (!this.idischecked) {
						JOptionPane.showMessageDialog((Component)null, "사용할 수 없는 아이디입니다.", "아이디 재입력", 2);
						RESIGSTER.this.idcanbeused = false;
					} else {
						JOptionPane.showMessageDialog((Component)null, "사용가능한 아이디입니다.", "현재아이디 사용가능", 2);
						RESIGSTER.this.idcheckBtn.setEnabled(false);
						RESIGSTER.this.idcanbeused = true;
					}
				} else {
					JOptionPane.showMessageDialog((Component)null, "아이디를 입력해 주세요.", "아이디 입력", 2);
					RESIGSTER.this.idText.grabFocus();
				}
			}
		});
		this.nncheckBtn.addActionListener(new ActionListener() {
			boolean nnischecked;

			public void actionPerformed(ActionEvent e) {
				if (RESIGSTER.this.nicknameText.getText().trim().length() != 0 && !RESIGSTER.this.nicknameText.getText().trim().equals("닉네임")) {
					this.nnischecked = RESIGSTER.this.user.getnnByCheck(RESIGSTER.this.getNickNameText());
					if (!this.nnischecked) {
						JOptionPane.showMessageDialog((Component)null, "사용할 수 없는 닉네임입니다.", "닉네임 재입력", 2);
						RESIGSTER.this.nncanbeused = false;
					} else {
						JOptionPane.showMessageDialog((Component)null, "사용가능한 닉네임입니다.", "현재닉네임 사용가능", 2);
						RESIGSTER.this.nncheckBtn.setEnabled(false);
						RESIGSTER.this.nncanbeused = true;
					}
				} else {
					JOptionPane.showMessageDialog((Component)null, "닉네임을 입력해 주세요.", "닉네임 입력", 2);
					RESIGSTER.this.nicknameText.grabFocus();
				}
			}
		});
	}

	private void checkValue() {
		this.signUpbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (RESIGSTER.this.idText.getText().trim().length() != 0 && !RESIGSTER.this.idText.getText().trim().equals("아이디")) {
					if (!RESIGSTER.this.idcanbeused) {
						JOptionPane.showMessageDialog((Component)null, "아이디 중복확인이 필요합니다.", "아이디 중복확인", 2);
						RESIGSTER.this.idcheckBtn.grabFocus();
					} else if (RESIGSTER.this.pwText.getText().trim().length() == 0) {
						JOptionPane.showMessageDialog((Component)null, "비밀번호를 입력해 주세요.", "비밀번호 입력", 2);
						RESIGSTER.this.pwText.grabFocus();
					} else if (RESIGSTER.this.pwCheckText.getText().trim().length() == 0) {
						JOptionPane.showMessageDialog((Component)null, "비밀번호 확인을 입력해 주세요.", "비밀번호 확인 입력", 2);
						RESIGSTER.this.pwCheckText.grabFocus();
					} else if (!RESIGSTER.this.pwText.getText().trim().equals(RESIGSTER.this.pwCheckText.getText().trim())) {
						JOptionPane.showMessageDialog((Component)null, "비밀번호가 같지 않습니다.!!", "비밀번호 확인", 2);
					} else if (RESIGSTER.this.nameText.getText().trim().length() != 0 && !RESIGSTER.this.nameText.getText().trim().equals("이름")) {
						if (RESIGSTER.this.nicknameText.getText().trim().length() != 0 && !RESIGSTER.this.nicknameText.getText().trim().equals("닉네임")) {
							if (!RESIGSTER.this.nncanbeused) {
								JOptionPane.showMessageDialog((Component)null, "닉네임 중복확인이 필요합니다.", "닉네임 중복확인", 2);
								RESIGSTER.this.nncheckBtn.grabFocus();
							} else if (RESIGSTER.this.emailText.getText().trim().length() != 0 && !RESIGSTER.this.emailText.getText().trim().equals("E-mail")) {
								if (RESIGSTER.this.SNSText.getText().trim().length() != 0 && !RESIGSTER.this.SNSText.getText().trim().equals("SNS")) {
									RESIGSTER.this.membershipProgress = true;
									RESIGSTER.this.user.joinCheck(RESIGSTER.this.getIdText(), RESIGSTER.this.getPwText(), RESIGSTER.this.getNameText(), RESIGSTER.this.getNickNameText(), RESIGSTER.this.getEmailText(), RESIGSTER.this.getSNSText());
									RESIGSTER.this.setVisible(false);
								} else {
									JOptionPane.showMessageDialog((Component)null, "SNS를 입력해 주세요.", "SNS 입력", 2);
									RESIGSTER.this.SNSText.grabFocus();
								}
							} else {
								JOptionPane.showMessageDialog((Component)null, "E-mail을 입력해 주세요.", "E-mail 입력", 2);
								RESIGSTER.this.emailText.grabFocus();
							}
						} else {
							JOptionPane.showMessageDialog((Component)null, "닉네임을 입력해 주세요.", "닉네임 입력", 2);
							RESIGSTER.this.nicknameText.grabFocus();
						}
					} else {
						JOptionPane.showMessageDialog((Component)null, "이름을 입력해 주세요.", "이름 입력", 2);
						RESIGSTER.this.nameText.grabFocus();
					}
				} else {
					JOptionPane.showMessageDialog((Component)null, "아이디를 입력해 주세요.", "아이디 입력", 2);
					RESIGSTER.this.idText.grabFocus();
				}
			}
		});
	}

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
		return this.membershipProgress;
	}

	public static void main(String[] args) {
	}
}
