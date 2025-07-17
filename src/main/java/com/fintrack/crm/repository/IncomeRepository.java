package com.fintrack.crm.repository;

import com.fintrack.crm.entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
    List<IncomeEntity> findByUserId(Long userId);
}

