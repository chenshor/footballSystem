package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.LogicLayer.*;

import java.lang.reflect.Field;
import java.io.*;
import java.util.*;
import org.json.simple.*;
import com.google.gson.*;
import org.json.simple.parser.JSONParser;

public class Controller implements IController{
    private List<Guest> currentGuestsList;
    private Map<Guest,IGuestService> GuestServices;
    private List<User> currentUserList;
    private Map<User,List<IUserService>> UserServices;
    private Representitive representitive;
    private Administrator administrator;
    public static Controller controllerSingleTone ;
    private static String configurationPath = "configurations.json";

    /**
     * initialise system from configuration file
     */
    public Controller() {
        try{
            currentGuestsList = new ArrayList<Guest>();
            GuestServices = new HashMap<Guest, IGuestService>();
            currentUserList = new ArrayList<User>();
            UserServices = new HashMap<User, List<IUserService>>();
            FileReader configurationFile = new FileReader(new File(configurationPath));
            initFromFile(configurationFile);
        }catch(IOException IOE){
            System.out.println("## There is no conf. file ##");
            IOE.printStackTrace();
        }
    }

    /**
     * initialise system with arguments
     * @param representitive
     * @param administrator
     */
    // first init
    public Controller(Representitive representitive, Administrator administrator) {
        this.representitive = representitive;
        this.administrator = administrator;
        currentGuestsList = new ArrayList<Guest>();
        GuestServices = new HashMap<Guest, IGuestService>();
        currentUserList = new ArrayList<User>();
        UserServices = new HashMap<User, List<IUserService>>();
        addUser(representitive.getUser());
        addUser(administrator);
        this.representitive.getUser().addRole(representitive);
        addServicesToUser(representitive.getUser());
//        saveData();
    }

    /**
     * id: Controller@1
     * initialise all configurations of system from file
     * @param configuration
     */
    private void initFromFile(FileReader configuration) {
        try{
            Object testObject = new JSONParser().parse(new FileReader("configurations.json"));
            JSONObject controllerJSON = (JSONObject) testObject;
            String admin = (String) controllerJSON.get("admin");
            String rep = (String) controllerJSON.get("rep");
            Gson objects = new Gson();
            this.administrator = objects.fromJson(admin, Administrator.class);
            this.representitive = objects.fromJson(rep, Representitive.class);
            addUser(representitive.getUser());
            addUser(administrator);
            representitive.getUser().addRole(representitive);
            addServicesToUser(representitive.getUser());
        }catch (Exception E){
            E.printStackTrace();
        }
    }

    /**
     * GuestServices getter
     * @return Map<Guest, IGuestService>
     */
    public Map<Guest, IGuestService> getGuestServices() {
        return GuestServices;
    }

