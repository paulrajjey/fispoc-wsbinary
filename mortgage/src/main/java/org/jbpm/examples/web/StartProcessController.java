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

//import org.jbpm.examples.ejb.ProcessBean;

//import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.redhat.bpms.examples.mortgage.Application;
import com.redhat.bpms.examples.mortgage.Applicant;
import com.redhat.bpms.examples.mortgage.Appraisal;
import com.redhat.bpms.examples.mortgage.Property;
@Model
public class StartProcessController {

   
    @Inject
    FacesContext facesContext;

    @Inject
    Logger logger;

    private String name;
    private String ssn;
    private Integer annualIncome;
    private String address;
    private Integer salesPrice;
    private Integer downPayment;
    private Integer amortization;
    private String eventName;
    private String eventData;
    private Integer mortgageId;
    
    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSsn() {
		return ssn;
	}


	public void setSsn(String ssn) {
		this.ssn = ssn;
	}


	public Integer getAnnualIncome() {
		return annualIncome;
	}


	public void setAnnualIncome(Integer annualIncome) {
		this.annualIncome = annualIncome;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getSalesPrice() {
		return salesPrice;
	}


	public void setSalesPrice(Integer salesPrice) {
		this.salesPrice = salesPrice;
	}


	public Integer getDownPayment() {
		return downPayment;
	}


	public void setDownPayment(Integer downPayment) {
		this.downPayment = downPayment;
	}


	public Integer getAmortization() {
		return amortization;
	}


	public void setAmortization(Integer amortization) {
		this.amortization = amortization;
	}


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getEventData() {
		return eventData;
	}


	public void setEventData(String eventData) {
		this.eventData = eventData;
	}
	
	

	public Integer getMortgageId() {
		return mortgageId;
	}


	public void setMortgageId(Integer mortgageId) {
		this.mortgageId = mortgageId;
	}


	public String sendSignal(){
		String message;
		 try {
			 RestApi api = new RestApi();
			 api.sendSignl(eventName, eventData, mortgageId.longValue());
		 } catch (Exception e) {
	            message = "Unable to start the business process.";
	            logger.log(Level.SEVERE, message, e);
	        }
	        //facesContext.getExternalContext().getFlash()
	          //      .put("msg", message);
	        return "index.xhtml?faces-redirect=true";
		
		
	}

	public String startProcess() {
        String message;
        try {
            //long processInstanceId = processBean.startProcess(recipient, reward);
            //message = "Process instance " + processInstanceId + " has been successfully started." ;
            //logger.info(message);
        	Application application = new Application();
        	application.setDownPayment(downPayment);
        	application.setAmortization(amortization);
        	Applicant  applicant = new Applicant();
        	applicant.setIncome(annualIncome);
        	applicant.setName(name);
        	applicant.setSsn(Integer.valueOf(ssn));
        	
        	Property property = new Property();
        	property.setPrice(salesPrice);
        	property.setAddress(address);
        	Appraisal appraisal = null;
        	if(downPayment > 29000){
        		appraisal = new Appraisal();
        	}
        	application.setApplicant(applicant);
        	application.setProperty(property);
        	application.setAppraisal(appraisal);
        	RestApi api = new RestApi();
        	api.startProcess(application);
        	System.out.println("my name is " + name);
        	logger.info("start controller");
        } catch (Exception e) {
            message = "Unable to start the business process.";
            logger.log(Level.SEVERE, message, e);
        }
        //facesContext.getExternalContext().getFlash()
          //      .put("msg", message);
        return "index.xhtml?faces-redirect=true";
    }
}
