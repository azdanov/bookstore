package dev.azdanov.notificationservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {

    boolean existsByEventId(String eventId);
}
