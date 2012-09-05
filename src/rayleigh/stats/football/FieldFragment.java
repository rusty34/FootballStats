package rayleigh.stats.football;

import java.util.ArrayList;

import rayleigh.stats.football.listeners.MoveListener;
import rayleigh.stats.football.listeners.PlayerListener;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class FieldFragment extends Fragment  {

	private int defaultX, defaultY;
	private int width, height;
	private boolean lockedPlayers;
	
	private RelativeLayout field;
	private ArrayList<Button> buttons;
	private MoveListener ml;
	private PlayerListener pl;
	
	private Context mContext;
	
	public FieldFragment(Context context, PlayerListener l) {
		//super(context);	
		
		mContext = context;
		
		defaultX = 10;
		defaultY = 10;
		
		width = 60;
		height = 80;
		
		buttons = new ArrayList<Button>();
		ml = new MoveListener(this);
		pl = l;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		field = new RelativeLayout(mContext);
		return field;
	}
	
	
	public RelativeLayout getLayout() {
		return field;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void addPlayerButtons(ArrayList<Player> p) {
		
		int padding = 10;
		int rowSize = 5;
		int colSize = 4;
		
		int row = 0, col = 0;
		
		if(p.size() <= rowSize * colSize) {
			for(int i = 0; i<p.size(); i++) {
				int xPos = defaultX + col * (padding + width);
				int yPos = defaultY + row * (padding + height);
				
				Button b = new Button(mContext);
				b.setId(p.get(i).getId());
				b.setContentDescription("player");
				b.setOnTouchListener(ml);
				if(p.get(i).getNumber()!=null)
					b.setText(p.get(i).getNumber());
				buttons.add(b);
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
				
				params.leftMargin = xPos;
				params.topMargin = yPos;
				
				field.addView(b, params);
				
				col++;
				if(col % rowSize == 0) {
					row++;
					col = 0;
				}
			}
		}
		else {
			Log.e(FootballStatsActivity.TAG, "Buttons full");
		}
	}
	
	public void setLock(boolean lock) {
		lockedPlayers = lock;
		
		if(lockedPlayers) {
			for(Button b: buttons) {
				b.setOnTouchListener(pl);
			}
		}
		else {
			for(Button b: buttons) {
				b.setOnTouchListener(ml);
			}
		}
	}
	
	public void enableButtons(boolean enabled) {
		for(Button b: buttons) {
			b.setEnabled(enabled);
		}
	}
	
}
