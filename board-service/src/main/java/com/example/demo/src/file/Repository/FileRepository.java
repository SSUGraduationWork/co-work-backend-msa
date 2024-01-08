package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<Files, Long> {

}