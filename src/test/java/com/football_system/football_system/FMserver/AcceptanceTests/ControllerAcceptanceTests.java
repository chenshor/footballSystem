package com.football_system.football_system.FMserver.AcceptanceTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ControllerAcceptanceTests {
    private static IController system;

    /**
     * id: A@42
     * acceptance test 1.1.1 - use case 1.1
     * initialise system
     */
    @Test
    public void initialiseTest(){
        system = new Controller();
        //checks there is administrator and representative
        assertTrue(system.getUserList().size() == 2);
        assertTrue(system.getUserList().get(1) instanceof Administrator);
        assertTrue(system.getUserList().get(0).getRoles().get(0) instanceof Representative);
        assertTrue(system.getUserServices().get(system.getUserList().get(0)).get(1) instanceof RepresentativeService);
    }

}


