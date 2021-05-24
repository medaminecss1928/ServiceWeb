package de.tekup.loan.endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import java.util.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import de.tekup.soap.models.whitetest.Address;
import de.tekup.soap.models.whitetest.Exam;
import de.tekup.soap.models.whitetest.GetExamsRequest;
import de.tekup.soap.models.whitetest.GetExamsResponse;
import de.tekup.soap.models.whitetest.Student;
import de.tekup.soap.models.whitetest.StudentRequest;
import de.tekup.soap.models.whitetest.WhiteTestResponse;
@Endpoint
public class WhiteTestEndPoint {
	
	List<Student> students = new ArrayList<>();
	List<Exam> exams = new ArrayList<>();
	public static final String nameSpace="http://www.tekup.de/soap/models/whitetest";
	@PayloadRoot(namespace = nameSpace, localPart = "StudentRequest")
	@ResponsePayload
	public WhiteTestResponse getWhiteTestStatus(@RequestPayload StudentRequest request) {
		
		Exam e = new Exam();
	    e.setCode("101");
	    e.setName("OCA JAVA");
	    exams.add(e);
	    Address ad=new Address();
    	ad.setCity("sousse");
    	ad.setPosteCode(1011);
    	ad.setStreet("Farht Hached");
        Student s = new Student();
        s.setId(1);
        s.setName("Nesrine");
        s.setAddress(ad);
        students.add(s.getId(),s);
		WhiteTestResponse response = new WhiteTestResponse();
		List<String> criteriaMismatchs = response.getCriteriaMismatch();
        if(existStudentById(request.getStudentId())==false) 
        	criteriaMismatchs.add("Student id doesn't exist");
    	if(existExamById(request.getExamCode())==false) 
    		criteriaMismatchs.add("Exam code doesn't exist");
        if(criteriaMismatchs.isEmpty()) {
			response.setIsEligeble(true);
			Student st=findStudentById(request.getStudentId());
    		Exam ex=findExamById(request.getExamCode());
    		response.setStudent(st);
    		response.setExam(ex);
    		
    		XMLGregorianCalendar d = null;
    		try {
    			DatatypeFactory dataFactory = DatatypeFactory.newInstance();
    			d = dataFactory.newXMLGregorianCalendar(new GregorianCalendar());
    			GregorianCalendar calendar = d.toGregorianCalendar();
    			calendar.add(Calendar.DAY_OF_MONTH, +15);
    			d= dataFactory.newXMLGregorianCalendar(calendar);
    		} catch (DatatypeConfigurationException exp) {		
    			exp.printStackTrace();
    		}
    		
           
    		response.setDate(d);
		} else {
			response.setIsEligeble(false);	
		}
        		
       return response;
		
        
		
	}
	public boolean existExamById(String id) {
        return exams.contains(id);
  }
    public Exam findExamById(String id) {
        for(int i=0;i<exams.size();i++) {
        	if(exams.get(i).getCode()==id) {
        		return exams.get(i);
        	}
        }
        return null;
    }
    public boolean existStudentById(int id) {
        return students.contains(id);
  }
    public Student findStudentById(int id) {
        for(int i=0;i<students.size();i++) {
        	if(students.get(i).getId()==id) {
        		return students.get(i);
        	}
        }
        return null;
    }
    @PayloadRoot(namespace = nameSpace, localPart = "GetExamsRequest")
	@ResponsePayload
    public GetExamsResponse listExams(@RequestPayload  GetExamsRequest request) {
	GetExamsResponse response = new GetExamsResponse();
		
		response.setExams(exams);
		return response;
	}
  

}