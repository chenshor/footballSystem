package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;
import com.football_system.football_system.FMserver.DataLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@EnableAutoConfiguration
@Table(name = "Fans")
public class Fan extends Role implements Serializable {
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Page> pages;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Game> games;
    private String name;


    public Fan(User user, String name) {
        super(user);
        this.name = name;
        pages = new ArrayList<>();
        games = new ArrayList<>();
    }


    public Fan(){}

    /**
     * returns fan followed pages
     * @return List<Page>
     */
    public List<Page> getPages() {
        return pages;
    }

    /**
     * returns DB instance
     * @return IDataManager
     */
    public IDataManager getDataManager() {
        return DataComp.getInstance();
    }

    /**
     * name getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * name setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * id: fan@1
     * add new pages to follow by fan
     * @param newPages
     */
    public void addPages(List<Page> newPages) {
        for(Page page: newPages){
            pages.add(page);
        }
    }

    /**
     * id: fan@2
     * adds new search history by fan by 3 criterias
     * @param criteria
     * @param query
     */
    public void addSearchHistory(Criteria criteria , String query) {
        if (query!=null){
            switch (criteria){
                case Name:
                    getDataManager().addNameHistory(this,query);
                    break;
                case KeyWord:
                    getDataManager().addKeyWordHistory(this,query);
                    break;
                case Category:
                    getDataManager().addCategoryHistory(this,query);
                    break;
            }
        }
    }

    /**
     * id: fan@3
     * add new complaint to user in DB
     * @param description
     */
    public void addComplaintToDataManager(String description) {
        Complaint complaint = new Complaint(this.getUser(),description, LocalDate.now().toString());
        getDataManager().addComplaint(this.getUser(),complaint);
    }

    /**
     * id: fan@4
     * category search history getter
     * @return List<String>
     */
    public List<String> getCategorySearchHistory() {
        return getDataManager().getCategorySearchHistory(this);
    }

    /**
     * id: fan@5
     * keyWord search history getter
     * @return List<String>
     */
    public List<String> getKeyWordSearchHistory() {
        return getDataManager().getKeyWordSearchHistory(this);
    }

    /**
     * id: fan@6
     * search by name history getter
     * @return List<String>
     */
    public List<String> getNameSearchHistory() {
        return getDataManager().getNameSearchHistory(this);
    }

    /**
     * id: fan@7
     * retrieves search history made by fan
     * @param criteria
     * @return List<String>
     */
    public List<String> retrieveSearchHistory(Criteria criteria) {
        List<String>searchHistory = new ArrayList<>();
        switch (criteria) {
            case Category:
                searchHistory = getCategorySearchHistory();
                break;
            case KeyWord:
                searchHistory = getKeyWordSearchHistory();
                break;
            case Name:
                searchHistory = getNameSearchHistory();
                break;
        }
        return searchHistory;
    }

//    /**
//     * id: fan@8
//     * update - observer
//     * @param game - Observable Game
//     * @param event - update
//     */
//    @Override
//    public void update(Observable game, Object event) throws ClassCastException{
//        if (this.games.contains(game)){
//            alertUser(event);
//        }
//    }

    /**
     * id: fan@9
     * Alert user - in this version by printing to console
     * @param event - Game Event
     */
    private void alertUser(Object event) throws ClassCastException{
        try {
            ((GameEventCalender) event).displayEvents();
        } catch (NullPointerException e){

        }
    }

    /**
     * id: fan@10
     * games followed by fan getter
     * @return
     */
    public List<Game> getGames() {
        return games;
    }


    public void addGameToSubscribe(Game game){
        games.add(game);
    }

    public void removeGameFromSubscribe(Game game){
        games.remove(game);
    }
}
