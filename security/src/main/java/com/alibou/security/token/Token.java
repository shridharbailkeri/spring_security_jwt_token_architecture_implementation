package com.alibou.security.token;


import com.alibou.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
// helps to build objects in easy way
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Token {
    @Id
    @GeneratedValue
    private Integer id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
