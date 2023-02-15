package org.server.dao;

import org.application.dto.UserDto;
import org.junit.jupiter.api.*;
import org.server.ServerSession;
import org.server.entity.User;
import org.server.entity.UserProfile;

import javax.persistence.Persistence;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTest {

    private static final ServerSession session = new ServerSession(
            Persistence.createEntityManagerFactory("catfish_h2_test"));

    private final UserDao userDao = new UserDao(session);

    @BeforeAll
    public static void setUp(){
        session.doInSession(entityManager -> {
            User user = User.builder()
                    .name("User").login("user").email("email@gmail.com").password("password123")
                    .build();
            user.setUserProfile(new UserProfile());
            entityManager.persist(user);
        });
    }

    @Test
    @Order(1)
    public void testCreateUser(){
        UserDto userDTO = new UserDto();
        userDTO.setEmail(null);
        userDTO.setName("User");
        userDTO.setLogin(null);
        userDTO.setPassword("password123");
        Assertions.assertThrows(RuntimeException.class, () -> userDao.createUser(userDTO));
        userDTO.setEmail("email@gmail.com");
        userDTO.setLogin("new_user");
        Assertions.assertThrows(RuntimeException.class, () -> userDao.createUser(userDTO));
    }

    @Test
    @Order(2)
    public void testReadUserById(){
        User user = userDao.readUser(1);
        Assertions.assertNotNull(user);
    }

    @Test
    @Order(3)
    public void testReadUserByEmail(){
        User userResult = userDao.readUserByEmail("email@gmail.com");
        Assertions.assertNotNull(userResult);
    }

    @Test
    @Order(4)
    public void testReadUserByLogin(){
        User userResult = userDao.readUserByLogin("user");
        Assertions.assertNotNull(userResult);
    }

    @Test
    @Order(5)
    public void testUpdateUser(){
        UserDto userDTO = new UserDto();
        userDTO.setEmail("email@gmail.com");
        userDTO.setPassword("password123");
        userDTO.setName("UserUpdated");
        userDTO.setLogin("user_updated");
        User userResult = userDao.updateUser(userDTO);
        Assertions.assertEquals(userResult.getName(), "UserUpdated");
        Assertions.assertEquals(userResult.getLogin(), "user_updated");
        Assertions.assertEquals(userResult.getPassword(), "password123");
    }

    @Test
    @Order(6)
    public void testDeleteUser(){
        Assertions.assertDoesNotThrow(() -> userDao.deleteUser(1));
    }
}
