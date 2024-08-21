
package com.jorge.movies.controllers;

import com.jorge.movies.models.Movie;
import com.jorge.movies.repositories.MovieRepository;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("api/movies")
public class MovieController {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @CrossOrigin   // para poder hacer la peticipon de desde un front
    @GetMapping("/all")
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
    
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() 
                -> ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie savedMovie = movieRepository.save(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }


    @CrossOrigin
    @PutMapping("/update/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie){
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        updatedMovie.setId(id);
        Movie savedMovie = movieRepository.save(updatedMovie);
        return ResponseEntity.ok().body(savedMovie);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        movieRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @PutMapping("/vote/{id}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id, @RequestBody double rating){
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Optional<Movie> movieOptional = movieRepository.findById(id);
        Movie movie = movieOptional.get();

        double newRating = ((movie.getVotes() * movie.getRating()) + rating) / (movie.getVotes() + 1);
        movie.setVotes(movie.getVotes() + 1);
        movie.setRating(newRating);

        Movie movieSaved = movieRepository.save(movie);

        return ResponseEntity.ok(movieSaved);
    }



}
