package org.server.service;

import org.application.dto.UserDto;
import org.application.dto.UserProfileDto;
import org.json.JSONObject;
import org.server.Server;
import org.server.ServerSession;
import org.server.dao.UserDao;
import org.server.dao.UserProfileDao;
import org.server.entity.User;
import org.server.entity.UserProfile;

public class UserService {

    private final UserDao userDao;
    private final UserProfileDao userProfileDao;

    public UserService(){
        userDao = new UserDao(Server.getSession());
        userProfileDao = new UserProfileDao(Server.getSession());
    }

    public UserService(ServerSession session){
        userDao = new UserDao(session);
        userProfileDao = new UserProfileDao(session);
    }

    public JSONObject findUserById(JSONObject jsonObject){
        Integer id = jsonObject.getInt("id");

        jsonObject = new JSONObject();
        try {
            User userResult = userDao.readUser(id);
            if(userResult == null)
                throw new RuntimeException("The user not found.");
            UserProfile userProfileResult = userProfileDao.readUserProfile(userResult.getId());

            jsonObject.put("user", new JSONObject(
                    new UserDto(userResult.getName(), userResult.getLogin(), userResult.getEmail(), userResult.getPassword())));

            jsonObject.put("userProfile", new JSONObject(new UserProfileDto(userProfileResult.getDescription())));
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject findUserByEmail(JSONObject jsonObject){
        String email = jsonObject.getString("email");

        jsonObject = new JSONObject();
        try {
            User userResult = userDao.readUserByEmail(email);
            if(userResult == null)
                throw new RuntimeException("The user not found.");
            UserProfile userProfileResult = userProfileDao.readUserProfile(userResult.getId());

            jsonObject.put("user", new JSONObject(
                    new UserDto(userResult.getName(), userResult.getLogin(), userResult.getEmail(), userResult.getPassword())));

            jsonObject.put("userProfile", new JSONObject(new UserProfileDto(userProfileResult.getDescription())));
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject findUserByLogin(JSONObject jsonObject){
        String login = jsonObject.getString("login");

        jsonObject = new JSONObject();
        try {
            User userResult = userDao.readUserByLogin(login);
            if(userResult == null)
                throw new RuntimeException("The user not found.");
            UserProfile userProfileResult = userProfileDao.readUserProfile(userResult.getId());

            jsonObject.put("user", new JSONObject(
                    new UserDto(userResult.getName(), userResult.getLogin(), userResult.getEmail(), null)));

            jsonObject.put("userProfile", new JSONObject(new UserProfileDto(userProfileResult.getDescription())));
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject createUser(JSONObject jsonObject){
        UserDto userDto = new UserDto();
        userDto.setEmail(jsonObject.getString("email"));
        userDto.setName(jsonObject.getString("name"));
        userDto.setLogin(jsonObject.getString("login"));
        userDto.setPassword(jsonObject.getString("password"));

        jsonObject = new JSONObject();
        try {
            userDao.createUser(userDto);
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject updateUser(JSONObject jsonObject){
        UserDto userDto = new UserDto();
        userDto.setEmail(jsonObject.getString("email"));
        userDto.setName(jsonObject.getString("name"));
        userDto.setLogin(jsonObject.getString("login"));
        userDto.setPassword(jsonObject.getString("password"));

        jsonObject = new JSONObject();
        try {
            userDao.updateUser(userDto);
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject deleteUser(JSONObject jsonObject){
        Integer id = jsonObject.getInt("id");

        jsonObject = new JSONObject();
        try {
            userDao.deleteUser(id);
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }


}
