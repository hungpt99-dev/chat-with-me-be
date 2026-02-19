package com.chatme.repository;

import com.chatme.entity.Notification;
import com.fast.cqrs.sql.annotation.Param;
import com.fast.cqrs.sql.annotation.Select;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;
import java.util.List;

@SqlRepository
public interface NotificationRepository extends FastRepository<Notification, String> {

    @Select("SELECT * FROM notifications ORDER BY created_at DESC")
    List<Notification> findAllDesc();

    @Select("SELECT * FROM notifications WHERE user_id = :userId ORDER BY created_at DESC")
    List<Notification> findByUserId(@Param("userId") String userId);
}
