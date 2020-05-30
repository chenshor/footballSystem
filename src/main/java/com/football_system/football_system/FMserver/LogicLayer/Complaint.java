package com.football_system.football_system.FMserver.LogicLayer;


import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@EnableAutoConfiguration
@Table(name = "Complaints")
public class Complaint implements Serializable {
    @Id
    private int Complaint_Id;
    @OneToOne
    private User user;
    private String description;
    private String commentAdmin;
    private String date; // format: "2010-12-12"
    private boolean answered;
    @Transient
    private static int Id_Generator = 3;


    public Complaint(User user,  String description, String date) {
        this.user = user;
        this.description = description;
        this.date = date;
        this.answered=false;
        Complaint_Id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Complaint(){}

    public User getUser() {
        int i =2;
        return user;
    }

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
        data().addComplaint(this);
    }

    public void setUser(User user) {
        this.user = user;
        data().addComplaint(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        data().addComplaint(this);
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        data().addComplaint(this);
    }

    public void setCommentAdmin(String commentAdmin) {
        this.commentAdmin = commentAdmin;
        data().addComplaint(this);
    }

    public String getCommentAdmin()
    {
        if(commentAdmin==null){
            return "no comments";
        }
        return commentAdmin;
    }

    /**
     * ID: Complaint@1
     * @return the full complaint details
     */
    public String getFullComplaint(){
        String com = "user: " + getUser().getFirstName()+ " " + getUser().getLastName() + "\ndescription: " + getDescription()
                + "\ndate: " + getDate() + "\ncomments: " + getCommentAdmin();
        return com;
    }




    /**
     * ID: Complaint@2
     * @param obj the complaint we want to compare
     * @return true if the two complaint are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this.Complaint_Id == (((Complaint)obj).Complaint_Id))
            return true;
        return false;
    }

    public int getComplaint_Id() {
        return Complaint_Id;
    }

    public void setComplaint_Id(int complaint_Id) {
        Complaint_Id = complaint_Id;
    }


}
