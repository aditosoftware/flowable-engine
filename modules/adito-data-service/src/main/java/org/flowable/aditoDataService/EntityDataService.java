package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.model.ConsumerDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EntityDataService
{
    @Autowired
    protected RestClientService restClientService;

    private final Logger logger = LoggerFactory.getLogger(EntityDataService.class);

    public List<ConsumerDataModel> getConsumers() {

        List<ConsumerDataModel> consumers = new ArrayList<>();
        try {
            logger.debug("GET " + RestURLs.CONSUMERS);
            String wsResult = restClientService.get(RestURLs.CONSUMERS);
            Gson gson = new Gson();
            ConsumerDataModel[] wsConsumers = gson.fromJson(wsResult, ConsumerDataModel[].class);
            consumers.addAll(Arrays.asList(wsConsumers));

        } catch (Exception e) {
            logger.error("Failed to load user task consumers", e);
        }

        return consumers;
    }
}
