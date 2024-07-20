package com.ghostchu.btn.btnserver.recordquery;

import com.ghostchu.btn.btnserver.recordquery.bean.QueryResultDTO;
import com.ghostchu.btn.btnserver.recordquery.bean.QueryResultDisplayDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller()
@RequestMapping("/recordQuery")
public class RecordQueryController {
    private final RecordQueryService recordQueryService;

    public RecordQueryController(RecordQueryService recordQueryService) {
        this.recordQueryService = recordQueryService;
    }

    @GetMapping("")
    public String recordQuery() {
        return "recordQuery/index";
    }

    @GetMapping("/query")
    public Object recordQuery(
            Model model,
            HttpServletResponse response,
            @RequestParam("ip") String ip,
            @RequestParam("peerId") String peerId,
            @RequestParam("clientName") String clientName,
            @RequestParam(value = "onlyBans",required = false) String onlyBans,
            @RequestParam("connector") String connector,
            @RequestParam(value = "export", required = false) String export
    ) throws IOException {
        if (ip.isBlank() && peerId.isBlank() && clientName.isBlank()) {
            model.addAttribute("error", "必须指定至少一个有效的过滤器条件");
            return "common/error";
        }
        if (ip.isBlank()) {
            ip = "%";
        }
        if (peerId.isBlank()) {
            peerId = "%";
        }
        if (clientName.isBlank()) {
            clientName = "%";
        }
        boolean onlyBansFilter = onlyBans != null && onlyBans.equals("on");
//        if(offset == null){
//            offset = 0L;
//        }
//        if(limit == null){
//            limit = 1000L;
//        }
//        if(limit > 1000){
//            model.addAttribute("error", "单次查询条数不可超过 1000 条");
//            return "common/error";
//        }

        List<QueryResultDTO> result = recordQueryService.queryResult(ip, peerId, clientName, onlyBansFilter, connector, 0, 500);
        model.addAttribute("results", result.stream().map(QueryResultDisplayDTO::from));
        if (export != null && export.equalsIgnoreCase("csv")) {
            sendCSV(model, response, generateCSV(result));
            return response;
        }

        return "recordQuery/result";
    }

    private void addToZip(ZipOutputStream zipOut, InputStream inputStream, String fileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            zipOut.write(buffer, 0, bytesRead);
        }

        zipOut.closeEntry();
        inputStream.close();
    }


    private byte[] generateCSV(List<QueryResultDTO> result) throws IOException {
        String[] fields = {"insertAt", "address", "peerId", "clientName", "torrentIdentifier",
                "torrentSize", "downloaded", "uploaded", "rtDownloadedSpeed", "rtUploadedSpeed",
                "progress", "downloadProgress", "flags", "module", "rule"};
        @Cleanup
        StringWriter stringWriter = new StringWriter();
        @Cleanup
        BufferedWriter writer = new BufferedWriter(stringWriter);
        @Cleanup
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(fields));
        csvPrinter.printRecords(result);
        csvPrinter.flush();
        csvPrinter.close();
        return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void sendCSV(Model model, HttpServletResponse response,  byte[] bytes) throws IOException {
        String fileName = "btn-query-exported-" + System.currentTimeMillis() + ".zip";
        @Cleanup
        InputStream inputStream = new ByteArrayInputStream(bytes);
        StreamingResponseBody responseBody = outputStream -> {
            try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
                addToZip(zipOut, inputStream, "exported-data.csv");
                zipOut.finish();
            } catch (IOException e) {
                e.printStackTrace();
                // 处理异常
            }
        };
        response.reset();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        responseBody.writeTo(response.getOutputStream());
    }
}
