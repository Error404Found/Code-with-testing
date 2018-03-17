package com.abhinendra.services.Implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;

import com.abhinendra.domain.Person;
import com.abhinendra.domain.Transaction;
import com.abhinendra.services.PersonService;
import com.abhinendra.services.TransactionService;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest 
{
	@Mock
	PersonService PersonSerMock;
	
	
	@InjectMocks
	PersonServiceImpl PersonImp; //gives repository issues
	
	@Before
	public void InitializeMockito()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCheckBalanceFile1()
	{
		Date yourDate = new Date(System.currentTimeMillis());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(yourDate);
		//cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date desiredDate = cal.getTime();
		
Transaction expectedtransaction = new Transaction("jIXOxhN3Zq0d","LaurineStoker","VenettaCharpentier",desiredDate,(float) 30688.10,"110879126035","113409298468","upload_fail");

		Person person=new Person("9457847595","Juie",98598);
		
//	when(((OngoingStubbing) PersonSerMock.findOnebyId((any(Transaction.class)))).thenReturn(person);
	
	boolean val=PersonImp.checkBalance(expectedtransaction);
	
	assertEquals(true,val);

	}
	
	
	
}
