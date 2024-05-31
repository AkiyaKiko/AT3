/**
 * KXO151 Assignment 3, 2024
 *
 * Mystery Mansion
 *
 * MysteryMansionGame.java
 * 
 * Contributors: Chengwei Yan 
 *               Student ID: 694659
 *               Email: cyan4@utas.edu.au
 *               Wentao Su 
 *               Student ID: 694641
 *               Email: wentaos@utas.edu.au
 * 
 * Statement of Purpose: This program is designed to play the Mystery Mansion Game.
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
        // Ask user to choose difficulty level
        System.out.println("\nChoose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard\n");
        System.out.print("Enter your choice: ");
        difficultyLevel = sc.nextInt();
        System.out.println();

        // Set the number of arrows based on the difficulty level
        switch (difficultyLevel) {
            case 1:
                MAX_ARROWS = MAXARROWS + 1; // Provide more arrows for easy mode
                break;
            case 2:
                MAX_ARROWS = MAXARROWS;
                break;
            case 3:
                MAX_ARROWS = MAXARROWS - 1; // Reduce arrows for hard mode
                break;
            default:
                MAX_ARROWS = MAXARROWS; // Default to medium mode
        }
    
    }

    //
    public void play() {
        // Turn off tracing
        tracing = false;
        bot.setTracing(false); 

        setDifficultyLevel(); // Set difficulty level

        explain(); // Explain the game

        numArrows = MAX_ARROWS; // Store the number of arrows in current play

        // Ask user if they want to play the game
        System.out.print("Would you like to play Mystery Mansion Game? ");
        String play = sc.next(); // Get user's response
        while(play.equalsIgnoreCase("y") || play.equalsIgnoreCase("yes")){
            System.out.println();

            // Start a new game
            gameOver = false;
            numArrows = MAX_ARROWS;
            gamesPlayed[TOTAL]++;
            bot.newGame();
            while (!gameOver) {
                playOne();
            }
            System.out.print("Would you like to play Mystery Mansion Game? ");
            play = sc.next();
        }

        // Print the number of games played
        System.out.println();
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
        if (bot.ghostNear()) {
            System.out.println("\nYou can smell something horrible.");
        }
        if (bot.pitNear()) {
            System.out.println("\nYou feel a cold wind.");
        }

        response = -1; // Reset response

        // Ask for user's move
        System.out.print("\nPlease choose from (W)alk, (S)hoot, or (Q)uit: ");
        
        // Implement user's move
        switch (sc.next().toLowerCase()) {
            case "w":
                System.out.print("Which room would you like to walk into? ");
                // Implement walking into another room
                switch (bot.tryWalk(sc.nextInt())) {
                    case 0:
                        gameOver = false;
                        response = 1;
                        break;
                    case 1:
                        gamesPlayed[EATEN]++;
                        gameOver = true;
                        response = 2;
                        break;
                    case 2:
                        gamesPlayed[FELL]++;
                        gameOver = true;
                        response = 3;
                        break;
                    case -1:
                        gameOver = false;
                        response = 0;
                        break;
                }
                break;
            case "s":
                // Implement shooting into another room
                if (numArrows > 0) {
                    System.out.print("Which room would you like to shoot into? ");
                    numArrows--;
                    switch (bot.tryShoot(sc.nextInt())) {
                        case -1: // Shoot into a Wall
                            response = 6;
                            break;
                        case 0: // Shoot into the ghost
                            gamesPlayed[WON]++;
                            gameOver = true;
                            response = 5;        
                            break;
                        case 3: // Shoot missing
                            gameOver = false;
                            response = 4;
                            break;
                    }
                }
                else {
                    // No arrows
                    response = 7;
                }
                break;
            case "q":
                // Quit the game
                gamesPlayed[QUIT]++;
                gameOver = true;
                break;
            default:
                System.out.println("Invalid action. Please try again.");
        }
        // Print the result of the move
        if(response >=0 && response <= 7)
            System.out.println(RESPONSES[response]);
            System.out.println();
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
