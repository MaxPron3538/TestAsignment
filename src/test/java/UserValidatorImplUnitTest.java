import main.repositories.UserRepository;
import main.validators.UserValidator;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class UserValidatorImplUnitTest {
    @InjectMocks
    UserValidator userValidator;

    @Test
    public void emailCorrectValidationTest(){
        boolean atCondition = userValidator.emailValidation("maxpron@gmail.com");
        boolean emailCondition = userValidator.emailValidation("maxpron@icloud.com");
        boolean domainCondition = userValidator.emailValidation("maxpron@ukr.net");
        Assert.assertTrue(atCondition);
        Assert.assertTrue(emailCondition);
        Assert.assertTrue(domainCondition);
    }
    @Test
    public void emailIncorrectValidationTest(){
        boolean atCondition = userValidator.emailValidation("maxprongmail.com");
        boolean emailCondition = userValidator.emailValidation("maxpron@.com");
        boolean domainCondition = userValidator.emailValidation("maxpron@ukr");
        Assert.assertFalse(atCondition);
        Assert.assertFalse(emailCondition);
        Assert.assertFalse(domainCondition);
    }

    @Test
    public void phoneNumberCorrectValidationTest(){
        boolean symbolCondition = userValidator.phoneNumberValidation("+3806563535567");
        boolean lengthCondition = userValidator.phoneNumberValidation("+34064676575");
        boolean prefixCodeCondition = userValidator.phoneNumberValidation("+436563535567");
        Assert.assertTrue(symbolCondition);
        Assert.assertTrue(lengthCondition);
        Assert.assertTrue(prefixCodeCondition);
    }

    @Test
    public void phoneNumberIncorrectValidationTest(){
        boolean symbolCondition = userValidator.phoneNumberValidation("+380656gt5567");
        boolean lengthCondition = userValidator.phoneNumberValidation("+3806565567");
        boolean prefixCondition = userValidator.phoneNumberValidation("6563255556778");

        Assert.assertFalse(symbolCondition);
        Assert.assertFalse(lengthCondition);
        Assert.assertFalse(prefixCondition);
    }

    @Test
    public void isEarlierThenCurrentDateCorrectTest(){
        LocalDate birth = LocalDate.of(2001,8,10);
        boolean condition = userValidator.isEarlierThenCurrentDateValidation(birth);
        Assert.assertTrue(condition);
    }

    @Test
    public void isEarlierThenCurrentDateIncorrectTest(){
        LocalDate birth = LocalDate.of(2024,9,6);
        boolean condition = userValidator.isEarlierThenCurrentDateValidation(birth);
        Assert.assertFalse(condition);
    }
}
