package org.mrshoffen.tasktracker.desk.internal;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.desk.service.DeskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/desks")
public class InternalDeskController {

    private final DeskService deskService;

//    @GetMapping("/id")
//    Mono<UUID> deskId(@RequestParam("taskName") String taskName,
//                      @RequestParam("workspace")) {}
}
