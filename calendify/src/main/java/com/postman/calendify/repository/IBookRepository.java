package com.postman.calendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postman.calendify.models.Book;

public interface IBookRepository extends JpaRepository<Book, Long> {

}
