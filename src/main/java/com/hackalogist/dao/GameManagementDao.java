package com.hackalogist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
		
	public boolean checkResponse(String userSessionId,String gameSessionId,String roundNumber,int[] patternOfFile) {
		GameSession theGameSession = runningGameSessions.get(gameSessionId);
		return theGameSession.ValidatePattern(patternOfFile);
		
	}
	
	public String requestFileForTile(String userSessionId, String gameSessionId, String tileNumber) {
		GameSession theGameSession = runningGameSessions.get(gameSessionId);
		return theGameSession.gettileToFile(tileNumber);
	}

	public void prepareGame(Map<String, Object> map, String userId) {
		System.out.println("i am inside");
		boolean isMultiplayer = Boolean.parseBoolean((String)map.get(Constants.IS_MULTIPLAYER));
		int numberOfTiles = Integer.parseInt((String)map.get(Constants.NUMBER_OF_TILES));
		//String difficulty = map.get(Constants.DIFFICULTY);
		String username = (String)map.get(Constants.USER_NAME);
		
		if(null != pendingGameSession) {
			if(isMultiplayer) {
				pendingGameSession.setUser2Id(userId);
				pendingGameSession.setUser2Name(username);
				pendingGameSession.setUser2Score(0);
				pendingGameSession = null;
				runningGameSessions.put(pendingGameSession.getGameSessionId(), pendingGameSession);
				HashMap< String , List<String>> responseMap=new HashMap<>();
				List<String> users=new LinkedList<>();
				users.add(pendingGameSession.getUser1Id());
				users.add(pendingGameSession.getUser2Id());
				responseMap.put(Constants.USER_ID, users);
				for(String user:users)
					ServerEndPoint.sendResponse(user, responseMap);
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
				if(null != gameSession.getUser2Id())
					users.add(gameSession.getUser2Id());
				responseMap.put(Constants.USER_ID, users);
				
				List<String> gameSessionId = new LinkedList<>();
				gameSessionId.add(gameSession.getGameSessionId().toString());			
				responseMap.put(Constants.CURRENT_SESSION_ID,gameSessionId);
				for(String user:users)
					ServerEndPoint.sendResponse(user, responseMap);
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
				HashMap< String , List<String>> responseMap=new HashMap<>();
				List<String> users=new LinkedList<>();
				users.add(gameSession.getUser1Id());
				if(null != gameSession.getUser2Id())
					users.add(gameSession.getUser2Id());
				responseMap.put(Constants.USER_ID, users);
				
				List<String> gameSessionId = new LinkedList<>();
				gameSessionId.add(gameSession.getGameSessionId().toString());			
				responseMap.put(Constants.CURRENT_SESSION_ID,gameSessionId);
				for(String user:users)
					ServerEndPoint.sendResponse(user, responseMap);
				
			}
		}
		
		//TODO: create tilenumbertosound Mapping
		tileNumberToSoundMapping = createTileNumberToSoundMapping(numberOfTiles);
		
		
	}

	private Map<Integer, String> createTileNumberToSoundMapping(int numberOfTiles) {
		return null;
	}

	public void startGame(Map<String, Object> map, String userId) {
		int roundNumber = Integer.parseInt((String)map.get(Constants.ROUND_NUMBER));
		List<Integer> pattern = new LinkedList<>();
		for(int i=0;i<roundNumber;i++)
			pattern.add(getRandomNumberInRange(1, roundNumber));
		UUID currentGameSessionKey = UUID.fromString((String)map.get(Constants.GAME_SESSION_ID));
		GameSession currentSession = runningGameSessions.get(currentGameSessionKey);
		HashMap< String , List<String>> responseMap=new HashMap<>();
		List<String> users=new LinkedList<>();
		users.add(currentSession.getUser1Id());
		if(null != currentSession.getUser2Id())
			users.add(currentSession.getUser2Id());
		responseMap.put(Constants.USER_ID, users);
		
		List<String> gameSessionId = new LinkedList<>();
		gameSessionId.add(currentSession.getGameSessionId().toString());			
		responseMap.put(Constants.CURRENT_SESSION_ID,gameSessionId);
		for(String user:users)
			ServerEndPoint.sendResponse(user, responseMap);
	}
	
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public void checkResponse(Map<String, Object> map, String userId) {
		@SuppressWarnings("unchecked")
		List<Integer> attemptedPattern = (List<Integer>)(map.get(Constants.RESULT_PATTERN));
		int attempt = (Integer)(map.get(Constants.ATTEMPT));
		
		UUID currentGameSessionKey = UUID.fromString((String)map.get(Constants.GAME_SESSION_ID));
		GameSession currentSession = runningGameSessions.get(currentGameSessionKey);
		
		if(isValidPattern(currentSession.getPattern(),attemptedPattern)) {
			updateScore(userId,currentSession,attempt);
			
			HashMap< String , List<String>> responseMap=new HashMap<>();
			List<String> users=new LinkedList<>();
			users.add(currentSession.getUser1Id());
			if(null != currentSession.getUser2Id())
				users.add(currentSession.getUser2Id());
			responseMap.put(Constants.USER_ID, users);
			
			List<String> gameSessionId = new LinkedList<>();
			gameSessionId.add(currentSession.getGameSessionId().toString());			
			responseMap.put(Constants.CURRENT_SESSION_ID,gameSessionId);
			for(String user:users)
				ServerEndPoint.sendResponse(user, responseMap);
			
		}
	}

	private void updateScore(String userId, GameSession currentSession, int attempt) {
		if(!currentSession.getUser1Id().equals(userId)) {
			int score = currentSession.getUser2Score();
			score = score + ((int)(10/attempt));
			currentSession.setUser2Score(score);
		}
		else {
			int score = currentSession.getUser2Score();
			score = score + ((int)(10/attempt));
			currentSession.setUser1Score(score);
		}
	}

	private boolean isValidPattern(List<Integer> pattern, List<Integer> attemptedPattern) {
		for(int i=0;i<pattern.size();i++)
			if(pattern.get(i) != attemptedPattern.get(i))
				return false;
		return true;
	}
	
}
