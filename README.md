JavaVPK
=======

A Java library for reading and extracting VPK files from most Valve games.

Installation
------

To install JavaVPK, you can build from the repo, or alternatively download the pre-built JAR.

If you're just wanting to extract VPK archives (or just see the library in action), a command-line application is provided.

Initial setup
------

To read and extract from an archive, you'll need to create a new ```Archive``` object, and then load the entries.

```java
File archiveFile = new File("some_archive.vpk");
Archive archive = new Archive(archiveFile);
archive.load();
```

You can then loop through each of the entries in the archive with the ```getEntries()``` method, and do as you please - for example, to list all of the entries in a single archive:

```java
for (Entry entry : archive.getEntries())
	System.out.println("Entry: " + entry.getFullName() + ": " + entry.getLength() + " bytes");
```

Roadmap
------

- Full support for version 1 (entry skipping)