package com.football_system.football_system.FMserver.LogicLayer;

public class ProxyAccountingSystem implements IAccountingSystem {

    private IAccountingSystem iAccountingSystem;

    public ProxyAccountingSystem(IAccountingSystem iAccountingSystem) {
        this.iAccountingSystem = iAccountingSystem;
    }

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        return this.iAccountingSystem.addPayment(teamName,date,amount);
    }
}
