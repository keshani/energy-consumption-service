package com.enefit.energyconsumption.modules.user.service;

import org.springframework.stereotype.Service;

import com.enefit.energyconsumption.modules.user.models.entity.User;
import com.enefit.energyconsumption.modules.user.models.dto.UserDto;

@Service
public interface UserInfoService {
    User getUserInfo(String userId);
}
