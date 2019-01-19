package com.hackalogist.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackalogist.service.GameManagementService;

@RestController
@RequestMapping
public class GameManagementController {

	@Autowired
	GameManagementService gameManegementService;
	
	@RequestMapping("/home")
	public String ValidateUserResponse() {
		return "Correct";
	}

	@PostMapping("/startGame")
	public void startGame(@RequestParam("usersessionid") String userSessionid, @RequestParam("nooftiles") int noOfTiles,
			@RequestParam("ismultiplayer") boolean isMultiPlayer, @RequestParam("gamelevel") String gameLevel) {
		
		if (isMultiPlayer) {
			this.gameManegementService.requestGameSession(userSessionid, isMultiPlayer);
		} else {
			
		}
	}

	@GetMapping("/sendAudio")
	public void getImage(@RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response) {
		try (ServletOutputStream output = response.getOutputStream(); FileInputStream fis = new FileInputStream(path)) {
			byte b[] = new byte[(int) new File(path).length()];
			fis.read(b);
			output.write(b);
			fis.close();
			output.close();
		} catch (IOException e) {
			System.out.println("ERROR: Cannot render image " + path);
		}
	}
}
