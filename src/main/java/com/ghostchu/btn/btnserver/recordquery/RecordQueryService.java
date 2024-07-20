package com.ghostchu.btn.btnserver.recordquery;

import com.ghostchu.btn.btnserver.recordquery.bean.QueryResultDTO;
import com.ghostchu.btn.btnserver.recordquery.dao.RecordQueryDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordQueryService {

    private final RecordQueryDao recordQueryDao;

    public RecordQueryService( RecordQueryDao recordQueryDao) {
        this.recordQueryDao = recordQueryDao;
    }

    public List<QueryResultDTO> queryResult(
            String ip,
            String peerId,
            String clientName,
            boolean onlyBans,
            String connector,
            long offset,
            long limit
    ) {
        String operator = "AND";
        if (connector.equalsIgnoreCase("or")) {
            operator = "OR";
        }
        return recordQueryDao.queryRecords(ip, peerId, clientName, onlyBans, operator, offset, limit);
    }
}
