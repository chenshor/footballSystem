package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.LogicLayer.*;


import java.util.List;
import java.util.Map;

public interface IController {

    boolean addUser(User newUser);

    void addGuest(Guest newGuest);

    void removeGuest(Guest guestToRemove);

    void removeUser(User userToRemove);

    void removeUserService(User user);

    List<Guest> getGuestsList();

    void setGuestsList(List<Guest> guestsList);

    List<User> getUserList();

    void setUserList(List<User> userList);

    Map<User, List<IUserService>> getUserServices();

    void setUserServices(Map<User, List<IUserService>> userServices);

    Representative getRepresentative();

    Administrator getAdministrator();

    void createFanServiceForUser(User user, Fan fan);

    void addServicesToUser(User userToSignIn);

//    public boolean removeRole(User user, Role role);
//
//    public boolean addRole(User user, Role role);
    void updateServicesToUser(User user);

     Map<Guest, IGuestService> getGuestServices();
}
