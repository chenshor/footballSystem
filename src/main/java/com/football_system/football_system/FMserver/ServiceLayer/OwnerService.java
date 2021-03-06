package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.LogicLayer.*;
import java.io.IOException;
import java.util.Map;

public class OwnerService extends AUserService{

    public OwnerService(Controller control) {
        super(control);
    }



    /**
     * id: OwnerService@2
     * owner adds a new manager to a requested team
     * @param owner
     * @param teamName
     * @param managerName
     * @param userName
     * @param email
     */
    public Manager insertNewManager(Owner owner, String teamName, String managerName, String userName,
                                 String email, Map<Manager.Permission, Boolean> permissionBooleanMap) throws IOException {
       Manager m = owner.insertNewManager(teamName,managerName,userName,email, permissionBooleanMap);
       return m;
    }

    /**
     * id: OwnerService@3
     * owner adds a new coach to the requested team with the following parameters
     * @param owner
     * @param teamName
     * @param name
     * @param qualification
     * @param job
     * @param userName
     * @param email
     */
    public Coach insertNewCoach(Owner owner, String teamName, String name, String qualification, String job, String userName,
                                String email) throws IOException {
        Coach c = owner.insertNewCoach(teamName,name,qualification,job,userName,email);
        return c;
    }

    /**
     * id: OwnerService@4
     * owner adds a new player to the selected team with the following parameters
     * @param owner
     * @param teamName
     * @param name
     * @param position
     * @param day
     * @param month
     * @param year
     * @param userName
     * @param email
     * @throws IOException
     */
    public Player insertNewPlayer(Owner owner, String teamName, String name, String position, int day ,
                                int month, int year , String userName, String email) throws IOException {
        Player p = owner.insertNewPlayer(teamName,name,position,day,month,year,userName,email);
        return p;
    }

    /**
     * id: OwnerService@5
     * owner adds a new stadium to a requested team
     * @param owner
     * @param teamName
     * @param stadiumName
     */
    public void insertNewStadium(Owner owner, String teamName, String stadiumName) throws IOException {
        owner.insertNewStadium(teamName,stadiumName);
    }

    /**
     * id: OwnerService@7
     * deletes the requested asset (Player/Coach/Manager) from the owne'r team
     * @param own
     * @param teamName
     * @param userName
     * @param email
     * @param toDelete
     */
    public void deleteRoleHolder(Owner own, String teamName, String userName, String email, RoleHolder toDelete)
            throws IOException {
        switch (toDelete.getClass().getSimpleName().toLowerCase()) {
            case "player":
                own.deletePlayer(teamName,userName,email);
                break;
            case "manager":
                own.deleteManager(teamName,userName,email);
                break;
            case "coach":
                own.deleteCoach(teamName,userName,email);
                break;
        }
    }

    /**
     * id: OwnerService@8
     * deletes the requested stadium from the owner's team
     * @param owner
     * @param teamName
     * @param stadium
     */
    public void deleteStadium(Owner owner, String teamName, String stadium) throws IOException{
        owner.deleteStadium(teamName,stadium);
    }

    /**
     * id: OwnerService@9
     * nominates an existing user to an additional owner of the provided team of the provided owner, iff he does not
     * owes this team allready
     * @param owner
     * @param teamName
     * @param user
     * @param name
     * @throws IOException
     */
    public void nominateNewOwner(Owner owner,String teamName, User user, String name) throws IOException {
        owner.nominateNewOwner(user,teamName,name);
    }

    ////////////////////////////////////////////////////// 6.1.3

    /**
     * id: OwnerService@10
     * updates a set of attributes that the owner chose for a team member of his
     * @param owner
     * @param teamName
     * @param roleHolder
     * @param attributes
     * @throws IOException
     */
    public void updateRoleHolder(Owner owner,String teamName, RoleHolder roleHolder,
                                 Map<String,String> attributes) throws IOException {
        owner.updateAssetAttributes(teamName,roleHolder,attributes);
    }

    /**
     * id: OwnerService@11
     * activates process of removing owner from all of his nominations in the selected team
     * @param own
     * @param nominatedOwner
     * @param teamName
     * @throws IOException
     */
    public void removeOwnership(Owner own, Owner nominatedOwner, String teamName) throws IOException {
        own.removeOwnership(nominatedOwner,teamName);
    }

    /**
     * id: OwnerService@12
     * closes the selected team's activity temporarily
     * @param own
     * @param team
     * @throws IOException if the selected team is not owned by the owner
     */
    public void closeTeamActivity(Owner own, Team team) throws IOException {
        own.closeTeamActivity(team);
    }

    /**
     * id: OwnerService@13
     * closes the selected team's activity temporarily
     * @param own
     * @param team
     * @throws IOException if the selected team is not owned by the owner
     */
    public void renewTeamActivity(Owner own, Team team) throws IOException {
        own.openTeamActivity(team);
    }
}

