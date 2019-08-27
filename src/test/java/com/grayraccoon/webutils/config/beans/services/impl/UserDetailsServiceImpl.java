package com.grayraccoon.webutils.config.beans.services.impl;

import com.grayraccoon.webutils.config.beans.entities.CustomUserDetails;
import com.grayraccoon.webutils.config.beans.entities.UsersEntity;
import com.grayraccoon.webutils.config.beans.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UsersEntity user = usersRepository.findFirstByEmailOrUsername(s, s);
        if (user == null) {
            throw new UsernameNotFoundException("Username or Email Not Found");
        }
        return new CustomUserDetails(user);

    }

}
