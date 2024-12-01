package com.irojas.demojwt.User;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; 

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {
       
        User user = User.builder()
        .id(userRequest.id)
        .firstname(userRequest.getFirstname())
        .lastname(userRequest.lastname)
        .country(userRequest.getCountry())
        .date(userRequest.getDate())
        .genter(userRequest.getGenter())
        .role(Role.USER)
        .subastas(userRequest.getSubastas())
        .build();
        
        userRepository.updateUser(user.id, user.firstname, user.lastname, user.country, user.genter);

        return new UserResponse("El usuario se registró satisfactoriamente");
    }

    public UserDTO getUser(Integer id) {
        User user= userRepository.findById(id).orElse(null);
       
        if (user!=null)
        {
            UserDTO userDTO = UserDTO.builder()
            .id(user.id)
            .username(user.username)
            .firstname(user.firstname)
            .lastname(user.lastname)
            .country(user.country)
            .date(user.date)
            .genter(user.genter)
            .subastas(user.subastas)
            .build();
            return userDTO;
        }
        return null;
    }

    public UserDTO getUser(String username) {
        User user= userRepository.findByUsername(username).orElse(null);
       
        if (user!=null)
        {
            UserDTO userDTO = UserDTO.builder()
            .id(user.id)
            .username(user.username)
            .firstname(user.firstname)
            .lastname(user.lastname)
            .country(user.country)
            .date(user.date)
            .genter(user.genter)
            .subastas(user.subastas)
            .build();
            return userDTO;
        }
        return null;
    }
}
