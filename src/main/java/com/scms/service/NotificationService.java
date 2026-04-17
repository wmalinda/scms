package com.scms.service;

import com.scms.domain.AppNotification;
import com.scms.pattern.observer.NotificationObserver;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Behavioural: Observer (Subject) — registers observers per user and delivers in-app notifications.
 */
public class NotificationService {

    private final CampusRepository repository;
    private final Map<String, List<NotificationObserver>> observersByUser = new ConcurrentHashMap<>();

    public NotificationService(CampusRepository repository) {
        this.repository = repository;
    }

    public void subscribe(String userId, NotificationObserver observer) {
        observersByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(observer);
    }

    public void unsubscribe(String userId, NotificationObserver observer) {
        List<NotificationObserver> list = observersByUser.get(userId);
        if (list != null) {
            list.remove(observer);
        }
    }

    public void publish(String userId, String message) {
        AppNotification n = new AppNotification(AppNotification.newId(), userId, message, Instant.now(), false);
        repository.appendNotification(n);
        List<NotificationObserver> list = observersByUser.get(userId);
        if (list != null) {
            for (NotificationObserver o : List.copyOf(list)) {
                o.onNotification(n);
            }
        }
    }

    public void publishToUsers(Iterable<String> userIds, String message) {
        for (String uid : userIds) {
            publish(uid, message);
        }
    }
}
