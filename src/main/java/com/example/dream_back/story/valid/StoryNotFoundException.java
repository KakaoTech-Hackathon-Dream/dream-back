package com.example.dream_back.story.valid;

public class StoryNotFoundException extends RuntimeException {
    public StoryNotFoundException(int id) {
        super("Story not found with id: " + id);
    }
}
