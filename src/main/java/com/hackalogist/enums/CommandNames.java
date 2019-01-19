package com.hackalogist.enums;

public enum CommandNames {
	
	PREPAREGAME("preparegame"),
	STARTGAME("startgame"),
	CHECKRESPONSE("checkresponse");
	
	
	private String value = "";
	
	CommandNames(String key) {
		this.value = key;
	}
	
	public String getValue() {
		return value;
	}
}
