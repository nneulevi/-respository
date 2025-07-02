package org.example.project2.Service;

import org.example.project2.Mapper.agendaMapper;
import org.example.project2.Mapper.meetMapper;
import org.example.project2.entity.Agenda;
import org.example.project2.entity.Meeting;
import org.example.project2.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class meetingService {
    @Autowired
    agendaMapper agendaMapper;
    @Autowired
    meetMapper meetMapper;

    public PageResult<Meeting> getPassedMeetingsByStartTime(Long id, String title, String content, String creator, LocalDateTime startTime,LocalDateTime endTime, int page, int size) {
        int offset = (page - 1) * size;
        List<Meeting> meets = meetMapper.findMeetingsByKeyword(id, title, content,creator, startTime, endTime, offset, size);
        Long total = meetMapper.findMeetingsByKeywordCount(id, title, content, creator, startTime, endTime);
        meets.sort((a,b)->b.getStartTime().compareTo(a.getStartTime()));
        return new PageResult<>(page, size, total, meets);
    }

    public PageResult<Meeting> getNotPassedMeeting(Long id, String title, String content, String creator, LocalDateTime startTime,LocalDateTime endTime, int page, int size){
        int offset = (page - 1) * size;
        List<Meeting> meets = meetMapper.findMeetingsNotPassed(id, title, content,creator, startTime, endTime, offset, size);
        Long total = meetMapper.findMeetingsNotPassedCount(id, title, content, creator, startTime, endTime);
        meets.sort((a,b)->b.getStartTime().compareTo(a.getStartTime()));
        return new PageResult<>(page, size, total, meets);
    }

    public PageResult<Meeting> getPersonalMeeting(Integer status,String creator,int page, int size) {
        int offset = (page - 1) * size;
        List<Meeting> meets = meetMapper.selectByStatusAndAuthor(status, creator, size,offset);
        Long total = meetMapper.selectByStatusAndAuthorCount(status, creator);
        return new PageResult<>(page, size, total, meets);
    }

    public PageResult<Agenda> getSubAgendaByTime(Long meeting_id,int page,int size){
        int offset = (page - 1) * size;
        List<Agenda> agenda = agendaMapper.findBystartTime(meeting_id, size, offset);
        Long total = agendaMapper.countByMeetingsId(meeting_id);
        return new PageResult<>(page,size,total,agenda);
    }

    public PageResult<Agenda> getSubAgendaByDuration(Long meeting_id,int page,int size){
        int offset = (page - 1) * size;
        List<Agenda> agenda = agendaMapper.findByDuration(meeting_id, size,offset);
        Long total = agendaMapper.countByMeetingsId(meeting_id);
        return new PageResult<>(page,size,total,agenda);
    }

    public Agenda getAgenda(Long id){
        Agenda agenda = agendaMapper.findById(id);
        return agenda;
    }


}
