package com.ghostchu.btn.btnserver.ping;

import com.ghostchu.btn.btnserver.clientdiscovery.ClientDiscoveryService;
import com.ghostchu.btn.btnserver.exception.AccessDeniedException;
import com.ghostchu.btn.btnserver.ping.bean.BtnBan;
import com.ghostchu.btn.btnserver.ping.bean.BtnBanPing;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeerPing;
import com.ghostchu.btn.btnserver.ping.bean.BtnRule;
import com.ghostchu.btn.btnserver.ping.dao.PingDao;
import com.ghostchu.btn.btnserver.rule.RuleEntity;
import com.ghostchu.btn.btnserver.rule.RuleEntityDto;
import com.ghostchu.btn.btnserver.rule.RuleService;
import com.ghostchu.btn.btnserver.user.UserEntity;
import com.ghostchu.btn.btnserver.user.UserService;
import com.ghostchu.btn.btnserver.userapp.UserApplicationEntity;
import com.ghostchu.btn.btnserver.userapp.UserApplicationService;
import inet.ipaddr.IPAddress;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PingService {
    @Autowired
    private UserService userService;
    @Autowired
    private ClientDiscoveryService clientDiscoveryService;
    @Autowired
    private UserApplicationService userAppService;
    @Autowired
    private RuleService ruleDao;
    @Autowired
    private PingDao dao;
    @Value("${ping-service-config.allow-anonymous}")
    private boolean allowAnonymous;
    @Value("${ping-service-config.ability.rules.auto_generated_rule_untrusted_thresholds}")
    private int untrustedThresholds;
    @Autowired
    private ModelMapper modelMapper;

    public BtnRule generateJsonRawMap() {
        List<RuleEntity> entities = new ArrayList<>(ruleDao.readRules(System.currentTimeMillis()));
        entities.addAll(ruleDao.readUntrustedRules(untrustedThresholds));
        List<RuleEntityDto> rules = entities
                .stream()
                .map((element) -> modelMapper.map(element, RuleEntityDto.class)).toList();
        return new BtnRule(rules);
    }

    public void handlePing(BtnPeerPing ping, IPAddress accessorAddress, String appId, String appSecret) throws AccessDeniedException {
        UserApplicationEntity userApp = userAppService.getUserApplication(appId, appSecret);
        UserEntity user = null;
        if (userApp == null) {
            if (!allowAnonymous) {
                throw new AccessDeniedException("This BTN instance required login for submit ping.");
            }
        } else {
            user = userApp.getUser();
        }
        dao.insertPing(ping, accessorAddress, user == null ? 0 : user.getId(), appId, appSecret, UUID.randomUUID().toString());
        clientDiscoveryService.discover(user, ping.getPeers());
    }

    public void handleBan(BtnBanPing ping, IPAddress accessorAddress, String appId, String appSecret) throws AccessDeniedException {
        UserApplicationEntity userApp = userAppService.getUserApplication(appId, appSecret);
        UserEntity user = null;
        if (userApp == null) {
            if (!allowAnonymous) {
                throw new AccessDeniedException("This BTN instance required login for submit ping.");
            }
        } else {
            user = userApp.getUser();
        }
        dao.insertBan(ping, accessorAddress, user == null ? 0 : user.getId(), appId, appSecret, UUID.randomUUID().toString());
        clientDiscoveryService.discover(user, ping.getBans().stream().map(BtnBan::getPeer).toList());
    }
}
