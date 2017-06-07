


cd "C:\Users\apace\git\DashBoard\Dash\"
dir > "C:\Users\apace\git\DashBoard\Dash\PathAtStart.txt"
set JarPath=D:\eclipse Luna\plugins\org.testng.eclipse_6.9.5.201506120235\lib\testng.jar
set SelPath=C:\Users\apace\git\DashBoard\Dash\Libs\*
set SelPathJar=C:\Users\apace\git\DashBoard\Dash\Libs\selenium-java-2.53.0.jar
set SelBin=C:\Users\apace\git\DashBoard\Dash\bin
echo %JarPath%;%SelPath%;%SelPathJar%;%SelBin% > "C:\Users\apace\git\DashBoard\Dash\ClassPath.txt"
dir > "C:\Users\apace\git\DashBoard\Dash\Done.txt"
set classpath=%JarPath%;%SelPath%;%SelPathJar%;%SelBin%
echo %classpath% > classPath.txt
java org.testng.TestNG C:\Users\apace\git\DashBoard\Dash\TestFirstTry.xml  > D:\Documents\DashAutomationTestResults\TestFirstTry.txt


