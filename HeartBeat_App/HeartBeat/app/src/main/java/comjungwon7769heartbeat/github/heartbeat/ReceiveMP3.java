package comjungwon7769heartbeat.github.heartbeat;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

/**
 * Created by Hobin on 2017-05-29.
 */

public class ReceiveMP3 extends Thread {
    Socket s;
    public String filename = null;
    DataOutputStream dos = null;
    BufferedInputStream bis=null;
    BufferedOutputStream bos=null;

    public ReceiveMP3(String filename){
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            init();
            receiveMP3();
        }
        catch(Exception e){
            Log.d("RECVTEST", e.getMessage());
        }
    }

    private void init(){
        try {
            s = new Socket(Constants.SERVERURL, 12001);
            dos = new DataOutputStream(s.getOutputStream());
            bis = new BufferedInputStream(s.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream("/storage/emulated/0/HeartBeat/tmp/myVoice/"+filename));
        }catch(Exception e){
            Log.d("RECVTEST", e.getMessage());
        }
    }

    private void receiveMP3(){
        try {
            dos.writeUTF(filename);
            int ch=0;
            while((ch=bis.read())!=-1){
                bos.write(ch);
                Log.d("RECVTEST",ch+"");
            }
            bos.flush();
            bos.close();
            dos.close();
            s.close();
        }catch(Exception e){
            Log.d("RECVTEST", e.getMessage());
        }
    }
}
