package rayleigh.stats.football.listeners;

import rayleigh.stats.football.FootballStatsActivity;
import rayleigh.stats.football.OpponentFragment.OpponentEventListener;
import rayleigh.stats.football.StatHandler;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OpponentListener implements OnTouchListener {

	OpponentEventListener eventListener;
	private View oppButton;
	private int lastId;
	private boolean keepPossession = false;
	private boolean buttonOff = false;
	
	public OpponentListener(View oB) {
		oppButton = oB;
	}
	
	public void setOpponentEventListener(OpponentEventListener activity) {
		eventListener = activity;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		String desc = v.getContentDescription().toString();
		
		if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			if(desc.equals("intercept")) {
				v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				FootballStatsActivity.getStatHandler().gameEvent(StatHandler.THEM, 1, StatHandler.EVENT_INTERCEPT);
			}
			else if(desc.equals("tackle")) {
				v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				oppButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				FootballStatsActivity.getStatHandler().gameEvent(StatHandler.THEM, 1, StatHandler.EVENT_TACKLE);
				buttonOff = true;
			}
			else if(desc.equals("shot_on_target")) {
				v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
				FootballStatsActivity.getStatHandler().gameEvent(StatHandler.US, lastId, StatHandler.EVENT_SHOT_ON_TARGET);
				keepPossession = true;
				buttonOff = true;
			}
			else if(desc.equals("shot_off_target")) {
				v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				oppButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				FootballStatsActivity.getStatHandler().gameEvent(StatHandler.THEM, 1, StatHandler.EVENT_SHOT_OFF_TARGET);
				buttonOff = true;
			}
			else if(desc.equals("shot_blocked")) {
				v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
				FootballStatsActivity.getStatHandler().gameEvent(StatHandler.US, lastId, StatHandler.EVENT_SHOT_BLOCKED);
				keepPossession = true;
				buttonOff = true;
			}
			else if(desc.equals("shot_woodwork")) {
				v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
				FootballStatsActivity.getStatHandler().gameEvent(StatHandler.US, lastId, StatHandler.EVENT_SHOT_WOODWORK);
				keepPossession = true;
				buttonOff = true;
			}
			
			//TODO: fix this logic
			if(!keepPossession) {
				//Disable button in other fragment
				
				keepPossession = false;
			}
			else {
				oppButton.getBackground().setColorFilter(null);
			}
			
		}
		else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
			if(buttonOff) {
				v.getBackground().setColorFilter(null);
				buttonOff = false;
			}
		}
		
		return true;
	}

}
