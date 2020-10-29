package tn.esprit.spring.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;


@RunWith(SpringRunner.class)
@SpringBootTest
class EmployeServiceImplTest {
	private static final Logger logger = LogManager.getLogger(EmployeServiceImpl.class);
	//*** Work done by @Houssem_Eddine_Rkaiess ***//
	
	@Autowired
	IEmployeService em;

	@Test
	void AddEmployeTest() throws Exception {
		
		
		//**** Test all methods that needs the Employe ID to fetch a result ****//
		logger.info("AddEmployeTest open: ");
		// Add Employe Assert
		Employe mockEmploye = new Employe(true,"User@Email.com","#UserNameTest","@Password","#UserLastNameTest",Role.INGENIEUR);
		int expectEmpID = em.addOrUpdateEmploye(mockEmploye);
		logger.info("employeID: "+expectEmpID );
		assertNotNull(expectEmpID);  //*****
		
		// GetEmployePrenomByIdTest Assert
		String AsserPrenom = em.getEmployePrenomById(expectEmpID);
		logger.info("Prenom returned: "+AsserPrenom );
		logger.info("AddEmployeTest close: ");
		assertEquals("#UserLastNameTest", AsserPrenom);   //*****
		
	}
	
	
	@Test
	void GetAllEmployeNamesTest() {
		List<String> EmployeToTest = new ArrayList<>();
		EmployeToTest.add("Houssem");
		List<String>  EmployeNamesDromDB = em.getAllEmployeNamesJPQL();
		
		assertNotNull(EmployeNamesDromDB);  //*****
	
		for (int i =0; i<=EmployeNamesDromDB.size()-1;i++) {
			EmployeToTest.add("#UserNameTest");
		}
		assertLinesMatch(EmployeToTest, EmployeNamesDromDB);  //*****
	}
	
	
	@Test
	void GetAllEmployeTest() {
		List<Employe>  EmployeFromromDB = em.getAllEmployes();
		
		assertNotNull(EmployeFromromDB);  //*****
	
		int countEmp = em.CountEmploye();
		
		assertNotNull(countEmp);     //*********
		
		assertEquals(countEmp, EmployeFromromDB.size());  //*****
	}
	
	
	
	@Test
	void DeleteEmployeTest() {
		
	// DeleteEmployeById Assert
		// => ID 8 is not exist in database
		assertThatThrownBy(()-> {
			em.deleteEmployeById(8);
			Employe e =em.getEmployeByID(8);
		}).isInstanceOf(NoSuchElementException.class)
		.hasMessage("No value present");
	}
	
	
	@Test 
	void AjoutContratTest() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = dateFormat.parse("2021-01-1");
		Contrat contrat = new Contrat(d,"CDI",4000.0f);
		int ref = em.ajouterContrat(contrat);
		assertEquals(4, ref);
		
	}
	
	
	@Test
	void AffecterContratAEmployeTest() {
		assertThatExceptionOfType(NoSuchElementException.class)
		  .isThrownBy(() -> {
			  em.affecterContratAEmploye(1, 0);
		}).withMessage("No value present");
		
		 em.affecterContratAEmploye(1, 1);
	}
	
	
	
	@Test
	void GetSalaireByEmployeIdJPQLTest() {
		
		float salary = em.getSalaireByEmployeIdJPQL(1);
		assertNotNull(salary);
		assertEquals(4000.0, salary);
				
	}
	
	
	
	@Test
	void MettreAjourEmailByEmployeIdJPQLTest() {
		String email ="Houssem.Rekhaies@gmail.com";
		em.mettreAjourEmailByEmployeId(email, 1);
		Employe assertEmp = em.getEmployeByID(1);
		assertEquals(email, assertEmp.getEmail());
	}
	
	
	@Test
	void AuthenticationTest() {
		
		Employe mockEmploye = new Employe(1,"rkaiess","Houssem","Houssem.Rekhaies@gmail.com","test",true,Role.ADMINISTRATEUR);
		
		Employe Assertemp =em.authenticate(mockEmploye.getEmail(), mockEmploye.getPassword());

		assertEquals(mockEmploye.toString(), Assertemp.toString());	//*****
	}
	

}
