package comjungwon7769heartbeat.github.heartbeat;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * Created by Hobin on 2017-05-29.
 */

public class SendMP3 extends Thread {
    Socket s;
    String filename = null;
    BufferedOutputStream bos = null;
    BufferedInputStream bis = null;
    FileInputStream fis = null;
    DataOutputStream dos = null;
    String fileName=null;

    public SendMP3(String fileName){
        try {
            s = new Socket(Constants.SERVERURL, 12000);
            this.fileName=fileName;
            bos = new BufferedOutputStream(s.getOutputStream());
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF("test.mp3");////////////////나중에 수정
            fis = new FileInputStream(filename);
            bis = new BufferedInputStream(fis);
        }catch(Exception e){
            Log.d("TEST_ SendMP3", e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            int ch = 0;

            System.out.println("test");
            while ((ch = bis.read()) != -1) {
                System.out.println("test");
                bos.write(ch);
            }
            System.out.println("test");

            bos.flush();
            bos.close();
            fis.close();
            s.close();

        }
        catch(Exception e){
            Log.d("TEST2_ SendMP3", e.getMessage());
        }
    }
}
