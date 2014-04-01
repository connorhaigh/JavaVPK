JavaVPK
=======

A Java library for reading and extracting VPK files from most Valve games.

This is a work in progress, and as such not all VPK formats are supported.

Installation
------

To install JavaVPK, you can build from the repo, or alternatively download the pre-built JAR.

Initial setup
------

To read and extract from an archive, you'll need to create a new ```Archive``` object, as follows:

```java
File archiveFile = new File("some_archive.vpk");
Archive archive = new Archive(archiveFile);
```

Make sure that you call the ```load()``` method after, as to populate the archive with entries:

```java
archive.load();
```

Roadmap
------

Planned support:

- Support for the preload data in the header
- Multi-chunk archives