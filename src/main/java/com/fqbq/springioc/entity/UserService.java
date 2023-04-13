package com.fqbq.springioc.entity;

import com.fqbq.springioc.stereotype.Component;

@Component
public class UserService {

    public void addUser(String name, int age) {
        System.out.println(name);
        System.out.println(age);
    }
}
