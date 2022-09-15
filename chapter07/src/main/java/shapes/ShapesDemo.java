package shapes;

public class ShapesDemo {
    public static void main(String[] args) {
        new ShapesDemo().run();
    }

    private void run() {
        // LSP: Implement the Graphics interface making it LSP compatible
        Graphics console = new ConsoleGraphics();

        // DIP: Inject Shapes dependency on Graphics
        var shapes = new Shapes(console);

        // OCP Shapes class can have any kind of Shape added to it
        // SRP each shape subclass eg Rectangle knows how to draw only one shape
        // LSP each shape subclass can be used wherever a Shape interface is needed
        shapes.add(new TextBox("Hello from the Shapes SOLID Demo"));
        shapes.add(new Rectangle(32,1));
        shapes.add(new TextBox("Using the SOLID principles to"));
        shapes.add(new TextBox("create an extensible mini-framework."));
        shapes.add(new TextBox("Draw shapes as ASCII art."));
        shapes.add(new TextBox("Following is a 5 x 3 Rectangle:"));
        shapes.add(new Rectangle(5,3));

        // Tell Don't Ask principle: shapes, you know what to do: draw yourselves
        shapes.draw();
    }
}
