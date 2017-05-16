package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import dto.MFDTO;
import dto.MsgDTO;

public class MsgDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "hobin";
	private String password = "qwaqwa12";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public MsgDAO() {
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

	public boolean addMsg(MsgDTO dto) {
		boolean res=false;
		int su=0;
		conn=getConnection();
		String sql="insert into msg_table values(?,?,?,?,?)";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getReceiver());
			pstmt.setString(2, dto.getSender());
			pstmt.setString(3, Calendar.getInstance().getTimeInMillis()+"");
			pstmt.setInt(4, dto.getData());
			pstmt.setInt(5,dto.getFlag());
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
	
	public MsgDTO getMsg(String id){
		MsgDTO dto = null;
		conn=getConnection();
		String sql="select * from msg_table where receiver=? order by time asc";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			while(rs.next()){
				dto = new MsgDTO();
				dto.setReceiver(rs.getString("receiver"));
				dto.setSender(rs.getString("sender"));
				dto.setTime(rs.getString("time"));
				dto.setData(rs.getInt("data"));
				dto.setFlag(rs.getInt("flag"));
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dto;
	}
	public boolean deleteMsg(String sender,String time){
		boolean res=false;
		conn=getConnection();
		String sql = "DELETE FROM msg_table WHERE sender=? and time=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, sender);
			pstmt.setString(2, time);
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
}
