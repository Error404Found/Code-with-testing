package com.abhinendra.services.Implementation;

import com.abhinendra.domain.Person;

import com.abhinendra.domain.SanctionList;
import com.abhinendra.domain.Transaction;
import com.abhinendra.repositories.PersonRepository;
import com.abhinendra.services.PersonService;
import com.querydsl.core.types.Predicate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Timed;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;


@Service("personService")
public class PersonServiceImpl implements PersonService {
	boolean FLAGFORCOMMIT=true;
    Predicate predicate;
    @Autowired
    PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person savePerson(Person person) throws Exception {
        return personRepository.save(person);
    }


    public boolean checkBalance(Transaction transaction)
    {
    	Person person = new Person();
    	person = findOnebyId(transaction);
    	boolean checkBool = true;
    	if(person.getBalance() < transaction.getAmount())
    		checkBool = false;
    	return checkBool;
    }
    
	@Override
	public void createUser(String filename) {
    
        Random rand = new Random();
        int range = rand.nextInt(1000000);
        float balance;       
        String charString = "0123456789";
        
        try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String name = new String();
			while((name=br.readLine()) != null)
			{
		        StringBuilder sb = new StringBuilder();
			    Person person;
	        	balance = rand.nextFloat()*range;
	            for( int i = 0 ; i < 12 ; i++ ) 
	                sb.append( charString.charAt( rand.nextInt(charString.length())));
	    		person = new Person(sb.toString(),name,balance);
	    		System.out.println(person.toString());
	    		try {
					savePerson(person);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
	}
	
    public ArrayList<Person> selectRecord() 
    {
        return (ArrayList<Person>) personRepository.findAll();
    }

	@Override
	public Person findOnebyId(Transaction transaction) 
	{
		return personRepository.findOne(transaction.getPayerAccount());
	}
	
	 public void showall()
	{
		for(Person p: personRepository.findAll())
		{
			System.out.println(p.getAccount()+" "+p.getName()+" "+p.getBalance());
		}
	}
	 
	private  TransactionTemplate transactionTemp;
	/* Timer timer =createTimer(70, "Created new timer");
	 
	 @Timed(millis = 70)
	    public void timeOutHandler(Timer timer){
	       System.out.println("timeoutHandler : ");        
	       timer.cancel();*/
	    //}
	 
	 @Transactional(rollbackFor=InsufficientAmtException.class)
	public void doTransaction(Transaction transaction) throws InsufficientAmtException
	{
		 	Random rand=new Random();
		 	int num=rand.nextInt(10);
		 	System.out.println("Random number is: "+num);
			withdraw(transaction.getPayerName(),transaction.getAmount());
			
			Person per = personRepository.findByname(transaction.getPayerName());
			System.out.println("Balance of "+per.getName()+" is :"+per.getBalance());
			try 
			{
				if(num<5)
				{
				//TimeUnit.SECONDS.sleep(3);
				
				System.out.println("Thread slept");
				}
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			deposit(transaction.getPayeeName(),transaction.getAmount());
			

			System.out.println("**************TRANSACTION COMMITED***********");
	}
	
	 @Transactional
	public void withdraw(String payerName, float Amt) 
	{
	
		System.out.println("**************inside withdraw with name:"+payerName);
		Person per = personRepository.findByname(payerName);
		System.out.println("Balance of "+per.getName()+" is :"+per.getBalance());
		float bal = per.getBalance();
		bal = bal - Amt;
		per.setBalance(bal);
		personRepository.save(per);
		System.out.println("last statement of withdraw");
		
	}
	
	 @Transactional
	public void deposit(String payeeName, float Amt) 
	{
		System.out.println("******************inside deposit with name:"+payeeName);
		Person per = personRepository.findByname(payeeName);
		System.out.println("Balance of "+per.getName()+" is :"+per.getBalance());
		float bal = per.getBalance();
		bal = bal + Amt;
		per.setBalance(bal);
		personRepository.save(per);
	}

}
