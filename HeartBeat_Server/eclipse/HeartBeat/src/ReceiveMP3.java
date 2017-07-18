import java.net.*;
import java.io.*;

public class ReceiveMP3 {
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(12000);
			System.out.println("ReceiveMP3 Server ready..");
			while (true) {
				Socket s = ss.accept();
				System.out.println("접속 성공");
				System.out.println(s.getInetAddress());// Ip address
				ReceiveServerThread st = new ReceiveServerThread(s);
				st.start();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}// end of class

class ReceiveServerThread extends Thread {
	Socket socket;

	public ReceiveServerThread(Socket s) {
		socket = s;
	}

	public void run() {
		try {
			// client단에서 전송된 file내용 read 계열 stream
			BufferedInputStream up = new BufferedInputStream(socket.getInputStream());
			DataInputStream fromClient = new DataInputStream(up);
			// 전송된 file명 reading
			System.out.println("파일명 받기 대기중...");
			String filename = fromClient.readUTF();
			System.out.println(filename + "\t을 받습니다.");

			// client단에서 전송되는 file 내용을 server단에 생성시킨 file에 write할수 있는 stream
			FileOutputStream toFile = new FileOutputStream("C:\\Users\\Hobin\\Desktop\\Server\\" + filename);
			BufferedOutputStream outFile = new BufferedOutputStream(toFile);
			int ch = 0;
			while ((ch = up.read()) != -1) {
				System.out.println(ch);//test
				outFile.write(ch);
			}
			outFile.flush();
			outFile.close();
			fromClient.close();
			socket.close();
			System.out.println("클라이언트 종료\n");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
