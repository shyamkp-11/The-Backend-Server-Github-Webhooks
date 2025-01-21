package com.shyampatel.webapp.githubplayroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @CreatedBy
    @Column (name = "created_by", updatable = false, nullable = false)
    protected Integer createdBy;

    @LastModifiedDate
    @Column (name = "updated_at", insertable = false)
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    @Column (name = "updated_by", insertable = false)
    protected Integer updatedBy;

}
