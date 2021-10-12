package org.flowable.ui.modeler.rest.app;

import com.google.gson.Gson;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RestController
@RequestMapping("/app")
public class JditoProcessesResource {

    @Value("${aditoUrl}")
    private String aditoUrl;

    @GetMapping(value = "/rest/jditoprocesses")
    public Map<String, String> getProcesses() {

        Map<String, String> processes = new HashMap<>();

        if (aditoUrl != null && !aditoUrl.isEmpty()) {

            try {
                SslContext sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
                HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

                WebClient.Builder clientBuilder = WebClient.builder()
                        .baseUrl(aditoUrl)
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .defaultHeaders(headers -> headers.setBasicAuth("flowableIdmService", "HczABCxBEUKSmwQEnT8vbmkE8Bj1hcXOKSbsLWBg"))
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                Gson gson = new Gson();

                WebClient.RequestHeadersSpec<?> spec = clientBuilder.build().get()
                        .uri("/services/rest/workflowServiceTasks_rest");
                String wsResult = spec.retrieve().bodyToMono(String.class).block();

                ProcessWrapper[] wsProcesses = gson.fromJson(wsResult, ProcessWrapper[].class);
                Arrays.stream(wsProcesses).forEach(wsProcess -> processes.put(wsProcess.id, wsProcess.name));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return processes;
    }

    private static class ProcessWrapper {
        String id;
        String name;
    }

}
