//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package RockPaperScissors;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI extends JFrame {
	connectingDB user = new connectingDB();
	private JPanel loginPanel = new JPanel(new GridLayout(3, 2));
	private JLabel idLabel = new JLabel("아이디 ");
	private JLabel pwLabel = new JLabel("비밀번호 ");
	private JTextField idText = new JTextField();
	private JPasswordField pwText = new JPasswordField();
	private JButton loginBtn = new JButton("로그인");
	private JButton idpwSearchBtn = new JButton("회원가입");
	public RESIGSTER register;
	private int flagforclient = 0;

	public GUI() {
		super("Welcome!");
		this.setContentPane(this.loginPanel);
		this.loginPanel.add(this.idLabel);
		this.loginPanel.add(this.pwLabel);
		this.loginPanel.add(this.idText);
		this.loginPanel.add(this.pwText);
		this.loginPanel.add(this.idpwSearchBtn);
		this.loginPanel.add(this.loginBtn);
		this.idLabel.setHorizontalAlignment(0);
		this.pwLabel.setHorizontalAlignment(0);
		this.setSize(400, 200);
		this.setLocationRelativeTo((Component)null);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);

		this.loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = GUI.this.idText.getText().trim();
				String pw = GUI.this.pwText.getText().trim();

				if (id.length() != 0 && pw.length() != 0) {

					String nickname = GUI.this.user.loginCheck(id, pw);

					if (nickname.equals("null")) {
						JOptionPane.showMessageDialog((Component)null, "로그인 실패", "로그인 확인!", -1);
					} else {
						JOptionPane.showMessageDialog((Component)null, "로그인 성공", "로그인 확인!", -1);
						GUI.this.setVisible(false);
						GUI.this.user.inputLoginNum(nickname);
						HandGameClient client = new HandGameClient("가위바위보 게임", nickname);
						client.setSize(1000, 400);
						client.show();
						client.creat(client);
						client.setLocationRelativeTo((Component)null);

						InetAddress local; //ip 저장하기
						try {
							local = InetAddress.getLocalHost();

							String ip = local.getHostAddress();
							user.inputlonginIP(nickname, ip);
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						}

					}
				} else {
					JOptionPane.showMessageDialog((Component)null, "아이디 또는 비밀번호를 입력 하셔야 됩니다.", "아이디나 비번을 입력!", -1);
				}
			}
		});

		this.register = new RESIGSTER(this);

		this.idpwSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.this.register.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		new GUI();
	}

	public int getFlagforclient() {
		return this.flagforclient;
	}

	public void setFlagforclient(int flagforclient) {
		this.flagforclient = flagforclient;
	}
}
