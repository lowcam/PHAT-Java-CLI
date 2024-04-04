
PHAT  - Password Hashing Algorithm Tool
CLI Java Version
v 1.1

The purpose of this tool is to let an individual enter text and have a hashed
output to use as the password to the site or program. Initially the program
will hash the input in SHA 256 and output in hexadecimal. The plans for this
program are to allow the selection of three different SHA lengths (256, 384
and 512). Also, the output numbering system will be selectable between
hexadecimal, base64, and base58. Also, the number of digits in the ouput
will be selectable in case a site can only have a certain number of digits
in a password. The last step will be for the output to be copied to the
clipboard so if can be pasted into the program or site.

Used openjdk-17 for compilation.

To compile the PHATJavaCLI.java file I did the following:
to get the PHATJavaCLI.class file:
javac PHATJavaCLI.java
to get the PHATJavaCLI.jar file:
jar -cmvf META-INF/MANIFEST.MF PHATJavaCLI.jar PHATJavaCLI.class


(C) 2024 Lorne Cammack, USA
Released under GNU Public License (GPL)
email lowcam.socailvideo@gmail.com


License

This project is licensed under the GNU GENERAL PUBLIC LICENSE v 3 - see the LICENSE file for details
