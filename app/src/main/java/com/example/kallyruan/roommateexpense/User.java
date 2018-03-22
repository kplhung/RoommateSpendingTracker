package com.example.kallyruan.roommateexpense;

import java.util.ArrayList;

/**
 * Created by Lily on 2/22/2018.
 */

public class User {
    private String username;
    private ArrayList<Group> groups;
    public User(String username){
        this.username = username;
        this.groups = new ArrayList<Group>();
    }
    public String getUsername(){
        return username;
    }

    public ArrayList<Group> getGroups() { return groups; }

    public void addGroup(Group group) { this.groups.add(group); }
}
