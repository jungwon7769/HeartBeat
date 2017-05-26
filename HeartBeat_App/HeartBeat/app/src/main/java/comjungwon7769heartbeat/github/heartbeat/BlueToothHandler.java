package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
        switch(msg.what) {
            case BlueToothCommunication.CONNECT_FAILD:
                Toast.makeText(mContext, "FAILD", Toast.LENGTH_SHORT).show();
                break;
            case BlueToothCommunication.CONNECT_NOT_ENABLE:
                Toast.makeText(mContext, "NOT ENABLED", Toast.LENGTH_SHORT).show();
                break;
            case BlueToothCommunication.CONNECT_NOT_SUPPORT:
                Toast.makeText(mContext, "NOT SUPPORT", Toast.LENGTH_SHORT).show();
                break;
            case BlueToothCommunication.CONNECT_SUCCESS:
                Toast.makeText(mContext, "CONNECT SUCCES", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
