package com.chen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.domain.Chat;
import com.chen.model.domain.User;
import com.chen.model.request.ChatRequest;
import com.chen.model.vo.ChatMessageVO;
import com.chen.model.vo.ChatVO;

import java.util.Date;
import java.util.List;

/**
* @author Shier
* @description 针对表【chat(聊天消息表)】的数据库操作Service
* @createDate 2023-06-17 21:50:15
*/
public interface ChatService extends IService<Chat> {
     List<ChatMessageVO> getPrivateChat(ChatRequest chatRequest, int chatType, User loginUser);

     List<ChatMessageVO> getCache(String redisKey, String id);

     void saveCache(String redisKey, String id, List<ChatMessageVO> chatMessageVos);

     ChatMessageVO chatResult(Long userId, Long toId, String text, Integer chatType, Date createTime);

     void deleteKey(String key, String id);

    List<ChatMessageVO> getTeamChat(ChatRequest chatRequest, int teamChat, User loginUser);

    List<ChatMessageVO> getHallChat(int chatType, User loginUser);

    Page<ChatVO> chatByAdmin(long currentPage, String searchText);
}
