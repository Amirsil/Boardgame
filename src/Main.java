import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int sizeOfBoard = 30;
        Scanner stdin = new Scanner(System.in);
        char[][] board = new char[sizeOfBoard][sizeOfBoard];
        int x1, y1; // PlayerA location
        int x2, y2; // PlayerB location
        int xRugTopLeftCorner, yRugTopLeftCorner; // Location of the rug's top left corner
        int rugLength; // Size of the rug
        Player playingPlayer, overallWinner;
        Player player1 = new Player(board,'1');
        Player player2 = new Player(board,'2');
        int direction;
        boolean isRoundOver;
        boolean isPlayerInsideRug;
        boolean isGameOver = false;
        while (!isGameOver) {
            // Get all the variables necessary to start the game as input
            System.out.println("A new game is starting!");
            x1 = safeInput(stdin, "Player1 x location: ", sizeOfBoard);
            y1 = safeInput(stdin, "Player1 y location:", sizeOfBoard);
            x2 = safeInput(stdin, "Player2 x location:", sizeOfBoard);
            y2 = safeInput(stdin, "Player2 y location:", sizeOfBoard);
            xRugTopLeftCorner = safeInput(stdin, "Rug top left corner x location:", sizeOfBoard);
            yRugTopLeftCorner = safeInput(stdin, "Rug top left corner y location:", sizeOfBoard);
            rugLength = safeRugInput(stdin, "Rug size: ", sizeOfBoard, xRugTopLeftCorner, yRugTopLeftCorner);

            player1.setLocation(x1, y1);
            player2.setLocation(x2, y2);
            try {
                initializeBoard(board, xRugTopLeftCorner, yRugTopLeftCorner, rugLength, player1, player2);
            } catch (Exception ignored) {
                System.out.println("Players can't start inside of the rug! restarting game...");
                continue;
            }
            printWhoWinsInLessSteps(board, player1, player2, sizeOfBoard);
            playingPlayer = player1;
            System.out.println("Player 1 is first!");
            Thread.sleep(1000);

            while (true) {
                showBoard(board);
                try {
                    direction = Integer.parseInt(stdin.nextLine());
                    if (direction < 0 || direction > 4) {
                        // Illegal direction
                        throw new Exception();
                    }
                } catch (Exception ignore) {
                    System.out.println("Illegal move, Your turn is over");
                    // Switch turns
                    if (playingPlayer == player1) {
                        playingPlayer = player2;
                    } else {
                        playingPlayer = player1;
                    }
                    continue;
                }
                isRoundOver = playingPlayer.walk(direction);
                if (isRoundOver) {
                    break;
                }
                // Switch turns
                if (playingPlayer == player1) {
                    playingPlayer = player2;
                } else {
                    playingPlayer = player1;
                }
            }
            // Announce the winner
            System.out.format("Player %c has won!\n", playingPlayer.getPlayerNum());

            if (!player1.wantsToPlayAgain(stdin) || !player2.wantsToPlayAgain(stdin)) {
                isGameOver = true;
                overallWinner = getOverallWinner(player1, player2);
                // Announce the overall winner and print scoreboard
                System.out.format("Player%c wins: %d\nPlayer%c wins: %d\n\nPlayer %c is the winner of the game!\n",
                        player1.getPlayerNum(), player1.getNumberOfWins(),
                        player2.getPlayerNum(), player2.getNumberOfWins(),overallWinner.getPlayerNum());
            }
        }




    }

    /**
     *
     * @param player1 Player 1
     * @param player2 Player 2
     * @return A reference to the winning player instance
     */
    private static Player getOverallWinner(Player player1, Player player2) {
        if (player1.getNumberOfWins() > player2.getNumberOfWins()){
            return player1;
        }
        else if (player1.getNumberOfWins() < player2.getNumberOfWins()) {
            return player2;
        }
        else{
            if (player1.getTotalSteps() < player2.getTotalSteps()) {
                return player1;
            }
            else if (player1.getTotalSteps() > player2.getTotalSteps()) {
                return player2;
            }
            else{
                if (new Random().nextBoolean()) {
                    return player1;
                }
                else{
                    return player2;
                }

            }
        }
    }

    /** Calculates the closest flag pixel to each player and announces the winner
     *  (the player with the shorter minimal distance)
     *
     * @param board The board is a two dimensional array representing a playing board
     * @param player1 Instance of player 1
     * @param player2 Instance of player 2
     * @param sizeOfBoard The size of the board
     */
    static void printWhoWinsInLessSteps(char[][] board, Player player1, Player player2, int sizeOfBoard) {
        double[] distancesFromPlayer1 = new double[sizeOfBoard*sizeOfBoard];
        double[] distancesFromPlayer2 = new double[sizeOfBoard*sizeOfBoard];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                distancesFromPlayer1[x+y*sizeOfBoard] = (board[x][y] == '*') ?
                        Math.abs(x - player1.getX()) + Math.abs(y - player1.getY()) : 1000;

                distancesFromPlayer2[x+y*sizeOfBoard] = (board[x][y] == '*') ?
                        Math.abs(x - player2.getX()) + Math.abs(y - player2.getY()) : 1000;
            }
        }
        Arrays.sort(distancesFromPlayer1);
        Arrays.sort(distancesFromPlayer2);

        if (distancesFromPlayer1[0] < distancesFromPlayer2[0]){
            System.out.format("Player %c can win in less steps!\n", player1.getPlayerNum());
        }
        else if (distancesFromPlayer1[0] > distancesFromPlayer2[0]){
            System.out.format("Player %c can win in less steps!\n", player2.getPlayerNum());
        }
        else{
            System.out.println("Both players can win in the same amount of steps");
        }
    }

    /**
     * 
     * @param stdin Scanner instance
     * @param message Output for guiding the user what he should type as input
     * @param boardSize The size of the board
     * @param xRugTopLeftCorner X coordinate of the The top left corner of the rug
     * @param yRugTopLeftCorner Y coordinate of the The top left corner of the rug
     * @return Rug size typed by the user (Special case safeInput for the rug size)
     */
    static int safeRugInput(Scanner stdin, String message, int boardSize, int xRugTopLeftCorner, int yRugTopLeftCorner) {
        int result;
        while (true) {
            try {
                System.out.println(message);
                result = Integer.parseInt(stdin.nextLine());
                // Validations
                if (xRugTopLeftCorner + result > boardSize || yRugTopLeftCorner + result > boardSize || result < 0){
                    throw new Exception();
                }
                return result;
            } catch (Exception ignored) {
                System.out.print("Invalid input, please enter ");
            }
        }
    }

    /**
     *
     * @param stdin Scanner instance
     * @param message Output for guiding the user what he should type as input
     * @param boardSize The size of the board
     * @return The X or Y coordinate typed by the user (Location on the board)
     */
    static int safeInput(Scanner stdin, String message, int boardSize) {
        int result;
        while (true) {
            try {
                System.out.println(message);
                result = Integer.parseInt(stdin.nextLine());
                // Validations
                if (result > boardSize || result < 0){
                    throw new Exception();
                }
                return result;
            } catch (Exception ignored) {
                System.out.print("Invalid input, please enter ");
            }
        }
    }

    /** The function initializes the board at the start of the game
     *
     * @param board The board is a two dimensional array representing a playing board
     * @param xRugTopLeftCorner X coordinate of the The top left corner of the rug
     * @param yRugTopLeftCorner Y coordinate of the The top left corner of the rug
     * @param rugLength The size of the rug
     * @param player1 Instance of player 1
     * @param player2 Instance of player 2
     *
     */
    static void initializeBoard(char[][] board, int xRugTopLeftCorner, int yRugTopLeftCorner, int rugLength, Player player1, Player player2) throws Exception {
        // Initialize board with blank squares
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[x][y] = ' ';
            }
        }

        for (int y = xRugTopLeftCorner; y < xRugTopLeftCorner + rugLength; y++) {
            for (int x = yRugTopLeftCorner; x < yRugTopLeftCorner + rugLength; x++) {
                board[x][y] = '*';
            }
        }
        if (board[player1.getX()][player1.getY()] == '*' || board[player2.getX()][player2.getY()] == '*') {
            throw new Exception("A player cannot begin on the rug");
        }
        board[player1.getX()][player1.getY()] = player1.getPlayerNum();
        board[player2.getX()][player2.getY()] = player2.getPlayerNum();
    }

    /** The function prints the content of the board
     *
     * @param board The board is a two dimensional array representing a playing board
     */
    static void showBoard(char[][] board) {
        for (int i = 0; i < board.length + 2; i++) {
            System.out.print("# ");
        }
        System.out.println();
        for (int y = 0; y < board.length; y++) {
            System.out.print("# ");
            for (int x = 0; x < board[y].length; x++) {
                System.out.print(board[x][y]);
                System.out.print(" ");
            }
            System.out.println("#");
        }
        for (int i = 0; i < board.length + 2; i++) {
            System.out.print("# ");
        }
        System.out.println();
    }

}

