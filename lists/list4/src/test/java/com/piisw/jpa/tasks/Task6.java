package com.piisw.jpa.tasks;

import com.piisw.jpa.entities.*;
import com.piisw.jpa.repositories.FollowerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
class Task6 {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @SuppressWarnings("preview")
    @Test
    void shouldFetchFollowerWithCommentsAndEventsInOneQuery() {
        // given
        Server server = new Server("MockServer", "127.0.0.1");
        entityManager.persist(server);

        RequestEvent event = new RequestEvent();
        event.setTime(LocalDateTime.now());
        event.setDuration(100);
        event.setServer(server);
        event.setDescription("Ważne zdarzenie");
        event.setAnalysisRequired(true);
        event.setMethod(HttpMethod.GET);
        entityManager.persist(event);

        Comment comment = new Comment();
        comment.setContent("To jest krytyczny błąd!");
        comment.setEvent(event);
        entityManager.persist(comment);

        Follower follower = new Follower();
        follower.setUserId("user123");
        follower.setSubscriptionDate(LocalDateTime.now());
        follower.setComments(Set.of(comment));
        entityManager.persist(follower);

        entityManager.flush();
        entityManager.clear(); // Czyścimy kontekst, żeby wymusić zapytanie do bazy (SELECT)

        // when
        Optional<Follower> foundFollower = followerRepository.findByUserId("user123");

        // then
        assertThat(foundFollower.isPresent(), is(true));

        // Wyciągamy zdefiniowane pola:
        Follower f = foundFollower.get();
        f.getComments().forEach(c -> {
            Event e = c.getEvent();
            System.out.println(STR."Follower Subscription: \{f.getSubscriptionDate()}");
            System.out.println(STR."Comment Content: \{c.getContent()}");
            System.out.println(STR."Event Description: \{e.getDescription()}");
            System.out.println(STR."Event Time: \{e.getTime()}");
            System.out.println(STR."Event Analysis Required: \{e.isAnalysisRequired()}");

            // Asercje gwarantujące poprawne wczytanie danych z bazy
            assertThat(c.getContent(), is("To jest krytyczny błąd!"));
            assertThat(e.getDescription(), is("Ważne zdarzenie"));
        });
    }
}