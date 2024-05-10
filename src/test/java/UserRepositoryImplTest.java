import main.Main;
import main.entities.User;
import main.repositories.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:Application-test.properties")
@ContextConfiguration(classes = {Main.class})
public class UserRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @Before
    public void setUp(){
        user1 = new User();
        user1.setEmail("maxpron1325@gmail.com");
        user1.setFirstName("Maksym");
        user1.setLastName("Pron");
        user1.setAddress("Kiev");
        LocalDate localDate1 = LocalDate.of(2001,10,9);
        user1.setBirthDate(localDate1);
        user1.setPhoneNumber("+360858674554");

        user2 = new User();
        user2.setEmail("Alex35@gmail.com");
        user2.setFirstName("Alexandr");
        user2.setLastName("Dumas");
        user2.setAddress("Paris");
        LocalDate localDate2 = LocalDate.of(1857,10,9);
        user2.setBirthDate(localDate2);
        user2.setPhoneNumber("+356264546635");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    public void findAllTest(){
        List<User> users = userRepository.findAll();
        Assert.assertNotNull(users);
        Assert.assertEquals(user1.getEmail(),users.get(0).getEmail());
        Assert.assertEquals(user2.getEmail(),users.get(1).getEmail());
    }

    @Test
    public void findByIdTest() {
        long id = userRepository.findByEmail(user1.getEmail()).get().getId();
        User savedUser = userRepository.findById(id).orElse(null);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(user1.getEmail(),savedUser.getEmail());
    }

    @Test
    public void findByEmailTest(){
        User savedUser = userRepository.findByEmail(user1.getEmail()).orElse(null);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(user1.getPhoneNumber(),savedUser.getPhoneNumber());
    }

    @Test
    public void findByPhoneNumberTest() {
        User savedUser = userRepository.findByPhoneNumber(user1.getPhoneNumber()).orElse(null);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(user1.getEmail(),savedUser.getEmail());
    }

    @Test
    public void findAllByBirthDateBetween(){
        LocalDate start = LocalDate.of(2001,5,25);
        LocalDate end = LocalDate.of(2003,3,12);

        List<User> users = userRepository.findAllByBirthDateBetween(start,end);
        Assert.assertNotNull(users);
        Assert.assertEquals(user1.getEmail(),users.get(0).getEmail());
        Assert.assertEquals(user1.getPhoneNumber(),users.get(0).getPhoneNumber());
    }
}
