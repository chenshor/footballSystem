package com.football_system.football_system.FMserver.ServiceLayer;
import com.football_system.football_system.FMserver.LogicLayer.*;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;

public class AdministratorService extends AUserService {
    private Administrator administrator;
    private static final Logger testLogger = Logger.getLogger(RefereeService.class);


    public AdministratorService(Administrator administrator) {
        this.administrator = administrator;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    /**
     * ID: AdministratorService@1
     * closes team for good
     * @param team the team we want to close
     */
    @Override
    public void closeTeam(Team team){
        administrator.closeTeam(team);
    }


    /**
     * ID: AdministratorService@2
     * displays all the complains in the system
     */
    @Override
    public Collection<List<Complaint>> showComplaints(){
        return administrator.showComplaints();
    }

    /**
     * ID: AdministratorService@3
     * adds a comment to a certain complaint
     * @param complaint the complaint we want to add comment to
     * @param comment the comment we want to add
     */
    @Override
    public void commentComplaint(Complaint complaint,String comment){
        administrator.commentComplaint(complaint,comment);
    }

    /**
     * ID: AdministratorService@4
     * deletes a user
     * @param user the user we want to delete
     */
    @Override
    public void deleteUser(User user){
        administrator.deleteUser(user);
    }

    /**
     * ID: AdministratorService@5
     * display the system activity that was written to te log
     */
    @Override
    public void displayLog() {
        administrator.displayLog();
    }


}


