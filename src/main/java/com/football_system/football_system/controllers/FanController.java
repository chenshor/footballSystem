package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.Alert;
import com.football_system.football_system.FMserver.LogicLayer.User;
import com.football_system.football_system.FMserver.ServiceLayer.FanService;
import com.football_system.football_system.FMserver.ServiceLayer.IUserService;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.serverObjects.SecurityObject;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/Fan")
public class FanController {

    private static Logger errorsLogger = Logger.getLogger("errors");
    private static Logger eventsLogger = Logger.getLogger("events");


    @RequestMapping(
            value = "/Subscribe",
            method = RequestMethod.POST)
    public ResponseEntity<Void> Subscribe(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null){
            errorsLogger.error("Authorization Error - Subscribe to game: Failed");
            return ResponseEntity.badRequest().build();
        }
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
            eventsLogger.info(" - The User : " + user.getUserName() + " - Subscribe to game successfully");
            return ResponseEntity.ok().build();

        }
        errorsLogger.error("General Error - Subscribe to game: Failed");
        return ResponseEntity.badRequest().build();

    }

    @RequestMapping(
            value = "/getUpdates",
            method = RequestMethod.POST)
    public List<Alert> getUpdates(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null){
            errorsLogger.error("Authorization Error - Get game updates: Failed");
            return null;
        }
        FanService fanService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof FanService) {
                fanService = (FanService) iUserService;
            }
        }
        if (fanService != null) {
            eventsLogger.info(" - The User : " + user.getUserName() + " - get game updates successfully");
            return user.getAlerts() ;

        }
        errorsLogger.error("General Error - Get game updates: Failed");
        return null;

    }

    @RequestMapping(
            value = "/setUpdatesReaded",
            method = RequestMethod.POST)
    public  ResponseEntity<Void> setUpdatesReaded(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null){
            errorsLogger.error("Authorization Error - Set updates to be read: Failed");
            return ResponseEntity.badRequest().build() ;
        }
        FanService fanService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof FanService) {
                fanService = (FanService) iUserService;
            }
        }
        if (fanService != null) {
          //  LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            for ( Alert alert: user.getAlerts()) {
                alert.setReaded(true);
            }
            eventsLogger.info(" - The User : " + user.getUserName() + " - Set updates to be read successfully");
            return ResponseEntity.ok().build();
        }
        errorsLogger.error("General Error - Set updates to be read: Failed");
        return ResponseEntity.badRequest().build();

    }

}
