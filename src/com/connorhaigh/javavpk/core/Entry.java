package com.connorhaigh.javavpk.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Entry 
{
	/**
	 * Create a new VPK archive entry.
	 * @param extension the extension of this entry
	 * @param path the path to this entry
	 * @param filename the file name of this entry
	 * @param crc the CRC checksum of this entry
	 * @param archiveIndex the archive index of this entry
	 * @param offset the offset of this entry
	 * @param length the length of this entry
	 * @param terminator the terminator of this entry
	 */
	public Entry(Archive archive, String extension, String path, String filename, int crc, short archiveIndex, int offset, int length, short terminator)
	{
		this.archive = archive;
		
		this.extension = extension;
		this.path = path;
		this.filename = filename;
		
		this.crc = crc;
		
		this.archiveIndex = archiveIndex;
		this.offset = offset;
		this.length = length;
		this.terminator = terminator;
	}
	
	/**
	 * Reads and returns the raw data for this entry.
	 * @return the raw data
	 * @throws IOException if the entry could not be read
	 */
	public byte[] readData() throws IOException
	{
		try (FileInputStream fileInputStream = new FileInputStream(this.archive.getFile()))
		{	
			//data array
			byte[] data = new byte[this.length];
			
			//check for offset
			if (this.archiveIndex == Entry.TERMINATOR)
				fileInputStream.skip(this.archive.getTreeLength());
			
			//read
			fileInputStream.skip(this.archive.getHeaderLength());
			fileInputStream.skip(this.offset);
			fileInputStream.read(data, 0, this.length);
			
			return data;
		}
		catch (Exception ex)
		{
			//error
			throw ex;
		}
	}
	
	/**
	 * Extract the data from this entry to the specified file.
	 * @param file the file to extract to
	 * @throws IOException if the entry could not be extracted
	 */
	public void extract(File file) throws IOException
	{
		try (FileOutputStream fileOutputStream = new FileOutputStream(file))
		{
			//write
			fileOutputStream.write(this.readData());
		}
		catch (Exception ex)
		{
			//error
			throw ex;
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
	public String getFilename()
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
	
	private String extension;
	private String path;
	private String filename;
	
	private int crc;
	
	private short archiveIndex;
	private int offset;
	private int length;
	private short terminator;
}
