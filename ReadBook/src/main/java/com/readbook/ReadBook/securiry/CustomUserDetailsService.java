package com.readbook.ReadBook.securiry;

import com.readbook.ReadBook.entity.AccountEntity;
import com.readbook.ReadBook.entity.RoleEntity;
import com.readbook.ReadBook.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountEntity user = userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(String.format("Email %s does not exist!", email));
        }
        return new CustomUserDetails(mapRolesToAuthorities(user.getRoleEntity()),
                                      user.getUsername(),
                                      user.getEmail(),
                                      user.getPassword()
                                        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(RoleEntity roles){
        return Collections.singleton(new SimpleGrantedAuthority(roles.getName()));
    }
}
