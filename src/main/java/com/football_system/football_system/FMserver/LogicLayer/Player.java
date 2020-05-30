package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;
@Entity
@EnableAutoConfiguration
@Table(name = "Players")
public class Player extends RoleHolder implements Serializable {

    private String position;
    private String name;
    String birthDate;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Page page;

    public Player(User user, String position, Team team, String name, String birthDate, Page page) {
        super(user);
        this.position = position;
        this.team = team;
        this.name = name;
        this.birthDate = birthDate;
        this.page = page;
    }

    public Player(User user, String position, Team team, String name, String birthDate) {
        super(user);
        this.position = position;
        this.team = team;
        this.name = name;
        this.birthDate = birthDate;
    }

    public Player() {}

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    /**
     * id: player@1
     * equals implemented for player
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return Objects.equals(position, player.position) &&
                Objects.equals(team, player.team) &&
                Objects.equals(name, player.name) &&
                Objects.equals(birthDate, player.birthDate) &&
                Objects.equals(page, player.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position, team, name, birthDate, page);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        data().addPlayer(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        data().addPlayer(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        data().addPlayer(this);
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        data().addPlayer(this);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        data().addPlayer(this);
    }

    /**
     * id: player@2
     * adds update to page
     * @param update
     */
    public void addUpdateToPage(String update) {
        page.addUpdate(update);
    }
}
