package com.example.registrationlogin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column
    private String address;
    @Column
    private String fullName;
    @Column
    private String phone;
    @Column
    private String age;

    private String token;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")}, // id bảng user
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")}) // id bảng roles

    // users_role = bảng trung gian: có user_id khóa ngoai với bảng user(id)
    //                                  role_id khóa ngoại tới bảng roles(id)
    //JoinColumn: tham chiếu đến bảng entity hiện tại
    // inverseJoinColumns: tham chiếu đến entity khác trong quan hệ
    private List<Role> roles = new ArrayList<>();

}
