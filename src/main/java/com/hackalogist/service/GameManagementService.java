package com.hackalogist.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackalogist.commons.Constants;
import com.hackalogist.dao.GameManagementDao;

@Service
public class GameManagementService {
	
	@Autowired
	GameManagementDao gameManagementDao;
	
		public String requestFileForTile(String userSessionId, String gameSessionId, String tileNumber) {
		return this.gameManagementDao.requestFileForTile(userSessionId, gameSessionId,tileNumber);
		
	}

		public void executeOperation(Map<String, Object> map, String userId) {
			String commandNameString = (String)map.get(Constants.COMMAND_NAME);
			if(null != commandNameString) {
				switch(commandNameString) {
				case Constants.STARTGAME:
					gameManagementDao.startGame(map,userId);
					break;
				case Constants.CHECKRESPONSE:
					gameManagementDao.checkResponse(map,userId);
					break;
				case Constants.PREPAREGAME:
					try {
						gameManagementDao.prepareGame(map,userId);
					} catch (Exception e) {
						gameManagementDao.prepareGame(map,userId);
						System.out.println("i am here");
						e.printStackTrace();
					}
					break;
				}
			}
		}
}
