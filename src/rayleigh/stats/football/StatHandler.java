package rayleigh.stats.football;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import android.util.Log;

public class StatHandler {

	public static final int US = 0;
	public static final int THEM = 1;
	
	public static final int FOOTBALL = 1;
	public static final int RUGBY = 2;
	//Add support for more sports
	
	public static final int EVENT_PASS_SUCCESS = 0;
	public static final int EVENT_INTERCEPT = 1;
	public static final int EVENT_TACKLE = 2;
	public static final int EVENT_SHOT_ON_TARGET = 3;
	public static final int EVENT_SHOT_OFF_TARGET = 4;
	public static final int EVENT_SHOT_BLOCKED = 5;
	public static final int EVENT_SHOT_WOODWORK = 6;
	public static final int EVENT_START_MATCH = 7;
	private boolean rebound = false;
	
	private Team lastTeam;
	private Player lastPlayer;
	
	private ArrayList<Team> teams;
	
	private Date startTime;
	
	private Date pauseStart;
	private long pauseTime;
	private boolean paused;
	
	private LinkedList<long[]> fiveMins;
	//private int updateRate;
	private double fiveMinCount;
	private int updateCount;
	
	private boolean matchStart;
	private int firstTeam, firstPlayer;
	private boolean firstTimeDone;
	private long fiveSecGameTime;
	
	
	public StatHandler(int sport, ArrayList<Team> teamList, int rate) {
		teams = teamList;
		//currentSport = sport;
		//updateRate = rate;
		
		updateCount = 0;
		fiveMinCount = Math.floor(300000 / rate);
		
		firstTeam = -1;
		firstPlayer = -1;
		matchStart = false;
		
		firstTimeDone = false;
		
		fiveSecGameTime = 0l;
		pauseTime = 0l;
		paused = false;
		
		fiveMins = new LinkedList<long[]>();
	}
	
	public void setTeamList(ArrayList<Team> teamList) {
		teams = teamList;
	}
	
	public void startMatch() {
		startTime = new Date();
		matchStart = true;
		gameEvent(firstTeam, firstPlayer, EVENT_START_MATCH);
	}
	
	public long getGameTime() {
		Date currentTime = new Date();
		
		long gameTime = currentTime.getTime() - startTime.getTime() - pauseTime;
		return gameTime;
	}
	
	public void pauseMatch() {
		pauseStart = new Date();
		pausePossession(true);
		paused = true;
	}
	
	public void unPauseMatch() {
		Date pauseEnd = new Date();
		pauseTime += pauseEnd.getTime() - pauseStart.getTime();
		pausePossession(false);
		paused = false;
	}
	
	public void endMatch() {
		//long totalTime = getGameTime();
		
	}
	
	public int[] getPassStats(int team) {
		int[] passCount = new int[2];
		
		for(Player p: teams.get(team).getPlayers()) {
			passCount[0]+= p.passSucceed;
			passCount[1]+= p.passCount;
		}
		
		return passCount;
	}
	
	public int[] getShotStats(int team) {
		int[] shotCount = new int[2];
		for(Player p: teams.get(team).getPlayers()) {
			shotCount[0]+= p.shotOnTarget;
			shotCount[1]+= p.shotCount;
		}
		return shotCount;
	}
	
	public int[] getPossessionStats() {
		int[] possStat = new int[2];
		long teamOne = getFiveMinPossession(US);
		long teamTwo = getFiveMinPossession(THEM);
		
		long time = 0l;
		if(getGameTime()<300000l) {
			time = fiveSecGameTime;
		}
		else {
			time = 300000l;
		}
		
		String timeOne = Float.toString(1f*teamOne*100/time);
		String timeTwo = Float.toString(1f*teamTwo*100/time);
		possStat[0] = Math.round(teamOne*100f/time);
		possStat[1] = Math.round(teamTwo*100f/time);
		
		Log.i(FootballStatsActivity.TAG, "Total - "+(teamOne+teamTwo));
		Log.i(FootballStatsActivity.TAG, "Time - "+time+" Team1 - "+teamOne+" Time1 - "+timeOne);
		Log.i(FootballStatsActivity.TAG, "Time - "+time+" Team2 - "+teamTwo+" Time2 - "+timeTwo);
		
		return possStat;
	}
	
