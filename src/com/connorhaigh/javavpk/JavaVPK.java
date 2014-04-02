package com.connorhaigh.javavpk;

import java.io.File;

import com.connorhaigh.javavpk.core.Archive;

public class JavaVPK 
{
	public static void main(String[] args)
	{
		File archiveFile = new File("D:/Program Files (x86)/Steam/steamapps/common/team fortress 2/tf/tf2_misc_dir.vpk");
		Archive archive = new Archive(archiveFile);
		
		try
		{
			archive.load();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static final int MINIMUM_VERSION = 2;
	public static final int MAXIMUM_VERSION = 2;
}
