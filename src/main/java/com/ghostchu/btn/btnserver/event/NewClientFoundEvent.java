package com.ghostchu.btn.btnserver.event;

import com.ghostchu.btn.btnserver.clientdiscovery.ClientDiscoveryEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Setter
@Getter
public class NewClientFoundEvent extends ApplicationEvent {
    private List<ClientDiscoveryEntity> clients;
    public NewClientFoundEvent( Object source, List<ClientDiscoveryEntity> clients) {
        super(source);
        this.clients = clients;
    }
}
