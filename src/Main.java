import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        int size = 10;
        Scanner stdin = new Scanner(System.in);
        char[][] board = new char[size][size];
        int x1, y1; // PlayerA location
        int x2, y2; // PlayerB location
        int xRugTopLeftCorner, yRugTopLeftCorner; // Location of the rug's top left corner
        int rugLength; // Size of the rug

        x1 = safeInput(stdin, "Player1 x location: ", size);
        y1 = safeInput(stdin, "Player1 y location:", size);
        x2 = safeInput(stdin, "Player2 x location:", size);
        y2 = safeInput(stdin, "Player2 y location:", size);
        xRugTopLeftCorner = safeInput(stdin, "Rug top left corner x location:", size);
        yRugTopLeftCorner = safeInput(stdin, "Rug top left corner y location:", size);
        rugLength = safeRugInput(stdin, "Rug size: ", size, xRugTopLeftCorner, yRugTopLeftCorner);


        initializeBoard(board, xRugTopLeftCorner, yRugTopLeftCorner, rugLength, x1, x2, y1, y2);
        showBoard(board);

        Player p1 = new Player(board, x1, y1);
        Player p2 = new Player(board, x2, y2);
        p1.walk(1);
        showBoard(board);
        p2.walk(2);
        showBoard(board);




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


    static void initializeBoard(char[][] board, int xRugTopLeftCorner, int yRugTopLeftCorner, int rugLength, int x1, int x2, int y1, int y2){
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
        board[x1][y1] = '1';
        board[x2][y2] = '2';
    }
    static void showBoard(char[][] board){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                System.out.print(board[x][y]);
                System.out.print("  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
