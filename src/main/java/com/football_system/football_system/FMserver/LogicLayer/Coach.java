package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;

public class Coach extends RoleHolder implements Serializable {

    private String name;
    String qualification;
    private String job;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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
