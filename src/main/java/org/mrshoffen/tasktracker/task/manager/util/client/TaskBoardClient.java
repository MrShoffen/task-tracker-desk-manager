package org.mrshoffen.tasktracker.task.manager.util.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "board-manager-ws")
public interface TaskBoardClient {

    @GetMapping("/internal/boards/id")
    String boardId(@RequestParam("boardName") String boardName, @RequestParam("userId") UUID userId);

}
