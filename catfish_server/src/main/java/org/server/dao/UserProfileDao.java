package org.server.dao;

import org.application.dto.UserProfileDto;
import org.server.ServerSession;
import org.server.entity.User;
import org.server.entity.UserProfile;

public class UserProfileDao {

    private ServerSession session;

    public UserProfileDao(ServerSession session) {
        this.session = session;
    }

    public UserProfile readUserProfile(Integer id){
        return session.doInSessionWithReturn(entityManager -> {
            User user = entityManager.
                    createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).
                    setParameter("id", id).
                    getSingleResult();
            return user.getUserProfile();
        });
    }

    public void update(UserProfileDto userProfileDtoDao){
        session.doInSession(entityManager -> {
            User user = entityManager.
                    createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).
                    setParameter("id", userProfileDtoDao.getUserId()).
                    getSingleResult();

            UserProfile userProfile = user.getUserProfile();
            userProfile.setDescription(userProfileDtoDao.getDescription());
        });
    }

}
