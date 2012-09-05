package rayleigh.stats.football;

import java.util.ArrayList;

import rayleigh.stats.football.OpponentFragment.OpponentEventListener;
import rayleigh.stats.football.listeners.PlayerListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

public class FootballStatsActivity extends Activity implements OpponentEventListener {
	
	private static final int STATS_ID = 12345;
	private static final int FIELD_ID = 56789;
	
	private static Context context;
	private static StatHandler sHandler;
	
	public static final String TAG = "FootballStats";
	private static final boolean debug = true;
	
	private FieldFragment field;
	private OpponentFragment oppButtons;
	private StatsTable table;
	private ArrayList<Team> teams;
	//private static final int teamCount = 2;
	private static final int playerCount = 11;
	private static final int updateRate = 5000;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FootballStatsActivity.context = getApplicationContext();
        
        LinearLayout main = new LinearLayout(context);
        
        setContentView(main);
        //setContentView(R.layout.main);
        
        if(debug)
        	Log.i(TAG, "--- ON CREATE ---");
        
        
        PlayerListener pl = new PlayerListener();
        
        
        LinearLayout l1 = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f);
        l1.setLayoutParams(params);
        l1.setId(FIELD_ID);
        {
	        field = new FieldFragment(context,pl);
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.add(FIELD_ID, field);
	        ft.commit();
        }
        main.addView(l1);
        
        //LinearLayout l2 = (LinearLayout) findViewById(R.id.linearLayout2);
        LinearLayout l2 = new LinearLayout(context);
        l2.setLayoutParams(params);
        l2.setBackgroundColor(Color.GREEN);
        l2.setOrientation(LinearLayout.VERTICAL);
        l2.setId(STATS_ID);
        {
        	oppButtons = new OpponentFragment(context, playerCount);
        	FragmentTransaction ft = getFragmentManager().beginTransaction();
        	ft.add(STATS_ID, oppButtons);
        	ft.commit();
        }
        main.addView(l2);
    	
    	table = new StatsTable(context, updateRate);
    	table.setBackgroundColor(Color.BLACK);
    	l2.addView(table);
        
    }
    
    public static Context getAppContext() {
    	return FootballStatsActivity.context;
    }
    
    public static StatHandler getStatHandler() {
    	return FootballStatsActivity.sHandler;
    }

	@Override
	public void opponentEvent(String buttonDesc) {
		
		if(buttonDesc.equals("add")) {
			teams = new ArrayList<Team>();
			Team t1 = new Team(0, Team.US, playerCount);
			Team t2 = new Team(1, Team.OPPONENT, 1);
			teams.add(t1);
			teams.add(t2);
			
			field.addPlayerButtons(t1.getPlayers());
			
			sHandler = new StatHandler(StatHandler.FOOTBALL, teams, updateRate);
		}
		else if(buttonDesc.equals("lock")) {
			field.setLock(true);
		}
		else if(buttonDesc.equals("unlock")) {
			field.setLock(false);
		}
		else if(buttonDesc.equals("start")) {
			//Start the match
			sHandler.startMatch();
			table.startMatch();
		}
		else if(buttonDesc.equals("pause")) {
			sHandler.pauseMatch();
			table.setPause(true);
		}
		else if(buttonDesc.equals("unpause")) {
			sHandler.unPauseMatch();
			table.setPause(false);
		}
		
	}
}