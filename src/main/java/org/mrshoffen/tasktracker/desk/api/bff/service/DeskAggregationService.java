package org.mrshoffen.tasktracker.desk.api.bff.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.mapper.WorkspaceMapper;
import org.mrshoffen.tasktracker.desk.repository.DeskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeskAggregationService {

    private final WorkspaceMapper workspaceMapper;

    private final DeskRepository deskRepository;

    public Flux<DeskResponseDto> getAllDesksInWorkspace(UUID workspaceId) {
        return deskRepository
                .findAllByWorkspaceId(workspaceId)
                .map(workspaceMapper::toDeskResponse);
    }
}
