package com.mulittle.skeleton.backend.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mulittle.skeleton.backend.model.AbstractAttachment;

import lombok.Getter;

@Getter
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class EvidenceContext {
  List<AbstractAttachment> attachments = new LinkedList<>();
}
