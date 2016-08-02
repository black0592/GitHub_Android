package com.zpauly.githubapp.presenter.files;

import android.content.Context;

import com.zpauly.githubapp.db.FileDirDao;
import com.zpauly.githubapp.db.FileDirModel;
import com.zpauly.githubapp.entity.response.RepositoryContentBean;
import com.zpauly.githubapp.network.repositories.RepositoriesMethod;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zpauly on 16-8-1.
 */

public class FilesPresenter implements FilesContract.Presenter {
    private Context mContext;

    private FilesContract.View mFilesView;

    private Subscriber<List<RepositoryContentBean>> contentSubscriber;
    private Subscriber<String> fileSubscriber;
    private Observer<List<FileDirModel>> observer;

    private RepositoriesMethod method;

    public FilesPresenter(Context context, FilesContract.View view) {
        mContext = context;
        mFilesView = view;
        mFilesView.setPresenter(this);
        start();
    }

    @Override
    public void start() {
        method = RepositoriesMethod.getInstance();
    }

    @Override
    public void stop() {
        FileDirDao.delete();
        if (contentSubscriber != null) {
            if (contentSubscriber.isUnsubscribed()) {
                contentSubscriber.unsubscribe();
            }
        }
    }

    @Override
    public void loadContent(String owner, String repo, String path) {
        contentSubscriber = new Subscriber<List<RepositoryContentBean>>() {
            @Override
            public void onCompleted() {
                mFilesView.loadContentSuccess();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mFilesView.loadContentFail();
            }

            @Override
            public void onNext(List<RepositoryContentBean> beanList) {
                mFilesView.loadingContent(beanList);
            }
        };
        method.getRepositoryContent(contentSubscriber, null, owner, repo, path);
    }

    @Override
    public void getContentFromCache(final String path) {
        observer = new Observer<List<FileDirModel>>() {
            @Override
            public void onCompleted() {
                mFilesView.getContentSuccess();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mFilesView.getContentFail();
            }

            @Override
            public void onNext(List<FileDirModel> fileDirModels) {
                mFilesView.gettingContent(fileDirModels);
            }
        };
        Observable.create(new Observable.OnSubscribe<List<FileDirModel>>() {
            @Override
            public void call(Subscriber<? super List<FileDirModel>> subscriber) {
                subscriber.onNext(FileDirDao.query("parentPath", path));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadFile(String owner, String repo, String path) {
        fileSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mFilesView.loadFileSuccess();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mFilesView.loadFileFail();
            }

            @Override
            public void onNext(String s) {
                mFilesView.loadingFile(s);
            }
        };
        method.getFileContent(fileSubscriber, "application/vnd.github.VERSION.raw",
                owner, repo, path);
    }
}