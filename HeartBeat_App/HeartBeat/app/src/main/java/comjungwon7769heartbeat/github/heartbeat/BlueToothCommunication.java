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
public class BlueToothCommunication {
	//상수 정의
	public static final int CONNECT_NOT_SUPPORT = -1, CONNECT_NOT_ENABLE = 2, CONNECT_SUCCESS = 1, CONNECT_FAILD = 0;
	private static final int CODE_EMOTION = 1, CODE_BZZ = 2, CODE_LED = 3, CODE_MY_BZZ = 4;
	public static final boolean BZZ_MY = true, BZZ_FR = false;

	//변수 정의
	private BluetoothAdapter btAdapter;
	private BluetoothDevice btDevice;
	private BluetoothSocket btSock;

	OutputStream outStream;
	InputStream inStream;

	public int checkConnect(String name) {
		if(btSock != null) {
			closeSock();
		}
		Log.i("Test", "tct");
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btDevice = null;
		btSock = null;
		outStream = null;
		inStream = null;

		//Device not support bluetooth
		if(btAdapter == null) {
			Log.i("Test", "not Support");
			return CONNECT_NOT_SUPPORT;
		}
		//Bluetooth Not Enabled, Request Enabled 필요
		if(!btAdapter.isEnabled()) {
			Log.i("Test", "not Enable");
			return CONNECT_NOT_ENABLE;
		}
		Log.i("Test", "bt connect continue");
		//Paired Device Search
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		if(pairedDevices.size() > 0) {
			for(BluetoothDevice device : pairedDevices) {
				//인자값에 해당하는 name 을 가진 device인 경우
				if(device.getName().contains(name)) {
					btDevice = device;
				}
			}
		}
		//Paired Device 중 name이 일치하는 Device 없음
		if(btDevice == null) {
			return CONNECT_FAILD;
		}

		//Socket Connect
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");    //SPP
		try {
			Log.i("Test", "Socket Connect~~");
			btSock = btDevice.createRfcommSocketToServiceRecord(uuid);
			btSock.connect();
			inStream = btSock.getInputStream();
			outStream = btSock.getOutputStream();
			return CONNECT_SUCCESS;
		} catch(IOException e) {
			e.printStackTrace();
		}

		return CONNECT_FAILD;
	}

	public boolean sendLED(String color) {
		if(btSock == null || !btSock.isConnected()) return false;

		String data = CODE_LED + "/" + color + "//";   //Message Create(정해놓은 Protocol에 따라 만든다)
		byte buf[] = data.getBytes();
		try {
			outStream.write(buf);
		} catch(IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean sendBzz(String color, boolean my_bzz) {
		if(!btSock.isConnected()) return false;

		String data;
		//Message Create
		if(my_bzz = BZZ_MY) {
			data = CODE_MY_BZZ + "//";
		}
		else {
			data = CODE_BZZ + "/" + color + "//";
		}

		byte buf[] = data.getBytes();
		try {
			Log.i("Test", "Trans~~");
			outStream.write(buf);
		} catch(IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean sendEmotion(Constants.Emotion emotion) {
		if(btSock == null || !btSock.isConnected()) return false;

		String data = CODE_EMOTION + "/" + emotion.getColor() + "//";   //Message Create
		byte buf[] = data.getBytes();
		try {
			outStream.write(buf);
		} catch(IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void closeSock() {
		try {
			if(inStream != null) inStream.close();
			if(outStream != null) outStream.close();
			if(btSock != null) btSock.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
