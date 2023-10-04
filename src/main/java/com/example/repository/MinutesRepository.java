package com.example.repository;

import com.example.entity.Minutes;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

//extends 뒤에는 CrudRepository를 작성하여 여러 기능을 사용할 수 있도록 하고, <> 안의 파라미터는 사용할 entity와 그 대푯값(이 프로젝트에서는 id)의 타입 작성
public interface MinutesRepository extends CrudRepository<Minutes, Long> {
    @Override
    ArrayList<Minutes> findAll();
    ArrayList<Minutes> findByDateLike(String date);
    Minutes findByDate(String date);

    ArrayList<Minutes> findByTeamId(Long teamId);
}
