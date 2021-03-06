# Stroke

Stroke is a port of the C++ Swift library ( http://swift.im/swiften/ )
The source is available from the Git repository at http://swift.im/git/stroke/

For XML parsing, Stroke depends on the Aalto XML Parser and the STAX2 API, from http://wiki.fasterxml.com/AaltoHome

It also depends upon http://www.jcraft.com/jzlib/, which is passed to ant in the jzlib-dir parameter. The passed folder should contain a jar called jzlib.jar.

It also depends upon icu4j from http://site.icu-project.org/

It also depends upon dnsjava from http://www.dnsjava.org/


To build, run:
ant -Dxpp-dir=third-party/xpp -Djzlib-dir=third-party/jzlib -Dicu4j-dir=third-party/ -Dstax2-dir=third-party/stax2/ -Daalto-dir=third-party/aalto/ 
Changing the paths to the relevant paths for the dependencies on your system

Easy version:
The included Makefile should, on Unixes with make/curl installed, grab the dependencies (once only) and build.


For development:
If you want to commit changes to Stroke, first run `make .git/hooks/commit-msg` to download a script that will generate change-ids needed by our review system.

## Differences from Swiften

Stroke tries to be a clean and accurate port of Swiften, in order to facilitate mirroring changes. Sometimes differences are either necessary or desirable.

* `VCard.getPhoto()` returns *null* instead of an empty `ByteArray` when there is no photo.

* `VCard` does not allocate empty collections for unused members.
The way a VCard is used, pessimistic allocation puts an undue load on the Java GC.
Callers have to check for *null* not just `isEmpty()`.