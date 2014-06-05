/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.examples.web;

import org.jbpm.examples.ejb.TaskBean;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskData;
import org.kie.api.task.model.TaskSummary;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Model
public class TaskMortController {

   ///@EJB
  // TaskBean taskBean;

    @Inject
    FacesContext facesContext;

    @Inject
    Logger logger;

    private String comment;
    private Map<String,Object> content;
    private  TaskSummary task;
    private long taskId;
    private String user;
    private List<TaskSummary> tasks;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

   
	public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Produces
    public List<TaskSummary> getTasks() {
        return tasks;
    }


    public TaskSummary getTask() {
		return task;
	}

	public void setTask(TaskSummary task) {
		this.task = task;
	}

	public void setTasks(List<TaskSummary> tasks) {
		this.tasks = tasks;
	}

	public void retrieveTasks () {
        String message;
        long id=0;
        try {
        	RestApi api = new RestApi();
        	RuntimeEngine engine =  api.getRuntimeEngine();
        	TaskService taskService = engine.getTaskService();
        	
        	List<TaskSummary> list;
            try {
                tasks = taskService.getTasksAssignedAsPotentialOwner("jey", "en-UK");
               
                //TaskSummary tsk = tasks.get(0);
                task = tasks.get(0);
                id = task.getId();
                 Task task = taskService.getTaskById(id);
                 if(task != null){
                	 System.out.println("id " + task.getId());
                	 taskService.start(id, "jey");
                	 
                	 //TaskData data = task.getTaskData();
                	
                 }else{
                	 System.out.println("no task " ); 
                 }
               
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        	/*
        	//tasks = api.getTaskList("jey");
           // tasks = taskBean.retrieveTaskList(user);
        	TaskSummary tsk = tasks.get(0);
        	id =tsk.getId();
        	task = api.getTask(id);
        	*/
            message = "Retrieved " + tasks.size() + " task(s) for user " + user + "." + id;
            logger.info(message);
        } catch (Exception e) {
            message = "Cannot retrieve task list for user " + user + "." + id;
            logger.log(Level.SEVERE, message, e);
            facesContext.getExternalContext().getFlash()
                    .put("msg", message);
        }
    }

    public void queryTask() {
        String message;
        try {
        	RestApi api = new RestApi();
        	//task =  api.getTask(taskId);
        	          //  task = api.getTaskList("jey");//taskBean.getTask(taskId);
         //   content = taskBean.getContent();
            message = "Loaded task " + taskId + ".";
            logger.info(message);
        } catch (Exception e) {
            message = "Unable to query for task with id = " + taskId;
            logger.log(Level.SEVERE, message, e);
            facesContext.getExternalContext().getFlash()
                    .put("msg", message);
        }
    }

    public String approveTask() {
        String message;
        try {
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("out_comment", comment);
            RestApi api = new RestApi();
            api.startTask(taskId, user);
            //taskBean.approveTask(user, taskId, result);
            message = "The task " + taskId + " has been successfully approved.";
            logger.info(message);
        } catch (Exception e) {
            message = "Unable to approve the task " + taskId + ".";
            logger.log(Level.SEVERE, message, e);
        }
        facesContext.getExternalContext().getFlash()
                .put("msg", message);
        return "index.xhtml?faces-redirect=true";
    }
}
