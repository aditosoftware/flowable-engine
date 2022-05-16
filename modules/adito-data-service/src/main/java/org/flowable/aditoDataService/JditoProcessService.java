package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.model.TaskFormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class JditoProcessService
{
    @Autowired
    protected RestClientService restClientService;

    public Map<String, String> getProcesses() {

        Map<String, String> processes = new HashMap<>();

        try {
            String wsResult = restClientService.get(RestURLs.JDITOPROCESS);
            Gson gson = new Gson();
            ProcessWrapper[] wsProcesses = gson.fromJson(wsResult, ProcessWrapper[].class);
            Arrays.stream(wsProcesses).forEach(wsProcess -> processes.put(wsProcess.id, wsProcess.name));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return processes;
    }

    public List<TaskFormField> getVariables(String jditoProcess, String currentValues) {

        List<TaskFormField> variables = new ArrayList<>();

        try {
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            //queryParams.put("jditoprocess", Collections.singletonList(jditoProcess));
            //queryParams.put("currentvalues", Collections.singletonList(currentValues));
            Map<String, String> headers = new HashMap<>();
            headers.put("Jditoprocess", jditoProcess);
            headers.put("Currentvalues", currentValues);
            String wsResult = restClientService.get(RestURLs.PROCESS_PARAMS, queryParams, headers);
            Gson gson = new Gson();

            TaskFormField[] wsVars = gson.fromJson(wsResult, TaskFormField[].class);
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
