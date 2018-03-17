package com.abhinendra.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.abhinendra.domain.*;

public interface TransactionService{

    public Transaction saveTransaction(Transaction transaction) throws Exception;
    public void Polling() throws IOException,InterruptedException;
    public Transaction readFile(String filename);
    public Object findAllTransaction();
    public void createTransactionFile(String filename);
	Transaction setParameters(String filename);
	boolean fieldValidate(Transaction transaction) throws ParseException;
	boolean isAlphaNumeric(String s);
}
