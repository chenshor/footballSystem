package com.football_system.football_system.FMserver.DataLayer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import com.football_system.football_system.FMserver.DataLayer.Repositories.*;
import com.football_system.football_system.FMserver.LogicLayer.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map;
//@RestController
//@RequestMapping(value = "/rest/user")
@Service
public class DataManager implements IDataManager {
    private HashMap<Fan,List<String>>fanSearchNameHistory;
    private HashMap<Fan,List<String>>fanSearchCategoryHistory;
    private HashMap<Fan,List<String>>fanSearchKeyWordHistory;
    private Map<User, List<Complaint>> complaints;
    private List<Page> pageList;
    private List<Administrator> administrators;
    private static final Logger systemLogger = Logger.getLogger(DataManager.class);
    private List<Guest> guestsList;

    @Autowired
    public IUserRepository IUR;
//    private List<User> userList;

    @Autowired
    private IAlertRepository IAR;
//    private Map<User, List<Alert>> alerts;

    @Autowired
    private ILeagueRepository ILR;
//    private List<League> leagueList;

    @Autowired
    private ISeasonRepository ISR;
//    private List<Season> seasonList;

    @Autowired
    private ITeamRepository ITR;
//    private List<Team> teamList;

    @Autowired
    private IGameRepository IGR;
//    private List<Game> gameList;

    @Autowired
    private IRefereeRepository IFR;
//    private LinkedList<Referee> RefereeList;

    @Autowired
    private IFanRepository IFanR;

    @Autowired
    private IManagerRepository IMR;

    @Autowired
    private IAdministratorRepository IAdminRepo;

    @Autowired
    private ICoachRepository ICR;

    @Autowired
    private IComplaintRepository IComplaintRepo;

    @Autowired
    private IFanRepository IfanRepo;

    @Autowired
    private IGameEventRepository IGER;

    @Autowired
    private IJudgementApproverl IJAR;

    @Autowired
    private IOwnerRepositor IOR;

    @Autowired
    private IPageRepository IPR;

    @Autowired
    private IPlayerRepository IPlayerRepo;

    @Autowired
    private IPolicyRepository IPolicyRepo;

    @Autowired
    private IRankPolicyRepository IRPR;

    @Autowired
    private IRepresentitiveRepository IRepRepo;

    @Autowired
    private  IResultRepository IResRepo;

    @Autowired
    private IGameEventRepository iGameEventRepository;

    @Autowired
    private IGameReportRepository IGRR;

    @Autowired
    private IRepresentitiveRepository iRepresentitiveRepository;


//    public DataManager() {
//        guestsList = new ArrayList<>();
////        userList = new ArrayList<>();
////        alerts = new HashMap<>();
//        complaints = new HashMap<>();
////        leagueList = new ArrayList<>();
////        seasonList = new ArrayList<>();
////        teamList = new ArrayList<>();
//        pageList = new ArrayList<>();
////        gameList = new ArrayList<>();
////        RefereeList = new LinkedList<>();
//        String propertiesPath = "log4j.properties";
//        fanSearchNameHistory = new HashMap<>();
//        fanSearchKeyWordHistory = new HashMap<>();
//        fanSearchCategoryHistory = new HashMap<>();
//        PropertyConfigurator.configure(propertiesPath);
//    }

    /**
     * clears all data repositories
     */
    public void resetDataBase() {
        this.ILR.deleteAll();
        this.IUR.deleteAll();
        this.IFR.deleteAll();
        this.IAR.deleteAll();
        this.IGR.deleteAll();
        this.ITR.deleteAll();
        this.ISR.deleteAll();
    }

    @Override
    public void addFan(Fan fan){
        IFanR.save(fan);
    }

    @Override
    public Fan getFan(int r_id) {
        if (IFanR.findById(r_id).isPresent())
            return IFanR.findById(r_id).get();
        else
            return null;
    }

    @Override
    public void deleteFan(int r_id) {
        IfanRepo.deleteById(r_id);
    }

