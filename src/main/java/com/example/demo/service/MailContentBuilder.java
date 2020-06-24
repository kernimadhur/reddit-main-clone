package com.example.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String message){
        Context context = new Context();                                       //context of thymeleaf
        context.setVariable("message", message);                        //message will be added in html message
            return templateEngine.process("mailTemplate", context);  //html fileName is passed with the context engine
    }
}
