package rayleigh.stats.football;

import java.util.Date;

public class Player {

	//Pass stats
	public int passSucceed, passCount;
	
	//Shot stats
	public int shotOnTarget, shotOffTarget, shotBlocked, shotWoodwork, shotCount;
	public final static int SHOT_ON_TARGET = 3;
	public final static int SHOT_OFF_TARGET = 4;
	public final static int SHOT_BLOCKED = 5;
	public final static int SHOT_WOODWORK = 6;
	public final static int SHOT_COUNT = 7;
	
	//Tackle stats
	//public int tacklesWon, tackleCount;
	
	//Header stats
	//private int headersWon, headerCount;
	
	private long possessionTime;
	private Date possessionGained;
	private Date possessionLost;
	private boolean hasPossession;
	
	private int id;
	private int teamId;
	private String name;
	private String number;
	
	public Player(int newID, int newTeamId) {
		passSucceed = 0;
		passCount = 0;
		
		shotOnTarget = 0;
		shotOffTarget = 0;
		shotBlocked = 0;
		shotWoodwork = 0;
		shotCount = 0;
		
		possessionTime = 0L;
		possessionGained = new Date();
		possessionLost = new Date();
		hasPossession = false;
		
		id = newID;
		teamId = newTeamId;
	}
	
	public Player(int newID, int newTeamId, String newNumber) {
		this(newID, newTeamId);
		number = newNumber;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setNumber(String newNumber) {
		number = newNumber;
	}
	
	public String getNumber() {
		return number;
	}
	
	public int getId() {
		return id;
	}
	
	public int getTeamId() {
		return teamId;
	}
	
	public void passAttempt(boolean success) {
		if(success) {
			passSucceed++;
			passCount++;
		}
		else {
			passCount++;
		}
	}
	
	public double getPassPercentage() {
		return passSucceed / passCount * 100.0;
	}
	
	public void shotAttempt(int shotType) {
		switch(shotType) {
		case SHOT_ON_TARGET:
			shotOnTarget++;
			shotCount++;
			break;
		case SHOT_OFF_TARGET:
			shotOffTarget++;
			shotCount++;
			break;
		case SHOT_BLOCKED:
			shotBlocked++;
			shotCount++;
			break;
		case SHOT_WOODWORK:
			shotWoodwork++;
			shotCount++;
			break;
		default:
			break;
		}
	}
	
	public double getShotAccuracy() {
		return shotOnTarget / shotCount * 100;
	}
	
	public void setPossession(boolean pos) {
		if(pos) {
			possessionGained = new Date();
		}
		else {
			if(hasPossession) {
				possessionLost = new Date();
				possessionTime += possessionLost.getTime() - possessionGained.getTime();
			}
		}
		hasPossession = pos;
	}
	
	public void setFirstPossession(Date start) {
		possessionGained = (Date)start.clone();
		//Log.i(FootballStatsActivity.TAG, "Start Time B - "+possessionGained.getTime());
		hasPossession = true;
	}
	
	public long getPossessionTime() {
		return possessionTime;
	}
}