    public boolean checkIfEmailExists(String email) {
        return IUR.findById(email).isPresent();
    }

    public void addNewUser(User newUser) {
        //userList.add(newUser);
        if (newUser!=null)
            IUR.save(newUser);
    }


    public User getUserByMail(String userName, String email) {
        if (IUR.findById(email).isPresent())
            return IUR.findById(email).get();
        else
            return null;
    }


    public void addUser(User user) {
        if (user != null){
            IUR.save(user);
        }
    }

    public List<Guest> getGuestsList() {
        return guestsList;
    }

    public void setGuestsList(List<Guest> guestsList) {
        this.guestsList = guestsList;
    }

    public List<User> getUserList() {
        return IUR.findAll();
    }

    public void setUserList(List<User> userList) {
        this.IUR.saveAll(userList);
    }

    /*
    deleted temporarily
    public Map<User, List<Alert>> getAlerts() {
        return IAR.findAll();
    }

    public void setAlerts(Map<User, List<Alert>> alerts) {
        this.alerts = alerts;
    }
*/

    public List<Complaint> getComplaint() {
        return IComplaintRepo.findAll();
    }

    public void setComplaint(Map<User, List<Complaint>> complaints) {
        this.complaints = complaints;
    }

    public List<League> getLeagueList() {
        return ILR.findAll();
    }

    public HashMap<Fan, List<String>> getFanSearchNameHistory() {
        return fanSearchNameHistory;
    }

    public HashMap<Fan, List<String>> getFanSearchCategoryHistory() {
        return fanSearchCategoryHistory;
    }

    public HashMap<Fan, List<String>> getFanSearchKeyWordHistory() {
        return fanSearchKeyWordHistory;
    }

    /**
     * id: dataManager@1
     * Search League by league type
     *
     * @param leagueType
     * @return League if existing
     */
    public League SearchLeagueByType(League.LeagueType leagueType) {
        List<League> leagueList = ILR.findAll();
        for (League league : leagueList) {
            if (league.getType() == leagueType) {
                return league;
            }
        }
        return null;
    }

    /**
     * id: dataManager@2
     * add New League To Data
     *
     * @param league to add
     */
    public void addLeague(League league) {
        if (!ILR.findById(league.getType()).isPresent()) {
            ILR.save(league);
            systemLogger.info("league been added , type:" + league.getType());
        }
    }

    /**
     * id: dataManager@3
     * Search Season by start and end dates
     *
     * @param start date of season
     * @param End   date of season
     * @return Season if found, else null
     */
    public Season SearchSeason(String start, String End) {
        if (ISR.findById(start).isPresent())
            return ISR.findById(start).get();
        return null;
    }


    /**
     * id: dataManager@4
     * add new season to data
     *
     * @param season season to add
     */
    public void addSeason(Season season) {
        //rewrite this func
        if (true) {
            ISR.save(season);
            systemLogger.info("Season been added , linked to League:" + " , Start date:" + season.getStart() +
                    " , End date:" + season.getEnd());
        } else if (SearchSeason(season.getStart(), season.getEnd()).getLeagueList().contains(season.getLeagueList())) {
            systemLogger.info("Season Linked to existing League:" + " , Start date:" + season.getStart() +
                    " , End date:" + season.getEnd());
        }
    }

    /**
     * id: dataManager@6
     * add New Referee To Data
     *
     * @param referee
     * @return if added successfully, if not -> already contains the element
     */
    public boolean addReferee(Referee referee) {
        if (!IFR.findById(referee.getR_id()).isPresent()) {
            IFR.save(referee);
            systemLogger.info("new Referee been added , belong to user : " + referee.getUser().getUserName());
            return true;
        }
        return false;
    }

    /**
     * id: dataManager@7
     * remove referee from data
     *
     * @param referee
     * @return
     */

    public boolean removeReferee(Referee referee) {
        if (IFR.findById(referee.getR_id()) != null) {
            IFR.deleteById(referee.getR_id());
            systemLogger.info("Referee been removed , belong to user : " + referee.getUser().getUserName());
            return true;
        }
        return false;
    }


