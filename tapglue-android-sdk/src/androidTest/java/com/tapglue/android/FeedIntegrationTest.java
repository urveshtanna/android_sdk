/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Follow;
import com.tapglue.android.entities.NewsFeed;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class FeedIntegrationTest extends ApplicationTestCase<Application> {
    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "user13";
    private static final String USER_2 = "user23";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);
    List<Post.Attachment> attachments = new ArrayList<>();

    public FeedIntegrationTest() {
        super(Application.class);
        configuration = new Configuration(TestData.URL, TestData.TOKEN);
        configuration.setLogging(true);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        tapglue = new Tapglue(configuration, getContext());

        user1 = tapglue.createUser(user1);
        user2 = tapglue.createUser(user2);
    }

    @Override
    protected void tearDown() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.deleteCurrentUser();

        super.tearDown();
    }

    public void testRetrievePostFeed() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.createConnection(new Follow(user1));
        List<Post> postFeed = tapglue.retrievePostFeed();

        assertThat(postFeed, hasItems(post));
    }

    public void testRetrieveEventFeed() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.createConnection(new Follow(user2));

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        List<Event> events = tapglue.retrieveEventFeed();

        Event event = events.get(0);
        assertThat(event.getType(), equalTo("tg_follow"));
    }

    public void testRetrieveEventsByUser() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.createConnection(new Follow(user2));
        Post post = tapglue.createPost(new Post(attachments, Post.Visibility.PUBLIC));
        tapglue.createLike(post.getId());

        List<Event> events = tapglue.retrieveEventsByUser(user1.getId());

        Event event = events.get(0);
        assertThat(event.getType(), equalTo("tg_like"));
        tapglue.deletePost(post.getId());
    }

    public void testRetrieveEventFeedMapsPosts() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.createConnection(new Follow(user2));
        Post post = tapglue.createPost(new Post(attachments, Post.Visibility.CONNECTION));

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.createConnection(new Follow(user1));
        tapglue.createLike(post.getId());

        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        List<Event> events = tapglue.retrieveEventFeed();

        assertThat(events.get(0).getPost(), not(nullValue()));
        assertThat(events.get(0).getPost().getId(), equalTo(post.getId()));
    }

    public void testRetrieveNewsFeedParsesPosts()  throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.createConnection(new Follow(user1));
        NewsFeed feed = tapglue.retrieveNewsFeed();

        assertThat(feed.getPosts(), hasItems(post));

    }

    public void testRetrieveNewsFeedParsesEvents() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.createConnection(new Follow(user2));

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        NewsFeed feed = tapglue.retrieveNewsFeed();

        assertThat(feed.getEvents().get(0).getType(), equalTo("tg_follow"));
    }

    public void testRetrieveNewsFeedMapsPosts() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.createConnection(new Follow(user2));
        Post post = tapglue.createPost(new Post(attachments, Post.Visibility.CONNECTION));

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.createConnection(new Follow(user1));
        tapglue.createLike(post.getId());

        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        List<Event> events = tapglue.retrieveNewsFeed().getEvents();

        assertThat(events.get(0).getPost(), not(nullValue()));
        assertThat(events.get(0).getPost().getId(), equalTo(post.getId()));
    }

    public void testRetrieveMeFeed() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.createConnection(new Follow(user2));

        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        List<Event> events = tapglue.retrieveMeFeed();

        Event event = events.get(0);
        assertThat(event.getType(), equalTo("tg_follow"));
    }
}
