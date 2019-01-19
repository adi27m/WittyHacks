package com.hackalogist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.hackalogist.model.GameSession;

@Repository
public class GameManagementDao {

	private Map<UUID,GameSession> runningGameSessions = new HashMap<>();
	GameSession pendingGameSession = null;
	
	public boolean requestGameSession(String userSessionId, boolean isMultiplePlayer) {
		GameSession theGameSession = null;
		if (isMultiplePlayer) {
			if (null == this.pendingGameSession) {
				theGameSession = new GameSession(userSessionId, null);
				pendingGameSession = theGameSession;
			} else {
				theGameSession = this.searchOpponent(userSessionId);
			}
		} else {
			
		}
		return true;
	}
	
	public GameSession searchOpponent(String requesterSessionId) {
		GameSession existingGameSession = null;
		for (Map.Entry<UUID, GameSession> entry : pendingGameSessions.entrySet()) {
		    
		}
		return null;
	}
}
