package ua.george_nika.webtester.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by George on 29.06.2015.
 */
@Component("pwdEncoder")
public class DefaultPasswordEncoder implements PasswordEncoder {
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword.toString());
    }
}
