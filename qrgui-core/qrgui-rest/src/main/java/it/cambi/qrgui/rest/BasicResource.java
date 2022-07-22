package it.cambi.qrgui.rest;

import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.RequiredArgsConstructor;

/**
 * @author luca Abstract Class for Basic Resource attributes
 *     <p>All class extending BasicResource implements a @Path Interface for rest easy services that
 *     is used as an @Inject
 */
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public abstract class BasicResource {

}
