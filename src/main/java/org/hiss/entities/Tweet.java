package org.hiss.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tweet {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long tweetID;

    @NotEmpty
    private String content;

    private Date time;

    private Integer likeCount;

    private Boolean likedByCurrentUser;

    @ManyToOne
    @JoinColumn(name="userid", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "tweet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Comment> comments;
}
