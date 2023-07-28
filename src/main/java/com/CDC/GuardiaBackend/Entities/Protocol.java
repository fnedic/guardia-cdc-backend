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
    
    private String title;
    private String firstAutor;
    private String secondAutor;
    private String introduction;
    private String generalInformation;
    private String procedures;
    private String annex;
    private String videoLink;
    private String drivelLink;
    private Date createdDate;

}
