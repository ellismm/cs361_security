#include <stdio.h>
#include <stdlib.h>
#include <time.h>

char sbox[] = {99, 244, 87, 39, 15, 37, 137, 143, 195, 38, 242, 203, 156, 124, 58, 69, 122, 116, 82, 226, 177, 184, 165, 11, 241, 57, 205, 253, 232, 146, 26, 95, 222, 1, 75, 148, 186, 129, 2, 255, 194, 121, 46, 27, 214, 206, 35, 73, 48, 200, 16, 170, 252, 18, 155, 227, 106, 96, 44, 78, 239, 158, 104, 196, 93, 79, 160, 29, 139, 166, 159, 212, 172, 204, 151, 91, 175, 84, 225, 213, 173, 237, 185, 76, 81, 198, 33, 230, 4, 149, 53, 115, 118, 80, 72, 64, 74, 240, 218, 162, 238, 215, 220, 251, 152, 130, 45, 191, 83, 65, 145, 51, 90, 62, 243, 114, 43, 88, 6, 236, 234, 85, 71, 28, 63, 192, 135, 233, 66, 126, 56, 178, 52, 157, 216, 217, 24, 164, 229, 193, 249, 60, 187, 8, 197, 67, 181, 100, 10, 107, 211, 127, 210, 89, 209, 55, 21, 20, 161, 188, 25, 32, 183, 36, 23, 47, 86, 132, 219, 19, 199, 59, 9, 201, 176, 190, 13, 117, 97, 40, 0, 3, 123, 248, 42, 250, 169, 30, 133, 140, 61, 101, 254, 108, 125, 113, 150, 22, 77, 202, 228, 7, 167, 182, 223, 70, 147, 41, 34, 5, 105, 94, 102, 189, 98, 154, 134, 235, 163, 14, 207, 109, 68, 247, 17, 245, 128, 120, 221, 92, 168, 103, 142, 138, 54, 208, 110, 49, 231, 179, 246, 153, 50, 31, 112, 111, 141, 136, 12, 144, 224, 174, 171, 180, 119, 131};

char inv_sbox[] = {180, 33, 38, 181, 88, 209, 118, 201, 143, 172, 148, 23, 248, 176, 219, 4, 50, 224, 53, 169, 157, 156, 197, 164, 136, 160, 30, 43, 123, 67, 187, 243, 161, 86, 208, 46, 163, 5, 9, 3, 179, 207, 184, 116, 58, 106, 42, 165, 48, 237, 242, 111, 132, 90, 234, 155, 130, 25, 14, 171, 141, 190, 113, 124, 95, 109, 128, 145, 222, 15, 205, 122, 94, 47, 96, 34, 83, 198, 59, 65, 93, 84, 18, 108, 77, 121, 166, 2, 117, 153, 112, 75, 229, 64, 211, 31, 57, 178, 214, 0, 147, 191, 212, 231, 62, 210, 56, 149, 193, 221, 236, 245, 244, 195, 115, 91, 17, 177, 92, 254, 227, 41, 16, 182, 13, 194, 129, 151, 226, 37, 105, 255, 167, 188, 216, 126, 247, 6, 233, 68, 189, 246, 232, 7, 249, 110, 29, 206, 35, 89, 196, 74, 104, 241, 215, 54, 12, 133, 61, 70, 66, 158, 99, 218, 137, 22, 69, 202, 230, 186, 51, 252, 72, 80, 251, 76, 174, 20, 131, 239, 253, 146, 203, 162, 21, 82, 36, 142, 159, 213, 175, 107, 125, 139, 40, 8, 63, 144, 85, 170, 49, 173, 199, 11, 73, 26, 45, 220, 235, 154, 152, 150, 71, 79, 44, 101, 134, 135, 98, 168, 102, 228, 32, 204, 250, 78, 19, 55, 200, 138, 87, 238, 28, 127, 120, 217, 119, 81, 100, 60, 97, 24, 10, 114, 1, 225, 240, 223, 183, 140, 185, 103, 52, 27, 192, 39};

unsigned short key = 0;

unsigned char encrypt(unsigned char p, unsigned short k) {
	unsigned char x = p^(k>>8);
	unsigned char y = sbox[x];
	return y^(k&0xff);
}

unsigned char decrypt(unsigned char c, unsigned short k) {
	unsigned char y = c^(k&0xff);
	unsigned char x = inv_sbox[y];
	return x^(k>>8);
}

void do_encrypt() {
	printf("%s", "Enter a hex byte to encrypt:\n");
	unsigned int x = 0;
	int r = scanf("%x", &x);
	if(r != 1) {
		printf("Not a valid byte\n");
		exit(1);
	}
	if((0xffffff00 & x) != 0) {
		printf("You entered more than one byte.\n");
		exit(1);
	}
	unsigned char c = x&0xff;
	printf("%x\n", encrypt(c, key));
}

void solve() {
	printf("Enter your guess as hex:\n");
	unsigned short guess;
	int r = scanf("%hx", &guess);
	if(r != 1) {
		printf("Bad input\n");
		exit(1);
	}
	if(guess == key) {
		printf("Solved!\n");
	}
	else {
		printf("Wrong key!\n");
		exit(1);
	}
}

void do_key_cycle() {
	unsigned int k = rand();
	key = k&0xffff;
	//printf("%x\n", key);
	int cnt = 0;
	while(cnt < 100) {
		printf("Encrypt or solve? e/s\n");
		char c;
		int r = scanf(" %c", &c);
		if(r != 1 || (c != 'e' && c != 's')) {
			printf("Invalid input\n");
			exit(1);
		}
		if(c == 'e') {
			do_encrypt();
		}
		if(c == 's') {
			solve();
			break;
		}
	}
}

void get_submission_data() {
	char * name1 = NULL;
	char * name2 = NULL;
	printf("Enter your name:\n");
	scanf("%ms", &name1);
	printf("Enter your name:\n");
	scanf("%ms", &name2);
	printf("Congrats %s and %s you solved the encryption :)\n", name1, name2);
	char buf[50];
	snprintf(buf, 50, "%s_%s", name1, name2);
	FILE * file = fopen(buf, "ab+");
	fprintf(file, "%s %s\n", name1, name2);
}

void main() {	
	srand(time(NULL));
	int i;
	for(i = 0; i < 10; i++) {
		do_key_cycle();
	}
	get_submission_data();
}