    public void setLeagueList(List<League> leagueList) {
        this.ILR.saveAll(leagueList);
    }

    public List<Season> getSeasonList() {
        return ISR.findAll();
    }

    public void setSeasonList(List<Season> seasonList) {
        this.ISR.saveAll(seasonList);
    }

    public List<Team> getTeamList() {
        return this.ITR.findAll();
    }

    public void setTeamList(List<Team> teamList) {
        this.ITR.saveAll(teamList);
    }

    public List<Referee> getRefereeList() {
        return IFR.findAll();
    }


    public static void writeData(IDataManager data, File file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(data);

            o.close();
            f.close();
        } catch (Exception e) {
        }

    }

    public static IDataManager readData(File file) {
        IDataManager Mdata = null;
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            Mdata = (IDataManager) oi.readObject();

            oi.close();
            fi.close();
        } catch (Exception e) {
        }
        return Mdata;
    }

    public List<Page> getPageList() {
        return IPR.findAll();
    }

    /**
     * ID: dataManager@8
     * adds a new Team to the teams list
     * @param team the new team we want to add
     */
    public void addTeam(Team team){
        if (team != null)
            this.ITR.save(team);
    }

    /**
     * ID: dataManager@9
     * adds a new complaint to the complaints map
     * @param complaint the new complaint we want to add
     */
    public void addComplaint(Complaint complaint){
        IComplaintRepo.save(complaint);
    }

    public List<Owner> getOwners() {
        List<Owner> owners = new ArrayList<Owner>();
        for (User user : IUR.findAll()) {
            if (user.getEmail().equals("aona@qn"))
            {
                System.out.println();
            }
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Owner) {
                    owners.add((Owner) role);
                }
            }
        }
        return owners;
    }

    public List<Manager> getManagers() {
        List<Manager> managers = new ArrayList<Manager>();
        for (User user : IUR.findAll()) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Manager) {
                    managers.add((Manager) role);
                }
            }
        }
        return managers;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();
        for (User user : IUR.findAll()) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Player) {
                    players.add((Player) role);
                }
            }
        }
        return players;
    }

    public List<User> searchUserByName(String name) {
        List<User> retrievedUsers = new ArrayList<>();
        String[] splitted = name.split(" ");
        if (splitted.length == 1){
            for (User user : IUR.findAll()) {
                if (user.getFirstName() != null && user.getFirstName().equals(splitted[0])) {
                    retrievedUsers.add(user);
                }
            }
        }else{
            for (User user : IUR.findAll()) {
                if (user.getFirstName() != null && user.getLastName() != null && user.getFirstName().equals(splitted[0]) && user.getLastName().equals(splitted[1])) {
                    retrievedUsers.add(user);
                }
            }
        }
        return retrievedUsers;
    }


    @Override
    public List<League> searchLeagueByName(String leagueName) {
        List<League> retrievedLeagues = new ArrayList<>();
        for (League league : ILR.findAll()) {
            if (league.getName() != null && league.getName().toLowerCase().equals(leagueName.toLowerCase())) {
                retrievedLeagues.add(league);
            }
        }
        return retrievedLeagues;
    }

    @Override
    public List<Team> searchTeamByName(String teamName) {
        List<Team> retrievedTeams = new ArrayList<>();
        for (Team team : ITR.findAll()) {
            if (team.getName() != null && team.getName().toLowerCase().equals(teamName.toLowerCase())) {
                retrievedTeams.add(team);
            }
        }
        return retrievedTeams;
    }

    @Override
    public void addNameHistory(Fan fan, String query) {
        if (fanSearchNameHistory.containsKey(fan)){
            fanSearchNameHistory.get(fan).add(query);
        }else{
            List<String>SearchHistory = new ArrayList<>();
            SearchHistory.add(query);
            fanSearchNameHistory.put(fan,SearchHistory);
        }

    }

    @Override
    public void addKeyWordHistory(Fan fan, String query) {
        if (fanSearchKeyWordHistory.containsKey(fan)){
            fanSearchKeyWordHistory.get(fan).add(query);
        }else{
            List<String>SearchHistory = new ArrayList<>();
            SearchHistory.add(query);
            fanSearchKeyWordHistory.put(fan,SearchHistory);
        }
    }

    @Override
    public void addCategoryHistory(Fan fan, String query) {
        if (fanSearchCategoryHistory.containsKey(fan)){
            fanSearchCategoryHistory.get(fan).add(query);
        }else{
            List<String>SearchHistory = new ArrayList<>();
            SearchHistory.add(query);
            fanSearchCategoryHistory.put(fan,SearchHistory);
        }
    }

    @Override
    public List<String> getCategorySearchHistory(Fan fan) {
        return this.fanSearchCategoryHistory.get(fan);
    }

    @Override
    public List<String> getKeyWordSearchHistory(Fan fan) {
        return this.fanSearchKeyWordHistory.get(fan);
    }

    @Override
    public List<String> getNameSearchHistory(Fan fan) {
        return this.fanSearchNameHistory.get(fan);
    }

    @Override
    public List<Game> getGameList() {
        return IGR.findAll();
    }
    @Override
    public void addGame(Game game) {
         IGR.save(game);
    }

    public List<Coach> getCoaches() {
        List<Coach> coaches = new ArrayList<Coach>();
        for (User user : IUR.findAll()) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Coach) {
                    coaches.add((Coach) role);
                }
            }
        }
        return coaches;
    }


    /**
     * ID: dataManager@10
     * adds new alert to the alerts map
     * @param alert the new alert we want to add
     */
    public void addAlert(Alert alert){
        IAR.save(alert);
    }

    @Override
    public void addGameReport(GameReport gameReport) {
        if (gameReport!=null)
            IGRR.save(gameReport);
    }

    @Override
    public void addJudgementApproval(JudgmentApproval judgmentApproval) {
        if (judgmentApproval != null)
            IJAR.save(judgmentApproval);
    }

    /**
     * dataManager@11
     * delets a user from the users list
     * @param user the user we want to delete
     */
    public void deleteUser(User user){
        IUR.delete(user);
    }

    @Override
    public List<Alert> getAlerts() {
        return IAR.findAll();
    }

    @Override
    public void addManager(Manager manager) {
        IMR.save(manager);
    }

    @Override
    public void deleteManager(Manager manager) {
        IMR.delete(manager);
    }

    @Override
    public void addPlayer(Player player) {
        IPlayerRepo.save(player);
    }

    @Override
    public void deletePlayer(Player player) {
        IPlayerRepo.delete(player);
    }

    @Override
    public void deleteCoach(Coach coach) {
        ICR.delete(coach);
    }

    @Override
    public void addCoach(Coach coach) {
        ICR.save(coach);
    }

    @Override
    public void addOwner(Owner owner) {
        IOR.save(owner);
    }

    @Override
    public void deleteOwner(Owner owner) {
        IOR.delete(owner);
    }

    @Override
    public void addPage(Page page) {
        IPR.save(page);
    }

    @Override
    public void addPolicy(Policy policy) {
        IPolicyRepo.save(policy);
    }

    @Override
    public void addRankedPolicy(RankPolicy rankPolicy) {
        IRPR.save(rankPolicy);
    }

    @Override
    public void addResult(Result result) {
        IResRepo.save(result);
    }

    @Override
    public void addGameEvent(GameEventCalender event) {
        iGameEventRepository.save(event);
    }

    @Override
    public User getUser(String userName) {
        return null;
    }

    @Override
    public void addRepresentetive(Representative representative) {
        iRepresentitiveRepository.save(representative);
    }

    @Override
    public void deleteJudgmentApproval(JudgmentApproval approval) {
        IJAR.deleteById(approval.getJudgement_Id());
    }

    @Override
    public void addAdministrator(Administrator administrator) {
        if (administrator != null)
            IAdminRepo.save(administrator);
    }

    @Override
    public List<JudgmentApproval> getJudge() {
        return IJAR.findAll();
    }
}


