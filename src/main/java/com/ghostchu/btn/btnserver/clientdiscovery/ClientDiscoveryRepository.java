package com.ghostchu.btn.btnserver.clientdiscovery;

import com.ghostchu.btn.btnserver.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ClientDiscoveryRepository extends CrudRepository<ClientDiscoveryEntity, Integer> {
    ClientDiscoveryEntity findByPeerIdAndClientName(String peerId, String clientName);

    List<ClientDiscoveryEntity> findByPeerId(String peerId);

    List<ClientDiscoveryEntity> findByClientName(String clientName);

    List<ClientDiscoveryEntity> findByFoundAtAfter(Timestamp foundAt);

    List<ClientDiscoveryEntity> findByFoundAtBetween(Timestamp foundAtStart, Timestamp foundAtEnd);

    List<ClientDiscoveryEntity> findByFoundBy(UserEntity foundBy);

    List<ClientDiscoveryEntity> findByFoundBy_Id(Integer id);

    @Query(value = "INSERT IGNORE INTO table (peerId, clientName, foundBy, foundAtAddress, foundAt) values (?, ?, ?, ?, ?)", nativeQuery = true)
    List<ClientDiscoveryEntity> writeButIgnore(List<ClientDiscoveryEntity> entities);

    List<ClientDiscoveryEntity> findAllByOrderByFoundAtDesc(Pageable pageable);

    @Query("SELECT new com.ghostchu.btn.btnserver.clientdiscovery.UserDiscoveryCount(cd.foundBy.id, COUNT(cd)) " +
            "FROM ClientDiscoveryEntity cd " +
            "WHERE cd.foundBy IS NOT NULL " +
            "GROUP BY cd.foundBy.id " +
            "ORDER BY COUNT(cd) DESC")
    List<UserDiscoveryCount> findUserDiscoveryRank(Pageable pageable);

}
