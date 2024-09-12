package de.telran.onlineshopforhomeandgarden1.scheduler;


import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DeliveryStatusScheduler {

    private final OrderRepository orderRepository;

    @Autowired
    public DeliveryStatusScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void scheduledDeliveryStatus() {
        List<Order> orders = orderRepository.findOrdersRequiringStatusUpdate();
        for (Order order : orders) {
            Status status = order.getStatus();
            switch (order.getStatus()) {
                case PENDING:
                    order.setStatus(Status.PAID);
                    break;
                case PAID:
                    order.setStatus(Status.ON_THE_WAY);
                    break;
                case ON_THE_WAY:
                    order.setStatus(Status.DELIVERED);
                    break;
                default:
                    break;
            }
            log.info("Order {} changed status from {} to {}", order.getId(), status, order.getStatus());
        }
            if(!orders.isEmpty()) {
                orderRepository.saveAll(orders);
        }

     }
   }

