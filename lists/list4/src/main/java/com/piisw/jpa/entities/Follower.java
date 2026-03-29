package com.piisw.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
// Zdefiniowanie grafu pobierającego powiązane relacje w jednym zapytaniu SQL (JOIN)
@NamedEntityGraph(
        name = "Follower.withCommentsAndEvents",
        attributeNodes = {
                @NamedAttributeNode(value = "comments", subgraph = "commentsSubgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "commentsSubgraph",
                        attributeNodes = {
                                @NamedAttributeNode("event")
                        }
                )
        }
)
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOLLOWER_ID_GENERATOR")
    @SequenceGenerator(name = "FOLLOWER_ID_GENERATOR", sequenceName = "FOLLOWER_SEQ", allocationSize = 1)
    private Long id;

    private String userId;

    private LocalDateTime subscriptionDate;

    // Relacja M:N - tworzy tabelę łączącą
    @ManyToMany
    @JoinTable(
            name = "follower_comment",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments;
}