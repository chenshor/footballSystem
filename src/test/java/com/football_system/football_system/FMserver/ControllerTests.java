//package com.football_system.football_system.FMserver;
//
//import com.football_system.football_system.FMserver.DataLayer.DataManager;
//import com.football_system.football_system.FMserver.DataLayer.IDataManager;
//import com.football_system.football_system.FMserver.LogicLayer.*;
//import com.football_system.football_system.FMserver.ServiceLayer.*;
//
//import org.junit.Test;
//import org.junit.BeforeClass;
//
//
//import static org.junit.Assert.assertEquals;
//
//public class ControllerTests {
//    static Controller testController;
//
//    @BeforeClass
//    public static void init(){
//        Administrator admin = new Administrator("A", "B", "C");
//        Representitive rep = new Representitive(new User("A", "B", "C", null), "TEST");
//        testController = new Controller(rep, admin);
//    }
//
//    @Test
//    public void testBuildingObject() {
//        Controller testController = new Controller();
//        Administrator testAdmin = testController.getAdministrator();
//        Representitive testRep = testController.getRepresentitive();
//        assertEquals(testAdmin.getPassword(),"B");
//    }
//}
