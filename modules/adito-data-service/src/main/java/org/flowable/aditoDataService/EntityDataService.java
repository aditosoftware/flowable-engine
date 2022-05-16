package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.model.ConsumerDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EntityDataService
{
    @Autowired
    protected RestClientService restClientService;

    public List<ConsumerDataModel> getConsumers() {

        List<ConsumerDataModel> consumers = new ArrayList<>();
        try {
            String wsResult = restClientService.get(RestURLs.CONSUMERS);
            Gson gson = new Gson();
            ConsumerDataModel[] wsConsumers = gson.fromJson(wsResult, ConsumerDataModel[].class);
            consumers.addAll(Arrays.asList(wsConsumers));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return consumers;
    }
}
