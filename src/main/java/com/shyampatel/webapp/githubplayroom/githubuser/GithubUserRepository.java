package com.shyampatel.webapp.githubplayroom.githubuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="githubUsers")
public interface GithubUserRepository extends JpaRepository<GithubUser, String> {

    GithubUser getGithubUserByGlobalId(String globalId);
}