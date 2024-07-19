package com.ghostchu.btn.btnserver.clientdiscovery;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientDiscoveryController {
    @Autowired
    private ClientDiscoveryService service;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/clientdiscovery")
    @ResponseBody
    public List<ClientDiscoveryEntityDto> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "50") Integer size){
       Pageable pageable = Pageable.ofSize(size).withPage(page);
       return service.queryLastFoundClients(pageable).stream().map((element) -> modelMapper.map(element, ClientDiscoveryEntityDto.class)).collect(Collectors.toList());
    }

    @GetMapping("/clientdiscovery/rank")
    public List<UserDiscoveryCountBaked> getUserDiscoveryRank(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return service.discoveryRank(page, size);
    }
}
