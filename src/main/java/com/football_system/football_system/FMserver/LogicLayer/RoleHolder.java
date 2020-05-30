package com.football_system.football_system.FMserver.LogicLayer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "RoleHolders")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class RoleHolder extends Role implements Serializable {
    @OneToOne
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
