package rayleigh.stats.football;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatsTable extends TableLayout {

	private String passString = "Passes - Successful/Total";
	private String shotString = "Shots - On Target/Total";
	private String possString = "Possession - T1 / T2";
	
	private TextView passStat;
	private TextView shotStat;
	private TextView possStat;
	
	private Context context;
	
	private Handler handler;
	//private long hStartTime = 0L;
	private StatHandler stats;
	private int updateRate;
	
	private boolean firstUpdate;
	private boolean paused;
	
	public StatsTable(Context newContext, int rate) {
		super(newContext);
		
		context = newContext;
		handler = new Handler();
		updateRate = rate;
		
		firstUpdate = true;
		paused = false;
		
		createStatsTable();
	}
	
	private void createStatsTable() {
		
		//Create the title row
		TableRow titleRow = new TableRow(context);
		titleRow.setGravity(Gravity.CENTER_HORIZONTAL);
		
		TextView title = new TextView(context);
		title.setText("Team Statistics");
		title.setGravity(Gravity.CENTER);
		title.setTypeface(Typeface.DEFAULT_BOLD);
		
		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.span = 2;
		
		titleRow.addView(title, params);
		
		this.addView(titleRow);
		
		//Pass Row
		TableRow passRow = new TableRow(context);
		
		TextView passTitle = new TextView(context);
		passTitle.setText(passString);
		passTitle.setGravity(Gravity.LEFT);
		passTitle.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		
		passStat = new TextView(context);
		passStat.setGravity(Gravity.CENTER_HORIZONTAL);
		
		passRow.addView(passTitle);
		passRow.addView(passStat);
		
		this.addView(passRow);
		
		//Shots Row
		TableRow shotRow = new TableRow(context);
		
		TextView shotTitle = new TextView(context);
		shotTitle.setText(shotString);
		shotTitle.setGravity(Gravity.LEFT);
		shotTitle.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		
		shotStat = new TextView(context);
		shotStat.setGravity(Gravity.CENTER_HORIZONTAL);
		
		shotRow.addView(shotTitle);
		shotRow.addView(shotStat);
		
		this.addView(shotRow);
		
		//Possession Row
		TableRow possRow = new TableRow(context);
		TextView possTitle = new TextView(context);
		possTitle.setText(possString);
		shotTitle.setGravity(Gravity.LEFT);
		shotTitle.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		
		possStat = new TextView(context);
		possStat.setGravity(Gravity.CENTER_HORIZONTAL);
		
		possRow.addView(possTitle);
		possRow.addView(possStat);
		
		this.addView(possRow);
		
	}
	
	public void startMatch() {
		stats = FootballStatsActivity.getStatHandler();
		
		updateStatsUI();
		//hStartTime = System.currentTimeMillis();
		handler.removeCallbacks(updateTimeTask);
		handler.postDelayed(updateTimeTask, updateRate);
	}
	
	public void setPause(boolean p) {
		paused = p;
	}
	
	private Runnable updateTimeTask = new Runnable() {
		public void run() {
			if(!paused)
				updateStatsUI();
				
			handler.postDelayed(this, updateRate);
		}
	};
	
	private void updateStatsUI() {
		stats.updateFiveMinutePossession();
		int[] passStats = stats.getPassStats(StatHandler.US);
		int[] shotStats = stats.getShotStats(StatHandler.US);
		int[] possStats = stats.getPossessionStats();
		
		Log.i(FootballStatsActivity.TAG, "Updating stats");
		updatePassStat(passStats[0], passStats[1]);
		updateShotStat(shotStats[0], shotStats[1]);
		updatePossessionStat(possStats[0], possStats[1]);
	}
	
	public void updatePassStat(int success, int total) {
		String stat = success + "/" + total;
		passStat.setText(stat);
	}
	
	public void updateShotStat(int onTarget, int total) {
		String stat = onTarget + "/" + total;
		shotStat.setText(stat);
	}
	
	public void updatePossessionStat(int teamOne, int teamTwo) {
		String stat;
		if(firstUpdate) {
			stat = "- / -";
			firstUpdate = false;
		}
		else {
			stat = teamOne +"% / " + teamTwo +"%";
		}
		possStat.setText(stat);
	}

}
