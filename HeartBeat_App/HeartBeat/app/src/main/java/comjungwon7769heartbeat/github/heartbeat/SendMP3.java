package comjungwon7769heartbeat.github.heartbeat;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

public class SendMP3 extends Thread {
	Socket s;
	public String filename = null;
	BufferedOutputStream bos = null;
	BufferedInputStream bis = null;
	FileInputStream fis = null;
	DataOutputStream dos = null;
	String sender;
	String receiver;

	public SendMP3(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	@Override
	public void run() {
		try {
			init();
			sendMP3();
		} catch(Exception e) {
		}
	}

	private void init() {
		try {
			s = new Socket(Constants.SERVERURL, 12000);
			bos = new BufferedOutputStream(s.getOutputStream());
			dos = new DataOutputStream(s.getOutputStream());

			String realName = filename.split("/")[7];
			dos.writeUTF(sender + "_" + receiver + "_" + realName);
			fis = new FileInputStream(filename);
			bis = new BufferedInputStream(fis);
		} catch(Exception e) {
		}
	}

	private void sendMP3() {
		try {
			int ch = 0;
			while((ch = bis.read()) != -1) {
				bos.write(ch);
			}
			bos.flush();
			bos.close();
			fis.close();
			s.close();
		} catch(Exception e) {
		}
	}
}
