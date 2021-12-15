package network;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.swing.*;



public class connectingDB {
	Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost/mydb";
	String user = "root";
	String passwd = "1q2w3e4r";
	
	public connectingDB() {	//Database 객체 생성 시 데이터베이스 서버와 연결한다.
		try {	//데이터베이스 연결은 try-catch문으로 예외를 잡아준다.
			//데이터베이스와 연결한다.
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, passwd);
			stmt = con.createStatement();
			System.out.println("[Server] MySQL 서버 연동 성공");	//데이터베이스 연결에 성공하면 성공을 콘솔로 알린다.
		} catch(Exception e) {	//데이터베이스 연결에 예외가 발생했을 때 실패를 콘솔로 알린다.
			System.out.println("[Server] MySQL 서버 연동 실패> " + e.toString());
		}
	}


	
	public String loginCheck(String _i, String _p) {
		
		String nickname = "null";	//반환할 닉네임 변수를 "null"로 초기화.
		
		//매개변수로 받은 id와 password값을 id와 pw값에 초기화한다.
		String id = _i;
		String pw = _p;
		
		try {
			//id와 일치하는 비밀번호와 닉네임이 있는지 조회한다.
			String checkingStr = "SELECT password, nickname FROM member WHERE id='" + id + "'";
			ResultSet result = stmt.executeQuery(checkingStr);
			/*
			 * stmt.executeUpdate(String command) 
			 * : command 변수의 명령을 수행해서 mysql의 정보를 업데이트 해주는 역할을 한다. 
			 * rs = stmt.executeQuery(String command) 
			 * : command 변수의 명령을 수행한 후 나온 결과를 rs에 담는다.
			 * */
			int count = 0;
			while(result.next()) {
				//조회한 비밀번호와 pw 값을 비교.
				if(pw.equals(result.getString("password"))) {	
					//true일 경우 nickname에 조회한 닉네임에 반환하고 로그인 성공을 콘솔로 알린다.
					nickname = result.getString("nickname");
					System.out.println("[Server] 로그인 성공");
				}
				
				else {	//false일 경우 nickname을 "null"로 초기화하고 로그인 실패를 콘솔로 알린다.
					nickname = "null";
					System.out.println("[Server] 로그인 실패");
				}
				count++;
			}
		} catch(Exception e) {	//조회에 실패했을 때 nickname을 "null"로 초기화. 실패를 콘솔로 알린다.
			nickname = "null";
			System.out.println("[Server] 로그인 실패 > " + e.toString());
		}
		
		return nickname;	//nickname 반환
	}
	
	//회원가입을 수행하는 메소드. 회원가입에 성공하면 true, 실패하면 false를 반환한다.
	public boolean joinCheck(String id, String pw, String name, String nickname, String email, String sns) {
		boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false
		
		//*****비밀번호 암호화*******************
		/*String raw=pw;
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		String salt = new String(Base64.getEncoder().encode(bytes));

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(salt.getBytes());
		md.update(raw.getBytes());
		String hex = String.format("%064x", new BigInteger(1, md.digest()));*/
		//**********************************
		try {
			//member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 승, 패는 초기값을 숫자 0으로 한다.
			
			String insertStr = "INSERT INTO member VALUES('" + id + "', '" + pw + "', '" + name + "', '" + nickname + "', '" + email + "','" + sns + "', 0, 0, null, 0, null);";
			stmt.executeUpdate(insertStr);
			flag = true;	//업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
			System.out.println("[Server] 회원가입 성공");
		} catch(Exception e) {	//회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 회원가입 실패 > " + e.toString());
		}
		
		return flag;	//flag 반환
	}
	
	public boolean getIdByCheck(String id) {
		  
		ResultSet rs;
		boolean result = true;
 
        try {
            String s = "select id from member where id=\""+id+"\";";
           
            rs = stmt.executeQuery(s); //실행
            System.out.println(rs);
            if (rs.next()) result = false; //레코드가 존재하면 false
 
        } catch (SQLException e) {
            System.out.println(e + "=>  getIdByCheck fail");
        } 
 
        return result;
 
    }



	
	
	//데이터베이스에 저장된 자신의 정보를 조회하는 메소드. 조회한 정보들을 String 형태로 반환한다.
	public void viewInfo(String _nn) {
		String msg = "null";	//반환할 문자열 변수를 "null"로 초기화.
		
		//매개변수로 받은 닉네임을 nick에 초기화한다.
		String nick = _nn;
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 이름과 이메일 정보를 조회한다.
			String viewStr = "SELECT name, email FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(viewStr);
			
			int count = 0;
			while(result.next()) {
				//msg에 "이름//닉네임//이메일" 형태로 초기화한다.
				msg = result.getString("name") + "//" + nick + "//" + result.getString("email");
				count++;
			}
			System.out.println("[Server] 회원정보 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 회원정보 조회 실패 > " + e.toString());
		}
		

	}
	
	//닉네임 받아서 패배 횟수 증가하
		public void inputwin(String _nn) {
			
			
			//매개변수로 받은 닉네임을 nick에 초기화한다.
			String nick = _nn;
			
			try {
				//member 테이블에서 nick이라는 닉네임을 가진 회원의 이름과 이메일 정보를 조회한다.
				String viewwin = "update member win=win+1 WHERE nickname='" + nick + "'";
				stmt.executeQuery(viewwin);
				
		
				System.out.println("[Server] 회원정보 수정 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
			} catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
				System.out.println("[Server] 회원정보 수정 실패 > " + e.toString());
			}
			
	
		}


	public static void main(String[] args) {
		
		
		
	}

}
