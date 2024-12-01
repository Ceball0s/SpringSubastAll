package com.irojas.demojwt.Subasta;

import com.irojas.demojwt.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;
import java.util.Date;
import com.irojas.demojwt.User.User;
import com.irojas.demojwt.User.UserDTO;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOP {
    int id;
    String username;
    String firstname;
    String lastname;
    String country;
    String date;
    String genter;
}
