package com.shyampatel.webapp.githubplayroom.githubuser;


import com.shyampatel.webapp.githubplayroom.entity.BaseEntity;
import com.shyampatel.webapp.githubplayroom.githubuserfcm.GithubUserFcm;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="github_users")
public class GithubUser extends BaseEntity {

    @Id
    @Column(name = "global_id")
    private String globalId;

    @Column(name="username")
    private String username;

    @Column(name="first_name", length = 45)
    private String firstName;

    @Column(name="last_name", length = 45)
    private String lastName;

    @Column(name="email", length = 64)
    private String email;

    @OneToMany(mappedBy = "githubUser", cascade = CascadeType.ALL)
    private List<GithubUserFcm> githubUserFcmS;

    // define constructors
    public GithubUser() {

    }

    public static GithubUser.Builder builder() {
        return new GithubUser.Builder();
    }

    public List<GithubUserFcm> getGithubUserFcmS() {
        return githubUserFcmS;
    }

    public void setGithubUserFcmS(List<GithubUserFcm> githubUserFcmS) {
        this.githubUserFcmS = githubUserFcmS;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GithubUser(String globalId, String username, String firstName, String lastName, String email, List<GithubUserFcm> githubUserFcmS) {
        this.globalId = globalId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.githubUserFcmS = githubUserFcmS;
    }



    @Override
    public String toString() {
        return "GithubUser{" +
                "email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", globalIf='" + globalId + '\'' +
                '}';
    }

    public static class Builder {
        private String globalId;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private List<GithubUserFcm> githubUserFcmS;

        public Builder setGlobalId(String globalId) {
            this.globalId = globalId;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setGithubUserFcmS(List<GithubUserFcm> githubUserFcmS) {
            this.githubUserFcmS = githubUserFcmS;
            return this;
        }

        public GithubUser build() {
            return new GithubUser(globalId, username, firstName, lastName, email, githubUserFcmS);
        }
    }

}








