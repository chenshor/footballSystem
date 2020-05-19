package com.football_system.football_system.FMserver.LogicLayer;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.football_system.football_system.FMserver.DataLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;

public class User implements Serializable {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String userName;
    private List<Role> roles;

    public List<Alert> getAlerts() {
        return alerts;
    }

    private List<Alert> alerts;

    private static IDataManager data(){
        return DataComp.getInstance();
    }


    public User(User other) {
        this.email = other.password;
        this.password = other.password;
        this.userName = other.userName;
        this.roles = other.getRoles();
        this.alerts = new LinkedList<>();
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = new ArrayList<Role>();
        this.userName = firstName+"@"+getLastName();

    }


    public User(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.roles = new LinkedList<>();
        this.alerts = new LinkedList<>();
    }

    /**
     * id: User@1
     * equals implementation for user
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) &&
                password.equals(user.password) &&
                firstName.equals(user.firstName) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, userName);
    }

    /**
     * id: User@2
     * add new Role
     * @param role
     * @return true if added successfully , else if already exists
     */
    public boolean addRole(Role role){
        if (role != null){
            if(!roles.contains(role)) {
                roles.add(role);
                return true;
            }
        }
        return false ;
    }

    /**
     * id: User@3
     * remove Role
     * @param role
     * @return Object that been removed , null if object removed before
     */
    public boolean removeRole(Role role){
        if (role != null){
            if(roles.contains(role)) {
                return roles.remove(role);
            }
        }
        return false ;
    }


    /**
     * id: User@4
     * find RefereeRoleIfExist  , else return null
     * @return
     */
    public Referee ifUserRoleIncludeReferee(){
        for(Role r : roles){
            if(r instanceof Referee){
                return (Referee)r ;
            }
        }
        return null;
    }
    /**
     * ID: User@5
     * adds a new Alert to the alerts list
     * @param alert the new alwert we want to add
     */
    public void addAlerts(Alert alert) {
        if(alerts==null){
            alerts = new LinkedList<>();
            alerts.add(alert);
        }else{
            this.alerts.add(alert);
        }

    }


    public static List<User> getAllUsers(){
        return data().getUserList();
    }

    /**
     * email getter
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * email setter
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * password getter
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * password setter
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * userName getter
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * userName setter
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * returns all roles of user
     * @return List<Role>
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * role setter
     * @param role
     */
    public void setRole(Role role){
        //if (!roles.contains(role))
            this.roles.add(role);
    }

    /**
     * role setter
     * @param roles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * firstName getter
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * lastName getter
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * firstName setter
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * lastName setter
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * id: User@6
     * change personal information with arguments given
     * @param firstName
     * @param lastName
     * @param email
     * @return boolean
     */
    public boolean updatePersonalInformation(String firstName, String lastName, String email){
        GuestService guestService = new GuestService(null,null);
        if (firstName !=null && lastName != null && email != null && guestService.mailAuthentication(email)){
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            return true;
        }
        return false;
    }

    /**
     * id: User@7
     * personal info getter
     * @return List<String>
     */
    public List<String> getPersonalDetails() {
        List<String> personalDetails = new ArrayList<>();
        personalDetails.add(firstName);
        personalDetails.add(lastName);
        personalDetails.add(email);
        return personalDetails;
    }

}
