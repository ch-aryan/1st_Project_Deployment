//package com.springbootbanking;
//
//
//
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TestController {
//
//    private final RegistrationService registrationService;
//
//    public TestController(RegistrationService registrationService) {
//
//        System.out.println("✅ TestController Constructor Called");
//
//        this.registrationService = registrationService;
//    }
//
//    @GetMapping("/test")
//    public String test() {
//        return "Spring Working";
//    }
//
//}