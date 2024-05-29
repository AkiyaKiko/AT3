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
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.print("Enter your choice: ");
        difficultyLevel = sc.nextInt();

        switch (difficultyLevel) {
            case 1:
                numArrows = MAXARROWS + 1; // Provide more arrows for easy mode
                break;
            case 2:
                numArrows = MAXARROWS;
                break;
            case 3:
                numArrows = MAXARROWS - 1; // Reduce arrows for hard mode
                break;
            default:
                numArrows = MAXARROWS;
        }
    
    }

    //
    public void play() {
        bot.setTracing(false);
        setDifficultyLevel();
        explain();
        System.out.println("Would you like to play Mystery Mansion Game? ");
        String play = sc.next();
        int arrows = numArrows;
        while(play.equalsIgnoreCase("y")){
            gameOver = false;
            numArrows = arrows;
            gamesPlayed[TOTAL]++;
            bot.newGame();
            while (!gameOver) {
                playOne();
            }
            System.out.println("Would you like to play Mystery Mansion Game? ");
            play = sc.next();
        }
        System.out.println(gamesDesc[TOTAL] + gamesPlayed[TOTAL] + " games.");
        System.out.println(gamesDesc[WON] + gamesPlayed[WON] + " games.");
        System.out.println(gamesDesc[EATEN] + gamesPlayed[EATEN] + " games.");
        System.out.println(gamesDesc[FELL] + gamesPlayed[FELL] + " games.");
        System.out.println(gamesDesc[QUIT] + gamesPlayed[QUIT] + " games.");
    }

    //
    public void playOne() {
        // Print current location and available information
        System.out.print("You are in Room #" + bot.getCurrent() + "\n");
        System.out.println("To your left is #" + String.valueOf(bot.nextRoom('l')) + ", to your right is #" + String.valueOf(bot.nextRoom('r')) + ", and ahead is #" + String.valueOf(bot.nextRoom('a')));
        System.out.println("You have " + numArrows + " arrows remaining.");
    
        // Check if there's a smell of the ghost or a breeze from the pit
        boolean ghost = bot.ghostNear();
        boolean pit = bot.pitNear();
    
        if (ghost) {
            System.out.println("You can smell something horrible.");
        }
        if (pit) {
            System.out.println("You feel a cold wind.");
        }


        // Ask for user's move
        System.out.println("Please choose from (W)alk, (S)hoot, or (Q)uit: ");
        char move = sc.next().charAt(0);
        response = -1;
    
        switch (move) {
            case 'w':
                System.out.println("Which room would you like to walk into?");
                // Implement walking into another room
                int roomToWalk = sc.nextInt();
                int walkResult = bot.tryWalk(roomToWalk);
                if (walkResult == 0) {
                    gameOver = false;
                    response = 1;
                } else if (walkResult == 1) {
                    gamesPlayed[EATEN]++;
                    gameOver = true;
                    response = 2;
                } else if (walkResult == 2) {
                    gamesPlayed[FELL]++;
                    gameOver = true;
                    response = 3;
                } else {
                    gameOver = false;
                    response = 0;
                }
                break;
            case 's':
                System.out.println("Which room would you like to shoot into?");
                // Implement shooting into another room
                if (numArrows > 0) {
                    numArrows--;
                    int roomToShoot = sc.nextInt();
                    int shotResult = bot.tryShoot(roomToShoot);
                    if (shotResult == 0) {
                        gamesPlayed[WON]++;
                        gameOver = true;
                        response = 5;
                    } else if (shotResult == 3) {
                        response = 6;
                    }
                    else {
                        response = 4;
                    }
                } else {
                    response = 7;
                }
                break;
            case 'q':
                // Quit the game
                gamesPlayed[QUIT]++;
                gameOver = true;
                break;
            default:
                System.out.println("Invalid move. Please try again.");
        }
        if(response >=0 && response <= 7)
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
