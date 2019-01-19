package com.hackalogist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.websocket.Session;

import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.stereotype.Repository;

import com.hackalogist.commons.Constants;
import com.hackalogist.model.GameSession;
import com.hackalogist.notifier.ServerEndPoint;

@Repository
public class GameManagementDao {

	private Map<UUID,GameSession> runningGameSessions = new HashMap<>();
	GameSession pendingGameSession = null;
	Map<Integer,String> tileNumberToSoundMapping = null;
	
	public GameSession searchOpponent(String requesterSessionId) {
		GameSession existingGameSession = null;
		for (Map.Entry<UUID, GameSession> entry : pendingGameSessions.entrySet()) {
		    
		}
		return null;
	}
	
	public boolean checkResponse(String userSessionId,String gameSessionId,String roundNumber,int[] patternOfFile) {
		GameSession theGameSession = runningGameSessions.get(gameSessionId);
		return theGameSession.ValidatePattern(patternOfFile);
		
	}
	
	public String requestFileForTile(String userSessionId, String gameSessionId, String tileNumber) {
		GameSession theGameSession = runningGameSessions.get(gameSessionId);
		return theGameSession.gettileToFile(tileNumber);
	}

	public void prepareGame(Map<String, String> map, String userId) {
		boolean isMultiplayer = Boolean.parseBoolean(map.get(Constants.IS_MULTIPLAYER));
		int numberOfTiles = Integer.parseInt(map.get(Constants.NUMBER_OF_TILES));
		//String difficulty = map.get(Constants.DIFFICULTY);
		String username = map.get(Constants.USER_NAME);
		
		if(null != pendingGameSession) {
			if(isMultiplayer) {
				pendingGameSession.setUser2Id(userId);
				pendingGameSession.setUser2Name(username);
				pendingGameSession.setUser2Score(0);
				pendingGameSession = null;
				runningGameSessions.put(pendingGameSession.getGameSessionId(), pendingGameSession);
			}
			else {
				GameSession gameSession = new GameSession();
				gameSession.setUser1Id(userId);
				gameSession.setUser1Name(username);
				gameSession.setUser1Score(0);
				gameSession.setOpponentName(null);
				gameSession.setTileToFileSoundMap(createTileNumberToSoundMapping(numberOfTiles));
				runningGameSessions.put(gameSession.getGameSessionId(), gameSession);
				HashMap< String , List<String>> responseMap=new HashMap<>();
				List<String> users=new LinkedList<>();
				users.add(gameSession.getUser1Id());
				users.add(gameSession.getUser2Id());
				responseMap.put(Constants.USER_ID, users);
				for(String user:users)
				ServerEndPoint.sendResponse((user, responseMap);
			}
		}
		else {
			if(isMultiplayer) {
				GameSession gameSession = new GameSession();
				gameSession.setUser1Id(userId);
				gameSession.setUser1Name(username);
				gameSession.setUser1Score(0);
				gameSession.setOpponentName(null);
				pendingGameSession = gameSession;
			}
			else {
				GameSession gameSession = new GameSession();
				gameSession.setUser1Id(userId);
				gameSession.setUser1Name(username);
				gameSession.setUser1Score(0);
				gameSession.setOpponentName(null);
				gameSession.setTileToFileSoundMap(createTileNumberToSoundMapping(numberOfTiles));
				runningGameSessions.put(gameSession.getGameSessionId(), gameSession);
				
			}
		}
		
		//TODO: create tilenumbertosound Mapping
		tileNumberToSoundMapping = createTileNumberToSoundMapping(numberOfTiles);
		
		
	}

	private Map<Integer, String> createTileNumberToSoundMapping(int numberOfTiles) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
