package org.quarkus.issues.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
public class Issue extends PanacheEntity {
    
    public String issue;
    public String creatorName;

}
