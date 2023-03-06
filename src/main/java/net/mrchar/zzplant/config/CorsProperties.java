package net.mrchar.zzplant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private List<String> origins;
    private List<String> methods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private List<String> headers = List.of("*");
    private boolean withCredentials = true;
}
