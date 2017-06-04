package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {
	//색상선택기의 색 목록
	private final int[] colors = new int[]{Color.RED, Color.parseColor("#FFFF00"), Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.parseColor("#FF00FF"), Color.RED};
	private int width, height;      //뷰의 가로, 세로 크기
	private String selectColor;     //사용자가 선택한 색상
	private Shader shader;          //색선택기 그라데이션용 쉐이더
	private Paint paint, centerPaint;   //선택기용 paint, 선택한색상 보여주기용 paint
	private float r;    //원크기계산용

	public ColorPickerView(Context context) {
		super(context);
		selectColor = "66CCFF";
		init();
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		selectColor = "66CCFF";
		init();
	}

	public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		selectColor = "66CCFF";
		init();
	}

	public String getSelectColor() {
		return selectColor;
	}

	public void init() {
		shader = new SweepGradient(width / 2, height / 2, colors, null);

		paint = new Paint();    //Color 선택용 원 초기화
		paint.setShader(shader);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(120);
		r = paint.getStrokeWidth() / 2f;

		centerPaint = new Paint();  //선택한 Color Display 원 초기화
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// height 크기 구하기
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

		// width 크기 구하기
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
		invalidate();   //크기맞게 다시그림
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//Color 원 그리기
		canvas.drawOval(new RectF(r, r, (width - r), height - r), paint);

		//중심원 그리기
		centerPaint.setColor(Color.parseColor("#" + selectColor));
		canvas.drawOval(new RectF(width / 4, height / 4, width * 3 / 4, height * 3 / 4), centerPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX() - width / 2;
		float y = -(event.getY() - height / 2);
		float angle, piece, percent;
		int color1, color2, index, mixColorR, mixColorG, mixColorB;

		//각도구하기
		angle = (float) Math.atan2(y, x) * (float) (180.0 / 3.14);
		if(angle < 0) angle += 360;
		angle = 360 - angle;
		piece = 360 / colors.length;

		//선택한 각도의 색구하기
		index = (angle == 0) ? 0 : (int) (angle / piece);
		color1 = colors[index];

		index++;
		if(index >= colors.length) index = 0;
		color2 = colors[index];

		percent = ((angle % piece) / piece);

		//색상혼합
		mixColorR = (int) ((Color.red(color1) * (1 - percent)) + (Color.red(color2) * percent));
		mixColorG = (int) ((Color.green(color1) * (1 - percent)) + (Color.green(color2) * percent));
		mixColorB = (int) ((Color.blue(color1) * (1 - percent)) + (Color.blue(color2) * percent));

		int c = Color.rgb(mixColorR, mixColorG, mixColorB);
		selectColor = Integer.toHexString(c).substring(2);  //선택색 저장

		invalidate();   //다시그림

		return true;
	}

}
