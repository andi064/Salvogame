package com.codeoftheweb.salvo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    GamePlayer findById (long id);
}
