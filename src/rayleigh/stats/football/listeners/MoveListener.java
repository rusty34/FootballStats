package rayleigh.stats.football.listeners;

import rayleigh.stats.football.FieldFragment;
import rayleigh.stats.football.FootballStatsActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MoveListener implements OnTouchListener {

	private FieldFragment field;
	
	private int buttonStartX = 0, buttonStartY = 0;
	private int cursorStartX=0, cursorStartY=0;
	private boolean dragging = false;
	
	public MoveListener(FieldFragment f) {
		field = f;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(dragging) {
			if(cursorStartX == 0 && cursorStartY == 0) {
				Log.i(FootballStatsActivity.TAG, "Dragging button "+v.getId());
				cursorStartX = (int)event.getRawX();
				cursorStartY = (int)event.getRawY();
			}
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(field.getWidth(), field.getHeight());
			int diffX = (int)event.getRawX() - cursorStartX;
			int diffY = (int)event.getRawY() - cursorStartY;
			
			params.leftMargin = buttonStartX + diffX;
			params.topMargin = buttonStartY + diffY;
			
			field.getLayout().updateViewLayout(v, params);
			
			if(event.getActionMasked() == MotionEvent.ACTION_UP) {
				Log.i(FootballStatsActivity.TAG, "End drag "+v.getId());
				cursorStartX = 0;
				cursorStartY = 0;
				((Button)v).setPressed(false);
				dragging = false;
			}
			return true;
		}
		else {
			Log.i(FootballStatsActivity.TAG, "Press on button "+v.getId());
			buttonStartX = (int)v.getX();
			buttonStartY = (int)v.getY();
			//((PlayerButton)v).setPressed(true);
			dragging = true;
			return true;
		}
	}

}
