package org.example;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int chosen = InputValidator.getIntInRange(
                -50,                         
                499,                          
                "Please enter a value",      
                "Your value is invalid",       
                scanner::nextLine,             
                System.out::println            // output consumer
        );

        System.out.println();
        System.out.println("The value chosen by the user is " + chosen);
    }
}
