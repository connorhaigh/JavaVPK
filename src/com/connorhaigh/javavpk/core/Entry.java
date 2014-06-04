package com.connorhaigh.javavpk.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.connorhaigh.javavpk.exceptions.ArchiveException;
import com.connorhaigh.javavpk.exceptions.EntryException;

public class Entry 
{
	/***
	 * Create a new VPK archive entry.
	 * @param archive the parent archive of this entry
	 * @param archiveIndex the archive index of this entry
	 * @param preloadData the small preload data, if any
	 * @param extension the extension of this entry
	 * @param path the path to this entry
	 * @param filename the filename of this entry
	 * @param crc the CRC checksum of this entry
	 * @param offset the offset of this entry
	 * @param length the length of this entry
	 * @param terminator the terminator to this entry
	 * @throws EntryException if the archive is null
	 */
	protected Entry(Archive archive, short archiveIndex, byte[] preloadData, String extension, String path, String filename, int crc, int offset, int length, short terminator) throws EntryException
	{
		if (archive == null)
			throw new EntryException("Parent archive cannot be null");
		
		this.archive = archive;
		this.archiveIndex = archiveIndex;
		
		this.preloadData = preloadData;
		
		this.extension = extension;
		this.path = path;
		this.filename = filename;
		
		this.crc = crc;
		this.offset = offset;
		this.length = length;
		this.terminator = terminator;
	}
	
	/**
	 * Reads and returns the raw data for this entry.
	 * If the entry has preload data, that is returned instead.
	 * @return the raw data
	 * @throws IOException if the entry could not be read
	 * @throws ArchiveException if a general archive exception occurs
	 */
	public byte[] readData() throws IOException, ArchiveException
	{
		//check for preload data
		if (this.preloadData != null)
			return this.preloadData;
		
		//get target archive
		File target = null;
		if (this.archive.isMultiPart())
			target = this.archive.getChildArchive(this.archiveIndex);
		else
			target = this.archive.getFile();
		
		try (FileInputStream fileInputStream = new FileInputStream(target))
		{
			if (this.archiveIndex == Entry.TERMINATOR)
			{
				//skip tree and header
				fileInputStream.skip(this.archive.getTreeLength());
				fileInputStream.skip(this.archive.getHeaderLength());
			}
			
			//read data
			byte[] data = new byte[this.length];
			fileInputStream.skip(this.offset);
			fileInputStream.read(data, 0, this.length);
			
			return data;
		}
	}
	
	/**
	 * Extract the data from this entry to the specified file.
	 * @param file the file to extract to
	 * @throws IOException if the entry could not be read
	 * @throws ArchiveException if a general archive exception occurs
	 */
	public void extract(File file) throws IOException, ArchiveException
	{
		try (FileOutputStream fileOutputStream = new FileOutputStream(file))
		{
			//write
			fileOutputStream.write(this.readData());
		}
	}
	
	/**
	 * Returns the parent archive for this entry.
	 * @return the parent archive
	 */
	public Archive getArchive()
	{
		return this.archive;
	}
	
	/**
	 * Returns the child archive index this entry is contained in.
	 * @return the child archive index
	 */
	public int getArchiveIndex()
	{
		return this.archiveIndex;
	}
	
	/**
	 * Returns this entry's extension.
	 * @return this entry's extension
	 */
	public String getExtension()
	{
		return this.extension;
	}
	
	/**
	 * Returns this entry's path.
	 * @return this entry's path
	 */
	public String getPath()
	{
		return this.path;
	}
	
	/**
	 * Returns this entry's filename.
	 * @return this entry's filename
	 */
	public String getFileName()
	{
		return this.filename;
	}
	
	/**
	 * Returns this entry's full path name.
	 * @return this entry's full path name
	 */
	public String getFullName()
	{
		return (this.path + "/" + this.filename + "." + this.extension);
	}
	
	/**
	 * Returns the CRC checksum of this entry.
	 * @return the CRC checksum
	 */
	public int getCrc()
	{
		return this.crc;
	}
	
	/**
	 * Returns the offset of this entry in the parent file.
	 * @return the offset of this entry
	 */
	public int getOffset()
	{
		return this.offset;
	}
	
	/**
	 * Returns the length of this entry, in bytes.
	 * @return the length of this entry
	 */
	public int getLength()
	{
		return this.length;
	}
	
	/**
	 * Returns the terminator of this entry.
	 * @return the terminator of this entry
	 */
	public int getTerminator()
	{
		return this.terminator;
	}
	
	public static final int TERMINATOR = 0x7FFF;
	
	private Archive archive;
	private short archiveIndex;
	
	private byte[] preloadData;
	
	private String extension;
	private String path;
	private String filename;
	
	private int crc;
	private int offset;
	private int length;
	private short terminator;
}
