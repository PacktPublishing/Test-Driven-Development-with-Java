package shapes;

import java.util.ArrayList;
import java.util.List;

public class Shapes {
    private final List<Shape> all = new ArrayList<>();
    private final Graphics graphics;

    public Shapes(Graphics graphics) {
        this.graphics = graphics;
    }

    public void add(Shape s) {
        all.add(s);
    }

    public void draw() {
        all.forEach(shape->shape.draw(graphics));
    }
}
