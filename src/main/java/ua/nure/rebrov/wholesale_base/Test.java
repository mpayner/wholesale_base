package ua.nure.rebrov.wholesale_base;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args){
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        String pass = "qwerty4";
        String encodedpass= b.encode(pass);
        System.out.println(encodedpass);
    }
}
