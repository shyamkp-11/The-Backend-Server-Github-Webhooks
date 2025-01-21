package com.shyampatel.webapp.githubplayroom.githubuserfcm;

import com.shyampatel.webapp.githubplayroom.githubuser.GithubUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder()
@NoArgsConstructor
@Table(name="github_users_fcm", uniqueConstraints = @UniqueConstraint(columnNames={"device_id", "github_user_id"}))
@EntityListeners(AuditingEntityListener.class)
public class GithubUserFcm {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "github_user_id")
    private GithubUser githubUser;

    @Column(name="device_id", length = 128, nullable = false)
    private String deviceId;

    @Column(name="fcm_token", length = 256, nullable = false)
    private String fcmToken;

    @Column(name="enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "enabled_updated_at", insertable = false)
    private Instant enabledUpdatedAt;

    @Column(name = "message_deliver_failure_count", nullable = false)
    private Integer messageDeliverFailureCount = 0;

    @Column(name = "signOut", nullable = false)
    private Boolean signedOut;

    @Column(name = "signed_out_at")
    private Instant signedOutAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column (name = "created_by", updatable = false, nullable = false)
    private Integer createdBy;

    // The @Builder doesn't respect the default value if it's not set during builder generation and will make it null with the @AllArgsConstructor.
    public GithubUserFcm(Long id, GithubUser githubUser, String deviceId, String fcmToken, Boolean enabled, Instant enabledUpdatedAt, Integer messageDeliverFailureCount, Boolean signedOut, Instant signedOutAt, LocalDateTime createdAt, Integer createdBy) {
        this.id = id;
        this.githubUser = githubUser;
        this.deviceId = deviceId;
        this.fcmToken = fcmToken;
        this.enabled = enabled;
        this.enabledUpdatedAt = enabledUpdatedAt;
        this.messageDeliverFailureCount =  Objects.requireNonNullElseGet(messageDeliverFailureCount, () -> this.messageDeliverFailureCount);
        this.signedOut = signedOut;
        this.signedOutAt = signedOutAt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}








