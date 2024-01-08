package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_name")
    private String name;
    @Column(name = "role")
    private String role;
    @Column(name = "student_number")
    private int studentNumber;
    private String university;
    private String department;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Builder
    public Member(String email, String name, String role, int studentNumber, String university, String department, String pictureUrl){
        this.email = email;
        this.name = name;
        this.role = role;
        this.studentNumber = studentNumber;
        this.university = university;
        this.department = department;
        this.pictureUrl = pictureUrl;
    }
}
