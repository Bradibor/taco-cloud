package xyz.bradibarus.tacocloud.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacoInserter;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.orderInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order_Tacos");
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        order.getTacos().forEach(taco -> saveOrderToTaco(taco, orderId));
        return order;
    }

    private long saveOrderDetails(Order order) {
        Map<String, Object> values = new HashMap<>();
        values.put("deliveryName", order.getName());
        values.put("deliveryStreet", order.getStreet());
        values.put("deliveryCity", order.getCity());
        values.put("deliveryState", order.getState());
        values.put("deliveryZip", order.getZip());
        values.put("ccNumber", order.getCcNumber());
        values.put("ccExpiration", order.getCcExpiration());
        values.put("cccvv", order.getCcCVV());
        values.put("placedAt", order.getPlacedAt());
        return orderInserter.executeAndReturnKey(values).longValue();
    }

    private void saveOrderToTaco(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }
}
