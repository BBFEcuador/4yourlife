package com.foryourlife.admin.programs.training.application;

import com.foryourlife.admin.programs.attendance.infraestructure.httpController.DaysEnum;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingValidationService {

    public void validateDateInTrainingPeriod(LocalDate today, LocalDate start, LocalDate end) {
        if (today.isBefore(start) || today.isAfter(end)) {
            throw new BaseException(
                    "Fuera de tiempo",
                    List.of("No se puede asignar acciones fuera del periodo del entrenamiento.")
            );
        }
    }

    public void validateDayConsistency(DaysEnum dayEnum, long dayNumber) {
        if ((dayEnum == DaysEnum.FRIDAY && dayNumber != 1) ||
                (dayEnum == DaysEnum.SATURDAY && dayNumber != 2) ||
                (dayEnum == DaysEnum.SUNDAY && dayNumber != 3)) {

            throw new BaseException(
                    "Día incorrecto",
                    List.of("El día seleccionado no coincide con el día del entrenamiento.")
            );
        }
    }
}
