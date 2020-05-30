//package com.football_system.football_system.FMserver.UnitTests;
//
//import com.football_system.football_system.FMserver.LogicLayer.*;
//import com.football_system.football_system.FMserver.ServiceLayer.*;
//import com.football_system.football_system.FMserver.DataLayer.*;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Observable;
//
//public class FanTests {
//    private static final Logger testLogger = Logger.getLogger(GuestTests.class);
//    private static Fan fan;
//    private static User user;
//
//
//    @BeforeClass
//    public static void init(){
//        user = new User("Eitan@gmail.com","1234","Eitan","David");
//        fan = new Fan(user,user.getFirstName());
//        String propertiesPath = "log4j.properties";
//        PropertyConfigurator.configure(propertiesPath);
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        DataComp.setDataManager(new DataManager());
//    }
//
//
//    /**
//     * id: U@49
//     * checks addPages func
//     */
//    @Test
//    public void addPagesTest(){
//        testLogger.info("Run: addPagesTest");
//        //checks if page added
//        List<Page>pages = new ArrayList<>();
//        Page testPage1 = new Page(new Player(user," ",null," ", "2001-01-01"));
//        pages.add(testPage1);
//        fan.addPages(pages);
//        assertTrue(fan.getPages().size() == 1);
//        //checks if right page added
//        testPage1.addUpdate("Eitan");
//        assertEquals("Eitan",testPage1.getUpdates().get(0));
//        testLogger.info("Ended: addPagesTest");
//    }
//
//    /**
//     * id: U@50
//     * checks addSearchHistoryFunc
//     */
//    @Test
//    public void addSearchHistoryTest(){
//        //checks adding to search for name history
//        Criteria criteria = Criteria.Name;
//        String query = "Eitan";
//        fan.addSearchHistory(criteria, query);
//        assertTrue(DataComp.getInstance().getFanSearchNameHistory().size() == 1);
//        //checks adding to search for KeyWord history
//        criteria = Criteria.KeyWord;
//        fan.addSearchHistory(criteria,query);
//        assertTrue(DataComp.getInstance().getFanSearchKeyWordHistory().size() == 1);
//        //checks adding to search for Category history
//        criteria = Criteria.Category;
//        fan.addSearchHistory(criteria,query);
//        assertTrue(DataComp.getInstance().getFanSearchCategoryHistory().size() == 1);
//    }
//
//    /**
//     * id: U@51
//     * checks getSearchHistory func
//     */
//    @Test
//    public void retrieveSearchHistoryTest(){
//        List<String>testSearch = new ArrayList<>();
//        testSearch.add("coaches");
//        DataComp.getInstance().getFanSearchCategoryHistory().put(fan,testSearch);
//        Criteria criteria = Criteria.Category;
//        List<String>searchHistory = fan.retrieveSearchHistory(criteria);
//        assertTrue(searchHistory.size() == 1);
//        assertTrue(searchHistory.get(0).equals("coaches"));
//    }
//
//    /**
//     * id: U@52
//     * checks addComplaintToDataManager func
//     */
//    @Test
//    public void addComplaintToDataManagerTest(){
//        String desc = "Eitan";
//        fan.addComplaintToDataManager(desc);
//        assertTrue(DataComp.getInstance().getComplaint().size() == 1);
//        assertTrue(DataComp.getInstance().getComplaint().get(fan.getUser()).get(0).getDescription().equals("Eitan"));
//    }
//
////    /**
////     * id: U@53
////     * checks update func
////     * with String
////     */
////    @Test (expected = ClassCastException.class)
////    public void updateNullTest() {
////        Observable game = new Game();
////        fan.getGames().clear();
////        fan.getGames().add(game);
////        assertEquals(1, fan.getGames().size());
////        fan.update(game, "String");
////    }
////
////    /**
////     * id: U@54
////     * checks update func
////     * with GameEventCalender
////     */
////    @Test
////    public void updateNotNullTest() {
////        Game game = new Game();
////        fan.getGames().clear();
////        fan.getGames().add(game);
////        assertEquals(1, fan.getGames().size());
////        Exception exception = null;
////        try{
////            fan.update(game, new GameEventCalender(game,"","","","",90));
////        } catch (Exception e) {
////            exception = e;
////        }
////        assertNull(exception);
////    }
//}
