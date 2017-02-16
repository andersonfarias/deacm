# Abstract

The DEACM stands for Data Envelopment Analysis for Capital Management and is a web application that is intended to help solve problems related to Capital Management.

The application is based on a Doctoral Thesis of the researcher Ney Paranaguá that proposed the concept of relative size and show how to use it to solve problems regarding Capital Management.

The Doctoral thesis proposes a series of mathematical models for optimization problems regarding Capital Management, that are difficult, error prone and time consuming to execute them by hand and make sense of the results, specially with a big amount of data. The DEACM application implements these mathematical models and make it easy to input data, execute the proposed models and analyze the results.

The major functions performed by the application are:

1. **Input or load data**: this feature enables the user to input data using a web interface and manually type in the matrix of DMUs (Decision Making Units) with it’s associated data: inputs, outputs and costs. It’s also possible to import a .csv file with the DMUs data along with the capital management model and it’s parameters in case of a bigger data.

2. **Execute base models (DEA and KAO’s)**: this feature frees the user for the need of giving the values of the DEA and KAO’s efficiency as input arguments for each DMU. This feature saves time and avoid errors in input data.
		
3. **Execute capital management models**: this feature enables the user to select a capital management model, give the parameters for that specific model and executes it. It automates the execution of the mathematical models proposed by Ney Paranaguá, in his Doctoral thesis.

# Developing DEACM

## Running the application locally

Before you can start following the tutorial do run the application, you first need the following pre-requisites to run the application:

* [Java JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
* [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [Maven](https://maven.apache.org/install.html)

Now that you have everything setup, you can go head and follow the steps:

1. To run this application locally, you first need to install the [AMPL](http://ampl.com/products/ampl/ampl-for-students/) and download the [AMPL API](http://ampl.com/products/ampl/). The DEACM application uses this API to model and solve the optimization problems. Follow the instructions on [their website](http://ampl.com/products/api/) for how to download the API for your specific Operational System. After you download and extract somewhere in your machine, please keep record of the complete path for the `ampl-1.2.2.jar` file.

2. Now, clone this project using the following command:

    ```
    git clone https://github.com/andersonfarias/deacm.git && cd deacm
    ```
    
3. Now, we need to make 2 changes. First, open the file `pom.xml` which is located at the root of the project. Open it and look for the following lines:

```xml
<dependency>
   <groupId>com.ampl</groupId>
   <artifactId>java.api</artifactId>
   <version>1.2</version>
   <scope>system</scope>
   <systemPath>CHANGE THIS VALUE HERE TO THE COMPLETE PATH OF YOUR ampl-1.2.2.jar file</systemPath>
</dependency>
```

Change the **systemPath** to the complete path of the `ampl-1.2.2.jar` file that you downloaded.

Now, open the file `Application.java` which is located at `deacm/src/main/java/br/edu/ifpi/ads/deacm`.
Look for the following line:

```java
public static final String MODELS_LOCATION = "CHANGE THIS VALUE HERE TO COMPLETE PATH OF THE OPTIMIZATION MODELS";
```

Change the variable value to the complete path where the optimization models are located. The optimizations models are inside the folder `deacm/src/main/resources/optimization`. A complete path, for example, could be `/Users/andersonfarias/Downloads/deacm/src/main/resources/optimization`. So, in this example, the code would be:

```java
public static final String MODELS_LOCATION = "/Users/andersonfarias/Downloads/deacm/src/main/resources/optimization";
```

4. Now, open a terminal, go to the project folder and execute the following command:

```sh
mvn spring-boot:run
```

When you first execute this command, it could take while, since it's going to download all the project's dependencies, build the application and execute it.

When the command finishes, it will produce an output like the following:

```sh
[INFO] br.edu.ifpi.ads.deacm.Application - Started Application in 59.307 seconds (JVM running for 60.013)
[INFO] br.edu.ifpi.ads.deacm.Application - Access URLs:
----------------------------------------------------------
	Local: 		http://127.0.0.1:8080
	External: 	http://192.168.0.113:8080
----------------------------------------------------------
```

When you see that, open a new browser window and access [http://localhost:8080](http://localhost:8080/) and you should see the home screen of the DEACM application.

If you want to know how to use the application, please refer to the **DEACM User Guide.pdf** document.

## Developing locally

If you wanna make code changes, fix bugs, add new features, feel free to submit pull-requests.

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

# Building for production

To optimize the DEACM client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

# Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test

# Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `DEACM`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/DEACM.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
