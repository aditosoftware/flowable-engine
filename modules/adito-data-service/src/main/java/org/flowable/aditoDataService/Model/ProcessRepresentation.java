package org.flowable.aditoDataService.Model;

public class ProcessRepresentation
{
    private String processXML;
    private String deploymentId;

    public ProcessRepresentation (String pProcessXML)
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
