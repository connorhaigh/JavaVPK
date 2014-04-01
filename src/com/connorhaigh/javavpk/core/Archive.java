package com.connorhaigh.javavpk.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import com.connorhaigh.javavpk.exceptions.ArchiveException;

public class Archive 
{
	/**
	 * Create a new VPK archive.
	 * @param file the archive file
	 */
	public Archive(File file)
	{
		this.file = file;
		
		this.signature = 0;
		this.version = 0;
		this.treeLength = 0;
		this.headerLength = 0;
		
		this.entries = new ArrayList<Entry>();
	}
	
	/**
	 * Load the raw data from file to this archive.
	 * @throws IOException if the archive could not be read
	 * @throws ArchiveException if a general archive exception occurs
	 */
	public void load() throws IOException, ArchiveException
	{
		try (FileInputStream fileInputStream = new FileInputStream(this.file))
		{
			//read header
			this.signature = this.readUnsignedInt(fileInputStream);
			this.version = this.readUnsignedInt(fileInputStream);
			this.treeLength = this.readUnsignedInt(fileInputStream);
			this.headerLength = 28;
			
			//check signature and version
			if (this.signature != Archive.SIGNATURE)
				throw new ArchiveException("Invalid signature");
			if (this.version != Archive.VERSION)
				throw new ArchiveException("Unsupported version");
			
			//miscellaneous
			//these parts of the header serve no purpose
			this.readUnsignedInt(fileInputStream);
			this.readUnsignedInt(fileInputStream);
			this.readUnsignedInt(fileInputStream);
			this.readUnsignedInt(fileInputStream);
			
			//extension loop
			while (true)
			{
				//get extension
				String extension = this.readString(fileInputStream);
				if (extension.isEmpty())
					break;
				
				//path loop
				while (true)
				{
					//get path
					String path = this.readString(fileInputStream);
					if (path.isEmpty())
						break;
					
					//filename loop
					while (true)
					{
						//get filename
						String filename = this.readString(fileInputStream);
						if (filename.isEmpty())
							break;
						
						//read data
						int crc = this.readUnsignedInt(fileInputStream);
						short preloadSize = this.readUnsignedShort(fileInputStream);
						short archiveIndex = this.readUnsignedShort(fileInputStream);
						int entryOffset = this.readUnsignedInt(fileInputStream);
						int entryLength = this.readUnsignedInt(fileInputStream);
						short terminator = this.readUnsignedShort(fileInputStream);
						
						//create entry
						Entry entry = new Entry(this, extension, path, filename, crc, archiveIndex, entryOffset, entryLength, terminator);
						this.entries.add(entry);
						
						//TODO: add preload support
						if (preloadSize > 0)
							fileInputStream.skip(preloadSize);
					}
				}
			}
		}
		catch (Exception ex)
		{
			//error
			throw ex;
		}
	}
	
	/**
	 * Reads a file input stream character by character until a null terminator is reached.
	 * @param fileInputStream the file input stream to read
	 * @return the assembled string
	 * @throws IOException if the stream could not be read
	 */
	private String readString(FileInputStream fileInputStream) throws IOException
	{
		//builder
		StringBuilder stringBuilder = new StringBuilder();
		
		//read
		int currentChar = 0;
		while ((currentChar = fileInputStream.read()) != 0x0)
			stringBuilder.append((char) currentChar);
		
		return stringBuilder.toString();
	}
	
	/**
	 * Reads an unsigned integer from a file input stream.
	 * @param fileInputStream the file input stream to read
	 * @return the unsigned integer
	 * @throws IOException if the stream could not be read
	 */
	private int readUnsignedInt(FileInputStream fileInputStream) throws IOException
	{
		//byte array
		byte[] buffer = new byte[4];
		fileInputStream.read(buffer, 0, 4);
		
		//byte buffer
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		
		return byteBuffer.getInt();
	}
	
	/**
	 * Reads an unsigned short from a file input stream.
	 * @param fileInputStream the file input stream to read
	 * @return the unsigned short
	 * @throws IOException if the stream could not be read
	 */
	private short readUnsignedShort(FileInputStream fileInputStream) throws IOException
	{
		//byte array
		byte[] buffer = new byte[2];
		fileInputStream.read(buffer, 0, 2);
		
		//byte buffer
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		
		return byteBuffer.getShort();
	}
	
	/**
	 * Returns the VPK archive file for this archive.
	 * @return the VPK archive file
	 */
	public File getFile()
	{
		return this.file;
	}
	
	/**
	 * Returns the signature of this archive.
	 * @return the signature
	 */
	public int getSignature()
	{
		return this.signature;
	}
	
	/**
	 * Returns the internal version of this archive.
	 * @return the internal version
	 */
	public int getVersion()
	{
		return this.version;
	}
	
	/**
	 * Returns the length of the root tree for this archive.
	 * @return the length of the root tree
	 */
	public int getTreeLength()
	{
		return this.treeLength;
	}
	
	/**
	 * Returns the length of the header for this archive.
	 * @return the length of the header
	 */
	public int getHeaderLength()
	{
		return this.headerLength;
	}
	
	/**
	 * Returns the list of entries in this archive.
	 * @return the list of entries
	 */
	public ArrayList<Entry> getEntries()
	{
		return this.entries;
	}
	
	public static final int SIGNATURE = 0x55AA1234;
	public static final int VERSION = 2;
	
	private File file;
	
	private int signature;
	private int version;
	private int treeLength;
	private int headerLength;
	
	private ArrayList<Entry> entries;
}
