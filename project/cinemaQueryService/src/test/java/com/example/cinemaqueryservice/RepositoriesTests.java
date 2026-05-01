package com.example.cinemaqueryservice;

import com.example.cinemaqueryservice.model.RepertoireView;
import com.example.cinemaqueryservice.model.ScreeningSeatView;
import com.example.cinemaqueryservice.repository.RepertoireViewRepository;
import com.example.cinemaqueryservice.repository.ScreeningSeatViewRepository;
import com.example.cinemaqueryservice.dto.MovieDictionaryDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RepositoriesTests {

	@Autowired
	private ScreeningSeatViewRepository screeningSeatViewRepository;

	@Autowired
	private RepertoireViewRepository repertoireViewRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldFindByScreeningId(){
        Long screeningID = 1L;

		List<ScreeningSeatView> queryResult = screeningSeatViewRepository.findByScreeningId(screeningID);

		assertThat(queryResult.size(), greaterThan(0));
	}

	@Test
	void shouldReturnEmptyInvalidScreeningId(){
		Long screeningID = -1L;

		List<ScreeningSeatView> queryResult = screeningSeatViewRepository.findByScreeningId(screeningID);

		assertThat(queryResult.size(), equalTo(0));
	}

	@Test
	void shouldMarkSeatsAsOccupied(){
		Long screeningID = 1L;
		List<Long> seatIDs = new ArrayList<>();

		List<ScreeningSeatView> view = screeningSeatViewRepository.findByScreeningId(screeningID);
		for(ScreeningSeatView v : view){
			if(v.isAvailable())
				seatIDs.add(v.getOriginalSeatId());
		}
		assertThat(view.getFirst().isAvailable(), equalTo(true));

		screeningSeatViewRepository.markSeatsAsOccupied(screeningID, seatIDs);

		view = screeningSeatViewRepository.findByScreeningId(screeningID);
		assertThat(view.getFirst().isAvailable(), equalTo(false));
	}

	@Test
	void shouldNotMarkOccupiedSeatsAsOccupied(){
		Long screeningID = 1L;
		List<Long> seatIDs = new ArrayList<>();

		List<ScreeningSeatView> view = screeningSeatViewRepository.findByScreeningId(screeningID);
		for(ScreeningSeatView v : view){
			if(v.isAvailable())
				seatIDs.add(v.getOriginalSeatId());
		}

		screeningSeatViewRepository.markSeatsAsOccupied(screeningID, seatIDs);

		view = screeningSeatViewRepository.findByScreeningId(screeningID);
		assertThat(view.getFirst().isAvailable(), equalTo(false));

		int nAffected = screeningSeatViewRepository.markSeatsAsOccupied(screeningID, seatIDs);
		assertThat(nAffected, greaterThan(0));

		view = screeningSeatViewRepository.findByScreeningId(screeningID);
		assertThat(view.getFirst().isAvailable(), equalTo(false));
	}

	@Test
	void shouldNotMarkUnexistentSeatsAsOccupied(){
		Long screeningID = -1L;
		List<Long> seatIDs = new ArrayList<>();

		int nAffected = screeningSeatViewRepository.markSeatsAsOccupied(screeningID, seatIDs);
		assertThat(nAffected, equalTo(0));
	}

	@Test
	void shouldHandleEmptySeatListAsOccupied(){
		Long screeningID = 1L;
		List<Long> seatIDs = new ArrayList<>();

		List<ScreeningSeatView> view = screeningSeatViewRepository.findByScreeningId(screeningID);

		assertThat(view.getFirst().isAvailable(), equalTo(true));

		screeningSeatViewRepository.markSeatsAsOccupied(screeningID, seatIDs);

		view = screeningSeatViewRepository.findByScreeningId(screeningID);
		assertThat(view.getFirst().isAvailable(), equalTo(true));
	}

	@Test
	void shouldFindFilteredRepertoire(){
		Long movieID = 1L;
		String roomName = "Sala 6 - 3D";

		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(movieID, roomName, pageable);

		assertThat(page.getContent().size(), lessThanOrEqualTo(pageSize));
		assertThat(page.getNumber(), equalTo(pageNumber));
		assertThat(page.getTotalElements(), greaterThan(0L));
		assertThat(page.getTotalPages(), greaterThan(0));
	}

	@Test
	void shouldFilterRepertoireWithoutMovieId(){
		String roomName = "Sala 6 - 3D";

		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(null, roomName, pageable);

		assertThat(page.getContent().size(), lessThanOrEqualTo(pageSize));
		assertThat(page.getNumber(), equalTo(pageNumber));
		assertThat(page.getTotalElements(), greaterThan(0L));
		assertThat(page.getTotalPages(), greaterThan(0));
	}

	@Test
	void shouldFilterRepertoireWithoutRoom(){
		Long movieID = 1L;

		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(movieID, null, pageable);

		assertThat(page.getContent().size(), lessThanOrEqualTo(pageSize));
		assertThat(page.getNumber(), equalTo(pageNumber));
		assertThat(page.getTotalElements(), greaterThan(0L));
		assertThat(page.getTotalPages(), greaterThan(0));
	}

	@Test
	void shouldNotFilterRepertoireWithoutFilter(){
		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(null, null, pageable);

		assertThat(page.getContent().size(), lessThanOrEqualTo(pageSize));
		assertThat(page.getNumber(), equalTo(pageNumber));
		assertThat(page.getTotalElements(), greaterThan(0L));
		assertThat(page.getTotalPages(), greaterThan(0));
	}

	@Test
	void shouldHandleEmptyFilter(){
		Long movieID = 1L;
		String roomName = "";

		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(movieID, roomName, pageable);

		assertThat(page.getContent().size(), lessThanOrEqualTo(pageSize));
		assertThat(page.getNumber(), equalTo(pageNumber));
		assertThat(page.getTotalElements(), greaterThan(0L));
		assertThat(page.getTotalPages(), greaterThan(0));
	}

	@Test
	void shouldHandleIncorrectIDFilter(){
		Long movieID = -1L;
		String roomName = "Sala 6 - 3D";

		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(movieID, roomName, pageable);

		assertThat(page.getContent().size(), equalTo(0));
		assertThat(page.getTotalElements(), equalTo(0L));
		assertThat(page.getTotalPages(), equalTo(0));
	}

	@Test
	void shouldHandleIncorrectStringFilter(){
		Long movieID = 1L;
		String roomName = "XYZ";

		int pageNumber = 2;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber,pageSize);

		Page<RepertoireView> page = repertoireViewRepository.findFilteredRepertoire(movieID, roomName, pageable);

		assertThat(page.getContent().size(), equalTo(0));
		assertThat(page.getTotalElements(), equalTo(0L));
		assertThat(page.getTotalPages(), equalTo(0));
	}

	@Test
	void shouldFindDistinctMovies(){
		List<MovieDictionaryDto> movies = repertoireViewRepository.findDistinctMovies();

		assertThat(movies.size(), greaterThan(0));
		assertThat(movies.size(), equalTo(25));
	}

	@Test
	void shouldFindDistinctRooms(){
		List<String> rooms = repertoireViewRepository.findDistinctRooms();

		assertThat(rooms.size(), greaterThan(0));
		assertThat(rooms.size(), equalTo(8));
	}

}
