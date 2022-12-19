.data
new_line: .asciiz "\n"

.text

main:
	li $a0 4

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
    neg $a0 $a0        # -4

    li $v0, 1
    syscall

    sub $sp,$sp,4
    sw $a0,($sp)

    li $v0, 4
    la $a0, new_line
    syscall

    lw $a0,($sp)
    addiu $sp,$sp,4

	li $a0 1

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 2

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		

	li $v0, 1
    syscall

    sub $sp,$sp,4
    sw $a0,($sp)

    li $v0, 4
    la $a0, new_line
    syscall

    lw $a0,($sp)
    addiu $sp,$sp,4
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 3

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		# 1+2+3

	li $v0, 1
    syscall

    sub $sp,$sp,4
    sw $a0,($sp)

    li $v0, 4
    la $a0, new_line
    syscall

    lw $a0,($sp)
    addiu $sp,$sp,4

	li $a0 5

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 3

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
    lw $t1 4($sp)
    neg $a0 $a0
    add $a0 $t1 $a0

    addiu $sp $sp 4     # 5-3

    li $v0, 1       # print part
    syscall

    sub $sp,$sp,4
    sw $a0,($sp)

    li $v0, 4
    la $a0, new_line
    syscall

    lw $a0,($sp)
    addiu $sp,$sp,4 # restore the result into $a0 which is stored on stack before print

	li $a0 2

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
     sw $a0 0($sp)
     addiu $sp $sp -4
	li $a0 2

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
     lw $t1 4($sp)
     mult $a0 $t1
     addiu $sp $sp 4		# 2*2

     mfhi $t0
     mflo $t1

     move $a0 $t1

     li $v0, 1       # print part
    syscall

    sub $sp,$sp,4
    sw $a0,($sp)

    li $v0, 4
    la $a0, new_line
    syscall

    lw $a0,($sp)
    addiu $sp,$sp,4 # restore the result into $a0 which is stored on stack before print


	li $a0 84

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 2

	li $v0, 1
	syscall

	sub $sp,$sp,4
    sw $a0,($sp)

	li $v0, 4
	la $a0, new_line
	syscall

	lw $a0,($sp)
    addiu $sp,$sp,4
    lw $t1 4($sp)
     div $t1 $a0
     addiu $sp $sp 4		# 84/2

     mfhi $t0               # rest
     mflo $t1               # cat

     move $a0 $t1

     li $v0, 1       # print part
     syscall

     sub $sp,$sp,4
     sw $a0,($sp)

     li $v0, 4
     la $a0, new_line
     syscall

     lw $a0,($sp)
     addiu $sp,$sp,4 # restore the result into $a0 which is stored on stack before print











	li $v0, 10	
	syscall		#exit
