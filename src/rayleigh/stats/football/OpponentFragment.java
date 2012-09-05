package rayleigh.stats.football;

import rayleigh.stats.football.listeners.OpponentListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class OpponentFragment extends Fragment {
	
	private OpponentEventListener event;
	
	private OpponentListener ol;
	
	private LinearLayout opponentButtons;
	private Context mContext;
	
	private int playerCount;
	
	private Button oppButton;
	
	private Button mLockPlayers;
	private boolean lockedPlayers = false;
	
	private Button mStartMatch;
	private boolean started = false;
	private boolean paused = false;
	
	public OpponentFragment(Context context, int count) {
		mContext = context;
		playerCount = count;
		
		oppButton = new Button(mContext);
    	ol = new OpponentListener(oppButton);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			event = (OpponentEventListener) activity;
			ol.setOpponentEventListener(event);
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OpponentEventListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		opponentButtons = new LinearLayout(mContext);
		opponentButtons.setOrientation(LinearLayout.VERTICAL);
        
        Button mNewPlayers = new Button(mContext);
        mNewPlayers.setText("Add Players");
        mNewPlayers.setContentDescription("add");
    	
    	mNewPlayers.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			event.opponentEvent(v.getContentDescription().toString());
    		}
    	});
    	
    	mLockPlayers = new Button(mContext);
        mLockPlayers.setText("Lock");
        mLockPlayers.setContentDescription("lock");
    	
    	mLockPlayers.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			if(!lockedPlayers) {
    				lockedPlayers = true;
    				event.opponentEvent("lock");
    				mLockPlayers.setText("Unlock");
    			}
    			else {
    				lockedPlayers = false;
    				event.opponentEvent("unlock");
    				mLockPlayers.setText("Lock");
    			}
    			
    		}
    	});
    	
    	mStartMatch = new Button(mContext);
    	mStartMatch.setText("Start Match");
    	mStartMatch.setContentDescription("start");
    	
    	mStartMatch.setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
    			if(!started) {
    				//Start the match
	    			mStartMatch.setText("Pause Match");
	    			started = true;
	    			event.opponentEvent("start");
    			}
    			else {
	    			if(!paused) {
	    				mStartMatch.setText("Unpause Match");
	    				paused = true;
	        			event.opponentEvent("pause");
	    			}
	    			else {
	    				mStartMatch.setText("Pause Match");
	    				paused = false;
	    				event.opponentEvent("unpause");
	    			}
    			}
    		}
    	});
    	
    	
    	oppButton.setText("Opponents");
    	oppButton.setContentDescription("intercept");
    	oppButton.setId(playerCount+1);
    	oppButton.setOnTouchListener(ol);
    	
    	Button tackleButton = new Button(mContext);
    	tackleButton.setText("Tackle");
    	tackleButton.setContentDescription("tackle");
    	tackleButton.setId(playerCount+1);
    	tackleButton.setOnTouchListener(ol);
    	
    	opponentButtons.addView(mNewPlayers);
    	opponentButtons.addView(mLockPlayers);
    	opponentButtons.addView(mStartMatch);
    	opponentButtons.addView(oppButton);
    	opponentButtons.addView(tackleButton);
		
		return opponentButtons;
	}

	
	public interface OpponentEventListener {
		public void opponentEvent(String buttonDesc);
	}
}
