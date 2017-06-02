import java.net.*;
import java.io.*;

public class SendMP3 {
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(12001);
			System.out.println("SendMP3 Server ready..");
			while (true) {
				Socket s = ss.accept();
				System.out.println("접속 성공");
				System.out.println(s.getInetAddress());// Ip address
				SendServerThread st = new SendServerThread(s);
				st.start();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}// end of class

class SendServerThread extends Thread {
	Socket socket;

	public SendServerThread(Socket s) {
		socket = s;
	}

	public void run() {
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			// 전송된 file명 reading
			System.out.println("파일명 받기 대기중...");
			String filename = dis.readUTF();
			System.out.println(filename + "\t을 받습니다.");

			BufferedInputStream bis = new BufferedInputStream(new FileInputStream("C:\\Users\\Hobin\\Desktop\\Server\\"+filename));
			int ch=0;
			BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
			while((ch=bis.read())!=-1){
				bos.write(ch);
				System.out.println("파일전송 : "+ch);
			}
			bos.flush();
			bos.close();
			bis.close();
			socket.close();
			
			System.out.println("클라이언트 종료\n");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
