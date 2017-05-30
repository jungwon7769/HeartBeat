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
    public String filename = null;
    BufferedOutputStream bos = null;
    BufferedInputStream bis = null;
    FileInputStream fis = null;
    DataOutputStream dos = null;

    @Override
    public void run() {
        try {
            init();
            sendMP3();
        }
        catch(Exception e){
            Log.d("TEST_ SendMP3", e.getMessage());
        }
    }

    private void init(){
        try {
            s = new Socket(Constants.SERVERURL, 12000);
            //this.filename=filename;
            bos = new BufferedOutputStream(s.getOutputStream());
            dos = new DataOutputStream(s.getOutputStream());
            ///////////////test
            Log.d("TESTTEST", filename);/*
            filename="/storage/emulated/legacy/Music/EXID - L.I.E.mp3";
            String realName = filename.split("/")[5];*/
            String realName = filename.split("/")[7];
            dos.writeUTF(realName);
            fis = new FileInputStream(filename);
            bis = new BufferedInputStream(fis);
        }catch(Exception e){
            Log.d("TEST_ SendMP3", e.getMessage());
        }
    }

    private void sendMP3(){
        try {
            int ch = 0;
            while ((ch = bis.read()) != -1) {
                bos.write(ch);
                Log.d("TESTTEST", ch+"");
            }
            bos.flush();
            bos.close();
            fis.close();
            s.close();
        }catch(Exception e){
            //Log.d("TEST_ SendMP3", e.getMessage());
        }
    }
}
