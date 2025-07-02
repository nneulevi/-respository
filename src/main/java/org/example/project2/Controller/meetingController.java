package org.example.project2.Controller;

import org.apache.ibatis.annotations.Update;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.agendaMapper;
import org.example.project2.Mapper.meetMapper;
import org.example.project2.Service.AIService;
import org.example.project2.Service.meetingService;
import org.example.project2.entity.Agenda;
import org.example.project2.entity.Meeting;
import org.example.project2.entity.PageResult;
import org.example.project2.entity.User_d;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
public class meetingController {
    @Autowired
    ChatModel chatModel;
    @Autowired
    agendaMapper agendaMapper;
    @Autowired
    meetMapper meetMapper;
    @Autowired
    meetingService meetService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AIService AiService;

    //用户提交
    @RequestMapping("/addAgenda")//传子议程的所有属性，必有所属meeting的id
    public ResponseEntity<Integer> submit(@RequestBody Agenda agenda) {
        int result = agendaMapper.insert(agenda);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/getAllAgendaByTime")//同下，按开始时间排序
    public ResponseEntity<PageResult<Agenda>> getAgenda(@RequestParam(required = false) long meeting_id,@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(meetService.getSubAgendaByTime(meeting_id,page,size));
    }

    @RequestMapping("/getAllAgendaByDuration")//以持续时间排序，传所属会议的id和分页用属性
    public ResponseEntity<PageResult<Agenda>> getAgendaByDuration(@RequestParam(required = false) long meeting_id,@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(meetService.getSubAgendaByDuration(meeting_id,page,size));
    }

    @RequestMapping("/getAgendaDetail")//传议程的id
    public ResponseEntity<Agenda> getAgenda(@RequestParam long id){
        return ResponseEntity.ok(meetService.getAgenda(id));
    }

    @RequestMapping("/updateAgendaInfo")//传Agenda的所有属性，判断当前用户和创作者是否相同
    public ResponseEntity<Integer> update(@RequestBody Agenda agenda){
        Agenda old = agendaMapper.findById(agenda.getId());
        if(old == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
        if(agenda.getTitle()!=null) old.setTitle(agenda.getTitle());
        if(agenda.getSpeaker()!=null) old.setSpeaker(agenda.getSpeaker());
        if(agenda.getStartTime()!=null) old.setStartTime(agenda.getStartTime());
        if(agenda.getDuration()!=null) old.setDuration(agenda.getDuration());
        if(agenda.getContent()!=null) old.setContent(agenda.getContent());
        int result = agendaMapper.updateAgenda(agenda.getId(),old);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/deleteAgenda")//传子议程的id
    public ResponseEntity<Integer> delete(@RequestParam long id){
        int result = agendaMapper.deleteById(id);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/getPassedMeetings")//已通过的会议，均可见，模糊查询，注意顺序
    public ResponseEntity<PageResult<Meeting>> getPassedMeetings(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String creator,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            return ResponseEntity.badRequest().body(null);
        }

        PageResult<Meeting> result = meetService.getPassedMeetingsByStartTime(
                id != null ? id : 0L, title, content, creator, startTime, endTime, page, size);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/getNotPassedMeetings")//管理员可见，待审核的会议
    public ResponseEntity<PageResult<Meeting>> getNotPassedMeetings(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String creator,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            return ResponseEntity.badRequest().body(null);
        }

        PageResult<Meeting> result = meetService.getNotPassedMeeting(
                id != null ? id : 0L, title, content, creator, startTime, endTime, page, size);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/getCertainStatusMeetings")//查找某用户创建的会议，传状态，创作者，分页属性
    public ResponseEntity<PageResult<Meeting>> getPersonalMeetings(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String creator,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Meeting> result = meetService.getPersonalMeeting(status, creator, page, size);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/getMeetingDetail")//获取会议信息，传meeting的id
    public ResponseEntity<Meeting> getMeeting(@RequestParam long id){
        return ResponseEntity.ok(meetMapper.findById(id));
    }

    @RequestMapping("/deleteMeeting")//要删除会议的id，当前登录用户的用户名
    public ResponseEntity<Integer> deleteMeeting(@RequestParam long id,@RequestParam String now_username){
        String now = meetMapper.getCreator(id);
        User_d user_d = userMapper.findByUsername(now_username);
        int status = user_d.getStatus();
        if(status == 0 && !now.equals(now_username)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
        int result = meetMapper.deleteById(id);
        agendaMapper.deleteByMeetingId(id);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/passMeeting")//选择会议的id
    public ResponseEntity<Integer> pass(@RequestParam(required = false) long id){
        int result = meetMapper.updateStatus(id);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/refuseMeeting")//选择会议的id
    public ResponseEntity<Integer> refuse(@RequestParam(required = false) long id){
        int result = meetMapper.refuseStatus(id);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/updateMeeting")//要修改的会议，当前登录用户的用户名,确保meeting已经存在
    public ResponseEntity<Integer> update(@RequestBody Meeting meeting,@RequestParam(required = false) String now_username){
        Meeting old = meetMapper.findById(meeting.getId());
        if(old == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
        User_d user_d = userMapper.findByUsername(now_username);
        int status = user_d == null ? 1 : user_d.getStatus();
        if(status == 0 && !old.getCreator().equals(now_username)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
        if (meeting.getTitle() != null) old.setTitle(meeting.getTitle());
        if (meeting.getCoverImage() != null) old.setCoverImage(meeting.getCoverImage());
        if (meeting.getAddress() != null) old.setAddress(meeting.getAddress());
        if (meeting.getStartTime() != null) old.setStartTime(meeting.getStartTime());
        if (meeting.getEndTime() != null) old.setEndTime(meeting.getEndTime());
        if (meeting.getContent() != null) old.setContent(meeting.getContent());
        if (meeting.getCreator() != null) old.setCreator(meeting.getCreator());
        int result = meetMapper.modify(old);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/addMeeting")//增加的会议信息，当前用户，同步调用addAgenda以同时增加会议的子议程
    public ResponseEntity<Integer> addMeeting(@RequestBody Meeting meeting,@RequestParam(required = false) String now_username){
        User_d user_d = userMapper.findByUsername(now_username);
        if(user_d == null) meeting.setStatus(1);
        else meeting.setStatus(user_d.getStatus());
        int result = meetMapper.insert(meeting);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/AIsummary")//处理富文本，返回值为AI的回答
    public ResponseEntity<String> ask(@RequestParam String prompt) {
        String result = AiService.chat(prompt);
        return ResponseEntity.ok(result);
    }
}
