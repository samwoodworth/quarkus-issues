package org.quarkus.issues.entity;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Issue extends PanacheEntity {
    
    public String issue;
    public String creatorName;

    public Issue() {}

    public Issue(String issue, String creatorName) {
        this.issue = issue;
        this.creatorName = creatorName;
    }
}
