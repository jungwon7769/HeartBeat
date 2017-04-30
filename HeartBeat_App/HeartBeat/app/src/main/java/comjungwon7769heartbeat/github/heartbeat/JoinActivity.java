package comjungwon7769heartbeat.github.heartbeat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JoinActivity extends Activity {

	private EditText txtID, txtPWD, txtNick;
	private Button btnJoin, btnUsable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);

		txtID = (EditText)findViewById(R.id.join_txtID);
		txtPWD = (EditText)findViewById(R.id.join_txtPWD);
		txtNick = (EditText)findViewById(R.id.join_txtNick);
		btnUsable = (Button) findViewById(R.id.join_btnUsable);
		btnJoin = (Button) findViewById(R.id.join_btnJoin);

		//중복검사 버튼 리스너 지정(ID_Usable_Check)
		btnUsable.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    ID_Usable_Check(txtID.getText().toString());
                }
            }
		);
		//회원가입 버튼 리스너 지정(Join)
		btnJoin.setOnClickListener(new View.OnClickListener(){
                 public void onClick(View v) {
                     Join(txtID.getText().toString(), txtPWD.getText().toString(), txtNick.getText().toString());
                 }
             }
		);

	}

	private  boolean ID_Usable_Check(String id){
		//ServerComu class Create
		//Request ID Exist

		return false;
	} //id_usable_check()

	private void Join(String id, String pwd, String Nick){
		//ServerComu Class Create
		//Request Join

		finish();
	} //join()
}
