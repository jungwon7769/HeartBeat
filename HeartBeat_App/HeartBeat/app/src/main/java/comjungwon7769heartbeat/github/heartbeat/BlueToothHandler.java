package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by SJCE on 2017-05-26.
 */

public class BlueToothHandler extends Handler{
    Context mContext;

    public BlueToothHandler(Context c){
        mContext = c;
    }

    @Override
    public void handleMessage(Message msg) {
        if(mContext == null) return;
        switch(msg.what) {
            case BlueToothCommunication.CONNECT_FAILD:
                Toast.makeText(mContext, mContext.getText(R.string.bt_notConnect), Toast.LENGTH_SHORT).show();
                break;
            case BlueToothCommunication.CONNECT_NOT_ENABLE:
                Toast.makeText(mContext, mContext.getText(R.string.bt_notEnable), Toast.LENGTH_SHORT).show();
                break;
            case BlueToothCommunication.CONNECT_NOT_SUPPORT:
                Toast.makeText(mContext, mContext.getText(R.string.bt_notSupport), Toast.LENGTH_SHORT).show();
                break;
            case BlueToothCommunication.CONNECT_SUCCESS:
                //Toast.makeText(mContext, "CONNECT SUCCES", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
