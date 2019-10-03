package com.cy.transferapi.bdd.stepdefs.util;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class BaseLoader {

    public String loadFile(String path) throws IOException {
        return new String(Files.readAllBytes(ResourceUtils.getFile("classpath:" + path).toPath()));
    }
}
