package com.connorhaigh.javavpk.core;

import java.util.ArrayList;

public class Directory 
{
	/**
	 * Create a new VPK directory.
	 * @param path the path of the directory
	 */
	public Directory(String path)
	{
		this.path = path.trim();
		this.entries = new ArrayList<Entry>();
	}
	
	/**
	 * Returns the path of this directory.
	 * @return the path
	 */
	public String getPath()
	{
		return this.path;
	}
	
	/**
	 * Add an entry to this directory.
	 * @param entry the entry
	 */
	public void addEntry(Entry entry)
	{
		this.entries.add(entry);
	}
	
	/**
	 * Remove an entry from this directory.
	 * @param entry the entry
	 */
	public void removeEntry(Entry entry)
	{
		this.entries.remove(entry);
	}
	
	/**
	 * Returns the list of entries in this directory.
	 * @return the list of entries
	 */
	public ArrayList<Entry> getEntries()
	{
		return this.entries;
	}
	
	private String path;
	private ArrayList<Entry> entries;
}
