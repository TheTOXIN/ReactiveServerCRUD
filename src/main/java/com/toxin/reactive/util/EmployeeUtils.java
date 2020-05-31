package com.toxin.reactive.util;

import com.toxin.reactive.entity.Employee;
import org.springframework.util.DigestUtils;

import java.util.Optional;

public class EmployeeUtils {

    public static String getAvatar(Employee e) {
        String name = Optional.ofNullable(e.getName()).orElse("");
        String hash = DigestUtils.md5DigestAsHex(name.getBytes());

        return "http://www.gravatar.com/avatar/" + hash + "?d=identicon";
    }
}
