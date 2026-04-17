package com.scms.service;

import com.scms.domain.User;
import com.scms.exception.UnauthorizedActionException;

/**
 * Structural: Facade — single entry point for the GUI and tests, coordinating repository and services.
 */
public class CampusFacade {

    private final CampusRepository repository;
    private final NotificationService notificationService;

    public CampusFacade() {
        this(CampusRepository.getInstance(), new NotificationService(CampusRepository.getInstance()));
    }

    public CampusFacade(CampusRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public CampusRepository getRepository() {
        return repository;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public User authenticate(String username) throws UnauthorizedActionException {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UnauthorizedActionException("Unknown user: " + username));
    }

}
