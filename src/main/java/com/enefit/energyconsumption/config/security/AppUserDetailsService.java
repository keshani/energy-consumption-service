package com.enefit.energyconsumption.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.enefit.energyconsumption.config.model.AppUserDetail;
import com.enefit.energyconsumption.modules.user.models.entity.User;
import com.enefit.energyconsumption.modules.user.service.UserInfoService;

/**
 * Custom implementation of {@link UserDetailsService} that loads user details from the database using {@code UserInfoService}.
 * @author Keshani
 * @since 2025/05/31
 *
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserInfoService userInfoService;

    public AppUserDetailsService(final UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userInfoService.getUserInfo(userName);
        return new AppUserDetail(user);
    }
}

