package com.ghostchu.btn.btnserver.clientdiscovery;

import com.ghostchu.btn.btnserver.event.NewClientFoundEvent;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeer;
import com.ghostchu.btn.btnserver.user.UserEntity;
import com.ghostchu.btn.btnserver.user.UserEntityDto;
import com.ghostchu.btn.btnserver.user.UserService;
import com.google.common.hash.Hashing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientDiscoveryService {
    @Autowired
    private ClientDiscoveryRepository repository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    public List<ClientDiscoveryEntity> queryLastFoundClients(Pageable pageable) {
        return repository.findAllByOrderByFoundAtDesc(pageable);
    }


    public List<UserDiscoveryCountBaked> discoveryRank(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findUserDiscoveryRank(pageRequest).stream().map(
                        s -> {
                            Optional<UserEntity> dto = userService.getUserById(s.getUserId());
                            return dto.map(user -> new UserDiscoveryCountBaked(modelMapper.map(user, UserEntityDto.class), s.getDiscoveryCount())).orElse(null);
                        })
                .filter(Objects::nonNull)
                .toList();

    }

    @Transactional
    public void discover(UserEntity user, List<BtnPeer> peers) {
        List<ClientDiscoveryEntity> entities = peers.stream().map(bp -> {
            ClientDiscoveryEntity discoveryEntity = new ClientDiscoveryEntity();
            discoveryEntity.setClientName(bp.getClientName());
            discoveryEntity.setPeerId(bp.getPeerId().substring(0,8));
            discoveryEntity.setFoundBy(user);
            discoveryEntity.setFoundAt(new Timestamp(System.currentTimeMillis()));
            discoveryEntity.setFoundAtAddress(bp.getIpAddress());
            return discoveryEntity;
        }).collect(Collectors.toList());
        discover(entities);
    }

    private void discover(List<ClientDiscoveryEntity> entities) {
        Set<String> discoveredInSingleReq = new HashSet<>();
        entities.removeIf(e -> e.getClientName().contains("Xunlei") || e.getClientName().contains("XunLei") || e.getClientName().startsWith("-XL"));
        entities.removeIf(e -> e.getPeerId().startsWith("-XL"));
        entities.removeIf(e -> e.getPeerId().startsWith("FD6"));
        entities.removeIf(e -> e.getClientName().startsWith("FDM/"));
        entities.removeIf(e -> e.getClientName().startsWith("Unknown"));
        entities.removeIf(e -> e.getClientName().isBlank() && e.getPeerId().isBlank());
        entities.removeIf(entity -> !discoveredInSingleReq.add(entity.getPeerId() + "@" + entity.getClientName()));
        // 刷进数据库
        writeButIgnore(entities);
    }


    public void writeButIgnore(List<ClientDiscoveryEntity> entities) {
        List<ClientDiscoveryEntity> newFounded = new ArrayList<>();
        for (ClientDiscoveryEntity entity : entities) {
            String id = Hashing.sha256().hashString(entity.getPeerId() + "@" + entity.getClientName(), StandardCharsets.UTF_8).toString();
            int update = entityManager.createNativeQuery("INSERT IGNORE INTO client_discovery (id, peer_id, client_name, found_by_id, found_at_address, found_at) VALUES (?, ?, ?, ?, ?, ?)")
                    .setParameter(1, id)
                    .setParameter(2, entity.getPeerId())
                    .setParameter(3, entity.getClientName())
                    .setParameter(4, entity.getFoundBy() == null ? null : entity.getFoundBy().getId())
                    .setParameter(5, entity.getFoundAtAddress())
                    .setParameter(6, entity.getFoundAt())
                    .executeUpdate();
            if (update != 0) {
                newFounded.add(entity);
            }
        }
        entityManager.flush();
        if (newFounded.isEmpty()) {
            return;
        }
        eventPublisher.publishEvent(new NewClientFoundEvent(this, newFounded));
    }


}
