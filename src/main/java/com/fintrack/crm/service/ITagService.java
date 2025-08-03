package com.fintrack.crm.service;

import com.fintrack.crm.dto.TagListDTO;
import com.fintrack.crm.dto.TagGroupDTO;
import com.fintrack.crm.enums.TransactionType;

import java.util.List;

public interface ITagService {
    List<TagListDTO> getAllTags();
    List<TagGroupDTO> getTagsGroupedByGroup();
    List<TagListDTO> getAllTagsByType(TransactionType type);
    List<TagListDTO> getTags(TransactionType type);
}

