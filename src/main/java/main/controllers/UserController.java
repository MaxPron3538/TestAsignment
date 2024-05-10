package main.controllers;

import main.DTO.UserDto;
import main.entities.User;
import main.exceptions.UserBadRequestException;
import main.exceptions.UserAlreadyExistsException;
import main.exceptions.UserNotFoundException;
import main.services.UserService;
import main.statuses.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> addUser(@RequestBody User user){
        if (userService.findByEmail(user.getEmail()).isPresent() ||
                userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistsException("User with such email or phone number already exists!");
        }
        ResponseStatus status = userService.addUser(user);

        switch (status) {
            case INVALID_SMALL_AGE: throw new UserBadRequestException("Failed to add user, the age is small then 18!");
            case INVALID_PHONE_NUMBER: throw new UserBadRequestException("Failed to add user,check field: phoneNumber!");
            case INVALID_BIRTHDATE: throw new UserBadRequestException("Failed to add user,check field: birthdate!");
            case INVALID_EMAIL: throw new UserBadRequestException("Failed to add user,check field: email!");
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/between")
    public ResponseEntity<List<User>> findUsersBetweenBirthDate(
           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
        List<User> findUsers = userService.findUsersBetweenBirthDate(start,end);

        if(findUsers == null){
            throw new UserBadRequestException("The time period is set incorrectly, check arguments: 'start={}&end={}'!");
        }
        if(findUsers.size() == 0){
            throw new UserNotFoundException("No users found in this date range!");
        }
        return new ResponseEntity<>(findUsers,HttpStatus.OK);
    }
    @PatchMapping("{id}")
    public ResponseEntity<?> updateUserField(@RequestBody UserDto userDto,@PathVariable long id){
        Optional<User> optionalUser = userService.findById(id);

        if(!optionalUser.isPresent()){
            throw new UserNotFoundException("User with such id is not exist!");
        }
        if(userService.findByEmail(userDto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User with email already exists!");
        }
        if(userService.findByPhoneNumber(userDto.getPhoneNumber()).isPresent()){
            throw new UserAlreadyExistsException("User with phone number already exists!");
        }
        ResponseStatus status = userService.updateUserField(userDto,optionalUser.get());

        switch (status) {
            case INVALID_SMALL_AGE: throw new UserBadRequestException("Failed to update user, the age is small then 18!");
            case INVALID_PHONE_NUMBER: throw new UserBadRequestException("Failed to update user,check field: phoneNumber!");
            case INVALID_BIRTHDATE: throw new UserBadRequestException("Failed to update user,check field: birthdate!");
            case INVALID_EMAIL: throw new UserBadRequestException("Failed to update user,check field: email!");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable long id){
        Optional<User> optionalUser = userService.findById(id);

        if(!optionalUser.isPresent()){
            throw new UserNotFoundException("User with such id is not exist!");
        }
        if(userService.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User with email already exists!");
        }
        if(userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw new UserAlreadyExistsException("User with phone number already exists!");
        }
        ResponseStatus status = userService.updateUser(user,optionalUser.get());

        switch (status) {
            case INVALID_SMALL_AGE: throw new UserBadRequestException("Failed to update user, the age is small then 18!");
            case INVALID_PHONE_NUMBER: throw new UserBadRequestException("Failed to update user,check field: phoneNumber!");
            case INVALID_BIRTHDATE: throw new UserBadRequestException("Failed to update user,check field: birthdate!");
            case INVALID_EMAIL: throw new UserBadRequestException("Failed to update user,check field: email!");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new UserNotFoundException("User with such id is not exist!");
    }

}
