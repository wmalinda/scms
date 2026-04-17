package com.scms.pattern.observer;

import com.scms.domain.AppNotification;

/**
 * Behavioural: Observer — receives {@link AppNotification} instances when campus events occur.
 */
@FunctionalInterface
public interface NotificationObserver {

    void onNotification(AppNotification notification);
}
