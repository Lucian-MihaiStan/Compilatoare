#stiva 20->1


        .data
new_line: .asciiz "\n"

        .text
        .globl main
main:   

    li $v1, 20

    while: ble $v1, 0, exit
        sub $sp,$sp,4
        sw $v1,($sp)

        sub $v1, $v1, 1 
        j while
    exit:

    li $v1, 20

    while2: ble $v1, 0, exit2
        lw $t2,($sp)
        addiu $sp,$sp,4

        li $v0, 1
        move $a0, $t2
        syscall

        li $v0, 4
        la $a0, new_line
        syscall

        sub $v1, $v1, 1 
        j while2
    exit2:

        li $v0, 10
        syscall