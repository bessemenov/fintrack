package com.fintrack.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tag_group", schema = "parameter")
public class TagGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_group_name", nullable = false)
    private String tagGroupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    public Long getId() {
        return id;
    }

    public String getTagGroupName() {
        return tagGroupName;
    }

    public TagEntity getTag() {
        return tag;
    }
}
