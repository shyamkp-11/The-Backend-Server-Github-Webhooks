package com.shyampatel.webapp.githubplayroom.githubuserfcm;

import com.shyampatel.webapp.githubplayroom.githubuser.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubUserFcmService {

    private final GithubUserFcmRepository repository;

    @Autowired
    public GithubUserFcmService(GithubUserFcmRepository repository) {
        this.repository = repository;
    }

    public GithubUserFcm saveUser(GithubUserFcm userFcm) {
        return repository.save(userFcm);
    }
}
