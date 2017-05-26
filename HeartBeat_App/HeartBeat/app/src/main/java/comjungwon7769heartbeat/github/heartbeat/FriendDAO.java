package comjungwon7769heartbeat.github.heartbeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by AH on 2017-05-06.
 */
public class FriendDAO extends SQLiteOpenHelper {
	public static final String DataBase_name = "Friend_table.db";
	public static final String table_name = "Friend_table";
	public static final String ID = "FRIEND_ID", NICK = "FRIEND_NICK", MODE = "FRIEND_MODE", COLOR = "COLOR";

	public FriendDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			//Table Create~~
			db.execSQL("CREATE TABLE " + table_name + "(" +
					ID + " TEXT PRIMARY KEY, " +
					NICK + " STRING," +
					MODE + " INTEGER," +
					COLOR + " STRING" +
					")");
		} catch(SQLException e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	//Get Friend DTO
	public FriendDTO getFriend(String friendID){
		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT * FROM " + table_name + " WHERE " + ID + " = '" + friendID + "'";
		Cursor cursor = db.rawQuery(sql, null);

		cursor.moveToFirst();
		if (cursor.getCount() < 1){
			db.close();
			return null;
		}

		FriendDTO friendDTO = new FriendDTO();
		friendDTO.setID(cursor.getString(0));
		friendDTO.setNick(cursor.getString(1));
		friendDTO.setMode(cursor.getInt(2));
		friendDTO.setColor(cursor.getString(3));

		cursor.close();
		db.close();

		return friendDTO;
	}

	//ADD Friend Method
	public boolean addFriend(FriendDTO friend) {
		long result = -1;
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			ContentValues value = new ContentValues();
			value.put(ID, friend.getID());
			value.put(NICK, friend.getNick());
			value.put(MODE, friend.getModeInt());
			value.put(COLOR, friend.getColor());
			result = db.insert(table_name, null, value);

		} catch(SQLException e) {
			e.printStackTrace();
		}
		db.close();

		if(result == -1) return false;
		else return true;
		/*
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "INSERT INTO " + table_name + "(" + ID + "," + NICK + "," + MODE + "," + COLOR + ") VALUES(" +
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
		*/
	}

	//DELETE Friend
	public boolean deleteFriend(String friendID) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE FROM " + table_name + " WHERE " + ID + " = '" + friendID + "'";
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

		String sql = "UPDATE " + table_name + " SET " + COLOR + "='" +color +"' WHERE " + ID + "='" + friendID +"'";
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

		Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " ORDER BY " + NICK + " ASC", null);

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

		cursor.close();
		db.close();
		return list_friend;
	}

	//색상값 반환 Method
	public String getColor(String friendID){
		String friendColor;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + ID + "='" + friendID + "'", null);

		cursor.moveToFirst();
		if (cursor.getCount() < 1){
			db.close();
			return null;
		}
		friendColor = cursor.getString(3);
		cursor.close();
		db.close();

		return friendColor;
	}

	public boolean deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE FROM " + table_name ;
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


}
