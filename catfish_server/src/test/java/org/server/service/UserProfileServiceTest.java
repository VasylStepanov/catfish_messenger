package org.server.service;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.server.ServerSession;
import org.server.entity.User;
import org.server.entity.UserProfile;

import javax.persistence.Persistence;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserProfileServiceTest {
    private static final ServerSession session = new ServerSession(
            Persistence.createEntityManagerFactory("catfish_h2_test"));

    private UserService userService = new UserService(session);
    private UserProfileService userProfileService = new UserProfileService(session);

    @BeforeAll
    public static void setUp(){
        session.doInSession(
            entityManager -> {
                User user = User.builder()
                    .name("User").login("user").email("email@gmail.com").password("password123")
                    .build();
                user.setUserProfile(new UserProfile());
                entityManager.persist(user);
            }
        );
    }

    @Test
    @Order(1)
    public void testUpdate(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1);
        jsonObject.put("description", "test description");

        jsonObject = userProfileService.update(jsonObject);
        Assertions.assertEquals("query executed!", jsonObject.getString("status"));
    }

    @Test
    @Order(2)
    public void testGetDescription(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);

        jsonObject = userService.findUserById(jsonObject);
        Assertions.assertEquals("test description",
                jsonObject.getJSONObject("userProfile").getString("description"));
    }
}
