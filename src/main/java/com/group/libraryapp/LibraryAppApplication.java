package com.group.libraryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryAppApplication {

  public static void main(String[] args) {
      System.out.println(1+1);
      SpringApplication.run(LibraryAppApplication.class, args);
  }

}
