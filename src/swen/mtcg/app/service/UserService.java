package swen.mtcg.app.service;

import swen.mtcg.app.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

        private Map<String, User> loggedinUser = new HashMap<>();

        public void addUser(User user){
                loggedinUser.put(user.getUsername(), user);
                System.out.println(loggedinUser.toString());
        }

        public Boolean checkUserLoggedIn(String username){

                if (loggedinUser.containsKey(username)) {
                        return true;
                }

                return false;
        }

        public User getUser(String username){
                return loggedinUser.get(username);
        }

        public void updateUser(User user){
                loggedinUser.replace(user.getUsername(), user);
        }

}
