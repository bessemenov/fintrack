package com.fintrack.crm.service.impl;

import com.fintrack.crm.entity.TagEntity;
import com.fintrack.crm.entity.TagGroupEntity;
import com.fintrack.crm.repository.TagRepository;
import com.fintrack.crm.repository.TagGroupRepository;
import com.fintrack.crm.service.ITagCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagCacheService implements ITagCacheService {

    private final TagRepository tagRepository;
    private final TagGroupRepository tagGroupRepository;

    public TagCacheService(TagRepository tagRepository, TagGroupRepository tagGroupRepository) {
        this.tagRepository = tagRepository;
        this.tagGroupRepository = tagGroupRepository;
    }

    @Override
    @Cacheable("tags")
    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    @Cacheable("tagGroups")
    public List<TagGroupEntity> getAllTagGroups() {
        return tagGroupRepository.findAll();
    }
}
