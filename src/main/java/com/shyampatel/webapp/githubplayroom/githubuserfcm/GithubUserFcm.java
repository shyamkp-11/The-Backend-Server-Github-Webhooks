package com.shyampatel.webapp.githubplayroom.githubuserfcm;

import com.shyampatel.webapp.githubplayroom.githubuser.GithubUser;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="github_users_fcm", uniqueConstraints = @UniqueConstraint(columnNames={"device_id", "github_user_id"}))
@EntityListeners(AuditingEntityListener.class)
public class GithubUserFcm {

    public GithubUserFcm() {
    }



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

    // The Lombok @Builder doesn't respect the default value if it's not set during builder generation and will make it null with the @AllArgsConstructor.
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GithubUser getGithubUser() {
        return githubUser;
    }

    public void setGithubUser(GithubUser githubUser) {
        this.githubUser = githubUser;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getEnabledUpdatedAt() {
        return enabledUpdatedAt;
    }

    public void setEnabledUpdatedAt(Instant enabledUpdatedAt) {
        this.enabledUpdatedAt = enabledUpdatedAt;
    }

    public Integer getMessageDeliverFailureCount() {
        return messageDeliverFailureCount;
    }

    public void setMessageDeliverFailureCount(Integer messageDeliverFailureCount) {
        this.messageDeliverFailureCount = messageDeliverFailureCount;
    }

    public Boolean getSignedOut() {
        return signedOut;
    }

    public void setSignedOut(Boolean signedOut) {
        this.signedOut = signedOut;
    }

    public Instant getSignedOutAt() {
        return signedOutAt;
    }

    public void setSignedOutAt(Instant signedOutAt) {
        this.signedOutAt = signedOutAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GithubUserFcm that = (GithubUserFcm) o;
        return Objects.equals(id, that.id) && Objects.equals(githubUser, that.githubUser) && Objects.equals(deviceId, that.deviceId) && Objects.equals(fcmToken, that.fcmToken) && Objects.equals(enabled, that.enabled) && Objects.equals(enabledUpdatedAt, that.enabledUpdatedAt) && Objects.equals(messageDeliverFailureCount, that.messageDeliverFailureCount) && Objects.equals(signedOut, that.signedOut) && Objects.equals(signedOutAt, that.signedOutAt) && Objects.equals(createdAt, that.createdAt) && Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, githubUser, deviceId, fcmToken, enabled, enabledUpdatedAt, messageDeliverFailureCount, signedOut, signedOutAt, createdAt, createdBy);
    }

    @Override
    public String toString() {
        return "GithubUserFcm{" +
                "id=" + id +
                ", githubUser=" + githubUser +
                ", deviceId='" + deviceId + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", enabled=" + enabled +
                ", enabledUpdatedAt=" + enabledUpdatedAt +
                ", messageDeliverFailureCount=" + messageDeliverFailureCount +
                ", signedOut=" + signedOut +
                ", signedOutAt=" + signedOutAt +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                '}';
    }

    public static class Builder {

        private Long id;
        private GithubUser githubUser;
        private String deviceId;
        private String fcmToken;
        private Boolean enabled;
        private Instant enabledUpdatedAt;
        private Integer messageDeliverFailureCount;
        private Boolean signedOut;
        private Instant signedOutAt;
        private LocalDateTime createdAt;
        private Integer createdBy;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setGithubUser(GithubUser githubUser) {
            this.githubUser = githubUser;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
            return this;
        }

        public Builder setEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setEnabledUpdatedAt(Instant enabledUpdatedAt) {
            this.enabledUpdatedAt = enabledUpdatedAt;
            return this;
        }

        public Builder setMessageDeliverFailureCount(Integer messageDeliverFailureCount) {
            this.messageDeliverFailureCount = messageDeliverFailureCount;
            return this;
        }

        public Builder setSignedOut(Boolean signedOut) {
            this.signedOut = signedOut;
            return this;
        }

        public Builder setSignedOutAt(Instant signedOutAt) {
            this.signedOutAt = signedOutAt;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public GithubUserFcm build() {
            return new GithubUserFcm(id, githubUser, deviceId, fcmToken, enabled, enabledUpdatedAt, messageDeliverFailureCount, signedOut, signedOutAt, createdAt, createdBy);
        }
    }
}








