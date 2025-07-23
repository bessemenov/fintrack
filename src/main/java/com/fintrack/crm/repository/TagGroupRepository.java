package com.fintrack.crm.repository;

import com.fintrack.crm.entity.TagGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagGroupRepository extends JpaRepository<TagGroupEntity, Long> {
}
