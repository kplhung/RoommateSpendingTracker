package com.example.kallyruan.roommateexpense;

import java.util.ArrayList;
import java.sql.ResultSet;
import android.database.SQLException;
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
    public User(String username, ArrayList<Group> groups){
        this.username = username;
        this.groups=groups;
    }

    //Singleton pattern. If already has this instance, return it. Otherwise, create a new one.
    public static User getInstance(String username){
            //get user information from database
            ArrayList<Group> allgroups = new ArrayList<Group>();
            String userEmail = LoginActivity.email;
            // try to connect dababase
            try {
                DBQueries db = DBQueries.getInstance();
                ResultSet rs = db.userGroups(userEmail);
                // get all group information and add to ArrayList<Group>
                try {
                    while (rs.next()) {
                        System.out.println("User group information loading.");
                        String code = rs.getString("group_id");
                        String name = db.groupName(code);
                        int participation = db.groupParticipation(code);
                        String alert = "Not implemented yet";
                        allgroups.add(new Group(code, name, participation, alert));
                    }
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }catch(SQLException e) {
                    e.printStackTrace();
            }
/*
  //right now something wrong with handling database, hence would use temporary data
            //hard-code data
            ArrayList<Group> allgroups = new ArrayList<Group>();
            //before the db is set up, we will use hard-coded data
            Group group1 = new Group("100", "test",2,"not yet");
            Group group2 = new Group("101", "test1",3,"not yet");
            allgroups.add(group1);
            allgroups.add(group2);
*/
            return new User(username,allgroups);
    }
        
    public String getUsername(){
        return username;
    }

    public ArrayList<Group> getGroups() { return groups; }

    public void addGroup(Group group) { this.groups.add(group); }

    public Group getNthGroup(int index){
        int total = groups.size();
        if(index>=0 && index< total) {
            return groups.get(index);
        }else{
            System.out.println("group index out of range.");
        }
        return null;
    }
}
