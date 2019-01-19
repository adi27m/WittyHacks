package com.hackalogist.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackalogist.commons.Constants;
import com.hackalogist.dao.GameManagementDao;
import com.hackalogist.enums.CommandNames;

@Service
public class GameManagementService {
	
	@Autowired
	GameManagementDao gameManagementDao;
	
		public String requestFileForTile(String userSessionId, String gameSessionId, String tileNumber) {
		return this.gameManagementDao.requestFileForTile(userSessionId, gameSessionId,tileNumber);
		
	}

		public void executeOperation(Map<String, String> map, String userId) {
			String commandNameString = map.get(Constants.COMMAND_NAME);
			if(null != commandNameString) {
				CommandNames commandName = CommandNames.valueOf(commandNameString);
				switch(commandName) {
				case STARTGAME:
					//gameManagementDao.startGame(map,userId);
					break;
				case CHECKRESPONSE:
					//gameManagementDao.checkResponse(map,userId);
					break;
				case PREPAREGAME:
					gameManagementDao.prepareGame(map,userId);
					break;
				}
			}
		}
}
