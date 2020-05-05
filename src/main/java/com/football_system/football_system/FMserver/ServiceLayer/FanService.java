package com.football_system.football_system.FMserver.ServiceLayer;
import com.football_system.football_system.FMserver.LogicLayer.*;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class FanService extends AUserService{
    Fan fan;
    private IController system;

    public FanService(Fan fan, IController system) {
        this.fan = fan;
        this.system = system;
    }

    /**
     * id: FanService@1
     * USE CASE - 3.2
     * add new pages to follow
     * @param newPages
     * @throws IOException
     */
    @Override
    public void addPages(List<Page> newPages) throws IOException {
        if (newPages != null){
            fan.addPages(newPages);
        }
    }

    /**
     * id: FanService@2
     * USE CASE - 3.3
     * add Games to the follow list of a Fan
     * @param games
     */
    public void followOnGames(List<Observable> games){
        games.forEach(game -> game.addObserver(this.fan));
    }

    /**
     * id: FanService@3
     * incoming games getter
     * @return
     * @throws Exception
     */
    private List<Observable> getIncomingGames() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        List<Observable> games;
        games = (List<Observable>) DataComp.getInstance().getGameList().stream().filter(game -> {
            try {
                return today.compareTo(format.parse(game.getDate())) < 0;
            } catch (ParseException e) {
                return false;
            }
        });
        return games;
    }

    /**
     * id: FanService@4
     * USE CASE - 3.4
     * add new complaint due to wrong information
     * @param description
     * @throws IOException
     */
    @Override
    public void report(String description) throws IOException {
        if (description != null) {
            fan.addComplaintToDataManager(description);
        }else{
            System.out.println("## there is no content in description ##");
        }
    }

    /**
     * id: FanService@5
     * USE CASE - 3.5
     * returns fan's search history
     * @return
     * @throws IOException
     */
    @Override
    public List<String> retrieveHistory(Criteria criteria) throws IOException {
        List<String>searchHistory = fan.retrieveSearchHistory(criteria);
        if (searchHistory == null){
            System.out.println("## there is no search history ##");
            return null;
        }
        for (String search: searchHistory){
            System.out.println(search);
        }
        return searchHistory;
    }

    /**
     * id: FanService@6
     * returns all data from DB related to query
     * uses guest class to search information, this function added due to use case 3.5
     * @param criteria
     * @param query
     * @throws IOException
     */
    @Override
    public void searchInformation(Criteria criteria, String query) throws IOException {
        if (query != null && criteria != null){
            Guest guest = new Guest();
            GuestService guestService = new GuestService(guest,system);
            guestService.searchInformation(criteria,query);
            fan.addSearchHistory(criteria,query);
        }
    }
}
