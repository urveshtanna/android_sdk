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

import com.tapglue.android.entities.User;
import com.tapglue.android.http.TapglueError;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class LoginIntegrationTest extends ApplicationTestCase<Application> {

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "loginIntegrationTestUser1";
    private static final String EMAIL_1 = "loginIntegrationTestUser1@email.com";

    Configuration configuration;
    Tapglue tapglue;

    public LoginIntegrationTest() {
        super(Application.class);
        configuration = new Configuration(TestData.URL, TestData.TOKEN);
        configuration.setLogging(true);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        tapglue = new Tapglue(configuration, getContext());
        User user = new User(USER_1, PASSWORD);
        user.setEmail(EMAIL_1);

        tapglue.createUser(user);
    }

    @Override
    protected void tearDown() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.deleteCurrentUser();
        super.tearDown();
    }

    public void testLoginWithUsername() throws Throwable {
        User user = tapglue.loginWithUsername(USER_1, PASSWORD);

        assertThat(user.getEmail(), equalTo(EMAIL_1));
    }

    public void testLoginWithEmail() throws Throwable {
        User user = tapglue.loginWithEmail("john@text.com", PasswordHasher.hashPassword("qwert"));

        assertThat(user.getEmail(), equalTo("john@text.com"));
    }


    public void testLoginWithWrongCredentials() throws IOException {
        try {
            tapglue.loginWithUsername("johnnnn12315233212", PasswordHasher.hashPassword("qwert"));
            fail("did not throw TapglueError on wrong credentials");
        } catch(TapglueError e) {

        }
    }

    public void testGetCurrentUserAfterLogin() throws IOException {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        User user = tapglue.getCurrentUser();

        assertThat(user.getEmail(), equalTo(EMAIL_1));
    }

    public void testGetCurrentUserAfterLogout() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));
        tapglue.logout();
        User user = tapglue.getCurrentUser();

        assertThat(user, nullValue());
    }

    public void testCreateAndDeleteUser() throws IOException {
        User user = new User("newUser", PASSWORD);
        User createdUser = tapglue.createUser(user);

        assertThat(createdUser.getUserName(), equalTo("newUser"));

        tapglue.loginWithUsername("newUser", PASSWORD);

        tapglue.deleteCurrentUser();

        assertThat(tapglue.getCurrentUser(), nullValue());
    }

    public void testUpdateUser() throws IOException {
        User user = new User("updateUserTest", PASSWORD);
        User createdUser = tapglue.createUser(user);

        User loggedInUser = tapglue.loginWithUsername("updateUserTest", PASSWORD);

        loggedInUser.setEmail("some@email.com");

        User updatedUser = tapglue.updateCurrentUser(loggedInUser);

        assertThat(updatedUser.getEmail(), equalTo("some@email.com"));

        tapglue.deleteCurrentUser();

        assertThat(tapglue.getCurrentUser(), nullValue());
    }
}
