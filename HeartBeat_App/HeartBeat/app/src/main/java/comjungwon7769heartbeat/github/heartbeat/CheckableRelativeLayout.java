package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable{

	public CheckableRelativeLayout(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	public void setChecked(boolean checked) {
		CheckBox cb = (CheckBox)findViewById(R.id.msgItem_check);
		if(cb.isChecked() != checked) cb.setChecked(checked);
	}

	@Override
	public boolean isChecked() {
		CheckBox cb = (CheckBox)findViewById(R.id.msgItem_check);
		return cb.isChecked();
	}

	@Override
	public void toggle() {
		CheckBox cb = (CheckBox) findViewById(R.id.msgItem_check) ;
		setChecked(cb.isChecked() ? false : true) ;
	}
}
