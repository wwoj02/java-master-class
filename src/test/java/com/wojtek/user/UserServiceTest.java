package com.wojtek.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock UserDao userDao;
    @InjectMocks UserService userService;

    @Test
    void canGetAllUsers() {
        List<User> users = mock();
        given(userDao.getUsers()).willReturn(users);

        var actual = userService.getUsers();

        then(userDao).should().getUsers();
        assertThat(actual).isSameAs(users);
    }

    @Test
    void canFindUserById() {
        User user = new User("Wojtek");
        given(userDao.findUserById(user.getId())).willReturn(Optional.of(user));

        var actual = userService.findUserById(user.getId());

        then(userDao).should().findUserById(user.getId());
        assertThat(actual).isEqualTo(user);
    }
}
