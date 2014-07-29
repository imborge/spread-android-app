package no.boraj.spread.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by borgizzle on 05.07.2014.
 */
public class Queue {
    @JsonProperty("queue_items")
    private List<QueueItem> items;

    public List<QueueItem> getItems() {
        return items;
    }

    public void setItems(List<QueueItem> items) {
        this.items = items;
    }
}
