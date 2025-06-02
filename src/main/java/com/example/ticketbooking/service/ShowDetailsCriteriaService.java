package com.example.ticketbooking.service;

import com.example.ticketbooking.entity.*;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.repository.MovieRepository;
import com.example.ticketbooking.repository.ShowTimeRepository;
import com.example.ticketbooking.repository.TheaterRepository;
import com.example.ticketbooking.ResponseBean.MovieShowDetailsResponseBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowDetailsCriteriaService {

    private static final Logger log = LoggerFactory.getLogger(ShowDetailsCriteriaService.class);
    @PersistenceContext
    private EntityManager entityManager;

    private final TheaterRepository theaterRepository;

    private final MovieRepository movieRepository;

    private final ShowTimeRepository showTimeRepository;

    public List<MovieShowDetailsResponseBean> searchShowDetails(String theater, String movie, LocalDateTime showTime) throws ResourceNotFoundException {

        if(theater != null && theaterRepository.findByName(theater).isEmpty())
        {
            log.error("No theater found with theater name {}" , theater);
            throw new ResourceNotFoundException("No theater found with given name " + theater);
        }

        if(movie != null && movieRepository.findMovieByName(movie).isEmpty())
        {
            log.error("No movie found with given Movie name {}", movie);
            throw new ResourceNotFoundException("No movie found with given name " + movie);
        }

        if(showTime != null && showTimeRepository.findByShowTime(showTime).isEmpty())
        {
            log.error("No showTime found with given showTime {}", showTime);
            throw new ResourceNotFoundException("No showTime found with given name " + showTime);
        }

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<MovieShowDetailsEntity> query = criteriaBuilder.createQuery(MovieShowDetailsEntity.class);
            Root<MovieShowDetailsEntity> root = query.from(MovieShowDetailsEntity.class);

            List<Predicate> predicates = new ArrayList<>();

            if (theater != null) {
                predicates.add(criteriaBuilder.equal(root.get("theater").get("theaterName"), theater));
            }

            if (movie != null) {
                predicates.add(criteriaBuilder.equal(root.get("movie").get("movieName"), movie));
            }

            if (showTime != null) {
                predicates.add(criteriaBuilder.equal(root.get("showTime").get("showTime"), showTime));
            }

            query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            TypedQuery<MovieShowDetailsEntity> typedQuery = entityManager.createQuery(query);
            List<MovieShowDetailsEntity> resultList = typedQuery.getResultList();
            List<MovieShowDetailsResponseBean> movieShowDetailsResponseBeans = new ArrayList<>();

            for (MovieShowDetailsEntity movieShowDetailsEntity : resultList) {
                MovieShowDetailsResponseBean movieShowDetailsResponseBean = MovieShowDetailsResponseBean.builder()
                        .theaterName(movieShowDetailsEntity.getTheater().getTheaterName())
                        .movieName(movieShowDetailsEntity.getMovie().getMovieName())
                        .showTime(movieShowDetailsEntity.getShowTime().getShowTime())
                        .availableSeats(movieShowDetailsEntity.getAvailableSeats())
                        .build();

                movieShowDetailsResponseBeans.add(movieShowDetailsResponseBean);
            }
            if (movieShowDetailsResponseBeans.isEmpty())
            {
                log.error("No data found with given criteria {} - {} - {}", movie, theater, showTime);
                throw new ResourceNotFoundException("No data found with given criteria");
            }
            else {
                log.info("Found {} data", movieShowDetailsResponseBeans.size());
                return movieShowDetailsResponseBeans;
            }
        }
    }
