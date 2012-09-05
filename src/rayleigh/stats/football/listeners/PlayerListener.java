package rayleigh.stats.football.listeners;

import rayleigh.stats.football.FootballStatsActivity;
import rayleigh.stats.football.StatHandler;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PlayerListener implements OnTouchListener {

	private View lastView;
	private boolean buttonOff = false;
	
	public PlayerListener() {
		lastView = null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		String desc = v.getContentDescription().toString();
		
		if(v != lastView) {
			if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				if(desc.equals("player")) {
					v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
					FootballStatsActivity.getStatHandler().gameEvent(StatHandler.US, id, StatHandler.EVENT_PASS_SUCCESS);
				}
				
				if(lastView != null) {
					lastView.getBackground().setColorFilter(null);
				}
				
				lastView = v;
				
			}
			else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
				if(buttonOff) {
					v.getBackground().setColorFilter(null);
					buttonOff = false;
				}
			}
		}
		
		return true;
	}

}
