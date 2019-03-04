package io.costax.revisiting.minimizemutability;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

// @Entity
public class Order {

    private final Date date;
    private List<OrderItem> items = new ArrayList<>();

    private Order(final Date date, List<OrderItem> items) {
        this.date = date;
        this.items = items;
    }

    public static Order of(final Date date, List<OrderItem> items) {
        Objects.requireNonNull(date);
        return new Order(
                new Date(date.getTime()),
                new ArrayList<>(items));
    }

    public final Date getDate() {
        // the java.util.Date class is not thread-safe,
        // so in this case we should return a defencive copy
        return new Date(date.getTime());
    }

    public List<OrderItem> getItems() {
        //Collections.unmodifiableList(this.items);
        return List.copyOf(this.items);
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

}
