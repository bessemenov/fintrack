package com.fintrack.crm.repository;

import com.fintrack.crm.entity.TagEntity;
import com.fintrack.crm.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByTagName(String tagName);

    List<TagEntity> findAllByType(TransactionType type);
}


