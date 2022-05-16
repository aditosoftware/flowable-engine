package org.flowable.ui.modeler.rest.app;

import org.flowable.aditoDataService.EntityDataService;
import org.flowable.aditoDataService.model.ConsumerDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@RequestMapping("/app")
public class EntityResource {

    @Autowired
    private EntityDataService entityDataService;

    @GetMapping(value = "/rest/consumers", produces = "application/json")
    public List<ConsumerDataModel> getConsumers ()
    {
        return entityDataService.getConsumers();
    }
}
