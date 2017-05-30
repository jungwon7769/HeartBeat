package comjungwon7769heartbeat.github.heartbeat;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AH on 2017-05-13.
 */
public class MsgDAO extends SQLiteOpenHelper {
	public static final String DataBase_name = "Msg_table.db";
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
					Sender + " STRING," +
					Flag + " INTEGER," +
					Time + " STRING," +
					Count + " INTEGER," +
					Mode + " INTEGER," +
					Sound + " STRING" +
					")");
		} catch(SQLException e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	//ADD Msg Method
	public boolean addMsg(MsgDTO message) {
		long result = -1;
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			ContentValues value = new ContentValues();
			value.put(Sender, message.getSender());
			value.put(Flag, message.getFlag());
			value.put(Time, Long.toString(message.getTime()));
			value.put(Count, message.getCount());
			value.put(Mode, message.getModeInt());
			value.put(Sound, message.getSoundPath());
			result = db.insert(Table_name, null, value);

		} catch(SQLException e) {
			e.printStackTrace();
		}
		db.close();

		if(result == -1) return false;
		else return true;
	}

	//DELETE Message
	public boolean deleteMsg(String sender, long time) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE FROM " + Table_name + " WHERE " + Sender + " = '" + sender + "' AND " + Time + "=" + Long.toString(time) + "";
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

	//전체 메시지 목록 반환 Method
	public ArrayList<MsgDTO> listMsg(int flag, String id) {
		Cursor cursor;
		ArrayList<MsgDTO> list_msg = new ArrayList<MsgDTO>();
		SQLiteDatabase db = this.getReadableDatabase();

		if(flag == Constants.msgFlag_any_id) {
			cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Sender + "='" + id + "' ORDER BY " + Time + " DESC", null);
		} else {
			cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Flag + "=" + flag + " ORDER BY " + Time + " DESC", null);
		}

		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			MsgDTO msgDTO = new MsgDTO();
			msgDTO.setSender(cursor.getString(0));
			msgDTO.setFlag(cursor.getInt(1));
			msgDTO.setTime(Long.parseLong(cursor.getString(2)));
			msgDTO.setCount(cursor.getInt(3));
			msgDTO.setMode(cursor.getInt(4));
			msgDTO.setSoundPath(cursor.getString(5));
			list_msg.add(msgDTO);

			cursor.moveToNext();
		}

		cursor.close();
		db.close();
		return list_msg;
	}

	//Flag에 따른 메세지 갯수 반환 Method
	public int countMsg(int flag) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Table_name + " WHERE " + Flag + " = " + flag, null);
		int nCount = cursor.getCount();
		cursor.close();
		db.close();

		return nCount;
	}

	//Msg 반환 Method
	public MsgDTO getMsg(String sender, long time) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Sender + "='" + sender + " AND " + Time + " = " + Long.toString(time), null);

		if(cursor.getCount() < 1) {
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

		cursor.close();
		db.close();

		return msgDTO;
	}

	//진동 메세지 카운트 반환 Method
	public int existBzz(String sender) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + Table_name + " WHERE " + Sender + "='" + sender + "' AND " + Flag + " = " + Constants.msgFlag_Bzz, null);

		if(cursor.getCount() < 1) {
			cursor.close();
			return 0;
		} else {
			cursor.moveToFirst();
			int count = cursor.getInt(3);

			cursor.close();
			db.close();

			return count;
		}
	}

	//진동 카운트 없데이트 Method
	public void updateMsg(String sender, int flag, int count, long time) {
		SQLiteDatabase db = this.getWritableDatabase();

		if(flag != Constants.msgFlag_Bzz) {
			db.close();
			return;
		}
		String sql = "UPDATE " + Table_name + " SET " + Count + "=" + count + "," + Time + "= '" + Long.toString(time) + "'  WHERE " + Sender + "='" + sender + "' AND " + Flag + "=" + flag;
		try {
			db.execSQL(sql);
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			db.close();
		}
	}
}
