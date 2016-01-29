/*
 * Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;
import com.tapglue.utils.TGPasswordHasher;

import java.util.HashMap;
import java.util.Map;

public class TGUser extends TGLoginUser<TGUser> {

    @Expose
    @SerializedName("images")
    HashMap<String, TGImage> mImages;

    @Expose
    @SerializedName("is_followed")
    private boolean isFollowed;

    @Expose
    @SerializedName("is_follower")
    private boolean isFollower;

    @Expose
    @SerializedName("is_friend")
    private boolean isFriend;

    @Expose
    @SerializedName("activated")
    private Boolean mActivated;

    @Expose
    @SerializedName("enabled")
    private Boolean mEnabled;

    @Expose
    @SerializedName("first_name")
    private String mFirstName;

    @Expose
    @SerializedName("last_login")
    private String mLastLogin;

    @Expose
    @SerializedName("last_name")
    private String mLastName;

    @Expose
    @SerializedName("custom_id")
    private String mLocalId;

    @Expose
    @SerializedName("session_token")
    private String mSessionToken;

    @Expose
    @SerializedName("social_ids")
    private Map<String, String> mSocialIds;

    @Expose
    @SerializedName("url")
    private String mUrl;

    /**
     * This constructor should be used only by automated processes, not by developers
     */
    public TGUser() {
        super(null, null, "");
        mCacheObjectType = TGCustomCacheObject.TGCacheObjectType.User.toCode();
    }

    public TGUser(String userName, String email, @NonNull String password) {
        super(userName, email, TGPasswordHasher.hashPassword(password));
        mCacheObjectType = TGCustomCacheObject.TGCacheObjectType.User.toCode();
    }

    /**
     * Is user activated?
     *
     * @return Is user activated?
     */
    public Boolean getActivated() {
        return mActivated;
    }

    /**
     * Get user custom ID
     *
     * @return custom ID
     */
    public String getCustomId() {
        return mLocalId;
    }

    /**
     * Set user custom ID
     *
     * @param mLocalId new custom ID value
     *
     * @return User
     */
    @NonNull
    public TGUser setCustomId(String mLocalId) {
        this.mLocalId = mLocalId;
        return this;
    }

    /**
     * Is user enabled?
     *
     * @return is user enabled?
     */
    public Boolean getEnabled() {
        return mEnabled;
    }

    /**
     * Get user first name
     *
     * @return user first name
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Set user first name
     *
     * @param mFirstName new first name value
     *
     * @return User
     */
    @NonNull
    public TGUser setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
        return this;
    }

    /**
     * Get user images
     *
     * @return user images
     */
    public HashMap<String, TGImage> getImages() {
        return mImages;
    }

    /**
     * Set user images
     *
     * @param mImages new images value
     *
     * @return User
     */
    @NonNull
    public TGUser setImages(HashMap<String, TGImage> mImages) {
        this.mImages = mImages;
        return this;
    }

    /**
     * Get last login date taken from server
     *
     * @return Last login date
     */
    public String getLastLogin() {
        return mLastLogin;
    }

    /**
     * Get user lasts name
     *
     * @return last name
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Set user last name
     *
     * @param mLastName new user last name
     *
     * @return User
     */
    @NonNull
    public TGUser setLastName(String mLastName) {
        this.mLastName = mLastName;
        return this;
    }

    /**
     * Get session token
     *
     * @return session token
     */
    public String getSessionToken() {
        return mSessionToken;
    }

    /**
     * Get social networking ids
     *
     * @return List of social ids
     */
    public Map<String, String> getSocialIds() {
        return mSocialIds;
    }

    /**
     * Set social networking ids
     *
     * @param mSocialIds
     *
     * @return User
     */
    @NonNull
    public TGUser setSocialIds(Map<String, String> mSocialIds) {
        this.mSocialIds = mSocialIds;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Set user url
     *
     * @param mUrl new url value
     *
     * @return
     */
    @NonNull
    public TGUser setUrl(String mUrl) {
        this.mUrl = mUrl;
        return this;
    }

    /**
     * Information if user is followed by current user
     *
     * @return Is user followed by current user?
     */
    public boolean isFollowed() {
        return isFollowed;
    }

    /**
     * Information if user if follower of current user
     *
     * @return Is user follower of current user?
     */
    public boolean isFollower() {
        return isFollower;
    }

    /**
     * Information if user is friend of current user
     *
     * @return Is user friend of current user?
     */
    public boolean isFriend() {
        return isFriend;
    }

    /**
     * Set user email
     *
     * @param mEmail new email value
     *
     * @return User
     */
    @NonNull
    public TGUser setEmail(String mEmail) {
        this.mEmail = mEmail;
        return this;
    }

    /**
     * Set user followed status.
     * This won't change anything on server, this is for app UI usage only
     *
     * @param isFollowed Is user followed by current user?
     *
     * @return User
     */
    @NonNull
    public TGUser setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
        return this;
    }

    /**
     * Set user friend status.
     * This won't change anything on server, this is for app UI usage only
     *
     * @param isFriend Is user followed by current user?
     *
     * @return User
     */
    @NonNull
    public TGUser setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
        return this;
    }

    /**
     * Set user password.
     * It will be hashed automatically
     *
     * @param mPassword new password value
     *
     * @return User
     */
    @NonNull
    public TGUser setPassword(@NonNull String mPassword) {
        this.mPassword = TGPasswordHasher.hashPassword(mPassword);
        return this;
    }

    /**
     * Set user password.
     * It won't be hashed.
     *
     * @param mPassword new password value
     *
     * @return User
     */
    @NonNull
    public TGUser setUnhashedPassword(@NonNull String mPassword) {
        this.mPassword = mPassword;
        return this;
    }

    /**
     * Set username
     *
     * @param mUserName new username value
     *
     * @return User
     */
    @NonNull
    public TGUser setUserName(String mUserName) {
        this.mUserName = mUserName;
        return this;
    }
}
