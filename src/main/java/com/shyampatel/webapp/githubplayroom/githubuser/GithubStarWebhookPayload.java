package com.shyampatel.webapp.githubplayroom.githubuser;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GithubStarWebhookPayload{
    public String action;
    public Date starred_at;
    public Repository repository;
    public User sender;
    private Map<String, Object> optional = new HashMap<>();
    @JsonAnySetter
    public void addOptional(String name, Object value) {
        optional.put(name, value);
    }
    @JsonAnyGetter
    public Object getOptional(String name) {
        return optional.get(name);
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Repository{
        public int id;
        public String node_id;
        public String name;
        public User owner;
        public Integer stargazers_count;
        private Map<String, Object> optional = new HashMap<>();
        @JsonAnySetter
        public void addOptional(String name, Object value) {
            optional.put(name, value);
        }
        @JsonAnyGetter
        public Object getOptional(String name) {
            return optional.get(name);
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class User{
        public String login;
        public String avatar_url;
        public Integer id;
        public String node_id;
        private Map<String, Object> optional = new HashMap<>();
        // getters/setters for all properties omitted for brevity
        @JsonAnySetter
        public void addOptional(String name, Object value) {
            optional.put(name, value);
        }
        @JsonAnyGetter
        public Object getOptional(String name) {
            return optional.get(name);
        }
    }

}

