package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Entity
@EnableAutoConfiguration
@Table(name = "Owners")
public class Owner extends RoleHolder implements Serializable {

    private String name;
    @OneToMany
    private List<Team> teamList;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Owner nominatedBy;
    @OneToMany(cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Alert> alerts;

    public Owner(User user, String name) {
        super(user);
        this.name = name;
        this.teamList = new LinkedList<>();
        alerts =new LinkedList<>();

    }

    public Owner(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(name, owner.name) &&
                Objects.equals(teamList, owner.teamList) &&
                Objects.equals(nominatedBy, owner.nominatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, teamList, nominatedBy);
    }

    /**
     * id: Owner@1
     * **this function is used for both UC 6.1.1 and UC 6.4**
     * creates a new instance of manager with the selected premissions with the argument parameters
     * and connecting it to the requested team
     * @param teamName
     * @param managerName
     * @param userName
     * @param email
     * @throws IOException if the user or the team are not exist
     */
    public Manager insertNewManager(String teamName, String managerName, String userName,
                                 String email, Map<Manager.Permission, Boolean> permissionBooleanMap) throws IOException { // tested

           if (validateExistingAssetType(teamName,email,userName)) {
            User user = this.getAssetUser(userName, email);
            Team team = getTeam(teamName);
            if (team.getManager(user) == null) {
                if (team.getOwner(user) == null) {
                    Manager manager = new Manager(user, managerName, team);
                    manager.setNominatedBy(this);
                    team.setManager(manager);
                    manager.setTeam(team);
                    user.setRole(manager);
                    data().addManager(manager);
                    data().addNewUser(user);
                    data().addTeam(team);
                    team.getRoleHolders().add(manager);
                    assignManagerPremissions(manager, permissionBooleanMap);
                    return manager;
                } else
                    throw new IOException("The selected user is allready nominated as the owner in the team");

            } else

                throw new IOException("The selected user is allready nominated as manager in this team");

        }
        return null;
    }

    /**
     * id: Owner@2
     * creates a new instance of coach with with the argument parameters
     * and connecting it to the requested team
     * @param teamName
     * @param name
     * @param qualification
     * @param job
     * @param userName
     * @param email
     * @throws IOException if the user or the team are not exist
     */
    public Coach insertNewCoach(String teamName, String name, String qualification, String job, String userName,
                                String email) throws IOException { // tested
        if (validateExistingAssetType(teamName,email,userName)) {
            User user = this.getAssetUser(userName, email);
            Team team = getTeam(teamName);
            Page page = new Page(null);
            Coach coach = new Coach(user, name, qualification, job, page, team);
            coach.setTeam(team);
            team.setCoach(coach);
            user.setRole(coach);
            data().addCoach(coach);
            data().addNewUser(user);
            data().addTeam(team);
            team.getRoleHolders().add(coach);
            return coach;
        }
        return null;
    }

    /**
     * id: Owner@3
     * creates a new instance of player with the following parameters
     * and connecting it to the requested team
     * @param teamName
     * @param name
     * @param position
     * @param day
     * @param month
     * @param year
     * @param userName
     * @param email
     * @throws IOException if the team or the user are not exist
     */
    public Player insertNewPlayer(String teamName, String name, String position, int day ,
                                int month, int year , String userName,String email) throws IOException { //tested

        if (validateExistingAssetType(teamName,email,userName)) {
            validateBirthDate(day,month,year);
            Team team = getTeam(teamName);
            User user = this.getAssetUser(userName, email);
            String date = day + "-" + month + "-" + year;
            Page page = new Page(null);
            Player player = new Player(user, position, team, name, date, page);
            team.setPlayer(player);
            player.setTeam(team);
            team.setPlayer(player);
            user.setRole(player);
            data().addPlayer(player);
            data().addNewUser(user);
            data().addTeam(team);
            team.getRoleHolders().add(player);
            return player;
        }
        return null;
    }

    /**
     * id: Owner@4
     * uploads the team's stadium
     * @param teamName
     * @param stadium
     * @throws IOException if the team is not exist
     */
    public void insertNewStadium(String teamName, String stadium) throws IOException { //tested
        validateExistingAssetType(teamName,"X","X");
        getTeam(teamName).setStadium(stadium);
        data().addTeam(team);
    }

    /**
     * id: Owner@5
     * deletes the player that owns the given user
     * @param teamName
     * @param userName
     * @param email
     * @throws IOException in the following cases:
     * the user or the team are not exist
     * the user does not exist in the database
     * the player is not a member in the selected team
     */
    public void deletePlayer(String teamName,String userName,String email) throws IOException { // tested
        if ( validateExistingAssetType(teamName,email,userName)) {
            User user = getAssetUser(userName, email);
            Team team = getTeam(teamName);
            if (team.getPlayer(user) != null) {
                for (Role role : user.getRoles()) {
                    if (role instanceof Player) {
                        Player player = (Player) role;
                        if (player.getTeam().equals(team)) {
                            team.getPlayerList().remove(player);
                            team.getRoleHolders().remove(player);
                            user.removeRole(player);
                            data().deletePlayer(player);
                            data().addNewUser(user);
                            data().addTeam(team);
                            break;
                        }
                    }
                }
            }
            else {
                throw new IOException("The selected user is not a player in the selected team");
            }
        }
    }

    /**
     * id: Owner@6
     * delete coach only if there is at least one coach in the coachList
     * @param teamName
     * @param userName
     * @param email
     */
    public void deleteCoach(String teamName,String userName,String email) throws IOException { //tested

        if (validateExistingAssetType(teamName,email,userName)) {
            Team team = getTeam(teamName);
            User user = getAssetUser(userName, email);
            if (team.getCoach(user)!= null) {
                if (team.getCoachList().size() < 2)
                    throw new IOException("Cannot remove the last coach of the team");
                else {
                    for (Role role : user.getRoles()) {
                        if (role instanceof Coach) {
                            Coach coach = (Coach) role;
                            if (coach.getTeam().equals(team)) {
                                team.getRoleHolders().remove(coach);
                                team.getCoachList().remove(coach);
                                user.removeRole(coach);
                                data().deleteCoach(coach);
                                data().addNewUser(user);
                                data().addTeam(team);
                                break;
                            }
                        }
                    }
                }
            }
            else {
                throw new IOException("The selected user is not a coach in the selected team");
            }
        }
    }

    /**
     * id: 0wner@7
     * Deletes a manager from the team's managerList\
     * @param teamName
     * @param userName
     * @param email
     * @throws IOException in the following cases:
     * user does not exist
     * team does not exist
     * there is only 1 manager in the team
     * the selected manager was not nominated by THIS owner
     */
    public void deleteManager(String teamName,String userName,String email) throws IOException { //tested

        if (validateExistingAssetType(teamName,email,userName)) {
            Team team = getTeam(teamName);
            User user = getAssetUser(userName, email);
            if (team.getManager(user)!= null) {
                if (team.getManagerList().size() < 2) {
                    throw new IOException("Cannot remove the last manager of the team");
                } else {

                    for (Role role : user.getRoles()) {
                        if (role instanceof Manager) {
                            Manager manager = (Manager) role;
                            if (manager.getTeam().equals(team)) {
                                if (manager.getNominatedBy()!=null &&manager.getNominatedBy().equals(this)) {
                                    team.getManagerList().remove(manager);
                                    team.getRoleHolders().remove(manager);
                                    user.removeRole(role);
                                    data().deleteManager(manager);
                                    data().addNewUser(user);
                                    data().addTeam(team);
                                    break;
                                } else
                                    throw new IOException("The selected manager was not nominated by you");
                            }
                        }
                    }
                }
            }
            else {
                throw new IOException("The selected user is not a manager in the selected team");
            }
        }
    }

    /**
     * id: Owner@9
     * returns the requiered team
     * @String teamName
     * @return Team
     */
    public Team getTeam(String teamName) {
        for ( Team t : teamList ) {
            if (t.getName().equals(teamName))
                return t;
        }
        return null;
    }

    private IDataManager data(){
        return DataComp.getInstance();
    }

    /**
     * id: Owner@10
     * retrieves the user list from the dataManager and returns the requiered user
     * @return user list
     */
    private User getAssetUser(String userName, String userEmail) {
        User user = data().getUserByMail(userName,userEmail);
        if (user != null)
            return user;
        return null;
    }

    /**
     * id: Owner@11
     * Searches a user in the dataManager
     * @param userName
     * @param email
     * @return true if user exists, else otherwise
     */
    public boolean findUser(String userName, String email) {
        if (this.getAssetUser(userName,email) != null)
            return true;
        return false;
    }

    /**
     * id: Owner@12
     * deletes the stadium of the chosen team, replace its value with "NO_STADIUM"
     * @param teamName
     * @param stadium
     */
    public void deleteStadium(String teamName, String stadium) throws IOException { //tested
        if (validateExistingAssetType(teamName,"X","X")) {
            Team team = getTeam(teamName);
            if (team.getStadium().toLowerCase().equals(stadium.toLowerCase()))
                team.setStadium("NO_STADIUM");
        }
        data().addTeam(team);
    }
    /**
     * id: Owner@13
     * adds a new team to the owne's teamList
     * @param team
     */
    public void addTeam(Team team) {
        if (!teamList.contains(team)){
            this.teamList.add(team);
            team.addOwner(this);
            data().addTeam(team);
            data().addOwner(this);
        }


    }

    /////////////////////////////////////////////////////////////////////////////////// uc2
    /**
     * id: Owner@14
     * nominates a new owner to the team
     * @param user
     * @param teamName
     * @param name
     * @throws IOException
     */
    public void nominateNewOwner(User user, String teamName, String name) throws IOException { //tested
        if (checkNewOwnerValidity(user,teamName))
            assignOwnerPremission(user,getTeam(teamName),name);
    }

    /**
     * id: Owner@15
     * checks whether the new owner has a valid account
     * checks whether the owner owes the chosen team
     * @param user
     * @param teamName
     * @return
     * @throws IOException
     */

    private boolean checkNewOwnerValidity(User user, String teamName) throws IOException {
        if (data().getUserList().contains(user)) {
            Team team = getTeam(teamName);
            if(team != null) {
                if (team.getOwner(user) != null)
                    throw new IOException("User is allready nominated as owner in this team");
                else
                    return true;
            }
            else
                throw new IOException("Owner doest not owe the selected team");
        }
        else {
            throw new IOException("User does not exist in the data base");
        }
    }


    /**
     * id: Owner@16
     * add a new owner to the selected team in term he is not an owner allready
     * @param user
     * @param team
     * @param name
     */
    private void assignOwnerPremission(User user, Team team, String name) {
        Owner newOwner = new Owner(user,name);
        user.setRole(newOwner);
        newOwner.addTeam(team);
        team.addOwner(newOwner);
        team.getRoleHolders().add(newOwner);
        newOwner.setNominatedBy(this);
        data().addOwner(newOwner);
        data().addTeam(team);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        data().addOwner(this);
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
        data().addOwner(this);
    }

    ////////////////////////////////////// 6.1.3 uc


    /**
     * id: Owner@17
     * checks whether the new owner has a valid account
     * checks whether the owner owes the chosen team
     * @param user
     * @param teamName
     * @return
     * @throws IOException
     */

    private boolean checkOwnerMembership(User user, String teamName) throws IOException {
        if (data().getUserList().contains(user)) {
            Team team = getTeam(teamName);
            if( team != null ) {
                Owner owner = team.getOwner(user);
                if (owner != null ) {
                    return true;
                }
                else
                    throw new IOException("Selected user is not a team member in the selected team");
            }
            else
                throw new IOException("Owner doest not owe the selected team");
        }
        else {
            throw new IOException("User does not exist in the data base");
        }
    }



    /**
     * id: Owner@18
     * update a set of attributes which selected by the owner, in term that the selected team member
     * is an existing and valid member of the owner's selected team
     * @param teamName
     * @param roleHolder
     * @param attributes
     * @throws IOException in the cases that were checked in Owner@17
     */
    public void updateAssetAttributes(String teamName, RoleHolder roleHolder,
                                      Map<String, String> attributes) throws IOException { //tested

        if (validateExistingAssetType(teamName,roleHolder.getUser().getEmail(),roleHolder.getUser().getUserName())) {
            for ( String attribute : attributes.keySet() ) {
                switch (roleHolder.getClass().getSimpleName().toLowerCase()) {
                    case "player":
                        Player player = (Player)roleHolder;
                        switch (attribute.toLowerCase()) {
                            case "birthdate":
                                player.setBirthDate(attributes.get(attribute));
                                break;
                            case "position":
                                player.setPosition(attributes.get(attribute));
                                break;
                            default:
                                throw new IOException("Invalid attribute selected: " + attribute);
                        }
                        data().addPlayer(player);
                        break;
                    case "coach":
                        Coach coach = (Coach)roleHolder;
                        switch (attribute.toLowerCase()) {
                            case "qualification":
                                coach.setQualification(attributes.get(attribute));
                                break;
                            case "job":
                                coach.setJob(attributes.get(attribute));
                                break;
                            default: throw new IOException("Invalid attribute selected: " + attribute);
                        }
                        data().addCoach(coach);
                        break;
                    case "manager":
                        throw new IOException("Owner can not update a manager details");
                }
            }
        }
    }

    public void setNominatedBy(Owner nominatedBy) {
        this.nominatedBy = nominatedBy;
        data().addOwner(this);
    }

    public Owner getNominatedBy() {
        return nominatedBy;
    }

    /**
     * id: Owner@19
     * removes the selected owner's ownership and in addition removes his whole roles in the specified team.
     * the following terms must be true:
     * -the selected owner has a valid and existing user
     * -the selected owner was nominated by THIS owner
     * -the selected team exists in THIS teamList
     * @param nominated
     * @param teamName
     * @throws IOException if one of the terms does not match
     */
    public void removeOwnership(Owner nominated, String teamName) throws IOException { //tested

        checkOwnerMembership(nominated.getUser(),teamName);
        Team team = getTeam(teamName);
        if (team.getOwner(nominated.getUser()) != null) {
            if (nominated.getNominatedBy() != null && nominated.getNominatedBy().equals(this)) {
                 for ( Role role : nominated.getUser().getRoles() ) {
                     if (role instanceof Player)
                         deletePlayer(teamName,nominated.getUser().getUserName(),nominated.getUser().getEmail());
                     else if (role instanceof Coach)
                         deleteCoach(teamName,nominated.getUser().getUserName(),nominated.getUser().getEmail());
                     else if (role instanceof Manager) // instance of manager
                         deleteManager(teamName,nominated.getUser().getUserName(),nominated.getUser().getEmail());
                     else {// OWNER
                         team.getOwnerList().remove(nominated);
                         team.getRoleHolders().remove(nominated);
                         data().addTeam(team);
                         data().deleteOwner(nominated);
                         data().deleteOwner(nominated);
                     }
                 }
                User u = nominated.getUser();
                u.removeRole(nominated);
                data().addNewUser(u);
                //CHECK THIS WHOLE PART
                /*
                User user = new User(nominated.getUser());
                DM.addUser(user);
                nominated = null;
                */
            }
            else
                throw new IOException("The selected owner can't be removed since he did not nominated by you");
        }
        else
            throw new IOException("The selected owner is not nominated as an owner of this team");
    }

    /**
     * id: Owner@20
     * assigns premissions to the chosen manager by the owner.
     * @param manager
     * @param permissionBooleanMap
     */
    private void assignManagerPremissions(Manager manager, Map<Manager.Permission, Boolean> permissionBooleanMap)
            throws IOException {
        manager.setPermissions(permissionBooleanMap,this);
    }

    ////////////////////////////////// uc6
    /**
     * id: Owner@21
     * closes the team and inform the managers and the owners of the team
     * @param team
     * @throws IOException
     */
    public void closeTeamActivity(Team team) throws IOException { //tested
        if (!getTeamList().contains(team))
            throw new IOException("Selected team is not owned by the owner");
        team.changeTeamActivity(this, Team.TeamStatus.activityClosed);
        data().addTeam(team);
    }

    /**
     * id: Owner@22
     * Opens the team and inform the managers and the owners of the team
     * @param team
     * @throws IOException
     */
    public void openTeamActivity(Team team) throws IOException {//tested
        if (!getTeamList().contains(team))
            throw new IOException("Selected team is not owned by the owner");
        team.changeTeamActivity(this, Team.TeamStatus.activityOpened);
        data().addTeam(team);
    }

    /**
     * id: Owner@23
     * validates that the team is exist and connected to the owner
     * validates that the email and userName are attached to an existing account
     * @return User if exists
     * @param teamName
     * @param email
     * @param userName
     * @return true if the user exists, else throws exception
     * @throws IOException
     */
    private boolean validateExistingAssetType(String teamName,String email, String userName) throws IOException {
        Team team = getTeam(teamName);
        if (team == null)
            throw new IOException("The chosen team does not exist, please choose a valid team");

        if (!(email.equals("X") && userName.equals("X"))) { // not a stadium
            boolean exists = findUser(userName, email);
            if (!exists)
                throw new IOException("The chosen user does not exist, please insert valid inputs");
        }
        return true;
    }

    /**
     * id: Owner@24
     * validates the ranges of a player's birthday
     * @param day
     * @param month
     * @param year
     * @throws IOException
     */
    private void validateBirthDate(int day, int month, int year) throws IOException {

        if (!(year > 1900 && year < 2020 )) {
            throw new IOException("Year of birth is illegal, please insert a year between " +
                    "1900 and 2020");
        }
        if (!(month > 0 && month < 13 )) {
            throw new IOException("Month is illegal, please insert valid month between 1 and 12");
        }
        if (!(day > 0 && day < 32 )) {
            throw new IOException("Day is illegal, please insert valid day between 1 and 31");
        }
    }
    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
        data().addOwner(this);
    }

    /**
     * id: Owner@20
     * adds a new alert to the alerts list
     * @param alert the new alert we want to add
     */
    public void addAlert(Alert alert){
        getAlerts().add(alert);
        data().addOwner(this);
    }




}

