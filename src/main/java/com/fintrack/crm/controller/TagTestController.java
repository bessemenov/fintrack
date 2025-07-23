package com.fintrack.crm.controller;

import com.fintrack.crm.entity.TagEntity;
import com.fintrack.crm.entity.TagGroupEntity;
import com.fintrack.crm.service.ITagCacheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test/tags")
public class TagTestController {

    private final ITagCacheService tagCacheService;

    public TagTestController(ITagCacheService tagCacheService) {
        this.tagCacheService = tagCacheService;
    }

    @GetMapping
    public List<TagEntity> getTags() {
        return tagCacheService.getAllTags();
    }

    @GetMapping("/groups")
    public List<TagGroupEntity> getTagGroups() {
        return tagCacheService.getAllTagGroups();
    }
}
