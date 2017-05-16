import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	final static int port = 1200;
	public static void main(String args[]) {
		
		ServerSocket server=null;
		try {
			server = new ServerSocket(port);
			while (true) {
				//클라이언트 기다리기
				System.out.println("Waiting for client on port " + server.getLocalPort() + "...");
				Socket client = server.accept();
				Client c = new Client(client);
				c.start();
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				server.close();
			} catch (IOException e) {
			}
		}
	}
}
