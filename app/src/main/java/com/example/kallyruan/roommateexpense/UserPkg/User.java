package com.example.kallyruan.roommateexpense.UserPkg;

import java.util.ArrayList;
import java.sql.ResultSet;
import android.database.SQLException;

import com.example.kallyruan.roommateexpense.DB.DBQueries;
import com.example.kallyruan.roommateexpense.GroupPkg.Group;

/**
 * Created by Lily on 2/22/2018.
 */

public class User {
    private String username;
    private ArrayList<Group> groups;
    private String userIcon;
    private String nickname;

    public User(String username){
        this.username = username;
        this.groups = new ArrayList<Group>();
    }

    public User(String username, ArrayList<Group> groups,String nickname,String userIcon){
        this.username = username;
        this.groups=groups;
        this.nickname=nickname;
        this.userIcon=userIcon;
    }

    //this function is to get all user group and user information, then return
    public static User getInstance(String username){
            //get user information from database
            ArrayList<Group> allgroups = new ArrayList<Group>();
            String userEmail = LoginActivity.email;
            String userIcon=null;
            String nickname=null;
            // try to connect dababase
            try {
                DBQueries db = DBQueries.getInstance();
                ResultSet rs = db.userGroups(userEmail);
                // get all group information and add to ArrayList<Group>
                try {
                    while (rs.next()) {
                        String code = rs.getString("group_id");
                        String name = db.getGroupName(code);
                        int participation = db.groupParticipation(code);
                        String alert = "Not implemented yet";
                        allgroups.add(new Group(code, name, participation, alert));
                    }
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                //get userIcon and nickname
                nickname=db.getNickname(username);
                userIcon=db.getIcon(username);

            }catch(SQLException e) {
                    e.printStackTrace();
            }
            return new User(username,allgroups,nickname,userIcon);
    }

    public String getUsername(){
        return username;
    }

    public ArrayList<Group> getGroups() { return groups; }

    public void addGroup(Group group) { this.groups.add(group); }

    public String getNickname(){ return nickname; }
    public String getUserIcon(){ return userIcon; }

    // this function is to return the selected group with index n
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
