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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.springframework.stereotype.Component;
import org.flowable.ui.common.model.TaskVariableRepresentation;

import java.util.*;

@Component
@RestController
@RequestMapping("/app")
public class TaskVariablesResource {

    @Value("${aditoUrl:https://host.docker.internal:8443}")
    private String aditoUrl;

    @GetMapping(value = "/rest/taskvariables")
    public List<TaskVariableRepresentation> getVariables(@RequestParam(value = "jditoProcess", required = false) String jditoProcess,
                                                         @RequestParam(value = "currentValues", required = false) String currentValues) {

        List<TaskVariableRepresentation> variables = new ArrayList<>();

        if (aditoUrl == null || aditoUrl.isEmpty())
            return variables;

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

            clientBuilder.defaultHeader("Jditoprocess", jditoProcess);
            clientBuilder.defaultHeader("Currentvalues", currentValues);

            Gson gson = new Gson();

            WebClient.RequestHeadersSpec<?> spec = clientBuilder.build().get()
                    .uri("/services/rest/workflowServiceTaskParams_rest");
            String wsResult = spec.retrieve().bodyToMono(String.class).block();

            TaskVariableRepresentation[] wsVars = gson.fromJson(wsResult, TaskVariableRepresentation[].class);
            variables = Arrays.asList(wsVars);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return variables;
    }
}
