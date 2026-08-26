package com.springbootbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args)  {

        SpringApplication.run(BankingApplication.class, args);

    }

}
/*

//    Class<?> t = User1.class;
//        System.out.println(t.getDeclaredFields());
//        System.out.println(t.getAnnotations());
//        Class <?> t1 = UserService.class;
//        System.out.println(t1.getAnnotations());

//        Class<Student> clazz = Student.class;
//
//        Constructor<Student> constructor =
//                clazz.getDeclaredConstructor();
//
//        Student student =
//                constructor.newInstance();
 */
