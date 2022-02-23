# remove-ide-files
Removes the following IDE files
*   .classpath
*   .project
*   .factorypath
*   .settings

Excludes the files whose path contains <i>node_modules</i>

# Tech Stack 
*   JDK 11
*   Maven 3

# Usage
<code>mvn clean install</code>

<code>java -jar remove-ide-files.jar [location [maxDepth]]<code>