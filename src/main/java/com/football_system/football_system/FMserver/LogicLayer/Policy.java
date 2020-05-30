package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "Policies")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Policy implements Serializable {
    @Id
    private int Policy_Id;
    @OneToOne
    private League league;
    @OneToOne
    private Season season;
    @Transient
    private static int Id_Generator = 8;

    private IDataManager data(){
        return DataComp.getInstance();
    }


    public Policy(League league, Season season) {
        this.league = league;
        this.season = season;
        Policy_Id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Policy(){}

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
        data().addPolicy(this);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
        data().addPolicy(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(this.Policy_Id == ((Policy)obj).Policy_Id){
            return true;
        }
        return false;
    }
}
