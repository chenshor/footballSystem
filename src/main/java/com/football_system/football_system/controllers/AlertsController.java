package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.Alert;
import com.football_system.football_system.FMserver.LogicLayer.Game;
import com.football_system.football_system.FMserver.LogicLayer.User;
import com.football_system.football_system.FMserver.ServiceLayer.IUserService;
import com.football_system.football_system.FMserver.ServiceLayer.RefereeService;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.LinkedHashMap;


@Controller
public class AlertsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void addEventToGame(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) return ;
        RefereeService refereeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RefereeService) {
                refereeService = (RefereeService) iUserService;
            }
        }
        if (refereeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            Integer game_id = Integer.parseInt(objects.get("game_id").toString());
            String description = (String) objects.get("description");
            String eventType = (String) objects.get("eventType");
            Boolean home = Game.checkIfHomeTeam(objects.get("team").toString() , game_id) ;
            Integer minute = Integer.parseInt(objects.get("minute").toString());
            HashMap<User , Alert> userAlertHashMap = refereeService.addEventGame(home , game_id , description ,eventType,minute );

            //sent to observers
            for(User user_toSend :userAlertHashMap.keySet()){
                try {
                    simpMessagingTemplate.convertAndSend("/topic/messages/" + user_toSend.getEmail(), userAlertHashMap.get(user_toSend));
                    System.out.println("send to:"+user.getEmail());
                }catch (Exception e){

                }


            }
        }
        return ;
    }

}
