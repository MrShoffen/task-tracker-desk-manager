package org.mrshoffen.tasktracker.desk.model.dto.links;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.utils.link.Link;
import org.mrshoffen.tasktracker.commons.utils.link.Links;
import org.mrshoffen.tasktracker.commons.utils.link.LinksInjector;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;

@RequiredArgsConstructor
public class DeskDtoLinksInjector extends LinksInjector<DeskResponseDto> {

    private final String apiPrefix;

    @Override
    protected Links generateLinks(DeskResponseDto dto) {
        return Links.builder()
                .addLink(Link.forName("allTasks")
                        .andHref(apiPrefix + "/workspaces/%s/desks/%s/tasks"
                                .formatted(dto.getWorkspaceId(), dto.getId()))
                        .andMethod("GET")
                        .build()
                )
                .addLink(Link.forName("createTask")
                        .andHref(apiPrefix + "/workspaces/%s/desks/%s/tasks"
                                .formatted(dto.getWorkspaceId(), dto.getId()))
                        .andMethod("POST")
                        .build()
                )
                .addLink(Link.forName("allDesks")
                        .andHref(apiPrefix + "/workspaces/%s/desks"
                                .formatted(dto.getWorkspaceId()))
                        .andMethod("GET")
                        .build()
                )
                .addLink(Link.forName("createDesk")
                        .andHref(apiPrefix + "/workspaces/%s/desks"
                                .formatted(dto.getWorkspaceId()))
                        .andMethod("POST")
                        .build()
                )
                .addLink(Link.forName("self")
                        .andHref(apiPrefix + "/workspaces/%s/desks/%s"
                                .formatted(dto.getWorkspaceId(), dto.getId()))
                        .andMethod("GET")
                        .build()
                )
                .addLink(Link.forName("allWorkspaces")
                        .andHref(apiPrefix + "/workspaces")
                        .andMethod("GET")
                        .build()
                )
                .addLink(Link.forName("createWorkspace")
                        .andHref(apiPrefix + "/workspaces")
                        .andMethod("POST")
                        .build()
                )
                .build();
    }
}
