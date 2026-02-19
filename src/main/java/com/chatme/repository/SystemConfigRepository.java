package com.chatme.repository;

import com.chatme.entity.SystemConfig;
import com.fast.cqrs.sql.annotation.SqlRepository;
import com.fast.cqrs.sql.repository.FastRepository;

@SqlRepository
public interface SystemConfigRepository extends FastRepository<SystemConfig, String> {
}
