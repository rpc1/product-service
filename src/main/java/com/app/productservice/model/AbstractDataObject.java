package com.app.productservice.model;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractDataObject {

    public AbstractDataObject(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public AbstractDataObject() {
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private String title;
    @Column(name = "is_deleted")
    private boolean deleted;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Id
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAdd) {
        this.createdAt = createdAdd;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
