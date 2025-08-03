package com.fintrack.crm.entity;

import com.fintrack.crm.enums.TransactionType;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tag", schema = "parameter")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_group_id", nullable = false)
    private TagGroupEntity group;

    public TagEntity() {}

    public TagEntity(Long id, String tagName, TagGroupEntity group) {
        this.id = id;
        this.tagName = tagName;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public TagGroupEntity getGroup() {
        return group;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setGroup(TagGroupEntity group) {
        this.group = group;
    }

    public TransactionType getType() {return type;}

    public void setType(TransactionType type) {this.type = type;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity)) return false;
        TagEntity that = (TagEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tagName, that.tagName) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagName, group);
    }

    @Override
    public String toString() {
        return "TagEntity{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                ", group=" + (group != null ? group.getTagGroupName() : "null") +
                '}';
    }
}



