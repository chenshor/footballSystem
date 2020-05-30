package com.football_system.football_system.FMserver.DataLayer.Repositories;

import com.football_system.football_system.FMserver.LogicLayer.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPolicyRepository extends JpaRepository<Policy,Integer> {
}
