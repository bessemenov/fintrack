package com.fintrack.crm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tag", schema = "parameter")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    public Long getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }
}
