package com.enefit.energyconsumption.modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enefit.energyconsumption.modules.user.models.entity.User;
import com.enefit.energyconsumption.modules.user.repository.UserInfoRepository;

/**
 * Service layer to handle all the City operation logics
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public User getUserInfo(String userName) {
        return userInfoRepository.findByUserName(userName).get();
    }
}



