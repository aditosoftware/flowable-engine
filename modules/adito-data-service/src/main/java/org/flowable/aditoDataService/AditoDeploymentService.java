package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.model.ProcessDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AditoDeploymentService
{
    @Value("${aditoProcessDefinitionUrl:/client/WorkflowDefinition/filter}")
    private String aditoProcessDefinitionUrl;

    @Autowired
    protected RestClientService restClientService;

    public ProcessDeployment deployProcess (String pProcessModelXML)
    {
        try {
            WebClient.RequestHeadersSpec<?> spec = restClientService.getWebClient().post()
                    .uri(RestURLs.DEPLOYMENT)
                    .body(Mono.just(new ProcessDeployment(pProcessModelXML)), ProcessDeployment.class);
            String processJson =  spec.retrieve().bodyToMono(String.class).block();

            Gson gson = new Gson();
            return gson.fromJson(processJson, ProcessDeployment.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getProcessDefinitionUrl ()
    {
        return restClientService.getAditoBaseUrl().replace("host.docker.internal", "localhost") + aditoProcessDefinitionUrl;
    }
}
