JavaVPK
=======

A Java library for reading and extracting VPK files from most Valve games.

Installation
------

To install JavaVPK, you can build from the repo, or alternatively download the pre-built JAR.

If you're just wanting to extract VPK archives (or just see the library in action), a command-line application is provided.

Initial setup
------

To read and extract from an archive, you'll need to create a new `Archive` object, and then load the entries.

```java
File archiveFile = new File("some_archive.vpk");
Archive archive = new Archive(archiveFile);
archive.load();
```

You can think of the archive as a virtual file system. Each of the entries are stored in a `Directory`, and each of the directories are stored in the `Archive`.

You can loop through each of the directories in the archive, and then loop through each of the entries in the directory, like so:

```java
for (Directory directory : archive.getDirectories())
	for (Entry entry : directory.getEntries())
		System.out.println(entry.getFullName() + " in " + directory.getPath());
```

Roadmap
------

- Library is complete (as far as I can tell)