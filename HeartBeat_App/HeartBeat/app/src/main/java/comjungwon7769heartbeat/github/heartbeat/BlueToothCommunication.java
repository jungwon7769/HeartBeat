package comjungwon7769heartbeat.github.heartbeat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AH on 2017-05-16.
 */
public class BlueToothCommunication implements Runnable {
    //상수 정의
    public static final int CONNECT_NOT_SUPPORT = -1, CONNECT_NOT_ENABLE = 2, CONNECT_SUCCESS = 1, CONNECT_FAILD = 0;
    public static final int CODE_EMOTION = 1, CODE_BZZ = 2, CODE_LED = 3, CODE_MY_BZZ = 4;
    //public static final boolean BZZ_MY = true, BZZ_FR = false;

    //변수 정의
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket btSock;

    private OutputStream outStream;
    private InputStream inStream;

    private int sendMode = -1;
    private Object data;

    public BlueToothHandler btHander;

    @Override
    public void run() {
        String msg;

        try {
            if (sendMode == -1) return;
            //블루투스 연결
            checkConnect(Constants.deviceName);
            //메시지 생성
            switch (sendMode) {
                case CODE_EMOTION:
                    if (data == null) return;
                    msg = CODE_EMOTION + "/" + ((Constants.Emotion) data).getColor() + "@";
                    sendMsg(msg);
                    break;
                case CODE_BZZ:
                    if (data == null) return;
                    msg = CODE_BZZ + "/" + data.toString() + "@";
                    sendMsg(msg);
                    break;
                case CODE_LED:
                    if (data == null) return;
                    msg = CODE_LED + "/" + data.toString() + "@";
                    sendMsg(msg);
                    break;
                case CODE_MY_BZZ:
                    msg = CODE_MY_BZZ + "@";
                    sendMsg(msg);
                    break;
            }

            Log.i("Test", "switch end");
        }catch (Exception e){
            Log.i("Test", "zz");}
        //블루투스 연결해제
        closeSock();

        Log.i("Test", "closeENd");
        return;
    }

    public boolean setSendMode(int value) {
        if (value > 4 || value < 1) return false;
        else {
            sendMode = value;
            return true;
        }
    }

    public void setData(Object value) {
        data = value;
    }

    private int checkConnect(String name) {
        if (btSock != null && btSock.isConnected()) {
            return CONNECT_SUCCESS;
        }
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btDevice = null;
        btSock = null;
        outStream = null;
        inStream = null;

        //Device not support bluetooth
        if (btAdapter == null) {
            return CONNECT_NOT_SUPPORT;
        }
        //Bluetooth Not Enabled, Request Enabled 필요
        if (!btAdapter.isEnabled()) {
            return CONNECT_NOT_ENABLE;
        }
        //Paired Device Search
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //인자값에 해당하는 name 을 가진 device인 경우
                if (device.getName().contains(name)) {
                    btDevice = device;
                }
            }
        }
        //Paired Device 중 name이 일치하는 Device 없음
        if (btDevice == null) {
            return CONNECT_FAILD;
        }

        //Socket Connect
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");    //SPP
        try {
            btSock = btDevice.createRfcommSocketToServiceRecord(uuid);
            btSock.connect();
            inStream = btSock.getInputStream();
            outStream = btSock.getOutputStream();
            return CONNECT_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CONNECT_FAILD;
    }

    private boolean sendMsg(String msg) {
        if (btSock == null || !btSock.isConnected()) return false;

        Log.i("Test", "sendMsg");

        byte buf[] = msg.getBytes();
        try {
            outStream.write(buf);
            outStream.write('\r');
            outStream.write('\n');
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void closeSock() {
        try {
            if (inStream != null) inStream.close();
            if (outStream != null) outStream.close();
            if (btSock != null) btSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
