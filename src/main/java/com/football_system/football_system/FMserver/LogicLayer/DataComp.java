package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.*;

import java.io.Serializable;

public class DataComp implements Serializable {

    private static DataManager DManager = new DataManager();
    public static IDataManager  getInstance(){
        return DManager;
    }

    public static void setDataManager(DataManager DManager) {
        DataComp.DManager = DManager;
    }
}