    /**
     * id: Controller@2
     * save to file current configurations of system
     */
    private void saveData() {
        Gson objects = new Gson();
        String adminJSON = objects.toJson(administrator);
        String repJSON = objects.toJson(representitive);
        JSONObject jsonWriter = new JSONObject();
        jsonWriter.put("admin", adminJSON);
        jsonWriter.put("rep", repJSON);
        try {
            PrintWriter pw = new PrintWriter("configurations.json");
            pw.write(jsonWriter.toJSONString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * id: Controller@3
     * removes user services from system
     * @param user
     */
    public void removeUserService(User user) {
        if (user != null && UserServices.containsKey(user)){
            this.UserServices.remove(user);
        }
    }

    /**
     * id: Controller@4
     * displays the team's asset's details to the screen
     * @param roleHolder
     */
    public void displayForm(RoleHolder roleHolder) {
        if (roleHolder==null)
            System.out.println("Name : String");
        else {
            Field[] declaredFields = roleHolder.getClass().getDeclaredFields();
            for (Field f : declaredFields) {
                System.out.println(f.getName() + " : " + f.getType().getSimpleName());
            }
        }
    }

    /**
     * GuestList getter
     * @return List<Guest>
     */
    public List<Guest> getGuestsList() {
        return currentGuestsList;
    }

    /**
     * GuestList setter
     * @param guestsList
     */
    public void setGuestsList(List<Guest> guestsList) {
        this.currentGuestsList = guestsList;
    }

    /**
     * UserList getter
     * @return List<User>
     */
    public List<User> getUserList() {
        return currentUserList;
    }

    /**
     * userList setter
     * @param userList
     */
    public void setUserList(List<User> userList) {
        this.currentUserList = userList;
    }

    /**
     * id: Controller@5
     * add user to system, also creates UserService for it
     * @param user
     * @return boolean
     */
    public boolean addUser(User user) {
        if (user != null){
            this.currentUserList.add(user);
            UserService userService = new UserService(user,this);
            List<IUserService>services = new ArrayList<>();
            services.add(userService);
            this.UserServices.put(user,services);
            return true;
        }
        return false;
    }

    /**
     * UserServices getter
     * @return Map<User, List<IUserService>>
     */
    public Map<User, List<IUserService>> getUserServices() {
        return UserServices;
    }

    /**
     * UserServices getter
     * @param userServices
     */
    public void setUserServices(Map<User, List<IUserService>> userServices) {
        UserServices = userServices;
    }

    /**
     * representative getter
     * @return Representitive
     */
    public Representitive getRepresentitive() {
        return representitive;
    }

    /**
     * administrator getter
     * @return Administrator
     */
    public Administrator getAdministrator() {
        return administrator;
    }

    /**
     * id: Controller@6
     * add new guest to system
     * @param newGuest
     */
    public void addGuest(Guest newGuest) {
        if(newGuest == null) return;
        this.currentGuestsList.add(newGuest);
        this.GuestServices.put(newGuest, new GuestService(newGuest, this));
    }

    /**
     * id: Controller@7
     * Removes Guest from system
     * @param guestToRemove
     */
    public void removeGuest(Guest guestToRemove) {
        this.GuestServices.remove(guestToRemove);
        this.currentGuestsList.remove(guestToRemove);
    }

    /**
     * id: Controller@8
     * removes user from system
     * @param userToRemove
     */
    public void removeUser(User userToRemove) {
        if (userToRemove != null){
            removeUserService(userToRemove);
            if (currentUserList.contains(userToRemove))
                this.currentUserList.remove(userToRemove);
        }
    }

    /**
     * id: Controller@9
     * clear all services for user and then calls again to addServicesToUser func
     * @param user
     */
    @Override
    public void updateServicesToUser(User user){
        if(user != null && getUserList().contains(user)) {
            getUserServices().get(user).clear();
            addServicesToUser(user);
        }
    }

    /**
     * id: Controller@10
     * add services to user according to its roles
     * @param user
     */
    @Override
    public void addServicesToUser(User user) {
        for (Role r: user.getRoles()){
            if (r instanceof Fan){
               createFanServiceForUser(user,(Fan)r);
            }else if(r instanceof Player){
                createPlayerServiceForUser(user,(Player)r);
            }else if (r instanceof Coach){
                createCoachServiceForUser(user,(Coach)r);
            }else if (r instanceof Referee){
                createRefereeServiceForUser(user,(Referee)r);
            }else if(r instanceof Owner){
                createOwnerServiceForUser(user,(Owner)r);
            }else if(r instanceof Representitive){
                createRepresentitiveServiceForUser(user,(Representitive)r);
            }else if(r instanceof Manager){
                createManagerServiceForUser(user,(Manager)r);
            }else{
                continue;
            }
        }
    }


    @Override
    public void createFanServiceForUser(User user, Fan fan) {
        if (user != null && fan != null){
            FanService fanService = new FanService(fan, this);
            UserServices.get(user).add(fanService);
        }
    }

    private void createManagerServiceForUser(User user, Manager r) {
        if (user != null && r != null){
            ManagerService managerService = new ManagerService(this);
            UserServices.get(user).add(managerService);
        }
    }


    private void createRepresentitiveServiceForUser(User user, Representitive r) {
        if (user != null && r != null){
            RepresentativeService representativeService = new RepresentativeService(this);
            UserServices.get(user).add(representativeService);
        }
    }


    private void createOwnerServiceForUser(User user, Owner r) {
        if (user != null && r != null){
            OwnerService ownerService = new OwnerService(this);
            UserServices.get(user).add(ownerService);
        }
    }

    private void createRefereeServiceForUser(User user, Referee r) {
        if (user != null && r != null){
            RefereeService refereeService = new RefereeService(r);
            UserServices.get(user).add(refereeService);
        }
    }

    private void createPlayerServiceForUser(User user, Player r) {
        if (user != null && r != null){
            PlayerService playerService = new PlayerService(r, this);
            UserServices.get(user).add(playerService);
        }
    }

    private void createCoachServiceForUser(User user, Coach r) {
        if (user != null && r != null){
            CoachService coachService = new CoachService(r,this);
            UserServices.get(user).add(coachService);
        }
    }
}
