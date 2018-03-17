package com.abhinendra.services.Implementation;

import com.abhinendra.domain.Transaction;

import com.abhinendra.domain.Person;
import com.abhinendra.domain.Status;
import com.abhinendra.repositories.TransactionRepository;
import com.abhinendra.services.PersonService;
import com.abhinendra.services.SanctionService;
import com.abhinendra.services.TransactionService;
import com.querydsl.core.types.Predicate;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Timed;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    Predicate predicate;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    PersonService personService;
    @Autowired
    SanctionService sanctionService;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws Exception {
        return transactionRepository.save(transaction);
    }
    
   
   /* public void createTimer(long duration) {
    	   context.getTimerService().createTimer(duration, "Hello World!");
    	}
    
    @Timed(millis = 70)
    public void timeOutHandler(Timer timer){
       System.out.println("timeoutHandler : " + timer.getInfo());        
       timer.cancel();
    }
    
    Timer timer = timerService.createTimer(70, "Created new timer");
    
    @Timeout
    public void timeout(Timer timer) {
        System.out.println("TimerBean: timeout occurred");
    }
    */
    
    @Override
    public void  createTransactionFile(String filename)
    {
    	ArrayList<Person> personList = personService.selectRecord();
    	int noOfrecords = personList.size();
    	String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    	Random rand = new Random();
    	File file = new File(filename);
    	String payerName,payeeName,payername,payeename;
    	String date;
    	String payerAccount,payeeAccount;
    	int index;
    	float amount;
    	BufferedWriter writer = null;
    	try {
    		writer = new BufferedWriter(new FileWriter(filename));
	    	
	    	for(int numOfRecords = 0 ; numOfRecords < 5 ; numOfRecords++)
	    	{
	    		DecimalFormat f = new DecimalFormat("##.00");
	            StringBuilder refId = new StringBuilder();
	            for(int i = 0 ; i < 12 ; i++)
	                refId.append(charSet.charAt(rand.nextInt(charSet.length())));
	            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	            Calendar cal = Calendar.getInstance();
	            date = dateFormat.format(cal.getTime());
	            date = date.replaceAll("/","");
	            
	            index = rand.nextInt(noOfrecords);
	            payerName = personList.get(index).getName();
	            payername = String.format("%-35s",payerName);
	            payerAccount = personList.get(index).getAccount();
	            
	            index = rand.nextInt(noOfrecords);
	            payeeName = personList.get(index).getName();
	            payeename = String.format("%-35s",payeeName);
	            payeeAccount = personList.get(index).getAccount();
	            
	            amount = rand.nextFloat()*100000;
	            String string_form = f.format(amount);
                string_form = String.format("%1$13s",string_form);
	            
	            writer.append(refId.toString());
	            writer.append(date);
	            writer.append(payername);
	            writer.append(payerAccount);
	            writer.append(payeename);
	            writer.append(payeeAccount);
	            writer.append(string_form);
	            writer.append("\r\n");
	    	}
	    	writer.close();
    	}catch(Exception e) {}
    }
    
    @Override
	public Transaction readFile(String filename)
    {
    	 Transaction transaction= new Transaction();
    	BufferedReader br;
        try 
        {
            br = new BufferedReader(new FileReader(filename));
            String line = new String();
           
            boolean value=false;
            while((line=br.readLine()) != null)
            {
                System.out.println(line);
                transaction = setParameters(line); //mocked
                String status;
                try {
					value = fieldValidate(transaction); //mocked
					if(value == true)
					{
						status = new String(new Status().fieldValidPass);
						String payerName = transaction.getPayeeName(); 
		            	String payeeName = transaction.getPayerName();
		            	float balance = transaction.getAmount();
						if(personService.checkBalance(transaction) == true) 
						{
							value = sanctionService.CheckSanctionList(transaction);
							if(value == true)
							{
								status = new String(new Status().statusValidPass);
								personService.doTransaction(transaction);	
							}
							else 
							{
								status = new String(new Status().statusValidFail);
							}
						}
		            }		
					else {
						status = new String(new Status().fieldValidFail);
					}
					transaction.setStatus(status);
					transaction = saveTransaction(transaction);					
				} catch (Exception e) {}
            }
            br.close();
         
	        String current = new File( "." ).getCanonicalPath();
	        Path temp = Files.move(Paths.get(current+"/"+filename),Paths.get(current+"/Archive/"+filename));
	 
	        if(temp != null)
	            System.out.println("File renamed and moved successfully");
	        else
	            System.out.println("Failed to move the file");
        } 
        catch (Exception e){
            e.printStackTrace();
        }
        
        return transaction;
    }
    
    @Override
	public boolean fieldValidate(Transaction transaction) throws ParseException
	{
        Calendar cal = Calendar.getInstance();
        Date date1;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        date1 = (Date) formatter.parse(formatter.format(cal.getTime()));
		boolean value = true;
		
		if(!isAlphaNumeric(transaction.getId()) || !isAlphaNumeric(transaction.getPayerName()) || !isAlphaNumeric(transaction.getPayeeName()) || !isAlphaNumeric(transaction.getPayerAccount()) || !isAlphaNumeric(transaction.getPayeeAccount()))
			value = false;
		if((transaction.getAmount() < 0 )|| transaction.getDate().compareTo(date1) != 0)
			value = false;
		return value;
	}
    
    @Override
    public boolean isAlphaNumeric(String s)
    {
        String pattern= "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }
    
    @Override
    public Transaction setParameters(String line)
    {
        char CharArr[]=new char[127];
        CharArr=line.toCharArray();
        char TransChar[]=new char[12];
        char PayerName[]=new char[35];
        char PayeeName[]=new char[35];
        char PayerAccount[] = new char[12];
        char PayeeAccount[] = new char[12];
        String payeeName = null,payerName = null,str = "",RefId = null;
        float amount = 0;
        char Amount[] = new char[12];
        Date date = null;
        int lineIndex = 0 , size = 0;

    	for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)											// Extracting the reference ID of transaction
    		TransChar[counter] = CharArr[lineIndex];
    	RefId = new String(TransChar);
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	for(int counter = 0 ; counter < 8 ; counter++,lineIndex++)
    	{
            str = str + CharArr[lineIndex];
            if( counter == 1 || counter == 3 )
            	str = str + '/';
    	}
        try {
            date = (Date) formatter.parse(str);										// Extracting the date of transaction
         }catch (ParseException e){
         }
        
        for(int counter = 0 ; CharArr[lineIndex] != ' ' ; counter++,lineIndex++,size++)
            PayerName[counter]=CharArr[lineIndex];
        PayerName = Arrays.copyOfRange(PayerName, 0 , size);
        size = 0;
        lineIndex = 55;
        payerName = new String(PayerName);											// Extracting the Payer name of transaction
        
        for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)
        	PayerAccount[counter] = CharArr[lineIndex];
        String payerAccount = new String(PayerAccount);								// Extracting the Payer Account of transaction
        
        for(int counter = 0 ; CharArr[lineIndex] != ' ' ; counter++,lineIndex++,size++)
            PayeeName[counter]=CharArr[lineIndex];
        PayeeName = Arrays.copyOfRange(PayeeName, 0 , size);
        lineIndex = 102;
        payeeName = new String(PayeeName);											// Extracting the Payee name of transaction
        
        for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)
        	PayeeAccount[counter] = CharArr[lineIndex];
        String payeeAccount = new String(PayeeAccount);								// Extracting the Payee Account of transaction
        
        while(CharArr[lineIndex] == ' ' && lineIndex >= 114)
            lineIndex++;
        int index=lineIndex;           
        for(int counter = 0 ; counter < 127-index ; counter++,lineIndex++)
            Amount[counter] = CharArr[lineIndex];     
        String amt = new String(Amount);    
        amount = Float.parseFloat(amt);												// Extracting the Amount of transaction
        String status = new String(new Status().uploadFail);
        Transaction transaction = new Transaction(RefId,payerName,payeeName,date,amount,payerAccount,payeeAccount,status);
        System.out.println("in here");
        
        transaction.toString();
        return transaction;
    }
    
    @Override
    public void Polling() throws IOException,InterruptedException 
    {
         String current = new File( "." ).getCanonicalPath();
		 Path faxFolder = Paths.get(current);
		 WatchService watchService = FileSystems.getDefault().newWatchService();
		 faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		 String fileName=new String();
		 boolean valid = true;
		 int files = 0;
		 do{
			 fileName = "sample"+ (files+1) + ".txt";
			 createTransactionFile(fileName);
			WatchKey watchKey = watchService.take();
		
			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) 
				{
					fileName = event.context().toString();
					System.out.println("File Created: " + fileName);
					readFile(fileName);	
				}
				if(StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind()))
				{
					fileName= event.context().toString();
					System.out.println("File Updated: " + fileName);
					//update SanctionList
				}
			}
			valid = watchKey.reset();
			files++;
		} while (files < 10);	 
	}
    
    @Override
    public Object findAllTransaction()
    {
    	return transactionRepository.findAll();
    }
}
