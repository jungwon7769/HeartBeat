package comjungwon7769heartbeat.github.heartbeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AH on 2017-05-06.
 */
public class FriendDAO extends SQLiteOpenHelper {
	private final String table_name = "Friend_table";
	private final String id = "FRIEND_ID", nick = "FRIEND_NICK", mode = "FRIEND_MODE", color = "COLOR";

	public FriendDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			//Table Create~~
			db.execSQL("CREATE TABLE " + table_name + "(" +
					id + " TEXT PRIMARY KEY, " +
					nick + " STRING," +
					mode + " INTEGER," +
					color + " STRING" +
					")");
		} catch(SQLException e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	//ADD Friend Method
	public boolean addFriend(FriendDTO friend) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "INSERT INTO " + table_name + "(" + id + "," + nick + "," + mode + "," + color + ") VALUES(" +
				"'" + friend.getID() + "','" + friend.getNick() + "'," + friend.getModeInt() + ",'" + friend.getColor() + "')";
		try {
			db.execSQL(sql);
			db.close();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			db.close();
			return false;
		}
	}

	//DELETE Friend
	public boolean deleteFriend(String friendID) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE FROM " + table_name + " WHERE " + id + " = '" + friendID + "'";
		try{
			db.execSQL(sql);
			db.close();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			db.close();
			return true;
		}
	}

	//친구 색 변경 Method
	public void changeColor(String friendID, String color) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "UPDATE " + table_name + " SET " + color + "='" +color +"' WHERE " + id + "='" + friendID +"'";
		try{
			db.execSQL(sql);
			db.close();
		}catch(Exception e) {
			e.printStackTrace();
			db.close();
		}
	}

	//전체 친구 목록 반환 Method
	public ArrayList<FriendDTO> listFriend() {
		ArrayList<FriendDTO> list_friend = new ArrayList<FriendDTO>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " ORDER BY " + nick + " ASC", null);

		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			FriendDTO friendDTO = new FriendDTO();
			friendDTO.setID(cursor.getString(0));
			friendDTO.setNick(cursor.getString(1));
			friendDTO.setMode(cursor.getInt(2));
			friendDTO.setColor(cursor.getString(3));
			list_friend.add(friendDTO);

			cursor.moveToNext();
		}


		return list_friend;
	}


}
