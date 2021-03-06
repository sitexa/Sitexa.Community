/*
 *   Copyright (C) 2015 Sitexa Open Source Project
 *   <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.sitexa.android.community.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sitexa.android.community.exception.ErrorMessageFactory;
import com.sitexa.android.community.internal.di.PerActivity;
import com.sitexa.android.community.mapper.UserModelDataMapper;
import com.sitexa.android.community.model.UserModel;
import com.sitexa.android.community.view.UserListView;
import com.sitexa.android.domain.User;
import com.sitexa.android.domain.exception.DefaultErrorBundle;
import com.sitexa.android.domain.exception.ErrorBundle;
import com.sitexa.android.domain.interactor.DefaultSubscriber;
import com.sitexa.android.domain.interactor.UseCase;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class UserListPresenter implements Presenter {

    private final static String TAG = UserListPresenter.class.getSimpleName();
    private final UseCase getUserListUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private UserListView viewListView;

    @Inject
    public UserListPresenter(@Named("userList") UseCase getUserListUserCase, UserModelDataMapper userModelDataMapper) {
        this.getUserListUseCase = getUserListUserCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    public void setView(@NonNull UserListView view) {
        this.viewListView = view;
    }

    public void initialize() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserListUseCase.execute(new UserListSubscriber());
    }


    //////////Presenter//////////
    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.getUserListUseCase.unsubscribe();
    }

    //////////for View//////////
    private void showViewLoading() {
        this.viewListView.showLoading();
    }

    private void hideViewLoading() {
        this.viewListView.hideLoading();
    }

    private void showViewRetry() {
        this.viewListView.showRetry();
    }

    private void hideViewRetry() {
        this.viewListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        Context context = this.viewListView.getContext();
        String errorMessage = ErrorMessageFactory.create(context, errorBundle.getException());
        this.viewListView.showError(errorMessage);
    }

    private void showUsersCollectionInView(Collection<User> usersCollection) {
        final Collection<UserModel> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);
        this.viewListView.renderUserList(userModelsCollection);
    }

    //////////Subscriber//////////
    private final class UserListSubscriber extends DefaultSubscriber<List<User>> {

        @Override
        public void onCompleted() {
            UserListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            UserListPresenter.this.hideViewLoading();
            UserListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            UserListPresenter.this.showViewRetry();
        }

        @Override
        public void onNext(List<User> users) {
            UserListPresenter.this.showUsersCollectionInView(users);
        }
    }
}
