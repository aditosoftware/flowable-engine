package org.flowable.engine.impl.cmd;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.form.FormHandlerHelper;
import org.flowable.engine.impl.form.TaskFormHandler;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.Flowable5Util;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.io.Serializable;
import java.util.List;

public class GetTaskFormFieldListenerCmd implements Command<List<String>>, Serializable
{

    private static final long serialVersionUID = 1L;
    protected String taskId;

    public GetTaskFormFieldListenerCmd(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public List<String> execute(CommandContext commandContext) {
        TaskEntity task = CommandContextUtil.getProcessEngineConfiguration(commandContext).getTaskServiceConfiguration().getTaskService().getTask(taskId);
        if (task == null) {
            throw new FlowableObjectNotFoundException("No task found for taskId '" + taskId + "'", Task.class);
        }

        if (task.getProcessDefinitionId() != null && Flowable5Util.isFlowable5ProcessDefinitionId(commandContext, task.getProcessDefinitionId()))
            return null;

        FormHandlerHelper formHandlerHelper = CommandContextUtil.getProcessEngineConfiguration(commandContext).getFormHandlerHelper();
        TaskFormHandler taskFormHandler = formHandlerHelper.getTaskFormHandlder(task);
        if (taskFormHandler == null) {
            throw new FlowableException("No taskFormHandler specified for task '" + taskId + "'");
        }

        return taskFormHandler.getFormFieldListeners();
    }
}
