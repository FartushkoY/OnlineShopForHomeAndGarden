package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrdersByUser(User user);

    Optional<Order> findOrderByUserAndId(User user, Long id);

    @Query(("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'PAID', 'ON_THE_WAY')"))
    List<Order> findOrdersRequiringStatusUpdate();

    @Query(("select sum(oi.quantity * oi.priceAtPurchase) as totalInTime, date_format(o.updatedAt, ?3) as updatedTime from OrderItem oi join oi.order o where o.updatedAt between ?1 and ?2 and o.status NOT IN ('PENDING', 'CANCELED') group by updatedTime order by updatedTime desc"))
    List<Object> getRevenueReportDetailedByDayOrHour(Instant instantStart, Instant instantEnd, String detailingPeriod);

    @Query(nativeQuery = true, value = "select sum(order_items.quantity * order_items.price_at_purchase) as total_in_time, extract(WEEK FROM orders.updated_at) as updated_time from order_items join orders on order_items.order_id = orders.order_id where orders.status not in ('PENDING', 'CANCELED') and orders.updated_at between ?1 and ?2 group by updated_time order by updated_time desc")
    List<Object> getRevenueReportDetailedByWeek(Instant instantStart, Instant instantEnd);

    @Query(nativeQuery = true, value = "select sum(order_items.quantity * order_items.price_at_purchase) as total_in_time, extract(YEAR_MONTH FROM orders.updated_at) as updated_time from order_items join orders on order_items.order_id = orders.order_id where orders.status not in ('PENDING', 'CANCELED') and orders.updated_at between ?1 and ?2 group by updated_time order by updated_time desc")
    List<Object> getRevenueReportDetailedByMonth(Instant instantStart, Instant instantEnd);

    @Query(nativeQuery = true, value = "select sum(order_items.quantity * order_items.price_at_purchase) as total_in_time, extract(YEAR FROM orders.updated_at) as updated_time from order_items join orders on order_items.order_id = orders.order_id where orders.status not in ('PENDING', 'CANCELED') and orders.updated_at between ?1 and ?2 group by updated_time order by updated_time desc")
    List<Object> getRevenueReportDetailedByYear(Instant instantStart, Instant instantEnd);

    @Query("select sum(oi.quantity * oi.priceAtPurchase) as total from OrderItem oi join oi.order o where o.updatedAt between ?1 and ?2 and o.status NOT IN ('PENDING', 'CANCELED')")
    BigDecimal getTotalResult(Instant instantStart, Instant instantEnd);
}
