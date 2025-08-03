package com.fintrack.crm.controller;

import com.fintrack.crm.dto.TagGroupDTO;
import com.fintrack.crm.dto.TagListDTO;
import com.fintrack.crm.enums.TransactionType;
import com.fintrack.crm.service.ITagService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @GetMapping("/tag-list")
    public ResponseEntity<List<TagListDTO>> getAllTags(
            @RequestParam(name = "type", required = false) TransactionType type) {
        return ResponseEntity.ok(tagService.getTags(type));
    }

    @GetMapping("/tag-group")
    public ResponseEntity<List<TagGroupDTO>> getGroupedTags() {
        return ResponseEntity.ok(tagService.getTagsGroupedByGroup());
    }

    @GetMapping("/tag-list-by-type")
    public ResponseEntity<List<TagListDTO>> getTagsByType(@RequestParam TransactionType type) {
        return ResponseEntity.ok(tagService.getAllTagsByType(type));
    }
}