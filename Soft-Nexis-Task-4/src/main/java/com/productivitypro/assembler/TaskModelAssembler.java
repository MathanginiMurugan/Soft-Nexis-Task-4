package com.productivitypro.assembler;

import com.productivitypro.controller.TaskController;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {
    @Override
    public EntityModel<Task> toModel(Task task) {
        EntityModel<Task> model = EntityModel.of(task,
                linkTo(methodOn(TaskController.class).getTaskById(task.getId())).withSelfRel(),
                linkTo(methodOn(TaskController.class).getAllTasks()).withRel("tasks"));

        return model;
    }
}

