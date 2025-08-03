package com.fintrack.crm.service.impl;

import com.fintrack.crm.dto.TagGroupDTO;
import com.fintrack.crm.dto.TagListDTO;
import com.fintrack.crm.enums.TransactionType;
import com.fintrack.crm.repository.TagGroupRepository;
import com.fintrack.crm.repository.TagRepository;
import com.fintrack.crm.service.ITagService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {

    private final TagRepository tagRepository;
    private final TagGroupRepository tagGroupRepository;

    @Override
    public List<TagListDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagListDTO(tag.getId(), tag.getTagName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagGroupDTO> getTagsGroupedByGroup() {
        return tagGroupRepository.findAll().stream()
                .map(group -> new TagGroupDTO(
                        group.getTagGroupName(),
                        group.getTags().stream()
                                .map(tag -> new TagListDTO(tag.getId(), tag.getTagName()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagListDTO> getAllTagsByType(TransactionType type) {
        return tagRepository.findAll().stream()
                .filter(tag -> tag.getType() == type)
                .map(tag -> new TagListDTO(tag.getId(), tag.getTagName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagListDTO> getTags(TransactionType type) {
        if (type != null) {
            return getAllTagsByType(type);
        } else {
            return getAllTags();
        }
    }
}



