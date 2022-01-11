package com.example.demo.model.database;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    @Column(name = "username")
    String username;

    @Column(name = "record_create_date")
    @CreationTimestamp
    Date recordCreateDate;

    @Column(name = "record_update_date")
    @CreationTimestamp
    Date recordUpdateDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(Date recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public Date getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(Date recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }
}
