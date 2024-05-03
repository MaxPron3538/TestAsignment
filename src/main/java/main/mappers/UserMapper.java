package main.mappers;

import main.DTO.UserDto;
import main.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserMapper {
    public static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    public User userDtoToUser(User user,UserDto userDto) {
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        user.setFirstName(userDto.getFirstName() != null ? userDto.getFirstName() : user.getFirstName());
        user.setLastName(userDto.getLastName() != null ? userDto.getLastName() : user.getLastName());
        user.setAddress(userDto.getAddress() != null ? userDto.getAddress() : user.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber() != null ? userDto.getPhoneNumber() : user.getPhoneNumber());
        user.setBirthDate(userDto.getBirthDate() != null ? userDto.getBirthDate() : user.getBirthDate());
        return user;
    }
}
