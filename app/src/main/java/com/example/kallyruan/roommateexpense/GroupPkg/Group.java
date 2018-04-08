package com.example.kallyruan.roommateexpense.GroupPkg;

import com.example.kallyruan.roommateexpense.UserPkg.User;

import java.util.ArrayList;

/**
 * POJO to store Group info
 * Created by Lily on 2/22/2018.
 */

public class Group {
    private String code;
    private String name;
    private ArrayList<User> users;
    private int participation;
    private String alert;

    public Group(String code, User user){
        this.code = code;
        this.users = new ArrayList<User>();
        this.users.add(user);
    }

    public Group(String id, String name, int participation,String alert){
        this.code = id;
        this.name = name;
        this.participation = participation;
        this.alert = alert;
    }

    /**
     * Getter methods
     */
    public String getCode(){
        return code;
    }

    public int getParticipation(){
        return participation;
    }

    public String getName(){return name;}

    public ArrayList<User> getUsers(){
        return users;
    }

    /**
     * Adds user to group
     * @param user to be added
     */
    public void addUser(User user){
        this.users.add(user);
    }

    public String getAlert(){
        return alert;
    }
}