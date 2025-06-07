package com.enefit.energyconsumption.modules.user.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * User entity for store user information
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Getter
@Setter
public class UserDto {
    private String userId;
    private String userFullName;
    private String password;
    private boolean enabled;
    private int pageSize;
    private int pageNumber;

}
