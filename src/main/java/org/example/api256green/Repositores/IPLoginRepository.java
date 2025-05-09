package org.example.api256green.Repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import org.example.api256green.entites.IPLogin;
import org.example.api256green.entites.Ip256ColorGreen;

@Repository
public interface IPLoginRepository extends JpaRepository<IPLogin,Integer> {

    @Query("select ipLG from IPLogin ipLG where ipLG.ipLogin = :ipCheck")
    IPLogin getIPLGByIp(@Param("ipCheck") String ipCheck);

}
