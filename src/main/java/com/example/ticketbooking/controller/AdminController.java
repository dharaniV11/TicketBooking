package com.example.ticketbooking.controller;

import com.example.ticketbooking.RequestBean.MovieRequestBean;
import com.example.ticketbooking.RequestBean.MovieShowDetailsRequestBean;
import com.example.ticketbooking.RequestBean.ShowTimeRequestBean;
import com.example.ticketbooking.RequestBean.TheaterRequestBean;
import com.example.ticketbooking.ResponseBean.ShowTimeResponseBean;
import com.example.ticketbooking.ResponseBean.TheaterResponseBean;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.ResponseBean.MovieResponseBean;
import com.example.ticketbooking.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public MovieRequestBean addMovie(@Valid @RequestBody MovieRequestBean movieRequestBean) {
        return adminService.addMovie(movieRequestBean);
    }

    @DeleteMapping("/deleteMovie/{movieId}")
    public String deleteMovie(@PathVariable UUID movieId) throws ResourceNotFoundException {
        return adminService.deleteMovieById(movieId);
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
