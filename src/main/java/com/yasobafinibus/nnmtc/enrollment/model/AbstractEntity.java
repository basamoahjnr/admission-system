package com.yasobafinibus.nnmtc.enrollment.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AbstractEntityListener.class)
public class AbstractEntity {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    Integer id;


    @Basic(optional = false)
    @Column(name = "CREATED_ON",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdOn;


    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;

    @Version
    @Column(name = "VERSION", columnDefinition = "integer DEFAULT 0", nullable = false)
    private long version = 0L;

    public Integer getId() {
        return this.id;
    }

    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return this.updatedOn;
    }

    public long getVersion() {
        return this.version;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    private void setVersion(long version) {
        this.version = version;
    }
}