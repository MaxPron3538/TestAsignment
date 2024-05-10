package main.services;

import main.DTO.UserDto;
import main.entities.User;
import main.mappers.UserMapper;
import main.repositories.UserRepository;
import main.statuses.ResponseStatus;
import main.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
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
    public ResponseStatus addUser(User user){
        LocalDate userBirth = user.getBirthDate();
        LocalDate now = LocalDate.now();

        if(!validator.emailValidation(user.getEmail()))
            return ResponseStatus.INVALID_EMAIL;
        if(!validator.phoneNumberValidation(user.getPhoneNumber()))
            return ResponseStatus.INVALID_PHONE_NUMBER;
        if(!validator.isEarlierThenCurrentDateValidation(userBirth))
            return ResponseStatus.INVALID_BIRTHDATE;

        if(Period.between(userBirth,now).getYears() >= minAge) {
            repository.save(user);
            return ResponseStatus.OK;
        }
        return ResponseStatus.INVALID_SMALL_AGE;
    }
    public Optional<User> findById(long id){
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

    public ResponseStatus updateUserField(UserDto userDto,User user){
        LocalDate userDtoBirth = userDto.getBirthDate();
        LocalDate now = LocalDate.now();

        if(userDto.getEmail() != null) {
            if(!validator.emailValidation(userDto.getEmail()))
                return ResponseStatus.INVALID_EMAIL;
        }
        if(userDto.getPhoneNumber() != null){
            if(!validator.phoneNumberValidation(userDto.getPhoneNumber()))
                return ResponseStatus.INVALID_PHONE_NUMBER;
        }
        if(userDto.getBirthDate() != null) {
            if(!validator.isEarlierThenCurrentDateValidation(userDto.getBirthDate()))
                return ResponseStatus.INVALID_BIRTHDATE;
            if(Period.between(userDtoBirth,now).getYears() < minAge)
                return ResponseStatus.INVALID_SMALL_AGE;
        }
        User savedUser = UserMapper.INSTANCE.userDtoToUser(user,userDto);
        repository.save(savedUser);
        return ResponseStatus.OK;
    }
    public ResponseStatus updateUser(User user,User findedUser){
        LocalDate userBirth = user.getBirthDate();
        LocalDate now = LocalDate.now();
        user.setId(findedUser.getId());

        if(!validator.emailValidation(user.getEmail()))
            return ResponseStatus.INVALID_EMAIL;
        if(!validator.isEarlierThenCurrentDateValidation(user.getBirthDate()))
            return ResponseStatus.INVALID_BIRTHDATE;
        if(!validator.phoneNumberValidation(user.getPhoneNumber()))
            return ResponseStatus.INVALID_PHONE_NUMBER;

        if(Period.between(userBirth,now).getYears() >= minAge) {
            repository.save(user);
            return ResponseStatus.OK;
        }
        return ResponseStatus.INVALID_SMALL_AGE;
    }
    public void deleteUser(long id){
        repository.deleteById(id);
    }
}
