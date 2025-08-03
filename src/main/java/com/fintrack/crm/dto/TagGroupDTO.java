package com.fintrack.crm.dto;

import java.util.List;

public class TagGroupDTO {
    private String groupName;
    private List<TagListDTO> tags;

    public TagGroupDTO() {}

    public TagGroupDTO(String groupName, List<TagListDTO> tags) {
        this.groupName = groupName;
        this.tags = tags;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<TagListDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagListDTO> tags) {
        this.tags = tags;
    }
}
