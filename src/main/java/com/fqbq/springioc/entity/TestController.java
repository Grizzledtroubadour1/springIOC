package com.fqbq.springioc.entity;


import com.fqbq.springioc.stereotype.Autowired;
import com.fqbq.springioc.stereotype.Component;

@Component
public class TestController {

    @Autowired
    private UserService userService;


    public void test(){
        userService.addUser("zhangsan",18);
    }

}
