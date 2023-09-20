package com.CDC.GuardiaBackend.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class Video {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;

    // @OneToOne
    // @JoinColumn(name = "protocol_id")
    // private Protocol protocolId;

    private String title;
    private String videoGroup;
    private String link;
    private Date date;
}
