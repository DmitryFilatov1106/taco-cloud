package ru.fildv.tacocloud.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.fildv.tacocloud.model.TacoOrder;
import ru.fildv.tacocloud.model.User;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(
            String deliveryZip, Date startDate, Date endDate);

    @Query("from TacoOrder o where o.deliveryCity = 'Moscow'")
    List<TacoOrder> readOrdersDeliveredInMoscow();

    List<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
