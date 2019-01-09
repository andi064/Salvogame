package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    Ship findById(long id);
}
