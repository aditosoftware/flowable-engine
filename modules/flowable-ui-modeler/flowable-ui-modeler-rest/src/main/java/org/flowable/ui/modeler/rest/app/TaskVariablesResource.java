package org.flowable.ui.modeler.rest.app;

import org.flowable.aditoDataService.JditoProcessService;
import org.flowable.aditoDataService.model.TaskFormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@RequestMapping("/app")
public class TaskVariablesResource {

    @Autowired
    private JditoProcessService jditoProcessService;

    @GetMapping(value = "/rest/taskvariables")
    public List<TaskFormField> getVariables(@RequestParam(value = "jditoProcess", required = false) String jditoProcess,
                                            @RequestParam(value = "currentValues", required = false) String currentValues) {

        return jditoProcessService.getVariables(jditoProcess, currentValues);
    }
}
