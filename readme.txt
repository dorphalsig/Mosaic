This is the GUI version to integrate with XMF2

1. download "com.ceteva.xmf" folder from github. https://github.com/xmf-xmodeler/XMF
2. put the "com.ceteva.xmf.system" in the same folder of the Java GUI project (otherwise will cause exception when editor receive message from XMF)
3. import the project "com.ceteva.xmf.machine" to Java GUI project
4. run the Java GUI project, uk.ac.mdx.xmf.swt.demo.Main.java

for Windows OS 
1. use org.eclipse.swt.win32.win32.x86_3.7.2.v3740f.jar

for Mac
1. use mac_swt.jar

for linux
1. use swt-linux.jar


Doc folder: Java API documents
package.doc: description for Java GUI as well as the communication between XMF

Duplicate icons:
there are a few duplicate icons as well as icon's folder, because the icon's path is sent back by XMF, and the original structure of the icons which 
spread out different projects; we can modify the XMF and tiny the icons folder later.

