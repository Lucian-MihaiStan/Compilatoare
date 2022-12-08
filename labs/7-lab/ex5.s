#var = 12
#print_int(var)
#while(var != 1)
#if (var mod 2 == 0)
#var = var / 2
#else
#var = 3 * var + 1
#print_string(",")
#print_int(var)

        .data
new_line: .asciiz "\n"
comma: .asciiz ","
        .text

        .globl main
main:   

    li $v1, 12
    li $t3, 2
    li $t4, 3

    li $v0, 1
    move $a0, $v1
    syscall

    while: beq $v1, 1, exit

        move $t2, $v1

        div $t2, $t3
        mfhi $t6

        bne $t6, 0, else
            mflo $v1
            j endif
        else:
            mult $v1, $t4
            mflo $v1
            add $v1, $v1, 1
        endif:

        li $v0, 4
        la $a0, comma
        syscall

        li $v0, 1
        move $a0, $v1
        syscall

        j while

    exit:

    li $v0, 10
    syscall