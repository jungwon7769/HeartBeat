package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by AH on 2017-05-13.
 */
public class MsgDAO extends SQLiteOpenHelper {
	public static final String Table_name = "Msg_table";
	public static final String Sender = "SENDER", Flag = "MSG_FLAG", Time = "TIME", Count = "COUNT", Mode = "MODE", Sound = "SOUND";

	public MsgDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			//Table Create
			db.execSQL("CREATE TABLE " + Table_name + "(" +
					Sender + " STRING REFERENCES " + FriendDAO.table_name + "(" + FriendDAO.ID + ") ON DELETE SET NULL," +
					Flag + " INTEGER," +
					Time + " INTEGER," +
					Count + " INTEGER" +
					Mode + " INTEGER" +
					Sound + "STRING" +
					")");
		} catch(SQLException e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	//ADD Msg Method
	public boolean addMsg(MsgDTO message) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "INSERT INTO " + Table_name + "(" + Sender + "," + Flag + "," + Time + "," + Count + "," + Mode + "," + Sound + ") VALUES(" +
				"'" + message.getSender() + "'," + message.getFlag() + "," + message.getTime() + "," +
				message.getCount() + "," + message.getMode() + ",'" + message.getSoundPath() + "')";
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

	//DELETE Message
	public boolean deleteMsg(String sender, int time) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE FROM " + Table_name + " WHERE " + Sender + " = '" + sender + "' AND " + Time + "=" + time;
		try {
			db.execSQL(sql);
			db.close();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			db.close();
			return true;
		}
	}

	//전체 친구 목록 반환 Method
	public ArrayList<MsgDTO> listMsg(int flag) {
		ArrayList<MsgDTO> list_msg = new ArrayList<MsgDTO>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Flag + "=" + flag + " ORDER BY " + Time + " DESC", null);

		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			MsgDTO msgDTO = new MsgDTO();
			msgDTO.setSender(cursor.getString(0));
			msgDTO.setFlag(cursor.getInt(1));
			msgDTO.setTime(cursor.getInt(2));
			msgDTO.setCount(cursor.getInt(3));
			msgDTO.setMode(cursor.getInt(4));
			msgDTO.setSoundPath(cursor.getString(5));
			list_msg.add(msgDTO);

			cursor.moveToNext();
		}

		db.close();
		return list_msg;
	}

	//Flag에 따른 메세지 갯수 반환 Method
	public int countMsg(int flag) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Table_name + " WHERE " + Flag + " = " + flag, null);
		int nCount = cursor.getCount();
		db.close();

		return nCount;
	}

	//Msg 반환 Method
	public MsgDTO getMsg(String sender, int time) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Sender + "='" + sender + " AND " + Time + " = " + time, null);

		if(cursor.isNull(0)) {
			db.close();
			return null;
		}
		cursor.moveToFirst();
		MsgDTO msgDTO = new MsgDTO();
		msgDTO.setSender(cursor.getString(0));
		msgDTO.setFlag(cursor.getInt(1));
		msgDTO.setTime(cursor.getInt(2));
		msgDTO.setCount(cursor.getInt(3));
		msgDTO.setMode(cursor.getInt(4));
		msgDTO.setSoundPath(cursor.getString(5));
		db.close();

		return msgDTO;
	}

	//진동 메세지 존재 여부 반환 Method
	public boolean existBzz(String sender) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Sender + "='" + sender + " AND " + Flag + " = " + Constants.msgFlag_Bzz, null);

		return (cursor.isNull(0));
	}

	//진동 카운트 없데이트 Method
	public void updateMsg(String sender, int flag, int count, int time) {
		SQLiteDatabase db = this.getWritableDatabase();

		if(flag != Constants.msgFlag_Bzz) {
			db.close();
			return;
		}
		String sql = "UPDATE " + Table_name + " SET " + Count + "=" + count + "," + Time + "= " + time + "  WHERE " + Sender + "='" + sender + "' AND" + Flag + "=" + flag;
		try {
			db.execSQL(sql);
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			db.close();
		}
	}
}