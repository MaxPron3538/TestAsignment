package main.services;

import main.DTO.UserDto;
import main.entities.User;
import main.mappers.UserMapper;
import main.repositories.UserRepository;
import main.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    @Autowired
    UserValidator validator;
    @Value("${user.minAge}")
    private int minAge;
    public List<User> getAllUsers(){
        return repository.findAll();
    }
    public boolean addUser(User user){
        LocalDate userBirth = user.getBirthDate();
        LocalDate now = LocalDate.now();

        if(!validator.emailValidation(user.getEmail()))
            return false;
        if(!validator.isEarlierThenCurrentDateValidation(userBirth))
            return false;
        if(!validator.phoneNumberValidation(user.getPhoneNumber()))
            return false;

        if(Period.between(userBirth,now).getYears() >= minAge) {
            repository.save(user);
            return true;
        }
        return false;
    }
    public Optional<User> findById(int id){
        return repository.findById(id);
    }
    public Optional<User> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber){
        return repository.findByPhoneNumber(phoneNumber);
    }
    public List<User> findUsersBetweenBirthDate(LocalDate start, LocalDate end){
        if(!validator.isEarlierThenCurrentDateValidation(start))
            return null;
        if(!validator.isEarlierThenCurrentDateValidation(end))
            return null;
        if(start.isBefore(end))
            return repository.findAllByBirthDateBetween(start,end);

        return null;
    }

    public boolean updateUserField(UserDto userDto,User user){
        LocalDate userBirth = user.getBirthDate();
        LocalDate now = LocalDate.now();

        if(userDto.getEmail() != null) {
            if(!validator.emailValidation(userDto.getEmail()))
                return false;
        }
        if(userDto.getPhoneNumber() != null){
            if(!validator.phoneNumberValidation(userDto.getPhoneNumber()))
                return false;
        }
        if(userDto.getBirthDate() != null) {
            if(!validator.isEarlierThenCurrentDateValidation(userDto.getBirthDate()))
                return false;
            if(Period.between(userBirth,now).getYears() < minAge)
                return false;
        }
        User savedUser = UserMapper.INSTANCE.userDtoToUser(user, userDto);
        repository.save(savedUser);
        return true;
    }
    public boolean updateUser(User user,User findedUser){
        LocalDate userBirth = user.getBirthDate();
        LocalDate now = LocalDate.now();
        user.setId(findedUser.getId());

        if(!validator.emailValidation(user.getEmail()))
            return false;
        if(!validator.isEarlierThenCurrentDateValidation(user.getBirthDate()))
            return false;
        if(!validator.phoneNumberValidation(user.getPhoneNumber()))
            return false;

        if(Period.between(userBirth,now).getYears() >= minAge) {
            repository.save(user);
            return true;
        }
        return false;
    }
    public void deleteUser(int id){
        repository.deleteById(id);
    }
}
