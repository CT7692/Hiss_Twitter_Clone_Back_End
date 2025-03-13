package org.hiss.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long commentID;

    @NotEmpty
    private String content;

    private Date time;

    private Integer likeCount;

    private Boolean likedByCurrentUser;

    @ManyToOne
    @JoinColumn(name="tweetid", nullable = false)
    private Tweet tweet;

    @ManyToOne
    @JoinColumn(name="userid", nullable = false)
    private User user;
}
