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

package com.sitexa.android.domain.interactor.blog;

import com.sitexa.android.domain.executor.PostExecutionThread;
import com.sitexa.android.domain.executor.ThreadExecutor;
import com.sitexa.android.domain.interactor.UseCase;
import com.sitexa.android.domain.repository.BlogRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by xnpeng on 15-9-5.
 */
public class GetBlogDetails extends UseCase {

    private final long blogId;
    private final BlogRepository blogRepository;

    @Inject
    public GetBlogDetails(long blogId, BlogRepository blogRepository,
                          ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.blogId = blogId;
        this.blogRepository = blogRepository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object... param) {
        return this.blogRepository.blog(this.blogId);
    }
}
