package com.evan.wj.dto;

import com.evan.wj.dto.base.OutputConverter;
import com.evan.wj.models.User;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO implements OutputConverter<UserDTO, User> {
    private int id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String realname;
    private boolean enabled;
    private String rolename;
}
