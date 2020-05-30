package com.football_system.football_system.FMserver.DataLayer.Repositories;

import com.football_system.football_system.FMserver.LogicLayer.Fan;
import com.football_system.football_system.FMserver.LogicLayer.GameReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameReportRepository extends JpaRepository<GameReport,Integer> {
}