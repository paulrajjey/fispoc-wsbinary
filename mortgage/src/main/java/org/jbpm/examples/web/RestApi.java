package org.jbpm.examples.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.jbpm.examples.ejb.ProcessOperationException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskData;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.api.model.ContentData;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceResponse;
import com.redhat.bpms.examples.mortgage.Application;
import com.redhat.bpms.examples.mortgage.Applicant;
import com.redhat.bpms.examples.mortgage.Appraisal;
import com.redhat.bpms.examples.mortgage.Property;


public class RestApi {

	//String deploymentId = "redhat.demo.demoprocess:demoprocess:1.0";
	String deploymentId = "com.fsi.bpms.mortgage:mortgage:1.5";
	//String deploymentId = "redhat.dgdemo:dgdemo:1.0";
	
    URL deploymentUrl;

    String userId = "admin";
    String password = "admin";

    //String userId = "bpm-admin";
    //String password = "JAXGCt7m_wig";
   
	public RestApi()  {
		super();
		 try {
			this.deploymentUrl = new URL("http://localhost:8080/business-central");
	//		 this.deploymentUrl = new URL("http://redhatbpms-brmsdemo.rhcloud.com/business-central");
			 //redhatbpms-brmsdemo.rhcloud.com/business-central
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public RuntimeEngine getRuntimeEngine(){
	RemoteRestRuntimeFactory restSessionFactory 
    = new RemoteRestRuntimeFactory(deploymentId, deploymentUrl, userId, password);
	 // create REST request
    RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
    return engine;
}
	
	
public void startProcess(Application application){
	
	String taskUserId = userId;
    
    RemoteRestRuntimeFactory restSessionFactory 
        = new RemoteRestRuntimeFactory(deploymentId, deploymentUrl, userId, password);

    // create REST request
    RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
    KieSession ksession = engine.getKieSession();
    Map<String, Object> params = new HashMap<String, Object>();
    
   
    params.put("application", application);
    
   
    ProcessInstance processInstance = ksession.startProcess("FSIMortgageApplication", params);
    //ksession.fireAllRules();
    System.out.println("process id " + processInstance.getProcessId());
    System.out.println("process id " + processInstance.getId());
   

}	
public void sendSignl(String eventName,Object data,long id){
	RuntimeEngine engine = getRuntimeEngine();
	KieSession ksession = engine.getKieSession();
	ksession.signalEvent(eventName, data, id);
}
public  void startTask(long taskId, String actorId){
	RuntimeEngine engine = getRuntimeEngine();
	TaskService taskService = engine.getTaskService();
	taskService.start(taskId, actorId);
}
public  Task getTask(long taskId) throws Exception {
    
	 Task task;
    Map<String,Object> content;
    try {
    	
    	RuntimeEngine engine = getRuntimeEngine();
    	TaskService taskService = engine.getTaskService();
    	task  = taskService.getTaskById(taskId);
    	//taskService.getContentById(taskId);
      // task =  (org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskResponse)taskService.getTaskById(taskId);
        //content = ((InternalTaskService) taskService).getTaskContent(taskId);
       
    } catch (Exception e) {
       
        throw new ProcessOperationException("Cannot get task " + taskId, e);
    }
    return task;
}
public List<TaskSummary> getTaskList(String actorId){
	
	RuntimeEngine engine = getRuntimeEngine();
	TaskService taskService = engine.getTaskService();
	List<TaskSummary> list;
    try {
        list = taskService.getTasksAssignedAsPotentialOwner(actorId, "en-UK");
       
       
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    return list;
	
}

public void getDeployment(){
	
	
}
public static void main(String[] ar){
	
	RestApi api = new RestApi();
	/*Application application = new Application();
	application.setDownPayment(28000);
	application.setAmortization(10);
	Applicant  applicant = new Applicant();
	applicant.setIncome(80000);
	applicant.setName("jey");
	Integer tt = new Integer(123456789);
	applicant.setSsn(tt);
	
	Property property = new Property();
	property.setPrice(100000);
	property.setAddress("239303");
	
	Appraisal appraisal = new Appraisal();
	application.setApplicant(applicant);
	application.setProperty(property);
	application.setAppraisal(appraisal);
	//RestApi api = new RestApi();
	api.startProcess(application);
	System.out.println("my name is " );
	
	
	List<TaskSummary> list = api.getTaskList("jey");
	*/
	
	
	try {
		//org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskResponse tsk = (org.kie.services.client.serialization.jaxb.impl.task.JaxbTaskResponse)api.getTask(2);
		//TaskData dat = tsk.getTaskData();
		List<TaskSummary> list = api.getTaskList("jey");
		TaskSummary sum = list.get(0);
		long id = sum.getId();
		
		RuntimeEngine engine = api.getRuntimeEngine();
    	TaskService taskService = engine.getTaskService();
    	Task task  = taskService.getTaskById(id);
    	TaskData dta = task.getTaskData();
    	System.out.println("test" +  id );
    	Content cont = taskService.getContentById(id);
    	
    	
    	//taskService.getContentById(arg0);
    	//TaskData dt  = task.getTaskData();
    	//long ids = dt.getDocumentContentId();
		System.out.println("test" +  cont );
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//api.startProcess();
}
}
