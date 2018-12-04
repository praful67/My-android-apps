package com.dev2.tracker.TrackYourFriends;

public class FriendId {
    public FriendId() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FriendId(String id, String name) {

        this.id = id;
        this.name = name;
    }

    String id , name;
}
