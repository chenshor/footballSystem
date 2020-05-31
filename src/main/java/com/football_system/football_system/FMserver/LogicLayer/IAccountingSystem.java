package com.football_system.football_system.FMserver.LogicLayer;

public interface IAccountingSystem {

    boolean addPayment(String teamName, String date, double amount);
}
