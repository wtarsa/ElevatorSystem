# Elevator System

A simple program which imitates a system which enables to control elevators in one building.

## Build

This project was made with maven. To build it, please use: `mvn package`.
The Runner.jar file will appear at target folder.
 Please use `java -jar target/Runner.jar` to run this program.

## Running

After running, the program will ask you to give a number of elevators 
and for each of them, it will ask you to give a number of the starting floor.

After that you can use two commands:
* `status` - it will give you list of status of all elevators
* `pickup a b` - it will delegate a new task to system, to pickup a person from floor number `a`. The sign of number `b`
 means the direction in which the person wants to go(b > 0 means UP, b < 0 means DOWN)


## Built With

* Java 11
* Maven

## Authors

* **Wiktor Tarsa** 

Kraków, January 2021
