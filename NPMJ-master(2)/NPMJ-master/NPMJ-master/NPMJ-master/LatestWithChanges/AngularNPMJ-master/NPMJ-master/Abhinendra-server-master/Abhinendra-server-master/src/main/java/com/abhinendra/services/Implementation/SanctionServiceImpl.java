package com.abhinendra.services.Implementation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abhinendra.domain.SanctionList;
import com.abhinendra.domain.Transaction;
import com.abhinendra.repositories.SanctionRepository;
import com.abhinendra.services.SanctionService;

@Service("sanctionService")
public class SanctionServiceImpl implements SanctionService{

	@Autowired
	SanctionRepository sanctionRepository;
	@Override
	public SanctionList saveSanctionEntry(SanctionList sanctionList) throws Exception {
		return sanctionRepository.save(sanctionList);
	}

	static int CountOfSanctionNames=0;

	@Override
	public void readSanctionList(String filename) {		
		try {
			
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = new String();
			while((line=br.readLine()) != null)
			{
			    if(line.contains("Name"))
			    {
			    	System.out.println(line);
				    line = line.substring(9);
				    System.out.println(line);
				    StringTokenizer st = new StringTokenizer(line,",");
				    SanctionList sanction;
				    while (st.hasMoreTokens()) 
				    {
			    		sanction = new SanctionList(st.nextToken().replaceAll("\\s", ""));
			    		SanctionServiceImpl.CountOfSanctionNames++;
			    		try {
							saveSanctionEntry(sanction);
						} catch (Exception e) {
							e.printStackTrace();
						}
				    } 
			    }
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
	}	
	@Override
	public boolean CheckSanctionList(Transaction transaction)
	{
    	System.out.println("INSIDE CHECK SANCTION LIST FUNCTION");
    	for(SanctionList sanction: sanctionRepository.findAll())
		{
			String Name1=transaction.getPayerName();
			String Name2=transaction.getPayeeName();
			
			System.out.println("name 1:"+sanction.getName());
			
			if(Name1.equalsIgnoreCase(sanction.getName()) || Name2.equalsIgnoreCase(sanction.getName()))
			{
				System.out.println("OOPS YOUR NAME IS IN THE SANCTION LIST");
				return false;
			}
		}		
		return true;
	}
	
	@Override
	public void UpdateSanctionList(String filename)
	{
		
	}
	
	
	
}
