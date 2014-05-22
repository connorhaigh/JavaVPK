package com.connorhaigh.javavpk;

import java.io.File;

import com.connorhaigh.javavpk.core.Archive;
import com.connorhaigh.javavpk.core.Entry;

public class JavaVPK 
{
	/**
	 * Main method.
	 * @param args application arguments
	 */
	public static void main(String[] args)
	{
		//header
		System.out.println("JavaVPK");
		System.out.println("(C) Connor Haigh 2014");
		System.out.println();
		
		//check for arguments
		if (args.length <= 0)
		{
			//usage
			System.out.println("Usage:");
			System.out.println("-i\tSpecify the input VPK file");
			System.out.println("-o\tSpecify the output directory");
			System.out.println("-v\tSpecify verbose output");
			
			return;
		}
		
		//parameters
		String input = null;
		String output = null;
		boolean verbose = false;
		
		try
		{
			//loop arguments
			for (int argument = 0; argument < args.length; argument++)
			{
				switch (args[argument])
				{
					case "-i":
					{
						input = args[++argument];
						
						break;
					}
					case "-o":
					{
						output = args[++argument];
						
						break;
					}
					case "-v":
					{
						verbose = true;
						
						break;
					}
				}
			}
			
			//check arguments
			if (input == null || output == null)
				throw new Exception();
		}
		catch (Exception exception)
		{
			//invalid arguments
			System.err.println("Invalid arguments specified");
			
			return;
		}
		
		//create files
		File inputFile = new File(input);
		File outputDirectory = new File(output);
		
		try
		{
			//create directory
			System.out.println("Creating output directory...");
			outputDirectory.mkdirs();
			
			//load
			System.out.println("Loading archive...");
			Archive archive = new Archive(inputFile);
			archive.load();
			
			if (verbose)
			{
				//details
				System.out.println("\t" + inputFile.getName());
				System.out.println("\tSignature: " + archive.getSignature());
				System.out.println("\tVersion: " + archive.getVersion());
			}
			
			//loop entries
			System.out.println("Extracting " + archive.getEntries().size() + " entries...");
			for (Entry entry : archive.getEntries())
			{	
				if (verbose)
					System.out.print("\tExtracting: " + entry.getFullName() + "... ");
				
				//extract
				File entryDirectory = new File(outputDirectory, entry.getPath());
				File entryFile = new File(outputDirectory, entry.getFullName());
				entryDirectory.mkdirs();
				entry.extract(entryFile);
				
				if (verbose)
					System.out.println("done");
			}
			
			//done
			System.out.println("Extracted " + archive.getEntries().size() + " entries successfully");
		}
		catch (Exception exception)
		{
			System.err.println();
			System.err.println("Error during extraction: " + exception.getMessage());
		}
	}
}
