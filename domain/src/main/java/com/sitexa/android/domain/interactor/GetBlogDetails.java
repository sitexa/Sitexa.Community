package com.sitexa.android.domain.interactor;

import com.sitexa.android.domain.executor.PostExecutionThread;
import com.sitexa.android.domain.executor.ThreadExecutor;
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
    protected Observable buildUseCaseObservable() {
        return this.blogRepository.blog(this.blogId);
    }
}
