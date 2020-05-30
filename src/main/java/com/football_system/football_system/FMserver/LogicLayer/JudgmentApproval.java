package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "JudgmentApprovals")
public class JudgmentApproval implements Serializable {
    @Id
    private int Judgement_Id;
    @OneToOne
    private League league;
    @OneToOne
    private Season season;
    @ManyToOne
    private Referee referee;
    private String Referees_Email;
    @Transient
    private static int Id_Generator = 6;

    public JudgmentApproval(League league, Season season) {
        this.league = league;
        this.season = season;
        Judgement_Id = Id_Generator;
        Id_Generator = Id_Generator + 10;
        DataComp.getInstance().addJudgementApproval(this);
    }

    public JudgmentApproval(League league, Season season,Referee referee) {
        this.referee = referee;
        this.season = season;
        this.league = league;
        this.Referees_Email = referee.getUser().getEmail();
        Judgement_Id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public JudgmentApproval(){
    }

    private IDataManager data(){
        return DataComp.getInstance();
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
        DataComp.getInstance().addJudgementApproval(this);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
        DataComp.getInstance().addJudgementApproval(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(this.Judgement_Id == ((JudgmentApproval)obj).Judgement_Id){
            return true;
        }
        return false;
    }

    public int getJudgement_Id() {
        return Judgement_Id;
    }

    public void setJudgement_Id(int judgement_Id) {
        Judgement_Id = judgement_Id;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public String getReferees_Email() {
        return Referees_Email;
    }
}
