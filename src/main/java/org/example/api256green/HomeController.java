package org.example.api256green;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.api256green.DTO.IpAddRestDTO;
import org.example.api256green.Repositores.IpDeviceRepository;
import org.example.api256green.Repositores.TableKeyRepository;
import org.example.api256green.DTO.TableKeyDTO;
import org.example.api256green.entites.IpDevice;
import org.example.api256green.entites.TableKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.example.api256green.Repositores.IPLoginRepository;
import org.example.api256green.Repositores.Ip256ColorGreenRepository;
import org.example.api256green.entites.IPLogin;
import org.example.api256green.entites.Ip256ColorGreen;

@RestController
@RequestMapping("/256-green-color-api")
public class HomeController {
    @Autowired
    TableKeyRepository tableKeyRepository;
    @Autowired
    Ip256ColorGreenRepository ip256ColorGreenRepository;
    @Autowired
    IPLoginRepository iPLoginRepository;
    @Autowired
    IpDeviceRepository ipDeviceRepository;
    
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

    @GetMapping("/get-ip-success")
    public List<Ip256ColorGreen> getListIPSuccess() {
        return ip256ColorGreenRepository.findAll();
    }
    
    @GetMapping("/get-all-ip-login")
    public List<IPLogin> getAllIpLogin() {
        return iPLoginRepository.findAll();
    }
    @GetMapping("/get-all-ip-device")
    public List<IpDevice> getAllIpDevice() {
        return ipDeviceRepository.findAll();
    }
    @GetMapping("/add-ip-login")
    public IPLogin getAddIpLogin() {
        IpAddRestDTO ipAddRestDTO = getIpAddRest();
        System.out.println(ipAddRestDTO.toString());

//        for (IPLogin ipLogin: iPLoginRepository.findAll()) {
//            if(!(ipLogin.getIpLogin().trim().equals(ipAddRestDTO.getIp().trim())) || iPLoginRepository.findAll() == null || iPLoginRepository.findAll().isEmpty()) {
//                IPLogin ipLoginSave = new IPLogin();
//                ipLoginSave.setIpLogin(ipAddRestDTO.getIp());
//                ipLoginSave.setCreatedate(new Date());
//                ipLoginSave.setStatus(1);
//                this.iPLoginRepository.save(ipLoginSave);
//            }
//        }
        if(iPLoginRepository.getIPLGByIp(ipAddRestDTO.getIp()) == null || iPLoginRepository.getIPLGByIp(ipAddRestDTO.getIp()).getId() == null) {
            IPLogin ipLoginSave = new IPLogin();
            ipLoginSave.setIpLogin(ipAddRestDTO.getIp());
            ipLoginSave.setCreatedate(new Date());
            ipLoginSave.setStatus(1);
            this.iPLoginRepository.save(ipLoginSave);
        }

        if(ipDeviceRepository.getIPDByIp(ipAddRestDTO.getIdDevice()) == null || ipDeviceRepository.getIPDByIp(ipAddRestDTO.getIdDevice()).getId() == null) {
            IpDevice ipDeviceSave = new IpDevice();
            ipDeviceSave.setIpDevice(ipAddRestDTO.getIdDevice());
            ipDeviceSave.setCreatedate(new Date());
            ipDeviceSave.setStatus(1);
            this.ipDeviceRepository.save(ipDeviceSave);
        }

//        for (IpDevice ipDevice: ipDeviceRepository.findAll()) {
//            if(!(ipDevice.getIpDevice().trim().equals(ipAddRestDTO.getIdDevice().trim())) || ipDeviceRepository.findAll() == null || ipDeviceRepository.findAll().isEmpty()) {
//                IpDevice ipDeviceSave = new IpDevice();
//                ipDeviceSave.setIpDevice(ipAddRestDTO.getIdDevice());
//                ipDeviceSave.setCreatedate(new Date());
//                ipDeviceSave.setStatus(1);
//                this.ipDeviceRepository.save(ipDeviceSave);
//            }
//        }

        return null;
    }

    public IpAddRestDTO getIpAddRest() {
        try {
            // Tạo HttpClient để gửi yêu cầu
            HttpClient client = HttpClient.newHttpClient();

            // URL của dịch vụ geolocation API (ip-api)
            String url = "http://ip-api.com/json/";

            // Gửi yêu cầu GET tới API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Nhận phản hồi từ API
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Nếu yêu cầu thành công (status code 200)
            if (response.statusCode() == 200) {
                // Phân tích dữ liệu JSON trả về
                String responseBody = response.body();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                // Lấy thông tin vị trí từ API
                String ip = jsonObject.get("query").getAsString(); // Địa chỉ IP
                String country = jsonObject.get("country").getAsString(); // Quốc gia
                String region = jsonObject.get("regionName").getAsString(); // Tỉnh/thành phố
                String city = jsonObject.get("city").getAsString(); // Thành phố
                String lat = jsonObject.get("lat").getAsString(); // Vĩ độ
                String lon = jsonObject.get("lon").getAsString(); // Kinh độ
                // Lấy địa chỉ IP cục bộ
                InetAddress inetAddress = InetAddress.getLocalHost();
                String localIp = inetAddress.getHostAddress();
                IpAddRestDTO ipAddRestDTO = new IpAddRestDTO(ip,localIp,country,region,city,lat,lon);
                return ipAddRestDTO;
            } else {
                System.out.println("Failed to get IP geolocation data.");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
