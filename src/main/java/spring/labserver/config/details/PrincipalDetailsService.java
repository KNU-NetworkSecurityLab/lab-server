package spring.labserver.config.details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.services.UserService;

// http://localhost:8080/login에 요청할 때 동작 함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
    
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername");
        User userEntity = userService.findByUserId(userId);
        return new PrincipalDetails(userEntity);
    }
    
}
