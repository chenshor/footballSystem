package com.football_system.football_system.FMserver.LogicLayer;

public class ProxyTaxesSystem implements ITaxesSystem {

    private ITaxesSystem iTaxesSystem;

    public ProxyTaxesSystem(ITaxesSystem iTaxesSystem) {
        this.iTaxesSystem = iTaxesSystem;
    }

    @Override
    public double getTaxeRate(double revenueAmount) {
        return this.iTaxesSystem.getTaxeRate(revenueAmount);
    }
}
