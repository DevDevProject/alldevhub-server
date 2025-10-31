package com.example.kafka.consumer;

import java.util.List;

import com.example.common.dto.kafka.OpenSourceRepoCreateDto;
import com.example.open_source.entity.Project;
import com.example.open_source.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenSourceConsumer {

    private final ProjectRepository projectRepository;

    @KafkaListener(
            topics = "open-source.repository.create.event",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void repositoryCreatedEvent(List<OpenSourceRepoCreateDto> repositories) {
        for(OpenSourceRepoCreateDto repository : repositories) {
            Project project = Project.make(repository);

            projectRepository.save(project);

            System.out.println(repository.getName());
        }
    }
}
