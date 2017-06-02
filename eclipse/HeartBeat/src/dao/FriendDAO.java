package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import dto.FriendDTO;

public class FriendDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "hobin";
	private String password = "qwaqwa12";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public FriendDAO() {
		try {
			Class.forName(driver);// 등록되었나 확인하는 것
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}// constructor

	public Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public boolean friendColor(String id1, String id2, String color){
		boolean res=false;
		int su=0;
		conn=getConnection();
		String sql="update friend_table set color=? where userid=? and friendid=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, color);
			pstmt.setString(2, id1);
			pstmt.setString(3, id2);
			su=pstmt.executeUpdate();
			if(su==1) res=true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public boolean addFriend(FriendDTO dto){
		boolean res=false;
		int su=0;
		conn=getConnection();
		String sql="insert into friend_table values(?,?,?,?)";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getFriendId());
			String flag;
			if(dto.getFlag()==true) flag="1";
			else flag="0";
			pstmt.setString(3,flag);
			pstmt.setString(4, "ffff00");
			su=pstmt.executeUpdate();
			if(su==1) res=true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public boolean deleteFriend(String id1, String id2){
		boolean res=false;
		conn=getConnection();
		String sql="delete friend_table where (userid=? and friendid=?)or(userid=? and friendid=?)";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id1);
			pstmt.setString(2, id2);
			pstmt.setString(3, id2);
			pstmt.setString(4, id1);
			int su=pstmt.executeUpdate();
			if(su>0) res=true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public boolean checkFriend (String id1, String id2){
		boolean res=false;
		conn=getConnection();
		String sql="select * from friend_table where userid=? and friendid=? and flag='1'";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id1);
			pstmt.setString(2, id2);
			rs=pstmt.executeQuery();
			if(rs.next()) res=true;
			else res=false;
		} catch (SQLException e) {
			e.printStackTrace();
			res=false;
		} finally{
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
}
