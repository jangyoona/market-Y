package com.markety.platform.config.security;

import com.markety.platform.dto.RoleDto;
import com.markety.platform.dto.UserDto;
import com.markety.platform.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class WebUserDetailService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto user = userMapper.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("isUser" + username));

        Set<RoleDto> roles = userMapper.findRolesById(user.getId());
        return new WebUserDetails(user, roles.stream().toList());

    }




//        Optional<UserDto> result = userMapper.findByUserName(username);

//        if (result.isEmpty()) {
//            throw new UsernameNotFoundException("isUser" + username);
//        } else {
//            UserDto user = result.get();
//            Set<RoleDto> roles = userMapper.findRolesById(user.getId());
//
//            return new WebUserDetails(user, roles.stream().toList());
//        }

    public class UserNotConfirmedException extends UsernameNotFoundException {
        public UserNotConfirmedException(String msg) {
            super(msg);
        }
    }
}
