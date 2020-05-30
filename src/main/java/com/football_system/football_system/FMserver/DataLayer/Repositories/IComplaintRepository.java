package com.football_system.football_system.FMserver.DataLayer.Repositories;

import com.football_system.football_system.FMserver.LogicLayer.Complaint;
import com.football_system.football_system.FMserver.LogicLayer.Fan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComplaintRepository extends JpaRepository<Complaint,Integer> {
}
