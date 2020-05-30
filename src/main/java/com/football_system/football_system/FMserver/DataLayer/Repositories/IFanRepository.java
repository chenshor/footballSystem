package com.football_system.football_system.FMserver.DataLayer.Repositories;

import com.football_system.football_system.FMserver.LogicLayer.Fan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFanRepository extends JpaRepository<Fan,Integer> {
}
