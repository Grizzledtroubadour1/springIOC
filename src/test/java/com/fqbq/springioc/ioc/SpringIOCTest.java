package com.fqbq.springioc.ioc;


import com.fqbq.springioc.entity.TestController;
import org.junit.Test;

import java.io.FileNotFoundException;

public class SpringIOCTest {
    @Test
    public void testScan() throws FileNotFoundException {
        SpringIOC springIOC = new SpringIOC();
        springIOC.initBeans();
        TestController instance = (TestController)springIOC.getInstance(TestController.class.getName());
        instance.test();
    }

    @Test
    public void testShiwu(){
        System.out.println(System.getProperty("user.dir"));
    }

    class A{
        public void methodA(){

        }
    }
}
