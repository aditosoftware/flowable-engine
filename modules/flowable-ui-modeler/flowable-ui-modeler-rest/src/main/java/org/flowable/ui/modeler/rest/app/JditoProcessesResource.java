package org.flowable.ui.modeler.rest.app;

import org.flowable.aditoDataService.JditoProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Component
@RestController
@RequestMapping("/app")
public class JditoProcessesResource
{
    @Autowired
    private JditoProcessService jditoProcessService;

    @GetMapping(value = "/rest/jditoprocesses")
    public Map<String, String> getProcesses()
    {
        return jditoProcessService.getProcesses();
    }
}
