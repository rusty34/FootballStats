package rayleigh.stats.football;

import java.util.ArrayList;
import java.util.Date;

public class Team {

	public static final int US = 0;
	public static final int OPPONENT = 1;
	
	private int teamId;
	private int teamType;
	private String teamName;
	private boolean hasPossession;
	
	private long timeInPossession;
	private long fiveMinPossession;
	
	private Date possessionGained;
	
	private ArrayList<Player> players;
	
	public Team(int id, int flag) {
		teamId = id;
		teamType = flag;
		hasPossession = false;
		
		timeInPossession = 0L;
		fiveMinPossession = 0L;
	}
	
	public Team(int id, int flag, int amount) {
		this(id, flag);
		createPlayers(amount);
	}
	
	public Team(int id, int flag, int amount, String name) {
		this(id, flag, amount);
		teamName = name;
	}
	
	public int getTeamId() {
		return teamId;
	}
	
	public int getTeamType() {
		return teamType;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setFirstPossession(Date start) {
		possessionGained = (Date)start.clone();
		//Log.i(FootballStatsActivity.TAG, "Start Time C - "+possessionGained.getTime());
		hasPossession = true;
	}
	
	public void setPossession(boolean pos) {
		if(pos) {
			if(!hasPossession)
				possessionGained = new Date();
		}
		else {
			if(hasPossession) {
				Date currentTime = new Date();
				long amount = currentTime.getTime() - possessionGained.getTime();
				
				timeInPossession += amount;
			}
		}
		hasPossession = pos;
	}
	
	public void setFiveMinPossesion(long time) {
		fiveMinPossession = time;
	}
	
	public long getFiveMinPossession() {
		return fiveMinPossession;
	}
	
	public long getPossessionTime() {
		long amount = 0L;
		if(hasPossession) {
			Date currentTime = new Date();
			amount = currentTime.getTime() - possessionGained.getTime();
		}
		
		return timeInPossession + amount;
	}
	
	private void createPlayers(int amount) {
		players = new ArrayList<Player>();
		for(int i = 1; i<amount+1; i++) {
			Player p = new Player(i, teamId, Integer.toString(i));
			players.add(p);
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
}
