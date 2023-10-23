package com.CDC.GuardiaBackend.Entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Data
@Entity
public class Protocol {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    
    private int views;
    private String title;
    private String autor1;
    private String autor2;
    private String intro;
    private String generalInfo;
    private String procedures;
    private String annexed;
    private String videoLink;
    private String driveLink;
    private Date publicationDate;
    private String protocolGroup; 

}
