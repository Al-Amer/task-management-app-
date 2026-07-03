package com.example.taskmanagement;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public String getWelcomeMessge (){
        return "Hallo from Spring Boot! 🎉";
    }
    public String getGoodbyeMessage(){
        return "Auf Wiedersehen from Spring Boot! 👋";
    }

}
