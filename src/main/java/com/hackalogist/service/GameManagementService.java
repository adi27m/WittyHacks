package com.hackalogist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackalogist.dao.GameManagementDao;

@Service
public class GameManagementService {
	
	@Autowired
	GameManagementDao gameManagementDao;
	
	public boolean requestGameSession(String userSessionId, boolean isMultiplayer) {
		return this.gameManagementDao.requestGameSession(userSessionId, isMultiplayer);
	}
}