	public long getPossession(int team) {
		long totalPossession = 0;
		
		for(Player p: teams.get(team).getPlayers()) {
			totalPossession += p.getPossessionTime();
		}
		
		return totalPossession;
	}
	
	public long getFiveMinPossession(int team) {
		return teams.get(team).getFiveMinPossession();
	}
	
	public void updateFiveMinutePossession() {
		long teamOnePos = teams.get(0).getPossessionTime();
		long teamTwoPos = teams.get(1).getPossessionTime();
		fiveSecGameTime = getGameTime();
		
		long[] times = new long[2];
		times[0] = teamOnePos;
		times[1] = teamTwoPos;
		
		fiveMins.add(times);
		if(updateCount > fiveMinCount) {
			fiveMins.remove();
			Log.i(FootballStatsActivity.TAG, "Five minutes have passed");
		}
		else {
			updateCount++;
		}
		
		long[] first = fiveMins.getFirst();
		long[] last = fiveMins.getLast();
		
		long teamOneTime = last[0] - first[0];
		long teamTwoTime = last[1] - first[1];
		
		//Log.i(FootballStatsActivity.TAG, "Team1Pos - "+teamOnePos+" Team1Time - "+teamOneTime);
		
		teams.get(0).setFiveMinPossesion(teamOneTime);
		teams.get(1).setFiveMinPossesion(teamTwoTime);
	}
	
	public void pausePossession(boolean paused) {
		if(paused)
			Log.i(FootballStatsActivity.TAG, "Last Team - "+lastTeam+" Last Player - "+lastPlayer);
		
		//Change possession of last team + player that had possession
		//true = paused = take possession away
		//false = not paused = give possession back
		lastTeam.setPossession(!paused);
		lastPlayer.setPossession(!paused);
	}
	
	public void gameEvent(int team, int player, int event) {
		if(matchStart && !paused) {
			
			Team t = teams.get(team);
			Player p = null;
			for(Player x: t.getPlayers()) {
				if(x.getId()==player)
					p = x;
			}
			
			if(p == lastPlayer)
				p = null;
			
			if(p!=null) {
				switch(event) {
				case EVENT_PASS_SUCCESS:
					if(!rebound)
						lastPlayer.passAttempt(true);
					lastPlayer.setPossession(false);
					p.setPossession(true);
					rebound = false;
					break;
				case EVENT_INTERCEPT:
					if(!rebound)
						lastPlayer.passAttempt(false);
					lastPlayer.setPossession(false);
					p.setPossession(true);
					rebound = false;
					break;
				case EVENT_TACKLE:
					lastPlayer.setPossession(false);
					p.setPossession(true);
					rebound = false;
					break;
				case EVENT_SHOT_ON_TARGET:
					lastPlayer.shotAttempt(Player.SHOT_ON_TARGET);
					rebound = true;
					break;
				case EVENT_SHOT_OFF_TARGET:
					lastPlayer.shotAttempt(Player.SHOT_OFF_TARGET);
					lastPlayer.setPossession(false);
					p.setPossession(true);
					rebound = true;
					break;
				case EVENT_SHOT_BLOCKED:
					lastPlayer.shotAttempt(Player.SHOT_BLOCKED);
					rebound = true;
					break;
				case EVENT_SHOT_WOODWORK:
					lastPlayer.shotAttempt(Player.SHOT_WOODWORK);
					rebound = true;
					break;
				case EVENT_START_MATCH:
					if(!firstTimeDone) {
						t.setFirstPossession(startTime);
						p.setFirstPossession(startTime);
						firstTimeDone = true;
					}
					break;
				default:
					break;
				}
				
				lastPlayer = p;
			}
			
			if(lastTeam != null && lastTeam.getTeamId() != t.getTeamId()) {
				lastTeam.setPossession(false);
				t.setPossession(true);
			}
			
			lastTeam = t;
		}
		else {
			firstTeam = team;
			firstPlayer = player;
		}
	}
	
}
