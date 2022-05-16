package org.flowable.aditoDataService.model;

public class ProcessDeployment
{
    private String processXML;
    private String deploymentId;

    public ProcessDeployment(String pProcessXML)
    {
        processXML = pProcessXML;
    }

    public String getProcessXML() {
        return processXML;
    }

    public void setProcessXML(String processXML) {
        this.processXML = processXML;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
}
