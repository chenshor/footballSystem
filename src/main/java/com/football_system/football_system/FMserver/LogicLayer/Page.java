package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {
    private List<User> followers;
    private RoleHolder roleHolder;
    private List<String> updates;

    public Page(RoleHolder roleHolder) {
        followers = new ArrayList<>();
        updates = new ArrayList<>();
        this.roleHolder = roleHolder;
    }

    public void addUpdate(String update) {
        if (update != null && update.length() > 0)
            updates.add(update);
    }

    public List<String> getUpdates() {
        return updates;
    }
}
