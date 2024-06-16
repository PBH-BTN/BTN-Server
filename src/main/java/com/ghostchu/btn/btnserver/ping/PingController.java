package com.ghostchu.btn.btnserver.ping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostchu.btn.btnserver.bean.ClientAuthenticationCredential;
import com.ghostchu.btn.btnserver.exception.AccessDeniedException;
import com.ghostchu.btn.btnserver.ping.bean.BtnBanPing;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeerPing;
import com.ghostchu.btn.btnserver.ping.bean.BtnRule;
import com.ghostchu.btn.btnserver.util.ServletUtil;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/ping")
@Slf4j
public class PingController {
    private final UUID configVersion = UUID.randomUUID();
    @Autowired
    private PingService service;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${ping-service-config.min_protocol_version}")
    private long minProtocolVersion;
    @Value("${ping-service-config.max_protocol_version}")
    private long maxProtocolVersion;
    @Value("${ping-service-config.ability.submit_peers.interval}")
    private long abilitySubmitPeersInterval;
    @Value("${ping-service-config.ability.submit_peers.endpoint}")
    private String abilitySubmitPeersEndpoint;
    @Value("${ping-service-config.ability.submit_peers.random_initial_delay}")
    private long abilitySubmitPeersRandomInitialDelay;
    @Value("${ping-service-config.ability.submit_bans.interval}")
    private long abilitySubmitBansInterval;
    @Value("${ping-service-config.ability.submit_bans.endpoint}")
    private String abilitySubmitBansEndpoint;
    @Value("${ping-service-config.ability.submit_bans.random_initial_delay}")
    private long abilitySubmitBansRandomInitialDelay;
    @Value("${ping-service-config.ability.rules.interval}")
    private long abilityRulesInterval;
    @Value("${ping-service-config.ability.rules.endpoint}")
    private String abilityRulesEndpoint;
    @Value("${ping-service-config.ability.rules.random_initial_delay}")
    private long abilityRulesRandomInitialDelay;
    @Autowired
    private HttpServletRequest req;

    @PostMapping("/peers/submit")
    public ResponseEntity<String> submitPeers(@RequestBody @Validated BtnPeerPing ping) throws AccessDeniedException {
        ClientAuthenticationCredential cred = ServletUtil.getAuthenticationCredential(req);
        cred.verifyOrThrow();
        log.info("[DEBUG] Submit peers request from client {} with AppId={} and AppSecret={}", ServletUtil.getIP(req), cred.appId(), cred.appSecret());
        IPAddress ip = new IPAddressString(ServletUtil.getIP(req)).getAddress();
        service.handlePing(ping, ip, cred.appId(), cred.appSecret());
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/bans/submit")
    public ResponseEntity<String> submitBans(@RequestBody @Validated BtnBanPing ping) throws AccessDeniedException {
        ClientAuthenticationCredential cred = ServletUtil.getAuthenticationCredential(req);
        cred.verifyOrThrow();
        log.info("[DEBUG] Submit bans request from client {} with AppId={} and AppSecret={}", ServletUtil.getIP(req), cred.appId(), cred.appSecret());
        IPAddress ip = new IPAddressString(ServletUtil.getIP(req)).getAddress();
        service.handleBan(ping, ip, cred.appId(), cred.appSecret());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/config")
    public String config() {
        ClientAuthenticationCredential cred = ServletUtil.getAuthenticationCredential(req);
        cred.verifyOrThrow();
        log.info("[DEBUG] Config request from client {} with AppId={} and AppSecret={}", ServletUtil.getIP(req), cred.appId(), cred.appSecret());
        IPAddress ip = new IPAddressString(ServletUtil.getIP(req)).getAddress();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("min_protocol_version", minProtocolVersion);
        jsonObject.addProperty("max_protocol_version", maxProtocolVersion);
        JsonObject ability = new JsonObject();

        JsonObject abilitySubmitPeers = new JsonObject();
        abilitySubmitPeers.addProperty("interval", abilitySubmitPeersInterval);
        abilitySubmitPeers.addProperty("endpoint", abilitySubmitPeersEndpoint);
        abilitySubmitPeers.addProperty("random_initial_delay", abilitySubmitPeersRandomInitialDelay);
        ability.add("submit_peers", abilitySubmitPeers);

        JsonObject abilitySubmitBans = new JsonObject();
        abilitySubmitBans.addProperty("interval", abilitySubmitBansInterval);
        abilitySubmitBans.addProperty("endpoint", abilitySubmitBansEndpoint);
        abilitySubmitBans.addProperty("random_initial_delay", abilitySubmitBansRandomInitialDelay);
        ability.add("submit_bans", abilitySubmitBans);

        JsonObject abilityRules = new JsonObject();
        abilityRules.addProperty("interval", abilityRulesInterval);
        abilityRules.addProperty("endpoint", abilityRulesEndpoint);
        abilityRules.addProperty("random_initial_delay", abilityRulesRandomInitialDelay);
        ability.add("rules", abilityRules);


        JsonObject abilityReconfigure = new JsonObject();
        abilityReconfigure.addProperty("interval", 1000 * 60 * 60 * 3);
        abilityReconfigure.addProperty("random_initial_delay", abilityRulesRandomInitialDelay);
        abilityReconfigure.addProperty("version", configVersion.toString());
        ability.add("reconfigure", abilityReconfigure);

        jsonObject.add("ability", ability);

        return jsonObject.toString();
    }

    @GetMapping("/rules/retrieve")
    public ResponseEntity<String> rule() throws IOException {
        ClientAuthenticationCredential cred = ServletUtil.getAuthenticationCredential(req);
        cred.verifyOrThrow();
        log.info("[DEBUG] Rule request from client {} with AppId={} and AppSecret={}", ServletUtil.getIP(req), cred.appId(), cred.appSecret());
        String version = req.getParameter("rev");
        BtnRule btn = service.generateJsonRawMap();
        String rev = Hashing.goodFastHash(32).hashString(objectMapper.writeValueAsString(btn), StandardCharsets.UTF_8).toString();
        if (rev.equals(version)) {
            log.info("No rule update for user {} - {}", ServletUtil.getIP(req), cred.appId());
            return ResponseEntity.status(204).build();
        }
        btn.setVersion(rev);
        log.info("Send new rule to user {} - {} -> {}", ServletUtil.getIP(req), cred.appId(), rev);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(btn));
    }
}
