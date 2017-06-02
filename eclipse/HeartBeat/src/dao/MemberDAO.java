package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.MemberDTO;

public class MemberDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "hobin";
	private String password = "qwaqwa12";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public MemberDAO() {
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
	
	public MemberDTO memberExist(String id, String pwd){
		MemberDTO dto=new MemberDTO();
		conn=getConnection();
		String sql="select * from member_table where id=? and pwd=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs=pstmt.executeQuery();
			if(rs.next()){
				dto.setId(rs.getString("id"));
				dto.setMmode(rs.getInt("mmode"));
				dto.setNick(rs.getString("nick"));
				dto.setPwd(rs.getString("pwd"));
			}else{
				dto=null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			dto=null;
		} finally{
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	public boolean updateNick(String nick, String id){
		boolean res=false;
		conn=getConnection();
		String sql="update member_table set nick=? where id=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setString(2, id);
			int su = pstmt.executeUpdate();
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
	
	public boolean idExist(String id){
		boolean res=false;
		conn=getConnection();
		String sql = "select * from member_table where id=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) res=true;
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
	
	public boolean join(MemberDTO dto){
		boolean res=false;
		conn=getConnection();
		String sql = "insert into member_table values(?,?,?,?)";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getNick());
			pstmt.setInt(4, dto.getMmode());
			int su=pstmt.executeUpdate();
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
	
	public boolean updateMode(int mode, String id){
		boolean res=false;
		conn=getConnection();
		String sql = "update member_table set mmode=? where id=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,mode);
			pstmt.setString(2, id);
			int su=pstmt.executeUpdate();
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
	
	
}
