package shapes;

public class Rectangle implements Shape {
    private final int width;
    private final int height;

    public Rectangle(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        for (int row=0; row < height; row++) {
            g.drawHorizontalLine(width);
        }
    }
}
