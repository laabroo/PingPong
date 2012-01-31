/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello.ping;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;

/**
 * @author laabroo
 */
public class MainPing extends GameCanvas implements Runnable {

    private MIDlet mainMIDlet;
    private Image ballImage;
    private Image padImage;
    private int TIME_SLEEP = 30;
    private Sprite ballSprite;
    private Sprite padSprite;
    private int ballX = getWidth() / 2;
    private int ballY = getHeight() / 2;
    private int padX = 10;
    private int padY = getHeight() / 2;
    private int speedBallX = 3;
    private int speedBallY = 1;
    private static final int speedPadY = 2;
    private int ballDirection = 1;

    public MainPing(MIDlet mIDlet) {
        super(false);
        mainMIDlet = mIDlet;
        this.setFullScreenMode(true);
    }

    public void startApp() {

        try {
            ballImage = Image.createImage("/hello/resource/ball.png");
            padImage = Image.createImage("/hello/resource/pad.png");

        } catch (IOException e) {
            e.getMessage();
        }

        ballSprite = new Sprite(ballImage, 3, 3);
        ballSprite.defineReferencePixel(2, 2);
        ballSprite.setRefPixelPosition(ballX, ballY);

        padSprite = new Sprite(padImage, 3, 20);
        padSprite.defineReferencePixel(1, 10);
        padSprite.setRefPixelPosition(padX, padY);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void run() {
        while (true) {
            updateScreen(getGraphics());
            try {
                Thread.sleep(TIME_SLEEP);
            } catch (Exception e) {
                e.getMessage();
            }

        }
    }

    private void createBackground(Graphics graphics) {
        graphics.setColor(0x000000);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    private void updateScreen(Graphics graphics) {
        createBackground(graphics);
        moveBall();
        ballSprite.setRefPixelPosition(ballX, ballY);
        ballSprite.paint(graphics);
        padSprite.setRefPixelPosition(padX, padY);
        padSprite.paint(graphics);
        flushGraphics();
    }

    private void movePaid() {
        int stateKey = getKeyStates();
        if ((stateKey & UP_PRESSED) != 0 && padY > padSprite.getHeight() / 2) {
            padY -= speedPadY;
        } else if ((stateKey & DOWN_PRESSED) != 0 && padY < getHeight() - padSprite.getHeight() / 2) {
            padY += speedPadY;
        } else if ((stateKey & KEY_NUM2) != 0 && padY > padSprite.getHeight() / 2) {
            padY -= speedPadY;
        } else if ((stateKey & KEY_NUM8) != 0 && padY < getHeight() - padSprite.getHeight() / 2) {
            padY += speedPadY;
        }
    }

    private void moveBall() {
        if (ballDirection == 0) {
            ballX -= speedBallX;
            ballY -= speedBallY;
        } else if (ballDirection == 1) {
            ballX += speedBallX;
            ballY -= speedBallY;
        } else if (ballDirection == 2) {
            ballX += speedBallX;
            ballY += speedBallY;
        } else if (ballDirection == 3) {
            ballX -= speedBallX;
            ballY += speedBallY;
        }

        if (ballDirection == 0 && ballSprite.collidesWith(padSprite, false)) {
            ballDirection = 1;
        } else if (ballDirection == 3 && ballSprite.collidesWith(padSprite, false)) {
            ballDirection = 2;
        }

        if (ballDirection == 0 && ballX < 0) {
            ballDirection = 1;
        } else if (ballDirection == 0 && ballY < 0) {
            ballDirection = 3;
        } else if (ballDirection == 1 && ballY < 0) {
            ballDirection = 2;
        } else if (ballDirection == 1 && ballX > getWidth()) {
            ballDirection = 0;
            if (TIME_SLEEP > 5) {
                TIME_SLEEP--;
            }
        } else if (ballDirection == 2 && ballY > getHeight()) {
            ballDirection = 1;
        } else if (ballDirection == 2 && ballX > getWidth()) {
            ballDirection = 3;
            if (TIME_SLEEP > 5) {
                TIME_SLEEP--;
            }
        } else if (ballDirection == 3 && ballY > getHeight()) {
            ballDirection = 0;
        } else if (ballDirection == 3 && ballX < 0) {
            ballDirection = 2;
        }
    }
}
