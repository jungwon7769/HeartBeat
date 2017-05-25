package me.blog.vhehvkd.threadandservice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by AH on 2017-05-26.
 */
public class MyHandler extends Handler {
	Context mContext;

	public MyHandler(Context c) {
		mContext = c;
	}

	@Override
	public void handleMessage(Message msg) {
		switch(msg.what) {
			case BluetoothThread.CONNECT_FAILD:
				Toast.makeText(mContext, "FAILD", Toast.LENGTH_SHORT).show();
				break;
			case BluetoothThread.CONNECT_NOT_ENABLE:
				Toast.makeText(mContext, "NOT ENABLED", Toast.LENGTH_SHORT).show();
				break;
			case BluetoothThread.CONNECT_NOT_SUPPORT:
				Toast.makeText(mContext, "NOT SUPPORT", Toast.LENGTH_SHORT).show();
				break;
			case BluetoothThread.CONNECT_SUCCESS:
				Toast.makeText(mContext, "CONNECT SUCCES", Toast.LENGTH_SHORT).show();
				break;
		}
	}

}

