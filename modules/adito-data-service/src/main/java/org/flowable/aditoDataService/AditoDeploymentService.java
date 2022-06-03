package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.model.ProcessDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${disableDeployRedirect:false}")
    private boolean disableDeployRedirect;

    @Autowired
    protected RestClientService restClientService;

    private final Logger logger = LoggerFactory.getLogger(AditoDeploymentService.class);

    public ProcessDeployment deployProcess (String pProcessModelXML)
    {
        try {
            logger.debug("POST " + RestURLs.DEPLOYMENT);
            WebClient.RequestHeadersSpec<?> spec = restClientService.getWebClient().post()
                    .uri(RestURLs.DEPLOYMENT)
                    .body(Mono.just(new ProcessDeployment(pProcessModelXML)), ProcessDeployment.class);
            String processJson =  spec.retrieve().bodyToMono(String.class).block();
            Gson gson = new Gson();
            return gson.fromJson(processJson, ProcessDeployment.class);

        } catch (Exception e) {
            logger.error("Process model could not be deployed", e);
        }

        return null;
    }

    public String getProcessDefinitionUrl ()
    {
        if (disableDeployRedirect)
            return null;
        return restClientService.getAditoBaseUrl().replace("host.docker.internal", "localhost") + aditoProcessDefinitionUrl;
    }
}
