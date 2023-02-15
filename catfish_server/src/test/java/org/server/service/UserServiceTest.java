package org.server.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.server.ServerSession;
import org.server.entity.User;
import org.server.entity.UserProfile;

import javax.persistence.Persistence;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {


    private static final ServerSession session = new ServerSession(
            Persistence.createEntityManagerFactory("catfish_h2_test"));

    private UserService service = new UserService(session);

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
    public void testFindUserById(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);

        jsonObject = service.findUserById(jsonObject);

        Assertions.assertEquals("query executed!", jsonObject.getString("status"));

        Assertions.assertEquals("User",
                jsonObject.getJSONObject("user").getString("name"));
        Assertions.assertEquals("user",
                jsonObject.getJSONObject("user").getString("login"));
        Assertions.assertEquals("email@gmail.com",
                jsonObject.getJSONObject("user").getString("email"));
        Assertions.assertEquals("password123",
                jsonObject.getJSONObject("user").getString("password"));
    }

    @Test
    @Order(2)
    public void testFindUserByEmail(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "email@gmail.com");

        jsonObject = service.findUserByEmail(jsonObject);

        Assertions.assertEquals("query executed!", jsonObject.getString("status"));

        Assertions.assertEquals("User",
                jsonObject.getJSONObject("user").getString("name"));
        Assertions.assertEquals("user",
                jsonObject.getJSONObject("user").getString("login"));
        Assertions.assertEquals("email@gmail.com",
                jsonObject.getJSONObject("user").getString("email"));
        Assertions.assertEquals("password123",
                jsonObject.getJSONObject("user").getString("password"));
    }

    @Test
    @Order(3)
    public void testFindUserByLogin(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", "user");

        jsonObject = service.findUserByLogin(jsonObject);
        Assertions.assertEquals("query executed!", jsonObject.getString("status"));

        Assertions.assertEquals("User",
                jsonObject.getJSONObject("user").getString("name"));
        Assertions.assertEquals("user",
                jsonObject.getJSONObject("user").getString("login"));
        Assertions.assertEquals("email@gmail.com",
                jsonObject.getJSONObject("user").getString("email"));
        JSONObject finalJsonObject = jsonObject;
        Assertions.assertThrows(JSONException.class,
                () -> finalJsonObject.getJSONObject("user").getString("password"));
    }

    @Test
    @Order(4)
    public void testCreateUser(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "NewUser");
        jsonObject.put("login", "newUser");
        jsonObject.put("email", "newEmail@gmail.com");
        jsonObject.put("password", "password123");

        Assertions.assertEquals("query executed!", service.createUser(jsonObject).getString("status"));

        jsonObject = service.findUserByEmail(jsonObject);

        Assertions.assertEquals("query executed!", jsonObject.getString("status"));

        Assertions.assertEquals("NewUser",
                jsonObject.getJSONObject("user").getString("name"));
        Assertions.assertEquals("newUser",
                jsonObject.getJSONObject("user").getString("login"));
        Assertions.assertEquals("newEmail@gmail.com",
                jsonObject.getJSONObject("user").getString("email"));
        Assertions.assertEquals("password123",
                jsonObject.getJSONObject("user").getString("password"));
    }

    @Test
    @Order(5)
    public void testUpdateUser(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "NewUpdateUser");
        jsonObject.put("login", "newUpdateUser");
        jsonObject.put("email", "newEmail@gmail.com");
        jsonObject.put("password", "pass222word123");

        Assertions.assertEquals("query executed!", service.updateUser(jsonObject).getString("status"));

        jsonObject = service.findUserByEmail(jsonObject);

        Assertions.assertEquals("query executed!", jsonObject.getString("status"));

        Assertions.assertEquals("NewUpdateUser",
                jsonObject.getJSONObject("user").getString("name"));
        Assertions.assertEquals("newUpdateUser",
                jsonObject.getJSONObject("user").getString("login"));
        Assertions.assertEquals("newEmail@gmail.com",
                jsonObject.getJSONObject("user").getString("email"));
        Assertions.assertEquals("pass222word123",
                jsonObject.getJSONObject("user").getString("password"));
    }

    @Test
    @Order(6)
    public void testDeleteUser(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 2);

        service.deleteUser(jsonObject);
        Assertions.assertEquals("The user not found.", service.findUserById(jsonObject).getString("status"));

    }
}
