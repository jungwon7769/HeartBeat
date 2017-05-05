package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by AH on 2017-05-05.
 */
public class ColorPickerView extends View{
	private final int[] colors = new int[] {
			Color.RED, Color.parseColor("#FFFF00"),Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.parseColor("#FF00FF")
	};
	private int width, height;
	private String selectColor;
	private Shader shader;
	private Paint paint, centerPaint;
	private float r;

	public ColorPickerView(Context context) {
		super(context);
		selectColor = "66CCFF";
		init();
	}
	public ColorPickerView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
		selectColor = "66CCFF";
		init();
	}
	public ColorPickerView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		selectColor = "66CCFF";
		init();
	}

	public void init(){
		shader = new SweepGradient(width/2, height/2, colors, null);

		paint = new Paint();    //Color 선택용 원 초기화
		paint.setShader(shader);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(80);
		r = paint.getStrokeWidth()/2f;

		centerPaint = new Paint();  //선택한 Color Display 원 초기화
	}

	@Override
	protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		// height 진짜 크기 구하기
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		switch(heightMode) {
			case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
				height = heightMeasureSpec;
				break;
			case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
				height = 20;
				break;
			case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
				height = MeasureSpec.getSize(heightMeasureSpec);
				break;
		}

		// width 진짜 크기 구하기
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		width = 0;
		switch(widthMode) {
			case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
				width = widthMeasureSpec;
				break;
			case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
				width = 20;
				break;
			case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
				width = MeasureSpec.getSize(widthMeasureSpec);
				break;
		}

		setMeasuredDimension(width, height);

		init();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas){
		//Color 원 그리기
		canvas.drawOval(new RectF(paint.getStrokeWidth()/2f, paint.getStrokeWidth()/2f,
				(width - paint.getStrokeWidth()/2f), height - paint.getStrokeWidth()/2f), paint);

		//중심원 그리기
		centerPaint.setColor(Color.parseColor("#" + selectColor));
		canvas.drawOval(new RectF(width/4, height/4, width*3/4, height*3/4), centerPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX() - width/2;
		float y = -(event.getY() - height/2);
		float angle;

		switch(event.getAction()) {
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_DOWN:
				angle = (float)Math.atan2(y, x);
				angle = (float)(angle * 180.0 / 3.14);

				Log.i("Test", "x " + x + "y " + y + "angle " + angle);
				break;
			case MotionEvent.ACTION_MOVE:
				angle = (float)Math.atan2(y, x);
				angle = (float)(angle * 180.0 / 3.14);

				Log.i("Test", "x " + x + "y " + y + "angle " + angle);

				break;
		}
		return true;
	}

}
