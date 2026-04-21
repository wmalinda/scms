package com.scms.service;

import com.scms.domain.AppNotification;
import com.scms.domain.StaffMember;
import com.scms.domain.User;
import com.scms.exception.DuplicateDataException;
import com.scms.pattern.observer.NotificationObserver;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Test
    void publish_deliversToObserverAndLogs() throws DuplicateDataException {
        CampusRepository repo = CampusRepository.newEmptyForTest();
        NotificationService ns = new NotificationService(repo);
        User u = new StaffMember(User.newId(), "obs", "Observer");
        repo.addUser(u);
        AtomicReference<String> received = new AtomicReference<>();
        ns.subscribe(u.getId(), (NotificationObserver) n -> received.set(n.getMessage()));
        ns.publish(u.getId(), "Hello");
        assertEquals("Hello", received.get());
        assertTrue(repo.notificationsForUser(u.getId()).stream().anyMatch(x -> "Hello".equals(x.getMessage())));
    }
}
