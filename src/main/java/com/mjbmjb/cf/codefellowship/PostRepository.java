package com.mjbmjb.cf.codefellowship;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post,Long> {
    Iterable<Post> findByUsername(String username);
}
