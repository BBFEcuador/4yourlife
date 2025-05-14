package com.foryourlife.admin.programs.training.infrastructure.httpControllers;

import com.foryourlife.admin.programs.training.domain.Training;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TrainingResponse {
        private String id;
        private String title;
        private LocalDate start;
        private LocalDateTime end;
        private boolean allDay;
        private String color;
        private Training embedded;

        public static class ExtendedProps {
            private String description;
            private String location;
            private String[] guests;

            public ExtendedProps(String description, String location, String[] guests) {
                this.description = description;
                this.location = location;
                this.guests = guests;
            }

            // Getters and setters
            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String[] getGuests() {
                return guests;
            }

            public void setGuests(String[] guests) {
                this.guests = guests;
            }
        }

        private ExtendedProps extendedProps;

    public TrainingResponse(String id, String title, LocalDate start, LocalDateTime end, boolean allDay, String color, ExtendedProps extendedProps, Training embedded) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.allDay = allDay;
        this.color = color;
        this.extendedProps = extendedProps;
        this.embedded = embedded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ExtendedProps getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(ExtendedProps extendedProps) {
        this.extendedProps = extendedProps;
    }

    public ExtendedProps createExtendedProps(String description, String location, String[] guests) {
        return new ExtendedProps(description, location, guests);
    }

    public Training getEmbedded() {
        return embedded;
    }
}
