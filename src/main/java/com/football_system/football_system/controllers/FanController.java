package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.Alert;
import com.football_system.football_system.FMserver.LogicLayer.Game;
import com.football_system.football_system.FMserver.LogicLayer.User;
import com.football_system.football_system.FMserver.ServiceLayer.FanService;
import com.football_system.football_system.FMserver.ServiceLayer.IUserService;
import com.football_system.football_system.FMserver.ServiceLayer.RefereeService;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/Fan")
public class FanController {



    @RequestMapping(
            value = "/Subscribe",
            method = RequestMethod.POST)
    public ResponseEntity<Void> Subscribe(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) return ResponseEntity.badRequest().build();
        FanService fanService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof FanService) {
                fanService = (FanService) iUserService;
            }
        }
        if (fanService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            Integer game_id = Integer.parseInt(objects.get("game_id").toString());
            Boolean Subscribe = Boolean.parseBoolean(objects.get("Subscribe").toString());
            if(Subscribe) {
                fanService.followOnGame(game_id);
            }else {
                fanService.removeFollowOnGame(game_id);
            }
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.badRequest().build();

    }

    @RequestMapping(
            value = "/getUpdates",
            method = RequestMethod.POST)
    public List<Alert> getUpdates(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) return null;
        FanService fanService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof FanService) {
                fanService = (FanService) iUserService;
            }
        }
        if (fanService != null) {
            return user.getAlerts() ;

        }
        return null;

    }

    @RequestMapping(
            value = "/setUpdatesReaded",
            method = RequestMethod.POST)
    public  ResponseEntity<Void> setUpdatesReaded(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) return ResponseEntity.badRequest().build() ;
        FanService fanService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof FanService) {
                fanService = (FanService) iUserService;
            }
        }
        if (fanService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            for ( Alert alert: user.getAlerts()) {
                alert.setReaded(true);
            }
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();

    }

}
