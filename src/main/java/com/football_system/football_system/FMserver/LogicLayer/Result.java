package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@EnableAutoConfiguration
@Table(name = "Results")
public class Result {
    @Id
    public int Result_Id;
    private Integer home;
    private Integer away;
    private static int Id_Generator = 9;

    public Result(Integer home, Integer away) {
        this.home = home;
        this.away = away;
        Result_Id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Result(){}

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public Integer getHome() {
        return home;
    }

    public Integer getAway() {
        return away;
    }

    public void homeScores(){
        home++;
        data().addResult(this);
    }

    public void awayScores(){
        away++;
        data().addResult(this);
    }

    public int getResult_Id() {
        return Result_Id;
    }

    public void setResult_Id(int result_Id) {
        Result_Id = result_Id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.Result_Id == ((Result)obj).Result_Id){
            return true;
        }
        return false;
    }
}
