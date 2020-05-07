package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.LogicLayer.*;

import java.util.List;

public interface IGuestService {

    User register(String firstName, String lastName, String email, String password);

    boolean logIn(String email, String password);

    User signIn(String email, String password);

    void showInformationByCategory(Interest interestIn);

    void searchInformation(Criteria criteria, String query);

    void filterResults(Filter filter, String query);

    List<League> getLeagues();

    List<Season> getSesons();

    List<Team> getTeams();
}
