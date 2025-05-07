package org.example.api256green;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/256-green-color-api")
public class HomeController {
    @Autowired
    TableKeyRepository tableKeyRepository;
    @GetMapping("/get-key")
    public List<Object[]> getAll() {
        return tableKeyRepository.getAllKey();
    }
    @GetMapping("/get-key-v2")
    public List<TableKeyDTO> getAllv2() {
        List<TableKeyDTO> listTableKey = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        for (TableKey tableKey: tableKeyRepository.findAll()) {
            // Định dạng thời gian thành String với định dạng "HH:mm dd-MM-yyyy"
            String formattedDate = formatter.format(tableKey.getCreatedate());
            // Sử dụng DTO để trả về dữ liệu, với createdate là String đã được định dạng
            TableKeyDTO tableKeyDTO = new TableKeyDTO(
                    tableKey.getId(),
                    tableKey.getKeyword(),
                    formattedDate,  // Trả về createdate dưới dạng String
                    tableKey.getStatus()
            );
            listTableKey.add(tableKeyDTO);
        }
        return listTableKey;
    }

    @GetMapping("/get-key-v3")
    public List<TableKeyDTO> getAllv3() {
        List<TableKeyDTO> listTableKey = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");

        // Lấy thời gian hiện tại và trừ đi 24 giờ
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAgo = now.minusHours(24);

        // Chuyển LocalDateTime thành Date để so sánh với createdate
        Date twentyFourHoursAgoDate = Date.from(twentyFourHoursAgo.atZone(ZoneId.systemDefault()).toInstant());

        // Duyệt qua tất cả các bản ghi từ repository
        for (TableKey tableKey : tableKeyRepository.findAll()) {
            // Lấy createdate từ cơ sở dữ liệu và kiểm tra nếu nó trong 24 giờ qua
            Date createdate = tableKey.getCreatedate();
            if (createdate != null && createdate.after(twentyFourHoursAgoDate) &&tableKey.getStatus() == 1) {
                // Định dạng thời gian thành String với định dạng "HH:mm dd-MM-yyyy"
                String formattedDate = formatter.format(createdate);
                // Sử dụng DTO để trả về dữ liệu, với createdate là String đã được định dạng
                TableKeyDTO tableKeyDTO = new TableKeyDTO(
                        tableKey.getId(),
                        tableKey.getKeyword(),
                        formattedDate,  // Trả về createdate dưới dạng String
                        tableKey.getStatus()
                );
                listTableKey.add(tableKeyDTO);
            }
        }
        return listTableKey;
    }

    @PostMapping("/add-new-key")
    public TableKey getAllv2(@RequestBody TableKeyDTO tableKeyDTO) {
        for (TableKey tableKeyUpdate: this.tableKeyRepository.findAll()) {
            if(tableKeyUpdate.getStatus() != 0) {
                tableKeyUpdate.setStatus(0);
                this.tableKeyRepository.save(tableKeyUpdate);
            }
        }
        TableKey tableKey = new TableKey();
        tableKey.setKeyword(tableKeyDTO.getKeyword());
        tableKey.setCreatedate(new Date());
        tableKey.setStatus(1);
        return this.tableKeyRepository.save(tableKey);
    }

    @DeleteMapping("/delete-all")
    public void getDeleteKey() {
        for (TableKey tableKeyUpdate: this.tableKeyRepository.findAll()) {
            this.tableKeyRepository.deleteById(tableKeyUpdate.getId());
        }
    }

}
