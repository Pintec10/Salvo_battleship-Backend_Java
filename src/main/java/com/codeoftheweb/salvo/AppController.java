package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    private PlayerRepository playerRepository;
    public void  test(){
        playerRepository.findByUserName("Barney");
    }
}
