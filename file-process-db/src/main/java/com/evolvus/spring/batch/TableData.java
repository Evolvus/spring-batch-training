package com.evolvus.spring.batch;

import java.util.Date;

import lombok.Data;

@Data
public class TableData {
   private String name;
   private String info;
   private Date loadDate;
   private String status;
}
