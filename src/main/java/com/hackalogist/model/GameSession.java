package com.hackalogist.model;

public class GameSession {
	
	private String gameId;
	private String user1Name;
	private String user2Name;
	private int user1Score=0;
	private int user2Score=0;
	private int pattern[];

	
	public void setGameId(String gameId) {
		this.gameId = gameId;
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
	public void setPattern(int[] pattern) {
		this.pattern = pattern;
	}

	public String getGameId() {
		return gameId;
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
	public int[] getPattern() {
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
		for(int i=0;i<pattern.length;i++)
		{
			if(pattern[i]!=userPattern[i])
			{
				return false;
			}
		}
		return true;
		
	}
	
}
