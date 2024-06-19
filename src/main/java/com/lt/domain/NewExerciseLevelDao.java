package com.lt.domain;

import lombok.Data;

import java.util.List;

@Data
public class NewExerciseLevelDao {
    private two_level twoLevel;
    private List<three_level> threeLevelList;
}
