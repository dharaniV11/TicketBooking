package com.example.ticketbooking.controller;

import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.requestBean.MovieRequestBean;
import com.example.ticketbooking.requestBean.MovieShowDetailsRequestBean;
import com.example.ticketbooking.requestBean.ShowTimeRequestBean;
import com.example.ticketbooking.requestBean.TheaterRequestBean;
import com.example.ticketbooking.responseBean.MovieResponseBean;
import com.example.ticketbooking.responseBean.MovieShowDetailsResponseBean;
import com.example.ticketbooking.responseBean.ShowTimeResponseBean;
import com.example.ticketbooking.responseBean.TheaterResponseBean;
import com.example.ticketbooking.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/addMovie")
    public ResponseEntity<MovieRequestBean> addMovie(@Valid @RequestBody MovieRequestBean movieRequestBean) {
        MovieRequestBean addedMovie = adminService.addMovie(movieRequestBean);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMovie);
    }

    @DeleteMapping("/deleteMovie/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID movieId) throws ResourceNotFoundException {
        adminService.deleteMovieById(movieId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/getAllMovies")
    public List<MovieResponseBean> getAllMovies() {
        return adminService.getAllMovies();
    }

    @PostMapping("/addTheater")
    public TheaterRequestBean addTheater(@Valid @RequestBody TheaterRequestBean theaterRequestBean) {
        return adminService.addTheater(theaterRequestBean);
    }

    @DeleteMapping("/deleteTheater/{theaterId}")
    public String deleteTheater(@PathVariable UUID theaterId) throws ResourceNotFoundException {
        return adminService.deleteTheaterById(theaterId);
    }

    @GetMapping("/getAllTheater")
    public List<TheaterResponseBean> getAllTheaters() {
        return adminService.getAllTheater();
    }

    @PostMapping("/addShowTime")
    public ShowTimeRequestBean addShowTime(@Valid @RequestBody ShowTimeRequestBean showTimeRequestBean) {
        return adminService.addShowTime(showTimeRequestBean);
    }

    @DeleteMapping("/deleteShowTime/{showTimeId}")
    public String deleteShowTime(@PathVariable UUID showTimeId) throws ResourceNotFoundException {
        return adminService.deleteShowTimeById(showTimeId);
    }

    @GetMapping("/getAllShowTime")
    public List<ShowTimeResponseBean> getAllShowTime() {
        return adminService.getAllShowTime();
    }

    @GetMapping("/getAllShowDetails")
    public List<MovieShowDetailsResponseBean> getAllShowAvailabilities() {
        return adminService.getAllShowAvailabilities();
    }

    @DeleteMapping("/deleteMovieShowDetails/{movieShowDetailsId}")
    public String deleteMovieShowDetails(@PathVariable UUID movieShowDetailsId) throws ResourceNotFoundException {
        return adminService.DeleteMovieShowDetails(movieShowDetailsId);
    }

    @PostMapping("/addMovieShowDetails")
    public ResponseEntity<String> addMovieShowDetails(@Valid @RequestBody MovieShowDetailsRequestBean movieShowDetailsRequestBean) {
        try {
            String response = adminService.addMovieShowDetails(movieShowDetailsRequestBean);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
