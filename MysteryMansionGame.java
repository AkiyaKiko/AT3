/**
 * KXO151 Assignment 3 (2024) Sample Solution
 *
 * Mystery Mansion
 *
 * MysteryMansionGame.java
 *
 *
 */
 
 
import java.util.Scanner;

public class MysteryMansionGame {
    private final int MAXARROWS = 3;
    public int MAX_ARROWS;
    private final int TOTAL = 0;
    private final int WON = 1;
    private final int EATEN = 2;
    private final int FELL = 3;
    private final int QUIT = 4;
    private final String[] RESPONSES = {"Ow, that hurt.  You just walked into a wall...",
            "Walk successful.",
            "You're dead.  The Ghost gotcha!",
            "You're dead.  You just fell in the pit.",
            "Arrow missed.  I guess the ghost wasn't in there...",
            "It's a direct hit.  You've killed the Ghost!",
            "What a waste.  The arrow just bounced off the wall...",
            "You can't shoot -- you have no arrows left!"};

    private boolean tracing;
    private MysteryMansionBot bot;
    private Scanner sc;
    private boolean gameOver;
    private int numArrows;
    private int response;
    private int[] gamesPlayed = {0, 0, 0, 0, 0};
    private String[] gamesDesc = {"You played a total of ",
            "You won ",
            "You lost (by being eaten) ",
            "You lost (by falling in the pit) ",
            "You lost (due to quitting) "};
    private int difficultyLevel;

    public MysteryMansionGame() {
        bot = new MysteryMansionBot();
        sc = new Scanner(System.in);

        
        play();
    }

    // Set difficulty level
    public void setDifficultyLevel() {
        System.out.println("Select difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        difficultyLevel = sc.nextInt();

        switch (difficultyLevel) {
            case 1:
                numArrows = MAX_ARROWS + 1; // Provide more arrows for easy mode
                break;
            case 2:
                numArrows = MAX_ARROWS;
                break;
            case 3:
                numArrows = MAX_ARROWS - 1; // Reduce arrows for hard mode
                break;
            default:
                numArrows = MAX_ARROWS;
        }
    
    }

    public void play() {
        setDifficultyLevel();
        explain();

        while (!gameOver) {
            playOne();
        }
    }

    public void playOne() {
        // Print current location and available information
        System.out.println("Current Room: " + bot.getCurrent());
        System.out.println("Connecting Rooms: " + bot.pitNear());
        System.out.println("Arrows: " + numArrows);
    
        // Check if there's a smell of the ghost or a breeze from the pit
        boolean ghost = bot.ghostNear();

    
        if (ghost) {
            System.out.println("You smell a horrible smell. The ghost might be nearby.");
        }

    
        // Ask for user's move
        System.out.println("What is your move?");
        System.out.println("1. Walk into another room");
        System.out.println("2. Shoot into another room");
        System.out.println("3. Quit the game");
        int move = sc.nextInt();
    
        switch (move) {
            case 1:
                // Implement walking into another room
                int roomToWalk = sc.nextInt();
                int walkResult = bot.tryWalk(roomToWalk);
                if (walkResult == 1) {
                    gameOver = true;
                    response = EATEN;
                } else if (walkResult == 2) {
                    gameOver = true;
                    response = FELL;
                }
                break;
            case 2:
                // Implement shooting into another room
                if (numArrows > 0) {
                    int roomToShoot = sc.nextInt();
                    int shotResult = bot.tryShoot(roomToShoot);
                    if (shotResult == 1) {
                        gameOver = true;
                        response = WON;
                    } else {
                        numArrows--;
                        System.out.println("Arrow missed. I guess the ghost wasn't in there...");
                    }
                } else {
                    System.out.println("You can't shoot -- you have no arrows left!");
                }
                break;
            case 3:
                // Quit the game
                gameOver = true;
                response = QUIT;
                break;
            default:
                System.out.println("Invalid move. Please try again.");
        }
        gamesPlayed[TOTAL]++;
        gamesPlayed[response]++;
        System.out.println(RESPONSES[response]);
    }

    public void explain() {
        trace("explain the game");
        System.out.println("Mystery Mansion Game!");
        System.out.println("================");
        System.out.println();
        System.out.println("You have to find and shoot the ghost and not fall in the pit.");
        System.out.println();
    }
    
    public void trace(String message) {
        if (tracing) {
            System.out.println("Mystery Mansion Game: " + message);
        }
    }



    public static void main(String[] args) {
        new MysteryMansionGame();
    }
}
