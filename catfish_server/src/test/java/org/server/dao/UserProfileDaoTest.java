package org.server.dao;

import org.application.dto.UserDto;
import org.application.dto.UserProfileDto;
import org.junit.jupiter.api.*;
import org.server.ServerSession;
import org.server.entity.User;
import org.server.entity.UserProfile;

import javax.persistence.Persistence;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileDaoTest {

    private static final ServerSession session = new ServerSession(
            Persistence.createEntityManagerFactory("catfish_h2_test"));

    private UserDao userDao = new UserDao(session);

    private UserProfileDao userProfileDao = new UserProfileDao(session);

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
    public void updateProfileText(){
        UserDto userDto = new UserDto();
        userDto.setEmail("email@gmail.com");
        UserProfileDto userProfileDto = new UserProfileDto();
        User user = userDao.readUserByEmail(userDto.getEmail());
        userProfileDto.setUserId(user.getId());
        userProfileDto.setDescription("Hello world");
        userProfileDao.update(userProfileDto);
    }


    @Test
    @Order(2)
    public void updateProfileTextWithInvalidSize(){
        UserDto userDto = new UserDto();
        userDto.setEmail("email@gmail.com");
        UserProfileDto userProfileDto = new UserProfileDto();
        User user = userDao.readUserByEmail(userDto.getEmail());
        userProfileDto.setUserId(user.getId());
        userProfileDto.setDescription("Some text, Some text, Some text, Some text, Some text, Some text, Some text, Some text,");
        Assertions.assertThrows(RuntimeException.class, () -> userProfileDao.update(userProfileDto));
    }
}
