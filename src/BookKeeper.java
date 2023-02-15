import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class BookKeeper {
	static boolean enableTest;
	ArrayList<Transaction> allTransactions =  new ArrayList<Transaction>();
	private static String purchasesTerms;
	

	public static void main(String[] args) throws Exception {
		System.out.println("running bookkeeper.");
		enableTest = true;
		
		BookKeeper bookKeeper = new BookKeeper();
		//String testFile = "C:\\Users\\User1\\Documents\\Transactions_Export_Dec_2022_32099777.csv";
		//String transactionFileName = args[0];
		String transactionFolder = args[0];
		purchasesTerms = args[1];
		
		bookKeeper.loadTransactionsFromFolder(transactionFolder);
		
		Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2022"); //set to 1st Jan 2020
		Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2022"); //set to 31st Dec 2025
		
		bookKeeper.verifyTransactions(bookKeeper.getAllTransactions());
		
		
		//createReport() - show total out, total in, total balance, calculate total VAT on input
		bookKeeper.createReport(fromDate, toDate);
	}

	private void loadTransactionsFromFolder(String transactionFolder) throws FileNotFoundException, ParseException {
		// TODO Auto-generated method stub
		//1. get list of files in folder and print name
		File directoryPath = new File(transactionFolder);
	      //List of all files and directories
	      String contents[] = directoryPath.list();
	      String absolutePath = directoryPath.getAbsolutePath();
	      System.out.println("List of files and directories in the specified directory:");
	      for(int i=0; i<contents.length; i++) {
	         setTransactions(absolutePath + "\\" + contents[i]);
	      }
	
		
		
		
	}

	private void createReport(Date fromDate, Date toDate) {
		System.out.println();
		System.out.println("Report from " + fromDate + " to " + toDate);
		
		fromDate.setDate(fromDate.getDate()-1);
		toDate.setDate(toDate.getDate()+1);
		allTransactions =  getTransactionsByDates(allTransactions, fromDate, toDate);
		//displayTransactions(allTransactions);
		Double totalIn = calculateTotalIn(allTransactions);
		Double totalOut = calculateTotalOut(allTransactions);
		Double totalBalance = 0.0;
				
		//calculate total balance
		totalBalance = totalIn - totalOut;
		
		//calculate total vat
		ArrayList<Transaction> salesTransactions = this.getTransactionsByDetails("NETWORKERS");
		double totalSales = calculateTotalIn(salesTransactions);
		double totalBeforeVAT = (totalSales/120) * 100;
		double VAT = totalSales - totalBeforeVAT;

		double totalPurchases = calculateTotalPurchases();
		
		ArrayList<Transaction> wages = this.getTransactionsByDetails("Wages");

		System.out.println("total in: +£" + totalIn);
		System.out.println("total out: -£" + totalOut);
		System.out.println("total sales (including VAT): +£" + totalSales);
		System.out.println("total before VAT: £" + totalBeforeVAT);
		System.out.println("VAT: £" + VAT);
		System.out.println("total wages: -£" + calculateTotalOut(wages));
		System.out.println("total dividends: -£" + calculateTotalOut(getTransactionsByDetails("Dividends")));
		System.out.println("total net: £" + totalBalance);
		System.out.println("total purchases: -£" + totalPurchases);
		
		
	}

	private double calculateTotalPurchases() {
		// TODO Auto-generated method stub
		double total = 0;
		String[] terms = purchasesTerms.split(",");
		for (String term: terms) {
			total = total + calculateTotalOut(getTransactionsByDetails(term));
		}
		return total;
	}

	private double calculateTotalIn(ArrayList<Transaction> transactions) {
		double total = 0.0;
		// TODO Auto-generated method stub
		for (Transaction transaction : transactions) {
			// calculate total in
			if (transaction.getIn() != null) {
				total = transaction.getIn() + total;
			}
		}
		return total;
	}
	
	private double calculateTotalOut(ArrayList<Transaction> transactions) {
		double total = 0.0;
		// TODO Auto-generated method stub
		for (Transaction transaction : transactions) {
			// calculate total out
			if (transaction.getOut() != null) {
				total = transaction.getOut() + total;
			}
		}
		return total;
	}

	public ArrayList<Transaction> getAllTransactions() {
		return allTransactions;
	}



	private void verifyTransactions(ArrayList<Transaction> transactionsList) {
		if (!enableTest)
			return;
		
		
		Transaction transaction = transactionsList.get(0);
		if (!transaction.getDate().equals("30/12/2022")) {
			System.out.println("date is incorrect");
		}
		
		if (!transaction.getDetails().equals("AXAPPP RE HOL")) {
			System.out.println("details is incorrect");
		}
		
		if (!transaction.getType().equals("Direct Debit")) {
			System.out.println("type is incorrect");
		}
		
		if (transaction.getIn() != null) {
			System.out.println("in is incorrect");
		}
	
		if (transaction.getOut() != 110.99) {
			System.out.println("out is incorrect");
		}
		
		System.out.println("Tests done");
	}



	public void setTransactions(String transactionsFileName) throws FileNotFoundException, ParseException {
		// open file create Transaction object for each line in the file
		Scanner sc = new Scanner(new File(transactionsFileName));

		sc.nextLine();
		
		while (sc.hasNext()) {
			String line = sc.nextLine();

			// split line into comma separated parts

			String[] parts = line.split(",");

			Transaction transaction = new Transaction();
			transaction.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(parts[0]) );
			transaction.setDetails(parts[1]);
			transaction.setType(parts[2]);


			if (parts[3].length() > 0)
				transaction.setIn(Double.parseDouble(parts[3]));
			
			
			if (parts[4].length() > 0) 
				transaction.setOut(Double.parseDouble(parts[4]));
			
			allTransactions.add(transaction);

		}

		System.out.println(allTransactions.size() + "transactions");


		sc.close();
	}
	
	private ArrayList<Transaction> getOutTransactions() {
		ArrayList<Transaction> outTransactions = new ArrayList<Transaction>();
		
		for (Transaction transaction : allTransactions) {
			if (transaction.getOut() != null) {
				outTransactions.add(transaction);
			}
		}
		
		return outTransactions;	
	}
	
	private ArrayList<Transaction> getInTransactions() {
		ArrayList<Transaction> inTransactions = new ArrayList<Transaction>();
		
		for (Transaction transaction: allTransactions) {
			if (transaction.getIn() != null) {
				inTransactions.add(transaction);
			}
		}
		return inTransactions ;
	}
	
	private void displayTransactions(ArrayList<Transaction> transactions) {
		for (Transaction transaction : transactions) {
			System.out.println(transaction.getDate() + " " + transaction.getDetails() + " out = " + transaction.getOut() +
					" in = " + transaction.getIn());
		}
	}
	
	private ArrayList<Transaction> getTransactionsByDetails(String searchTerm) {
		ArrayList<Transaction> results = new ArrayList<Transaction>();
		
		for (Transaction transaction: allTransactions) {
			if (transaction.getDetails().contains(searchTerm)) {
				results.add(transaction);
			}
		}
		return results;
	}
	
	private ArrayList<Transaction> getTransactionsByDates(ArrayList<Transaction> transactions, Date fromDate, Date toDate) {
		ArrayList<Transaction> results = new ArrayList<Transaction>();
		
		for (Transaction transaction: transactions) {
			if (transaction.getDate().after(fromDate) && transaction.getDate().before(toDate)) {
				results.add(transaction);
			}
		}
		return results;
	}
	
}
