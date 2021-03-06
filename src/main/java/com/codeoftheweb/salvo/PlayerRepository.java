package com.codeoftheweb.salvo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByEmail(String email); //set doesnt get repetition
}