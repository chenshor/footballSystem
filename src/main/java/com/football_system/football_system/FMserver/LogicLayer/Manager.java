package com.football_system.football_system.FMserver.LogicLayer;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


import javax.persistence.*;
import javax.persistence.Table;
import java.io.IOException;
import java.util.*;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "Managers")
public class Manager extends RoleHolder implements Serializable {

    public enum Permission {
        playerDeletion, playerAddition, coachAddition, coachDeletion
    }

    private String name;
    @OneToOne
    private Team team;
    @OneToOne
    private Owner nominatedBy;
    @Transient
    private Map<Permission, Boolean> permissionBooleanMap;
    @OneToMany(cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Alert> alerts;

    private IDataManager data(){
        return DataComp.getInstance();
    }


    public Manager(User user, String name, Team team) {
        super(user);
        this.name = name;
        this.team = team;
        this.permissionBooleanMap = new HashMap<>();
        this.alerts = new LinkedList<>();
    }

    public Manager(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Manager manager = (Manager) o;
        return Objects.equals(name, manager.name) &&
                Objects.equals(team, manager.team);
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
        data().addManager(this);
    }
    /**
     * ID: Manager@1
     * add a new alert to thr alert list
     * @param alert the new alert
     */
    public void addAlert(Alert alert){
        getAlerts().add(alert);
        data().addManager(this);
    }

    /**
     * id: Manager@2
     * assigns permissions to THIS manager by the owner iff the owner nominated the manager
     * @param permissionBooleanMap
     * @param owner
     * @throws IOException if the manager was not nominated by the owner
     */
    public void setPermissions(Map<Permission, Boolean> permissionBooleanMap, Owner owner) throws IOException {
        if (this.nominatedBy.equals(owner)) {
            this.permissionBooleanMap = permissionBooleanMap;
        }
        else
            throw new IOException("Can not assign permission to this manager since he did not nominated by the owner");
    }

    /**
     * id: Manager@3
     * creates a new instance of coach with the following parameters
     * and connecting it to the requested team
     * @param teamName
     * @param name
     * @param qualification
     * @param job
     * @param userName
     * @param email
     * @throws IOException if the manager is not permitted to execute this function
     */
    public void insertNewCoach(String teamName, String name, String qualification, String job, String userName,
                               String email) throws IOException { //tested
        if ( this.permissionBooleanMap.get(Permission.coachAddition).booleanValue() == true ) {
            this.nominatedBy.insertNewCoach(teamName,name,qualification,job,userName,email);
        }
        else
            throw new IOException("This manager is not permitted to nominate a new coach to the team");
    }

    /**
     * id: Manager@4
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
     * @throws IOException if the manager is not permitted to execute this function
     */
    public void insertNewPlayer(String teamName, String name, String position, int day ,
                                int month, int year , String userName,String email) throws IOException { //tested
        if ( this.permissionBooleanMap.get(Permission.playerAddition).booleanValue() == true ) {
            this.nominatedBy.insertNewPlayer(teamName,name,position,day,month,year,userName,email);
        }
        else
            throw new IOException("This manager is not permitted to nominate a new player to the team");
    }


    /**
     * id: Manager@5
     * deletes the player that owns the given user
     * @param teamName
     * @param userName
     * @param email
     * @throws IOException if the manager is not permitted to execute this function
     */
    public void deletePlayer(String teamName,String userName,String email) throws IOException { //tested
        if ( this.permissionBooleanMap.get(Permission.playerDeletion).booleanValue() == true ) {
            this.nominatedBy.deletePlayer(teamName,userName,email);
        }
        else
            throw new IOException("This manager is not permitted to delete a player from the team");
    }

    /**
     * id: Manager@6
     * delete coach only if there is at least one coach in the coachList
     * @param teamName
     * @param userName
     * @param email
     * @throws IOException if the manager is not permitted to execute this function
     */
    public void deleteCoach(String teamName,String userName,String email) throws IOException { //tested

        if ( this.permissionBooleanMap.get(Permission.coachDeletion).booleanValue() == true ) {
            this.nominatedBy.deleteCoach(teamName,userName,email);
        }
        else
            throw new IOException("This manager is not permitted to delete a coach from the team");
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, team);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        data().addManager(this);
    }

    public Owner getNominatedBy() {
        return nominatedBy;
    }

    public void setNominatedBy(Owner nominatedBy) {
        this.nominatedBy = nominatedBy;
        data().addManager(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        data().addManager(this);
    }

    public Map<Permission, Boolean> getPermissionBooleanMap() {
        return permissionBooleanMap;
    }



}
