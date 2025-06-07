package com.example.clonestagram.Security;


import com.example.clonestagram.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var result = userRepository.findByUserId(username);


        if (result.isEmpty()){
            throw new UsernameNotFoundException("존재하지 않는 ID");
        }

        var user = result.get();

        // 권한 부여 위한 리스트
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 권한 부여
        authorities.add(new SimpleGrantedAuthority("normal"));

        var customUser = new CustomUser(user.getUserId(),
                user.getPassword(),
                authorities);

        // 아이디 받을수있게 커스텀
        customUser.userId = user.getUserId();
        customUser.userName = user.getUserName();
        customUser.userProfile = user.getUserProfile();
        customUser.userProfileMessage = user.getUserProfileMessage();       // 유저 상태메시지? 가져오기 위해 추가

        return customUser;
    }

    // 커스텀 유저
    public class CustomUser extends User {

        public String userId;
        public String userProfile;
        public String userName;
        public String userProfileMessage;

        public CustomUser(String username,
                          String password,
                          List<GrantedAuthority> authorities ) {
            super(username, password, authorities);
        }
    }
}
