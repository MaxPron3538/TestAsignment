import main.DTO.UserDto;
import main.entities.User;
import main.mappers.UserMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.Table;
import java.time.LocalDate;

public class UserMapperImplTest {
    User user;
    UserDto userDto;

    @Before
    public void setUp(){
        user = new User();
        user.setId(1);
        user.setEmail("maxpron@gmail.com");
        user.setFirstName("Maksym");
        user.setLastName("Pron");
        user.setAddress("Kiev");
        LocalDate localDate1 = LocalDate.of(2001,7,6);
        user.setBirthDate(localDate1);
        user.setPhoneNumber("+3608586745546");

        userDto = new UserDto();
        userDto.setEmail("maxpron1325@gmail.com");
        userDto.setAddress("Odessa");
        userDto.setPhoneNumber("+3608545636675");
    }

    @Test
    public void UserDtoToUserTest(){
        User mappedUser = UserMapper.INSTANCE.userDtoToUser(user,userDto);
        Assert.assertEquals(userDto.getEmail(),mappedUser.getEmail());
        Assert.assertEquals(userDto.getAddress(),mappedUser.getAddress());
        Assert.assertEquals(userDto.getPhoneNumber(),mappedUser.getPhoneNumber());
    }
}
