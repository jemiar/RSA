# RSA Algorithm

###1. Note
. Program use choose file functionality, therefore you can just navigate to the files you want to input (primes file, message file, key file)  
. Generated files are stored in same level with src folder. Generated files: privatekey.txt, publickey.txt, blockfile.txt, unblock.txt, result.txt(using same for both encrypt or decrypt)  
. Prime test algorithm is referenced from: https://en.wikipedia.org/wiki/Primality_test

###2. Steps to run the program  
1. Download/Copy the source code to a new project on your Eclipse with the same name
2. Compile and run
3. Input 2 prime numbers, then generate a public/private keys pair
4. Choose a primes file, and generate a public/private keys pair
5. Choose a text file, input blocking size, block the message
6. Choose a file generated from blocking process, input the same blocking size used for blocking, generate the original message
7. Choose a file that has been blocked, choose a key file(in xml format), encrypt/decrypt the file
