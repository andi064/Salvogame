package com.codeoftheweb.salvo;

//import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findById(long id); // you dont need a list bcz and ID
}