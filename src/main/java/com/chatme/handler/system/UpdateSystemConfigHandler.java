package com.chatme.handler.system;


import com.chatme.entity.SystemConfig;
import com.chatme.repository.SystemConfigRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSystemConfigHandler implements CommandHandler<UpdateSystemConfigHandler.Command> {

    public record Command(
        String configKey,
        String configValue,
        String description
    ) {}

    private final SystemConfigRepository systemConfigRepository;

    @Override
    public void handle(Command cmd) {
        SystemConfig config = new SystemConfig(cmd.configKey(), cmd.configValue(), cmd.description());
        systemConfigRepository.save(config);
    }
}
