package com.fintrack.crm.service;

import com.fintrack.crm.entity.TagEntity;
import com.fintrack.crm.entity.TagGroupEntity;

import java.util.List;

public interface ITagCacheService {
    List<TagEntity> getAllTags();
    List<TagGroupEntity> getAllTagGroups();
}
