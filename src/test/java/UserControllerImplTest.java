import com.fasterxml.jackson.databind.ObjectMapper;
import main.DTO.UserDto;
import main.Main;
import main.controllers.UserController;
import main.entities.User;
import main.repositories.UserRepository;
import main.services.UserService;
import main.statuses.ResponseStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {Main.class})
public class UserControllerImplTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    User user1;
    User user2;

    @Before
    public void setUp(){
        JacksonTester.initFields(this,new ObjectMapper());

        user1 = new User();
        user1.setId(1);
        user1.setEmail("maxpron@gmail.com");
        user1.setFirstName("Maksym");
        user1.setLastName("Pron");
        user1.setAddress("Kiev");
        LocalDate localDate1 = LocalDate.of(2001,10,9);
        user1.setBirthDate(localDate1);
        user1.setPhoneNumber("+360858674554");

        user2 = new User();
        user2.setId(2);
        user2.setEmail("Alex35@gmail.com");
        user2.setFirstName("Alexandr");
        user2.setLastName("Dumas");
        user2.setAddress("Paris");
        LocalDate localDate2 = LocalDate.of(1857,10,9);
        user2.setBirthDate(localDate2);
        user2.setPhoneNumber("+356264546635");

        List<User> users = Arrays.asList(user1,user2);
        given(userService.getAllUsers()).willReturn(users);
    }

    @Test
    public void getAllUsersTest() throws Exception {
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email",is(user1.getEmail())));
    }

    @Test
    public void createUserTest() throws Exception{
        String requestBody = "{"
                + "\"email\": \"JhonDoe@gmail.com\","
                + "\"firstName\": \"John\","
                + "\"lastName\": \"Doe\","
                + "\"birthDate\": \"1990-01-01\","
                + "\"address\": \"123 Main St\","
                + "\"phoneNumber\": \"+126734567890\""
                + "}";

        Mockito.when(userService.addUser(Mockito.any(User.class)))
                .thenReturn(ResponseStatus.OK);

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void findUsersBetweenBirthDateTest() throws Exception{
        LocalDate start = LocalDate.of(2001,6,8);
        LocalDate end = LocalDate.of(2003,8,5);

        given(userService.findUsersBetweenBirthDate(start,end)).willReturn(List.of(user1));

        mvc.perform(MockMvcRequestBuilders.get("/users/between")
                .param("start",start.toString()).param("end",end.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].email",is(user1.getEmail())));
    }
    @Test
    public void updateUserFieldTest() throws Exception{
        String requestBody = "{"
                + "\"email\": \"AlfredHichkock@gmail.com\","
                + "\"birthDate\": \"1936-09-12\","
                + "\"phoneNumber\": \"+2354654546567\""
                + "}";

        long id = userService.getAllUsers().get(0).getId();
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(user1));
        Mockito.when(userService.updateUserField(Mockito.any(UserDto.class),Mockito.any(User.class)))
                .thenReturn(ResponseStatus.OK);

        mvc.perform(MockMvcRequestBuilders.patch("/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void updateUserTest() throws Exception{
        String requestBody = "{"
                + "\"email\": \"KarlSagan@gmail.com\","
                + "\"firstName\": \"Karl\","
                + "\"lastName\": \"Sagan\","
                + "\"birthDate\": \"1995-06-08\","
                + "\"address\": \"123 Main St\","
                + "\"phoneNumber\": \"+4535353236654\""
                + "}";

        long id = userService.getAllUsers().get(0).getId();
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(user1));
        Mockito.when(userService.updateUser(Mockito.any(User.class),Mockito.any(User.class)))
                .thenReturn(ResponseStatus.OK);

        mvc.perform(MockMvcRequestBuilders.put("/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception{
        long id = userService.getAllUsers().get(0).getId();
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(user1));

        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
