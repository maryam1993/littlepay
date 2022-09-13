<h1 align="center"> Trips </h1> <br>



## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Assumptions](#assumptions)
- [Quick Start](#quick-start)
- [Run Local](#run-local)
- [Testing](#testing)


## Introduction

This is a simple spring boot project that receives a list of taps for a line of bus. Then it will process the
given taps list and generates suitable trips. These trips are registered in another file called trips.csv as an output.

## Features

Each tap can be a tap-on or a tap-off. Based on the stopId of the tapOn and the stopId of the tapOff, the type of the 
trip can vary:

Completed Trips:
If a passenger taps on at one stop and taps off at another stop, this is called a complete trip. 
The amount the passenger will be charged for the trip will be determined based on the table above. 
For example, if a passenger taps on at stop 3 and taps off at stop 1, they will be charged $7.30.

Incomplete trips:
If a passenger taps on at one stop but forgets to tap off at another stop, this is called an incomplete trip.
The passenger will be charged the maximum amount for a trip from that stop to any other stop they could have travelled to.
For example, if a passenger taps on at Stop 2, but does not tap off, 
they could potentially have travelled to either stop 1 ($3.25) or stop 3 ($5.50), so they will be charged the higher value of $5.50.

Cancelled trips:
If a passenger taps on and then taps off again at the same bus stop, this is called a cancelled trip. 
The passenger will not be charged for the trip.

No matter if the tap file is not sorted by time. The service will take care of that.

generateTrips service is the main service. At first, it will read the taps.csv file. Then we have a List of taps. After that, 
taps will be divided based on the pan and each of them will be processed individually. Each pan, based on the number of taps and its types 
can have one or more trips. 
After all the related trips to each pan is processed, all of these trips will be written to the trips.csv file.

## Assumptions

* It is assumed that a certain taps.csv file is specified to one line, and it is in the time of a day (24 hours).
* The input file which is the taps.csv has a correct format and data.
* Each tapOn and tapOff has to have the mutual BusId and CompanyId. This means that two taps with different BusId and CompanyId will be 
considered as two Incomplete trips.


## Quick Start

With running the TripApplication.java, the generateTrips service will be called and the taps in the taps.csv input file
will be processed and the result will be available through trips.csv file.


## Run Local
In order to run and test the application you can either run it in debug mode using your
IDE env or run the command below.

```bash
$ mvn spring-boot:run
```

## Testing
The unit test are available in test package. Different situation and conditions are covered through unit test in TripApplicationTest.java 
using junit 5.
Another way to run them is through below command:
```bash
$ mvn clean package
```









