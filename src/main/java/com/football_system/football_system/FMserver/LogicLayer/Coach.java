package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "Coaches")
public class Coach extends RoleHolder implements Serializable {

    private String name;
    String qualification;
    private String job;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Page page;

    public Coach(User user, String name, String qualification, String job, Page page, Team team) {
        super(user);
        this.name = name;
        this.qualification = qualification;
        this.job = job;
        this.page = page;
        this.team = team;
    }

    public Coach() {}

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        data().addCoach(this);
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
        data().addCoach(this);
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
        data().addCoach(this);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        data().addCoach(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        data().addCoach(this);
    }

    /**
     * id: coach@1
     * add update To Page
     * @param update
     */
    public void addUpdateToPage(String update) {
        page.addUpdate(update);
    }

}
