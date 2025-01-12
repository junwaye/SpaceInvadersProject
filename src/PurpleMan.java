import java.awt.*;

public class PurpleMan {

    private int x, y, size;

    public PurpleMan(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics2D g2){
        g2.fillRect(x, y, size, size);
    }

    public void move(int vx){
        x += vx;

    }

    public void shiftDown(){
        y += size;

    }

    public Rectangle getHitBox(){
        return new Rectangle(x,y,size,size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

}
