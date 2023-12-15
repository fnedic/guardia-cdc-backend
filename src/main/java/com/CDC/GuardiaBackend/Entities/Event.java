package com.CDC.GuardiaBackend.Entities;

import com.CDC.GuardiaBackend.Enums.EventStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @JoinColumn(name = "user_id")
    private String userId;

    private String title;
    private Date startDate;
    private Date endDate;
    private String color;
    private String oldOwnerTitle;
    private String oldOwnerId;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

}
