package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
		// dao : 데이터베이스 접근 객체의 약자 실질적으로 db에서 게시글을 넣고 불러옴
		private Connection conn; // connection:db에접근하게 해주는 객체
		//private PreparedStatement pstmt;
		private ResultSet rs;
		// mysql에 접속해 주는 부분
		public BbsDAO() { // 생성자 실행될때마다 자동으로 db연결이 이루어 질 수 있도록함
			try {
				String dbURL = "jdbc:mysql://localhost:3306/BBS?&useSSL=false&useUnicode=true&"
						+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; 
				String dbID = "root";
				String dbPassword = "1234";
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			} catch (Exception e) {
				e.printStackTrace(); // 오류가 무엇인지 출력
			}
		}
		
		//현재 시간을 가져오는 함수
		public String getDate() {
			String SQL = "SELECT NOW()";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				if (rs.next()) {
				return rs.getString(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
		
		//bbsID 가져오는 함수
		public int getNext() {
			String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL) ;
				rs = pstmt.executeQuery();
				if (rs.next()) {
				return rs.getInt(1) + 1;
				}
				return 1; // 첫 번째 게시물인 경우
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}
		
		public int write(String bbsTitle, String userID, String bbsContent) {
			String SQL = "INSERT INTO BBS VALUES(?, ?, ?, ?, ?, ?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext());
				pstmt.setString(2, bbsTitle);
				pstmt.setString(3, userID);
				pstmt.setString(4, getDate());
				pstmt.setString(5, bbsContent);
				pstmt.setInt(6, 1);
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}
		
		//DB에서 글목록 가져오기, 리스트에 담아 반환해주는 ArrayList<Bbs> 함수 생성
		public ArrayList<Bbs> getList(int pageNumber){ 
			String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER by bbsID DESC LIMIT 10";
			//String SQL = "SELECT * FROM BBS WHERE bbsID < ? bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
			ArrayList<Bbs>list = new ArrayList<Bbs>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					list.add(bbs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list; 
		}
		
		//boolean함수 추가, 10단위 페이징 처리를 위한 함수
		public boolean nextPage(int pageNumber) {
			//String SQL = "SELECT * FROM BBS WHERE bbsID < ? bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
			String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
			ArrayList<Bbs>list = new ArrayList<Bbs>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
				rs = pstmt.executeQuery();
				if (rs.next()) {
				return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false; 	
		}
		
		public Bbs getBbs(int bbsID) {
			String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, bbsID);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					return bbs;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//수정 함수
		public int update(int bbsID, String bbsTitle, String bbsContent) {
			String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID =?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, bbsTitle);
				pstmt.setString(2, bbsContent);
				pstmt.setInt(3, bbsID);
				return pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}
		
		//삭제 함수
		public int delete(int bbsID) {
			String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);   
				pstmt.setInt(1, bbsID);
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}

}
