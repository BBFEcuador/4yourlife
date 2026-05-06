package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.OnNullDesistedAttend;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@DomainEventSubscriber({OnNullDesistedAttend.class})
public class RemoveParticipantOnNullAttend {
    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;

    public RemoveParticipantOnNullAttend(ParticipantRepository participantRepository, AttendanceRepository attendanceRepository) {
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @EventListener
    public void on(OnNullDesistedAttend event) {
        var user = event.getUser();
        var attendance = event.getAttendance();

        if (attendance.getFridayAttendance() == null) attendance.setFridayAttendance(AttendanceStatus.DESERTO);
        if (attendance.getSaturdayAttendance() == null) attendance.setSaturdayAttendance(AttendanceStatus.DESERTO);
        if (attendance.getSundayAttendance() == null) attendance.setSundayAttendance(AttendanceStatus.DESERTO);

        attendance.setActive(false);
        var participant = participantRepository.findByUserId(user.getId());
        if (participant.isPresent()) {
            participant.get().setIsDesertor(true);
            participant.get().setIsLingerer(true);
//            if (team.isPresent()){
//                team.get().removeUser(participant.get());
//                teamRepository.save(team.get());
//            }
            participantRepository.save(participant.get());
        }
        attendanceRepository.save(attendance);
    }
}
