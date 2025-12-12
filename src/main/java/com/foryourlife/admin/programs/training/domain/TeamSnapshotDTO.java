package com.foryourlife.admin.programs.training.domain;

import java.util.List;

public class TeamSnapshotDTO {

    private String id;
    private String name;
    private String photo;
    private Integer trainingNumber;

    private TrainerSnapshotDTO trainer;
    private TrainingSnapshotDTO training;

    private List<String> userIds;
    private List<String> masterLifeIds;
    private List<String> staffIds;
    private List<String> visionaryIds;

}
