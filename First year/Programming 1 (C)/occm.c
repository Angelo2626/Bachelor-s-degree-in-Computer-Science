#include <stdio.h>

#define ROWS 3
#define COLS 3

int countDuplicates(int matrix[ROWS][COLS], int rows, int cols) {
    int count = 0;
    int freq[100] = {0};  // Array per memorizzare la frequenza degli elementi (assumendo che i numeri siano tra 0 e 99)

    // Calcolo della frequenza degli elementi
    for(int i = 0; i < rows; i++) {
        for(int j = 0; j < cols; j++) {
            freq[matrix[i][j]]++;
        }
    }

    // Conto quanti elementi compaiono almeno due volte
    for(int i = 0; i < 100; i++) {
        if(freq[i] >= 2) {
            count++;
        }
    }

    return count;
}

int main() {
    int matrix[ROWS][COLS] = {
        {1, 2, 3},
        {4, 5, 6},
        {1, 2, 7}
    };

    int duplicateCount = countDuplicates(matrix, ROWS, COLS);
    printf("Numero di elementi che compaiono almeno due volte: %d\n", duplicateCount);

    return 0;
}
