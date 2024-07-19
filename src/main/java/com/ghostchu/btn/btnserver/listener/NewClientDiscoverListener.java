//package com.ghostchu.btn.btnserver.listener;
//
//import com.ghostchu.btn.btnserver.clientdiscovery.ClientDiscoveryEntity;
//import com.ghostchu.btn.btnserver.event.NewClientFoundEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class NewClientDiscoverListener  implements ApplicationListener<NewClientFoundEvent> {
//    @Value("${mirai.config.qq}")
//    private long qq;
//    @Value("${mirai.config.verifyKey}")
//    private String verifyKey;
//    @Value("${mirai.config.mirai-url}")
//    private String host;
//    @Value("${mirai.config.push-group}")
//    private long group;
//
//
//    @Override
//    public void onApplicationEvent(NewClientFoundEvent event) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("发现 ").append(event.getClients().size()).append(" 个新的客户端特征信息").append("\n");
//        for (ClientDiscoveryEntity client : event.getClients()) {
//            builder.append("PeerID: ").append(client.getPeerId()).append("\n");
//            builder.append("客户端名称: ").append(client.getClientName()).append("\n");
//            String discover = "匿名 BTN 用户";
//            if(client.getFoundBy() != null){
//                discover = client.getFoundBy().getNickname();
//            }
//            builder.append("关联 IP: ").append(client.getFoundAtAddress()).append("\n");
//            builder.append("发现者: ").append(discover).append("\n");
//        }
////        try {
////            MiraiHttpSession session = MiraiHttp.createSession(verifyKey,host , qq);
////            session.getApi().sendGroupMessage(group, new MessageChain[]{
////                    new Plain(builder.toString())
////            });
////        } catch (VerifyKeyError | RobotNotFound e) {
////             log.error("Failed to push QQ messages", e);
////        }
//    }
//}
