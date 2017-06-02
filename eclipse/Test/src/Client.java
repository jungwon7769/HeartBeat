import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) throws Exception {
		Socket s = new Socket("1.236.102.161", 12001);
		String filename = null;
		try {
			filename = "1496127871132.3gp";
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(filename);
			
			BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Hobin\\Desktop\\Client\\"+filename));
			int ch=0;
			while((ch=bis.read())!=-1){
				bos.write(ch);
				System.out.println("파일수신 : "+ch);
			}
			bos.flush();
			bos.close();
			dos.close();
			s.close();
			
			System.out.println("파일전송 완료\n");

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}