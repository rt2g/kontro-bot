package info.kontro.mongo;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

@Embedded
public class LineOrder {
    @Reference(lazy = false)
    private Line line;
    private String lineId;
    private Integer order;

    LineOrder() {
    }

    LineOrder(Integer order, Line line) {
        this.order = order;
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    public void updateLine() {
        lineId = line.getName();
    }
}
