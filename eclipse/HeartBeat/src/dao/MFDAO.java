package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import dto.MFDTO;

public class MFDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "hobin";
	private String password = "qwaqwa12";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public MFDAO() {
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
	public HashMap<String,MFDTO> listFriend(String id){
		HashMap<String,MFDTO> res = new HashMap<>();
		conn=getConnection();
		String sql="select friendid,color,nick,mmode from FRIEND_TABLE T0 INNER JOIN MEMBER_TABLE T1 ON T0.FRIENDID=T1.ID where T0.userid=? and T0.flag='1' order by T0.FRIENDID asc";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			while(rs.next()){
				MFDTO dto = new MFDTO();
				dto.setColor(rs.getString("color"));
				dto.setFriendid(rs.getString("friendid"));
				dto.setMode(rs.getInt("mmode"));
				dto.setNick(rs.getString("nick"));
				res.put(dto.getFriendid(), dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			res=null;
		} finally{
			try {
				if(rs!=null) rs.close();
				if(pstmt !=null)pstmt.close();
				if(conn!=null)conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	
	}
	
/*	//테스트용 나중에 지우기!!//잘됨 나중에 또안되면 이걸로 다시 체크...하기...ㅎ
	public HashMap<String,MFDTO> test(String id){
		HashMap<String,MFDTO> res = new HashMap<>();
		//int res=0;
		conn=getConnection();
		String sql="select friendid,color,nick,mmode from FRIEND_TABLE T0 INNER JOIN MEMBER_TABLE T1 ON T0.FRIENDID=T1.ID where T0.userid=? and T0.flag='1' order by T0.FRIENDID asc";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			while(rs.next()){
				MFDTO dto = new MFDTO();
				dto.setColor(rs.getString("color"));
				dto.setFriendid(rs.getString("friendid"));
				dto.setMode(rs.getInt("mmode"));
				dto.setNick(rs.getString("nick"));
				res.put(dto.getFriendid(), dto);
				//res++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			res=null;
		} finally{
			try {
				if(rs!=null) rs.close();
				if(pstmt !=null)pstmt.close();
				if(conn!=null)conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	
	}*/
}
