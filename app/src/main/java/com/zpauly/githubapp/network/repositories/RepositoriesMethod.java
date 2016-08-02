package com.zpauly.githubapp.network.repositories;

import android.support.annotation.Nullable;

import com.zpauly.githubapp.Api;
import com.zpauly.githubapp.entity.response.RepositoriesBean;
import com.zpauly.githubapp.entity.response.RepositoryContentBean;
import com.zpauly.githubapp.utils.RetrofitUtil;
import com.zpauly.githubapp.utils.StringConverterFactory;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zpauly on 16-7-14.
 */

public class RepositoriesMethod {
    private static RepositoriesMethod instance;

    private Retrofit retrofit;

    private RepositoriesService service;

    private RepositoriesMethod() {
        retrofit = RetrofitUtil.initRetrofit(Api.GitHubApi);
        service = retrofit.create(RepositoriesService.class);
    }

    public static RepositoriesMethod getInstance() {
        if (instance == null) {
            synchronized (RepositoriesBean.class) {
                if (instance == null) {
                    instance = new RepositoriesMethod();
                }
            }
        }
        return instance;
    }

    public void getOwendRepositories(Observer<List<RepositoriesBean>> observer, String auth
            , @Nullable List<String> affiliation, @Nullable String sort, int pageId) {
        service.getOwendRepositories(auth, affiliation, sort, pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getRepositories(Observer<List<RepositoriesBean>> observer, String username,
                                @Nullable List<String> affiliation, @Nullable String sort, int pageId) {
        service.getRepositories(username, affiliation, sort, pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getRepositoryContent(Observer<List<RepositoryContentBean>> observer, String acc,
                                     String owner, String repo, String path) {
        service.getRepositoryContent(acc, owner, repo, path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getFileContent(Observer<String> observer, String acc,
                               String owner, String repo, String path) {
        Retrofit retrofit = RetrofitUtil.initCustomRetrofit(Api.GitHubApi, StringConverterFactory.create(),
                RxJavaCallAdapterFactory.create());
        RepositoriesService service = retrofit.create(RepositoriesService.class);
        service.getFileContent(acc, owner, repo, path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getRepository(Observer<RepositoriesBean> observer, String username, String repo) {
        service.getRepository(username, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getReadMe(Observer<String> observer, String username, String repo) {
        Retrofit retrofit = RetrofitUtil.initCustomRetrofit(Api.GitHubApi, StringConverterFactory.create(),
                RxJavaCallAdapterFactory.create());
        RepositoriesService service = retrofit.create(RepositoriesService.class);
        service.getReadMe("application/vnd.github.VERSION.html", username, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
