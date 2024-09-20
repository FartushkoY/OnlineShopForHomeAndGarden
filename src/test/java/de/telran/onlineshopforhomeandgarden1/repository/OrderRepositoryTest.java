package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;


    @Test
    void findOrdersRequiringStatusUpdateTest() {
        List<Order> result = orderRepository.findOrdersRequiringStatusUpdate();
        assertEquals(List.of(orderRepository.findById(1L).get(), orderRepository.findById(4L).get(),
                orderRepository.findById(5L).get(), orderRepository.findById(6L).get(),
                orderRepository.findById(8L).get(), orderRepository.findById(9L).get(),
                orderRepository.findById(10L).get(), orderRepository.findById(23L).get(),
                orderRepository.findById(24L).get(), orderRepository.findById(25L).get(),
                orderRepository.findById(26L).get(), orderRepository.findById(28L).get(),
                orderRepository.findById(30L).get(), orderRepository.findById(31L).get(),
                orderRepository.findById(32L).get(), orderRepository.findById(33L).get(),
                orderRepository.findById(34L).get()), result);
    }

    @Test
    public void getRevenueReportDetailedByDayOrHourTest() {
        LocalDate startDate = LocalDate.of(2024, 05, 05);
        Instant instantStart = startDate.atStartOfDay(ZoneId.of("CET")).toInstant();
        LocalDate endDate = startDate.plusDays(2);
        Instant instantEnd = endDate.atStartOfDay(ZoneId.of("CET")).toInstant();

//        DetailedByDay
        List<Object> result = orderRepository.getRevenueReportDetailedByDayOrHour(instantStart, instantEnd, "%Y %m %d");
        assertEquals(result.size(), 2);
        assertEquals(Arrays.deepToString((Object[]) result.get(0)), "[61.05, 2024 05 06]");
        assertEquals(Arrays.deepToString((Object[]) result.get(1)), "[55.25, 2024 05 05]");

//        DetailedByHour
        result = orderRepository.getRevenueReportDetailedByDayOrHour(instantStart, instantEnd, "%Y %m %d %H");;
        assertEquals(result.size(), 3);
        assertEquals(Arrays.deepToString((Object[]) result.get(0)), "[20.00, 2024 05 06 15]");
        assertEquals(Arrays.deepToString((Object[]) result.get(1)), "[41.05, 2024 05 06 12]");
        assertEquals(Arrays.deepToString((Object[]) result.get(2)), "[55.25, 2024 05 05 15]");
    }


    @Test
    public void getRevenueReportDetailedByWeekTest() {
        LocalDate startDate = LocalDate.of(2024, 01, 01);
        Instant instantStart = startDate.atStartOfDay(ZoneId.of("CET")).toInstant();
        LocalDate endDate = startDate.plusMonths(8);
        Instant instantEnd = endDate.atStartOfDay(ZoneId.of("CET")).toInstant();

        List<Object> result = orderRepository.getRevenueReportDetailedByWeek(instantStart, instantEnd);
        assertEquals(result.size(), 2);
        assertEquals(Arrays.deepToString((Object[]) result.get(0)), "[117.35, 22]");
        assertEquals(Arrays.deepToString((Object[]) result.get(1)), "[116.30, 18]");

    }


    @Test
    void getRevenueReportDetailedByMonthTest() {
        LocalDate startDate = LocalDate.of(2024, 01, 01);
        Instant instantStart = startDate.atStartOfDay(ZoneId.of("CET")).toInstant();
        LocalDate endDate = startDate.plusYears(1);
        Instant instantEnd = endDate.atStartOfDay(ZoneId.of("CET")).toInstant();

        List<Object> result = orderRepository.getRevenueReportDetailedByMonth(instantStart, instantEnd);
        assertEquals(result.size(), 3);
        assertEquals(Arrays.deepToString((Object[]) result.get(0)), "[4228.05, 202409]");
        assertEquals(Arrays.deepToString((Object[]) result.get(1)), "[117.35, 202406]");
        assertEquals(Arrays.deepToString((Object[]) result.get(2)), "[116.30, 202405]");
    }


    @Test
    void getRevenueReportDetailedByYearTest() {
        LocalDate startDate = LocalDate.of(2023, 01, 01);
        Instant instantStart = startDate.atStartOfDay(ZoneId.of("CET")).toInstant();
        LocalDate endDate = startDate.plusYears(2);
        Instant instantEnd = endDate.atStartOfDay(ZoneId.of("CET")).toInstant();

        List<Object> result = orderRepository.getRevenueReportDetailedByYear(instantStart, instantEnd);
        assertEquals(result.size(), 2);
        assertEquals(Arrays.deepToString((Object[]) result.get(0)), "[4461.70, 2024]");
        assertEquals(Arrays.deepToString((Object[]) result.get(1)), "[41.05, 2023]");
    }


    @Test
    void getTotalResultTest() {
        LocalDate startDate = LocalDate.of(2023, 01, 01);
        Instant instantStart = startDate.atStartOfDay(ZoneId.of("CET")).toInstant();
        LocalDate endDate = startDate.plusYears(2);
        Instant instantEnd = endDate.atStartOfDay(ZoneId.of("CET")).toInstant();

        BigDecimal result = orderRepository.getTotalResult(instantStart, instantEnd);
        assertEquals(result, BigDecimal.valueOf(4502.75));
    }
}