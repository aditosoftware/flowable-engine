package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.Model.TaskVariableRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class JditoProcessService extends BaseAditoDataService
{
    public Map<String, String> getProcesses() {

        Map<String, String> processes = new HashMap<>();

        if (canCallWebservices())
        {
            try {
                WebClient.Builder clientBuilder = getWebClientBuilder();

                WebClient.RequestHeadersSpec<?> spec = clientBuilder.build().get()
                        .uri("/services/rest/workflowServiceTasks_rest");
                String wsResult = spec.retrieve().bodyToMono(String.class).block();

                Gson gson = new Gson();

                ProcessWrapper[] wsProcesses = gson.fromJson(wsResult, ProcessWrapper[].class);
                Arrays.stream(wsProcesses).forEach(wsProcess -> processes.put(wsProcess.id, wsProcess.name));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return processes;
    }

    public List<TaskVariableRepresentation> getVariables(String jditoProcess, String currentValues) {

        List<TaskVariableRepresentation> variables = new ArrayList<>();

        if (!canCallWebservices())
            return variables;

        try {
            WebClient.Builder clientBuilder = getWebClientBuilder();

            clientBuilder.defaultHeader("Jditoprocess", jditoProcess);
            clientBuilder.defaultHeader("Currentvalues", currentValues);

            WebClient.RequestHeadersSpec<?> spec = clientBuilder.build().get()
                    .uri("/services/rest/workflowServiceTaskParams_rest");
            String wsResult = spec.retrieve().bodyToMono(String.class).block();

            Gson gson = new Gson();

            TaskVariableRepresentation[] wsVars = gson.fromJson(wsResult, TaskVariableRepresentation[].class);
            variables = Arrays.asList(wsVars);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return variables;
    }

    private static class ProcessWrapper {
        String id;
        String name;
    }
}
