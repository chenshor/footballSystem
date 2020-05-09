package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import com.football_system.football_system.logicTest.UserTest;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
public class LoginController {

    private static Guest guest;
    private static IGuestService guestService;
    private static Logger errorsLogger = Logger.getLogger("errors");
    private static Logger eventsLogger = Logger.getLogger("events");

    /**
     * demo post req -> {"reqID":"123456","functionName":"add User","object":[{"name":"avi","mail":"avi@walla.com"}]}
     * @param securityObject
     * @return
     * @throws Exception
     */
    @RequestMapping(
            value = "/Login",
            method = RequestMethod.POST)
    public SecurityObject process(@RequestBody SecurityObject securityObject)
            throws Exception {
        guest = new Guest();
        FootballSystemApplication.system.addGuest(guest);
        guestService = FootballSystemApplication.system.getGuestServices().get(guest);

        LinkedHashMap<String,String> data = (LinkedHashMap<String,String> ) securityObject.getObject().get(0) ;


        User reg =  guestService.register("da","s",data.get("email"), data.get("password")) ;
        if(guestService.logIn(data.get("email") , data.get("password") + "dd")) {
            eventsLogger.info(" - " + reg.getUserName() + " - successful login");
            SecurityObject so = new SecurityObject("", "key12344", "", null);
            return so;
        }
        errorsLogger.error(" - failed login by " + reg.getUserName());
        return null ;
    }

}
