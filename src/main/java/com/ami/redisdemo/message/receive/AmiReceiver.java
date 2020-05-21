package com.ami.redisdemo.message.receive;

import org.springframework.stereotype.Component;

@Component
public class AmiReceiver {

    public void receiverAmi1(String message){
        System.out.println(message);
    }

    public void receiverAmi2(String message){
        System.out.println(message);
    }

    public void receiverAmi3(String message){
        System.out.println("Ami3 get Message = " + message);
    }

}