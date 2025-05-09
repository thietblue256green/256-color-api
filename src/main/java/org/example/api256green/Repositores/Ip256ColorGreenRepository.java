package org.example.api256green.Repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import org.example.api256green.entites.Ip256ColorGreen;

@Repository
public interface Ip256ColorGreenRepository extends JpaRepository<Ip256ColorGreen,Integer> {

}
