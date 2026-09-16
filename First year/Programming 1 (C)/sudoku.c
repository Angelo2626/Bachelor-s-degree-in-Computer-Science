#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define N 9
#define UNASSIGNED 0

int grid[N][N];

int findUnassignedLocation(int *row, int *col);

int isSafe(int row, int col, int num) {
    for (int x = 0; x < N; x++)
        if (grid[row][x] == num)
            return 0;

    for (int x = 0; x < N; x++)
        if (grid[x][col] == num)
            return 0;

    int startRow = row - row % 3;
    int startCol = col - col % 3;
    for (int i = 0; i < 3; i++)
        for (int j = 0; j < 3; j++)
            if (grid[i + startRow][j + startCol] == num)
                return 0;

    return 1;
}

int solveSudoku() {
    int row, col;
    if (!findUnassignedLocation(&row, &col))
        return 1;

    for (int num = 1; num <= 9; num++) {
        if (isSafe(row, col, num)) {
            grid[row][col] = num;
            if (solveSudoku())
                return 1;
            grid[row][col] = UNASSIGNED;
        }
    }
    return 0;
}

int findUnassignedLocation(int *row, int *col) {
    for (*row = 0; *row < N; (*row)++)
        for (*col = 0; *col < N; (*col)++)
            if (grid[*row][*col] == UNASSIGNED)
                return 1;
    return 0;
}

void printGrid() {

    printf("\n");
    printf("  - - -   - - -   - - - \n");
    
    for (int row = 0; row < N; row++) {
        for (int col = 0; col < N; col++)
            if(col == 0 || col == 3| col == 6){
                printf("| %d ", grid[row][col]);
            }else{
                printf("%d ", grid[row][col]);
            }
        printf("|");
        if(row == 2 || row == 5){ 
            printf("\n| - - - + - - - + - - - |");
        }
        printf("\n");
    }
    printf("  - - -   - - -   - - - \n");
    printf("\n");
}

void generateSudoku() {
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            grid[i][j] = UNASSIGNED;

    srand(time(NULL));
    for (int i = 0; i < 20; i++) {
        int row = rand() % N;
        int col = rand() % N;
        int num = rand() % 9 + 1;
        if (isSafe(row, col, num) && grid[row][col] == UNASSIGNED)
            grid[row][col] = num;
        else
            i--;
    }

    if (solveSudoku())
        printGrid();
    else
        printf("Nessuna soluzione trovata\n");
}

int main() {
    int n;
    while(1){
        printf("Premere 1 per tentare la creazione di una tabella sudoku, 0 per terminare il programma: ");
        scanf("%d", &n);
        if(n==1){
            generateSudoku();
        }else{
            break;
        }
    }
    
    return 0;
}