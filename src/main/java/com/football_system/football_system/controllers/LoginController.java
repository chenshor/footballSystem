package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

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
        if(guestService.logIn(data.get("email") , data.get("password"))) {
            User user = SecurityObject.Authorization(securityObject);
            List<String> roles =new LinkedList<>();
            for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
                if (iUserService instanceof RefereeService) {
                    roles.add("Referee");
                }
                if (iUserService instanceof FanService) {
                    roles.add("Fan");
                }
                if (iUserService instanceof RepresentativeService) {
                    roles.add("Representative");
                }
            }
            List<Object> obj = new LinkedList<>();
            obj.add(roles);
            SecurityObject so = new SecurityObject(data.get("email"), "key12344", "", obj);
            eventsLogger.info(" - The User : " + user.getUserName() + " - Log in successfully");
            return so;
        }
        errorsLogger.error("General Error - Can not login to user:"+data.get("email"));
        return null ;
    }

}
