package com.football_system.football_system.FMserver.ServiceLayer;
import com.football_system.football_system.FMserver.LogicLayer.*;

import java.io.IOException;
import java.util.List;

public class UserService extends AUserService {
    private User user;
    private IController system;

    public UserService(User user, IController system) {
        this.user = user;
        this.system = system;
    }

    /**
     * id: UserService@1
     * USE CASE - 3.1
     * logOut from system
     */
    @Override
    public void logOut() throws IOException {
        system.removeUser(user);
    }

    /**
     * id: UserService@2
     * USE CASE - 3.6
     * show personal details
     * @return
     * @throws IOException
     */
     public List<String> showPersonalInformation() throws IOException {
        List<String> personalDetails = user.getPersonalDetails();
        String firstName = personalDetails.get(0);
        String lastName = personalDetails.get(1);
        String email = personalDetails.get(2);
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println();
        return personalDetails;
     }


    /**
     * id: UserService@3
     * USE CASE - 3.7
     * edit personal details
     * @param firstName
     * @param lastName
     * @param email
     * @throws IOException
     */
    @Override
    public boolean editPersonalInformation(String firstName, String lastName, String email) throws IOException {
        boolean isPersonalInfoLegal =  user.updatePersonalInformation(firstName,lastName,email);
        if (!isPersonalInfoLegal){
            System.out.println("## Personal info that have been entered is invalid ##");
            return false;
        }else{
            return true;
        }

    }




}
