#unsigned int i = 20;
#while(i > 0)
#print_int(i)
#i--


        .data
new_line: .asciiz "\n"

        .text
        .globl main
main:   

    li $v1, 20

    while: ble $v1, 0, exit
        li $v0, 1
        move $a0, $v1
        syscall

        li $v0, 4
        la $a0, new_line
        syscall

        sub $v1, $v1, 1 
        j while

exit:
        li $v0, 10
        syscall