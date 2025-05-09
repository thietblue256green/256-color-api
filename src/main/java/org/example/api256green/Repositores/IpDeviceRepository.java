package org.example.api256green.Repositores;

import org.example.api256green.entites.IpDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IpDeviceRepository extends JpaRepository<IpDevice,Integer> {
    @Query("select IpD from IpDevice IpD where IpD.ipDevice = :ipCheck")
    IpDevice getIPDByIp(@Param("ipCheck") String ipCheck);

}
