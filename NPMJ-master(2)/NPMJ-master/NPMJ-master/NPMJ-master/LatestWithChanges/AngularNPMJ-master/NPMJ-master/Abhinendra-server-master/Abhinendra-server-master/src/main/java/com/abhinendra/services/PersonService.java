package com.abhinendra.services;

import java.util.ArrayList;
import java.util.List;

import com.abhinendra.domain.*;
import com.abhinendra.services.Implementation.InsufficientAmtException;

public interface PersonService{

    public Person savePerson(Person person) throws Exception;
    public void createUser(String filename);
    public Person findOnebyId(Transaction transaction);
    public void showall();
    //public void doTransaction(String fromName, String PayeeName, float amt);
    public void withdraw(String payerName, float Amt);
    public void deposit(String payeeName, float Amt);
    public boolean checkBalance(Transaction transaction);

    public ArrayList<Person> selectRecord();
    public void doTransaction(Transaction transaction) throws InsufficientAmtException;
	
}