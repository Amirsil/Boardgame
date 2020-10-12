import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int size = 10;
        Scanner stdin = new Scanner(System.in);
        char[][] board = new char[size][size];
        int x1, y1; // PlayerA location
        int x2, y2; // PlayerB location
        int xRugTopLeftCorner, yRugTopLeftCorner; // Location of the rug's top left corner
        int rugLength; // Size of the rug
        Player p;
        Player p1 = new Player(board, -1, -1, '1');;
        Player p2 = new Player(board, -1, -1, '2');
        int direction;
        boolean isRoundOver;
        boolean isPlayerInsideCarpet;
        boolean isGameOver = false;
        while (!isGameOver) {
            System.out.println("A new game is starting!");
            x1 = safeInput(stdin, "Player1 x location: ", size);
            y1 = safeInput(stdin, "Player1 y location:", size);
            x2 = safeInput(stdin, "Player2 x location:", size);
            y2 = safeInput(stdin, "Player2 y location:", size);
            xRugTopLeftCorner = safeInput(stdin, "Rug top left corner x location:", size);
            yRugTopLeftCorner = safeInput(stdin, "Rug top left corner y location:", size);
            rugLength = safeRugInput(stdin, "Rug size: ", size, xRugTopLeftCorner, yRugTopLeftCorner);

            p1.setLocation(x1, y1);
            p2.setLocation(x2, y2);
            isPlayerInsideCarpet = initializeBoard(board, xRugTopLeftCorner, yRugTopLeftCorner, rugLength, p1, p2);
            if (isPlayerInsideCarpet){
                System.out.println("Players can't start inside of the carpet! restarting game...");
                continue;
            }
            p = p1;
            System.out.println("Player 1 is first!");
            Thread.sleep(1000);

            while (true) {
                showBoard(board);
                try {
                    direction = Integer.parseInt(stdin.nextLine());
                    System.out.println(direction);
                    if (direction < 0 || direction > 4) {
                        throw new Exception();
                    }
                } catch (Exception ignore) {
                    System.out.println("Illegal move, Your turn is over");
                    // Switch turns
                    if (p == p1) {
                        p = p2;
                    } else {
                        p = p1;
                    }
                    continue;
                }
                isRoundOver = p.walk(direction);
                if (isRoundOver) {
                    break;
                }
                // Switch turns
                if (p == p1) {
                    p = p2;
                } else {
                    p = p1;
                }
            }
            System.out.format("%c is the winner!\n Player%c wins: %d\n Player%c wins: %d\n",
                    p.getPlayerNum(), p1.getPlayerNum(), p1.getNumberOfWins(),
                    p2.getPlayerNum(), p2.getNumberOfWins());

            if (!p1.wantsToPlayAgain(stdin) || !p2.wantsToPlayAgain(stdin)){
                isGameOver = true;
            }
        }




    }

    static int safeRugInput(Scanner stdin, String message, int boardSize, int xRugTopLeftCorner, int yRugTopLeftCorner){
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

    static boolean initializeBoard(char[][] board, int xRugTopLeftCorner, int yRugTopLeftCorner, int rugLength, Player p1, Player p2){
        // Initialize board with blank squares
        for (int y = 0; y < board.length; y++){
            for (int x = 0; x < board[y].length; x++){
                board[x][y] = '.';
            }
        }

        for (int y = xRugTopLeftCorner; y < xRugTopLeftCorner + rugLength; y++){
            for (int x = yRugTopLeftCorner; x < yRugTopLeftCorner + rugLength; x++){
                board[x][y] = '*';
            }
        }
        if (board[p1.getX()][p1.getY()] == '*' || board[p2.getX()][p2.getY()] == '*'){
            return true;
        }
        board[p1.getX()][p1.getY()] = p1.getPlayerNum();
        board[p2.getX()][p2.getY()] = p2.getPlayerNum();
        return false;
    }
    static void showBoard(char[][] board){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                System.out.print(board[x][y]);
                System.out.print("  ");
            }
            System.out.println();
        }
    }

}

