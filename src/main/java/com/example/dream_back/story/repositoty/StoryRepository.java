package com.example.dream_back.story.repositoty;


import com.example.dream_back.story.entity.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<StoryEntity, Integer> {
}