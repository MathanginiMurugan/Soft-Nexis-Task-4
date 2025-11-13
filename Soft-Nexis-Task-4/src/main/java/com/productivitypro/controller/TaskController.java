package com.productivitypro.controller;

import com.productivitypro.assembler.TaskModelAssembler;

import com.productivitypro.exception.TaskNotFoundException;
import com.productivitypro.model.Task;
import com.productivitypro.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repository;
    private final TaskModelAssembler assembler;

    public TaskController(TaskRepository repository, TaskModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // GET /api/tasks
    @GetMapping
    public CollectionModel<EntityModel<Task>> getAllTasks() {
        List<EntityModel<Task>> tasks = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(tasks,
                linkTo(methodOn(TaskController.class).getAllTasks()).withSelfRel());
    }

    // GET /api/tasks/{id}
    @GetMapping("/{id}")
    public EntityModel<Task> getTaskById(@PathVariable Long id) {
        Task t = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return assembler.toModel(t);
    }

    // POST /api/tasks
    @PostMapping
    public ResponseEntity<EntityModel<Task>> createTask(@Valid @RequestBody Task newTask) {
        Task saved = repository.save(newTask);
        EntityModel<Task> model = assembler.toModel(saved);
        URI location = linkTo(methodOn(TaskController.class).getTaskById(saved.getId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    // PUT /api/tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Task>> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskRequest) {
        Task updated = repository.findById(id)
                .map(task -> {
                    task.setTitle(taskRequest.getTitle());
                    task.setDescription(taskRequest.getDescription());
                    task.setDueDate(taskRequest.getDueDate());
                    task.setCompleted(taskRequest.isCompleted());
                    return repository.save(task);
                })
                .orElseThrow(() -> new TaskNotFoundException(id));

        EntityModel<Task> model = assembler.toModel(updated);
        return ResponseEntity.ok(model);
    }

    // DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task t = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        repository.delete(t);
        return ResponseEntity.noContent().build();
    }
}

