package org.flowable.ui.modeler.rest.app;

import org.flowable.aditoDataService.AditoDeploymentService;
import org.flowable.aditoDataService.Model.ProcessRepresentation;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
@RequestMapping("/app")
public class DeploymentResource {

    @Autowired
    private AditoDeploymentService aditoDeploymentService;

    @Autowired
    private ModelService modelService;

    @PostMapping(value = "/rest/process-deploy/{modelId}")
    public ProcessRepresentation deployProcess(@PathVariable String modelId)
    {
        Model processModel = modelService.getModel(modelId);
        byte[] processModelXML = modelService.getBpmnXML(modelService.getBpmnModel(processModel));

        return aditoDeploymentService.deployProcess(new String(processModelXML));
    }

    @GetMapping(value = "/rest/adito-process-definition-url", produces = "text/plain")
    public String getAditoProcessDefinitionUrl ()
    {
        return aditoDeploymentService.getProcessDefinitionUrl();
    }
}
