package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;

public abstract class RoleHolder extends Role implements Serializable {
    protected Team team;
    public RoleHolder(User user) {
        super(user);
    }

    public Team getTeam() {
        return team;
    }

    public RoleHolder() {
        super();
    }

    public boolean equals(Role obj) {
        if (obj instanceof RoleHolder) {
            RoleHolder roleHolder = (RoleHolder)obj;
            if ( this.getUser().equals(roleHolder.getUser())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
