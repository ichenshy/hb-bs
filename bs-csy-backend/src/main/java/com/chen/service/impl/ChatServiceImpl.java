package com.chen.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.ErrorCode;
import com.chen.exception.BusinessException;
import com.chen.mapper.ChatMapper;
import com.chen.model.domain.Chat;
import com.chen.model.domain.Team;
import com.chen.model.domain.User;
import com.chen.model.request.ChatRequest;
import com.chen.model.vo.ChatMessageVO;
import com.chen.model.vo.ChatVO;
import com.chen.model.vo.TeamVO;
import com.chen.model.vo.UserVO;
import com.chen.model.vo.WebSocketVO;
import com.chen.service.ChatService;
import com.chen.service.TeamService;
import com.chen.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.chen.constants.ChatConstant.CACHE_CHAT_HALL;
import static com.chen.constants.ChatConstant.CACHE_CHAT_PRIVATE;
import static com.chen.constants.ChatConstant.CACHE_CHAT_TEAM;
import static com.chen.constants.SystemConstants.PAGE_SIZE;
import static com.chen.constants.UserConstants.ADMIN_ROLE;

/**
 * @author ChenShengyuan
 * @description 针对表【chat(聊天消息表)】的数据库操作Service实现
 * @createDate 2023-06-17 21:50:15
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Override
    public List<ChatMessageVO> getPrivateChat(ChatRequest chatRequest, int chatType, User loginUser) {
        Long toId = chatRequest.getToId();
        if (toId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ChatMessageVO> chatRecords = getCache(CACHE_CHAT_PRIVATE, loginUser.getId() + String.valueOf(toId));
        if (chatRecords != null) {
            saveCache(CACHE_CHAT_PRIVATE, loginUser.getId() + String.valueOf(toId), chatRecords);
            return chatRecords;
        }
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.and(privateChat -> privateChat.eq(Chat::getFromId, loginUser.getId()).eq(Chat::getToId, toId).or().eq(Chat::getToId, loginUser.getId()).eq(Chat::getFromId, toId)).eq(Chat::getChatType, chatType);
        // 两方共有聊天
        List<Chat> list = this.list(chatLambdaQueryWrapper);
        List<ChatMessageVO> chatMessageVOList = list.stream().map(chat -> {
            ChatMessageVO ChatMessageVO = chatResult(loginUser.getId(), toId, chat.getText(), chatType, chat.getCreateTime());
            if (chat.getFromId().equals(loginUser.getId())) {
                ChatMessageVO.setIsMy(true);
            }
            return ChatMessageVO;
        }).collect(Collectors.toList());
        saveCache(CACHE_CHAT_PRIVATE, loginUser.getId() + String.valueOf(toId), chatMessageVOList);
        return chatMessageVOList;
    }

    @Override
    public List<ChatMessageVO> getCache(String redisKey, String id) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<ChatMessageVO> chatRecords;
        if (redisKey.equals(CACHE_CHAT_HALL)) {
            chatRecords = (List<ChatMessageVO>) valueOperations.get(redisKey);
        } else {
            chatRecords = (List<ChatMessageVO>) valueOperations.get(redisKey + id);
        }
        return chatRecords;
    }

    @Override
    public void saveCache(String redisKey, String id, List<ChatMessageVO> chatMessageVOS) {
        try {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            // 解决缓存雪崩
            int i = RandomUtil.randomInt(2, 3);
            if (redisKey.equals(CACHE_CHAT_HALL)) {
                valueOperations.set(redisKey, chatMessageVOS, 2 + i / 10, TimeUnit.MINUTES);
            } else {
                valueOperations.set(redisKey + id, chatMessageVOS, 2 + i / 10, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.error("redis set key error");
        }
    }

    private ChatMessageVO chatResult(Long userId, String text) {
        ChatMessageVO ChatMessageVO = new ChatMessageVO();
        User fromUser = userService.getById(userId);
        WebSocketVO fromWebSocketVo = new WebSocketVO();
        BeanUtils.copyProperties(fromUser, fromWebSocketVo);
        ChatMessageVO.setFormUser(fromWebSocketVo);
        ChatMessageVO.setText(text);
        return ChatMessageVO;
    }

    @Override
    public ChatMessageVO chatResult(Long userId, Long toId, String text, Integer chatType, Date createTime) {
        ChatMessageVO ChatMessageVO = new ChatMessageVO();
        User fromUser = userService.getById(userId);
        User toUser = userService.getById(toId);
        WebSocketVO fromWebSocketVo = new WebSocketVO();
        WebSocketVO toWebSocketVo = new WebSocketVO();
        BeanUtils.copyProperties(fromUser, fromWebSocketVo);
        BeanUtils.copyProperties(toUser, toWebSocketVo);
        ChatMessageVO.setFormUser(fromWebSocketVo);
        ChatMessageVO.setToUser(toWebSocketVo);
        ChatMessageVO.setChatType(chatType);
        ChatMessageVO.setText(text);
        ChatMessageVO.setCreateTime(DateUtil.format(createTime, "yyyy-MM-dd HH:mm:ss"));
        return ChatMessageVO;
    }

    @Override
    public void deleteKey(String key, String id) {
        if (key.equals(CACHE_CHAT_HALL)) {
            redisTemplate.delete(key);
        } else {
            redisTemplate.delete(key + id);
        }
    }

    @Override
    public List<ChatMessageVO> getTeamChat(ChatRequest chatRequest, int chatType, User loginUser) {
        Long teamId = chatRequest.getTeamId();
        if (teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求有误");
        }
        List<ChatMessageVO> chatRecords = getCache(CACHE_CHAT_TEAM, String.valueOf(teamId));
        if (chatRecords != null) {
            List<ChatMessageVO> chatMessageVOS = checkIsMyMessage(loginUser, chatRecords);
            saveCache(CACHE_CHAT_TEAM, String.valueOf(teamId), chatMessageVOS);
            return chatMessageVOS;
        }
        Team team = teamService.getById(teamId);
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getChatType, chatType).eq(Chat::getTeamId, teamId);
        List<ChatMessageVO> chatMessageVOS = returnMessage(loginUser, team.getUserId(), chatLambdaQueryWrapper);
        saveCache(CACHE_CHAT_TEAM, String.valueOf(teamId), chatMessageVOS);
        return chatMessageVOS;
    }

    @Override
    public List<ChatMessageVO> getHallChat(int chatType, User loginUser) {
        List<ChatMessageVO> chatRecords = getCache(CACHE_CHAT_HALL, String.valueOf(loginUser.getId()));
        if (chatRecords != null) {
            List<ChatMessageVO> chatMessageVOS = checkIsMyMessage(loginUser, chatRecords);
            saveCache(CACHE_CHAT_HALL, String.valueOf(loginUser.getId()), chatMessageVOS);
            return chatMessageVOS;
        }
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getChatType, chatType);
        List<ChatMessageVO> chatMessageVOS = returnMessage(loginUser, null, chatLambdaQueryWrapper);
        saveCache(CACHE_CHAT_HALL, String.valueOf(loginUser.getId()), chatMessageVOS);
        return chatMessageVOS;
    }

    private List<ChatMessageVO> checkIsMyMessage(User loginUser, List<ChatMessageVO> chatRecords) {
        return chatRecords.stream().peek(chat -> {
            if (chat.getFormUser().getId() != loginUser.getId() && chat.getIsMy()) {
                chat.setIsMy(false);
            }
            if (chat.getFormUser().getId() == loginUser.getId() && !chat.getIsMy()) {
                chat.setIsMy(true);
            }
        }).collect(Collectors.toList());
    }

    private List<ChatMessageVO> returnMessage(User loginUser, Long userId, LambdaQueryWrapper<Chat> chatLambdaQueryWrapper) {
        List<Chat> chatList = this.list(chatLambdaQueryWrapper);
        return chatList.stream().map(chat -> {
            ChatMessageVO ChatMessageVO = chatResult(chat.getFromId(), chat.getText());
            boolean isCaptain = userId != null && userId.equals(chat.getFromId());
            if (userService.getById(chat.getFromId()).getRole() == ADMIN_ROLE || isCaptain) {
                ChatMessageVO.setIsAdmin(true);
            }
            if (chat.getFromId().equals(loginUser.getId())) {
                ChatMessageVO.setIsMy(true);
            }
            ChatMessageVO.setCreateTime(DateUtil.format(chat.getCreateTime(), "yyyy年MM月dd日 HH:mm:ss"));
            return ChatMessageVO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<ChatVO> chatByAdmin(long currentPage, String searchText) {
        QueryWrapper<Chat> wrapper = new QueryWrapper<>();
        // 添加第二个条件
        if (StringUtils.isNotBlank(searchText)) {
            wrapper.like("text", searchText);
        }
        Page<Chat> chatPage = this.page(new Page<>(currentPage, PAGE_SIZE), wrapper);
        if (CollectionUtils.isEmpty(chatPage.getRecords())) {
            return new Page<>();
        }
        Page<ChatVO> chatVoPage = new Page<>();
        BeanUtils.copyProperties(chatPage, chatVoPage, "records");
        List<ChatVO> collect = chatPage.getRecords().stream().map(chat -> {
            Long fromId = chat.getFromId();
            User fromUser = userService.getById(fromId);
            UserVO fromUserVO = new UserVO();
            BeanUtils.copyProperties(fromUser, fromUserVO);
            ChatVO chatVO = new ChatVO();
            UserVO toUserVO = new UserVO();
            TeamVO teamVO = new TeamVO();
            if (chat.getChatType() == 1) {
                Long toId = chat.getToId();
                User toUser = userService.getById(toId);
                BeanUtils.copyProperties(toUser, toUserVO);
            }
            if (chat.getChatType() == 2) {
                Long teamId = chat.getTeamId();
                Team toTeam = teamService.getById(teamId);
                BeanUtils.copyProperties(toTeam, teamVO);
            }
            BeanUtils.copyProperties(chat,chatVO);
            chatVO.setFromUser(fromUserVO);
            chatVO.setToUser(toUserVO);
            chatVO.setTeamVO(teamVO);
            return chatVO;
        }).collect(Collectors.toList());
        chatVoPage.setRecords(collect);
        return chatVoPage;
    }
}




