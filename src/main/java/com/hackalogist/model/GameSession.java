package com.hackalogist.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameSession {

	private UUID gameSessionId;
	private String user1Name;
	private String user1Id;
	private String user2Name;
	private String user2Id;
	private int user1Score;
	private int user2Score;
	private String opponentName=null;
	private List<Integer> pattern;
	private Map<Integer,String> tileToFileSoundMap = new HashMap<>();
 
	public GameSession() {
		this.setGameSessionId(UUID.randomUUID());
		user1Score = 0;
		user2Score = 0;
	}
	
	public GameSession(String userName1, String userName2) {
		this.setGameSessionId(UUID.randomUUID());
		this.user1Name = userName1;
		this.user2Name = userName2;
		this.opponentName = "";
	}
	
	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}
	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}
	public void setUser1Score(int user1Score) {
		this.user1Score = user1Score;
	}
	public void setUser2Score(int user2Score) {
		this.user2Score = user2Score;
	}
	public void setPattern(List<Integer> pattern) {
		this.pattern = pattern;
	}

	public String getUser1Name() {
		return user1Name;
	}
	public String getUser2Name() {
		return user2Name;
	}
	public int getUser1Score() {
		return user1Score;
	}
	public int getUser2Score() {
		return user2Score;
	}
	public List<Integer> getPattern() {
		return pattern;
	}

	public void updateUser1Score(int user1Score) {
		this.user1Score = this.user1Score+user1Score;
	}
	
	public void updateUser2Score(int user2Score) {
		this.user2Score = this.user2Score+user2Score;
	}
	
	public boolean ValidatePattern(int[] userPattern)
	{
		for(int i=0;i<pattern.size();i++)
		{
			if(pattern.get(i)!=userPattern[i])
			{
				return false;
			}
		}
		return true;
		
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public UUID getGameSessionId() {
		return gameSessionId;
	}

	public void setGameSessionId(UUID gameSessionId) {
		this.gameSessionId = gameSessionId;
	}

	public String getUser1Id() {
		return user1Id;
	}

	public void setUser1Id(String user1Id) {
		this.user1Id = user1Id;
	}

	public String getUser2Id() {
		return user2Id;
	}

	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}

	public Map<Integer,String> getTileToFileSoundMap() {
		return tileToFileSoundMap;
	}

	public void setTileToFileSoundMap(Map<Integer,String> tileToFileSoundMap) {
		this.tileToFileSoundMap = tileToFileSoundMap;
	}

	public String gettileToFile(String tileNumber) {
		return tileToFileSoundMap.get(tileNumber);
	}
	
	
	
}
