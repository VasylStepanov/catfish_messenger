package org.server.dao;

import org.application.dto.UserDto;
import org.server.ServerSession;
import org.server.entity.User;
import org.server.entity.UserProfile;


public class UserDao {

    private ServerSession session;

    public UserDao(ServerSession session) {
        this.session = session;
    }

    public void createUser(UserDto userDto){
        User user = User.builder().
                name(userDto.getName()).
                login(userDto.getLogin()).
                email(userDto.getEmail()).
                password(userDto.getPassword()).build();
        user.setUserProfile(new UserProfile());

        session.doInSession(entityManager -> entityManager.persist(user));
    }

    public User readUser(Integer id){
        return session.doInSessionWithReturn(entityManager -> entityManager.find(User.class, id));
    }

    public User readUserByEmail(String email){
        return session.doInSessionWithReturn(entityManager ->
            entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult()
        );
    }

    public User readUserByLogin(String login){
        return session.doInSessionWithReturn(entityManager ->
                entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                        .setParameter("login", login)
                        .getSingleResult()
        );
    }

    public User updateUser(UserDto userDto){
        return session.doInSessionWithReturn(entityManager -> {
            User user = entityManager.
                    createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).
                    setParameter("email", userDto.getEmail()).
                    getSingleResult();

            if(!user.getName().equals(userDto.getName()))
                user.setName(userDto.getName());

            if(!user.getLogin().equals(userDto.getLogin()))
                user.setLogin(userDto.getLogin());

            if(!user.getPassword().equals(userDto.getPassword()))
                user.setPassword(userDto.getPassword());

            return user;
        });
    }

    public void deleteUser(Integer id){
        session.doInSession(entityManager -> {
            User user = entityManager.find(User.class, id);
            entityManager.remove(user);
        });
    }
}
