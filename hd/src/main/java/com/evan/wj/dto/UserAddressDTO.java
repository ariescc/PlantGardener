package com.evan.wj.dto;

import com.evan.wj.dto.base.OutputConverter;
import com.evan.wj.models.UserAddress;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserAddressDTO implements OutputConverter<UserAddressDTO, UserAddress> {
    private String address;
    private String postcode;
    private String houseNumber;
    private String contactPerson;
    private String tag;
}
