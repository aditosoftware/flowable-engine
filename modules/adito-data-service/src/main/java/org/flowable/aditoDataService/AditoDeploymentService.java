package org.flowable.aditoDataService;

import com.google.gson.Gson;
import jdk.internal.joptsimple.internal.Strings;
import org.flowable.aditoDataService.Model.ProcessRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AditoDeploymentService extends BaseAditoDataService
{
    @Value("${aditoProcessDefinitionUrl:#{null}}")
    private String aditoProcessDefinitionUrl;

    public ProcessRepresentation deployProcess (String pProcessModelXML)
    {
        if (canCallWebservices())
        {
            try {
                WebClient.Builder clientBuilder = getWebClientBuilder();

                WebClient.RequestHeadersSpec<?> spec = clientBuilder.build().post()
                        .uri("/services/rest/workflowDeploy_rest")
                        .body(Mono.just(new ProcessRepresentation(pProcessModelXML)), ProcessRepresentation.class);
                String processJson =  spec.retrieve().bodyToMono(String.class).block();

                Gson gson = new Gson();
                return gson.fromJson(processJson, ProcessRepresentation.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getProcessDefinitionUrl ()
    {
        if (Strings.isNullOrEmpty(aditoProcessDefinitionUrl))
            return aditoBaseUrl.replace("host.docker.internal", "localhost") + "/client/WorkflowDefinition/filter";

        return aditoProcessDefinitionUrl;
    }
}
