import main.DTO.UserDto;
import main.Main;
import main.entities.User;
import main.repositories.UserRepository;
import main.services.UserService;
import main.statuses.ResponseStatus;
import main.validators.UserValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceImplUnitTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserValidator userValidator;

    @InjectMocks
    UserService userService;

    User user1;
    User user2;

    LocalDate start;
    LocalDate end;

    @Before
    public void setUp(){
        user1 = new User();
        user1.setId(1);
        user1.setEmail("maxpron1325@gmail.com");
        user1.setFirstName("Maksym");
        user1.setLastName("Pron");
        user1.setAddress("Kiev");
        LocalDate localDate1 = LocalDate.of(2001,7,6);
        user1.setBirthDate(localDate1);
        user1.setPhoneNumber("+360858674554");

        user2 = new User();
        user2.setId(2);
        user2.setEmail("Alex23@gmail.com");
        user2.setFirstName("Oleksiy");
        user2.setLastName("Petrov");
        user2.setAddress("Odessa");
        LocalDate localDate2 = LocalDate.of(2002,9,5);
        user2.setBirthDate(localDate2);
        user2.setPhoneNumber("+380789567569");

        start = LocalDate.of(2000,5,6);
        end = LocalDate.of(2002,12,3);
    }
    @Test
    public void addUserTest() {
        Mockito.when(userValidator.phoneNumberValidation(user1.getPhoneNumber())).thenReturn(true);
        Mockito.when(userValidator.emailValidation(user1.getEmail())).thenReturn(true);
        Mockito.when(userValidator.isEarlierThenCurrentDateValidation(user1.getBirthDate())).thenReturn(true);
        Mockito.when(userRepository.save(user1)).thenReturn(user1);

        ResponseStatus status = userService.addUser(user1);
        Assert.assertEquals(ResponseStatus.OK,status);
    }
    @Test
    public void findAllUsersTest() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1,user2));

        int userCount = 2;
        List<User> users = userService.getAllUsers();
        Assert.assertEquals(users.size(),userCount);
    }
    @Test
    public void findUserByIdTest(){
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(user2));

        Optional<User> optionalUser = userService.findById(2);
        Assert.assertEquals(user2.getEmail(),optionalUser.get().getEmail());
    }
    @Test
    public void findUserByEmailTest(){
        Mockito.when(userRepository.findByEmail("maxpron1325@gmail.com")).thenReturn(Optional.of(user1));
        Mockito.when(userValidator.emailValidation(user1.getEmail())).thenReturn(true);

        Optional<User> optionalUser = userService.findByEmail("maxpron1325@gmail.com");
        Assert.assertEquals(user1.getId(),optionalUser.get().getId());
    }
    @Test
    public void findUserByPhoneNumberTest(){
        Mockito.when(userRepository.findByPhoneNumber("+360858674554")).thenReturn(Optional.of(user1));

        Optional<User> optionalUser = userService.findByPhoneNumber("+360858674554");
        Assert.assertEquals(user1.getId(),optionalUser.get().getId());
    }

    @Test
    public void findUsersBetweenBirthDate(){
        Mockito.when(userValidator.isEarlierThenCurrentDateValidation(start)).thenReturn(true);
        Mockito.when(userValidator.isEarlierThenCurrentDateValidation(end)).thenReturn(true);
        Mockito.when(userRepository.findAllByBirthDateBetween(start,end)).thenReturn(List.of(user1));

        List<User> users = userService.findUsersBetweenBirthDate(start,end);
        Assert.assertEquals(user1.getId(),users.get(0).getId());
    }

    @Test
    public void updateUserFieldTest(){
        UserDto userDto = new UserDto();
        LocalDate birthDate = LocalDate.of(2000,10,9);
        userDto.setBirthDate(birthDate);
        userDto.setPhoneNumber("+380864674356");
        userDto.setEmail("maxpron@gmail.com");
        Mockito.when(userValidator.phoneNumberValidation(userDto.getPhoneNumber())).thenReturn(true);
        Mockito.when(userValidator.emailValidation(userDto.getEmail())).thenReturn(true);
        Mockito.when(userValidator.isEarlierThenCurrentDateValidation(birthDate)).thenReturn(true);

        ResponseStatus status = userService.updateUserField(userDto,user1);
        Assert.assertEquals(ResponseStatus.OK,status);
    }

    @Test
    public void updateUserTest(){
        User updateUser = new User();
        updateUser.setEmail("maxpron@gmail.com");
        updateUser.setFirstName("Maksym");
        updateUser.setLastName("Pron");
        updateUser.setAddress("New York");
        LocalDate localDate1 = LocalDate.of(2001,10,18);
        updateUser.setBirthDate(localDate1);
        updateUser.setPhoneNumber("+3607785435665");

        Mockito.when(userValidator.phoneNumberValidation(updateUser.getPhoneNumber())).thenReturn(true);
        Mockito.when(userValidator.emailValidation(updateUser.getEmail())).thenReturn(true);
        Mockito.when(userValidator.isEarlierThenCurrentDateValidation(updateUser.getBirthDate())).thenReturn(true);

        ResponseStatus status = userService.updateUser(updateUser,user1);
        Assert.assertEquals(ResponseStatus.OK,status);
    }
}
