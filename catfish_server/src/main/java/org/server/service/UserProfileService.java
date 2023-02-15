package org.server.service;

import org.application.dto.UserProfileDto;
import org.json.JSONObject;
import org.server.Server;
import org.server.ServerSession;
import org.server.dao.UserProfileDao;

public class UserProfileService {


    private final UserProfileDao userProfileDao;

    public UserProfileService(){
        userProfileDao = new UserProfileDao(Server.getSession());
    }

    public UserProfileService(ServerSession session){
        userProfileDao = new UserProfileDao(session);
    }

    public JSONObject update(JSONObject jsonObject){
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUserId(jsonObject.getInt("userId"));
        userProfileDto.setDescription(jsonObject.getString("description"));

        jsonObject = new JSONObject();
        try {
            userProfileDao.update(userProfileDto);
            jsonObject.put("status", "query executed!");
        } catch (RuntimeException e) {
            jsonObject.put("status", e.getMessage());
        }
        return jsonObject;
    }
}
