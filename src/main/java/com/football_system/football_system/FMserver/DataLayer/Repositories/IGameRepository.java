package com.football_system.football_system.FMserver.DataLayer.Repositories;

import com.football_system.football_system.FMserver.LogicLayer.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameRepository extends JpaRepository<Game, Integer> {
}
