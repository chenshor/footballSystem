package com.football_system.football_system.FMserver.LogicLayer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@EnableAutoConfiguration
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Role  implements Serializable {
    @Id
    private int R_id;
    @ManyToOne
    private User user;
    @Transient
    private static int Id_Generator = 1;

    public Role(User user) {
        this.user = user;
        R_id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Role() {}

    //cant get user, right?
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this.R_id == ((Role)o).R_id){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getR_id() {
        return R_id;
    }

    public void setR_id(int r_id) {
        R_id = r_id;
    }
}
