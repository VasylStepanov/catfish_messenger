package org.server.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(name = "loginUniqueConstraint", columnNames = "login"),
    @UniqueConstraint(name = "loginEmailConstraint", columnNames = "email"),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "login", nullable = false, length = 16)
    private String login;

    @Column(name = "email", nullable = false, length = 32)
    private String email;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    public void setUserProfile(UserProfile userProfile){
        userProfile.setUser(this);
        this.userProfile = userProfile;
    }

    public static class UserBuilder {
        private UserBuilder userProfile(UserProfile userProfile) { return this; }
    }
}
