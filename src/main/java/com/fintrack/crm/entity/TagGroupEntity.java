package com.fintrack.crm.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tag_group", schema = "parameter")
public class TagGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_group_name", nullable = false)
    private String tagGroupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TagEntity> tags;

    public TagGroupEntity() {}

    public TagGroupEntity(Long id, String tagGroupName, List<TagEntity> tags) {
        this.id = id;
        this.tagGroupName = tagGroupName;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getTagGroupName() {
        return tagGroupName;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTagGroupName(String tagGroupName) {
        this.tagGroupName = tagGroupName;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagGroupEntity)) return false;
        TagGroupEntity that = (TagGroupEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tagGroupName, that.tagGroupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagGroupName);
    }

    @Override
    public String toString() {
        return "TagGroupEntity{" +
                "id=" + id +
                ", tagGroupName='" + tagGroupName + '\'' +
                '}';
    }
}


