package main.controllers;

import main.DTO.UserDto;
import main.entities.User;
import main.services.UserService;
import org.checkerframework.common.reflection.qual.GetMethod;
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
        if(!userService.findByEmail(user.getEmail()).isPresent()
                || !userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            if(userService.addUser(user)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }
    @GetMapping("/between")
    public ResponseEntity<List<User>> findUsersBetweenBirthDate(
           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
        List<User> findUsers = userService.findUsersBetweenBirthDate(start,end);

        if(findUsers == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(findUsers.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(findUsers,HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateUserField(@RequestBody UserDto userDto,@PathVariable int id){
        Optional<User> optionalUser = userService.findById(id);

        if(userService.findByEmail(userDto.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        if(userService.findByPhoneNumber(userDto.getPhoneNumber()).isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        if(optionalUser.isPresent()){
            if(userService.updateUserField(userDto,optionalUser.get())){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable int id){
        Optional<User> optionalUser = userService.findById(id);

        if(userService.findByEmail(user.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        if(userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        if(optionalUser.isPresent()){
            if(userService.updateUser(user,optionalUser.get())){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
