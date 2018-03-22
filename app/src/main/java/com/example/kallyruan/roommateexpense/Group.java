package com.example.kallyruan.roommateexpense;

import java.util.ArrayList;

/**
 * Created by Lily on 2/22/2018.
 */

public class Group {
    private String code;
    private ArrayList<User> users;

    public Group(String code, User user){
        this.code = code;
        this.users = new ArrayList<User>();
        this.users.add(user);
    }

    public String getCode(){
        return code;
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public void addUser(User user){
        this.users.add(user);
    }
}
