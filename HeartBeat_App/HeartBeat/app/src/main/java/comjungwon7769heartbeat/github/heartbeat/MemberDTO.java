package comjungwon7769heartbeat.github.heartbeat;

/**
 * Created by Hobin on 2017-05-25.
 */

public class MemberDTO {
    private String id;
    private String pwd;
    private String nick;
    private int mmode;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public int getMmode() {
        return mmode;
    }
    public void setMmode(int mmode) {
        this.mmode = mmode;
    }
}
