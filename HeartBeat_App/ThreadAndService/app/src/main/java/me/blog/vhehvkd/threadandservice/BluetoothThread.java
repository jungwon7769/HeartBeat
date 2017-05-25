package me.blog.vhehvkd.threadandservice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothThread implements Runnable {
	//상수 정의
	public static final int CONNECT_NOT_SUPPORT = -1, CONNECT_NOT_ENABLE = 2, CONNECT_SUCCESS = 1, CONNECT_FAILD = 0;

	//변수 정의
	private BluetoothAdapter btAdapter;
	private BluetoothDevice btDevice;
	private BluetoothSocket btSock;

	OutputStream outStream;
	InputStream inStream;

	public String btName, btData;
	MyHandler myHandler;



	@Override
	public void run() {
		int chk = checkConnect(btName);
		switch(chk){
			case CONNECT_FAILD:
				myHandler.sendEmptyMessage(CONNECT_FAILD);
				break;
			case CONNECT_NOT_ENABLE:
				myHandler.sendEmptyMessage(CONNECT_NOT_ENABLE);
				break;
			case CONNECT_NOT_SUPPORT:
				myHandler.sendEmptyMessage(CONNECT_NOT_SUPPORT);
				break;
			case CONNECT_SUCCESS:
				myHandler.sendEmptyMessage(CONNECT_SUCCESS);
				break;
		}

		if(btData != null) {
			sendMsg(btData);
		}

		return;
	}

	public int checkConnect(String name) {
		if(btSock != null && btSock.isConnected()) {
			return CONNECT_SUCCESS;
		}
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		btDevice = null;
		btSock = null;
		outStream = null;
		inStream = null;

		//Device not support bluetooth
		if(btAdapter == null) {
			return CONNECT_NOT_SUPPORT;
		}
		//Bluetooth Not Enabled, Request Enabled 필요
		if(!btAdapter.isEnabled()) {
			return CONNECT_NOT_ENABLE;
		}
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

	public boolean sendMsg(String msg) {
		if(btSock == null || !btSock.isConnected()) return false;

		byte buf[] = msg.getBytes();
		try {
			outStream.write(buf);
		} catch(IOException e) {
			e.printStackTrace();
		}

		return true;
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
