.data
myvar: .word 0

myvar2: .word 0

.text
myfunc:
    move $fp $sp
    sw $ra 0($sp)
    addiu $sp $sp -4
	li $a0 4
    lw $ra 4($sp)
    addiu $sp $sp 28
    lw $fp 0($sp)
    jr $ra

avg:
    move $fp $sp
    sw $ra 0($sp)
    addiu $sp $sp -4
	lw $a0, 4($fp)
	sw $a0 0($sp)
	addiu $sp $sp -4
	lw $a0, 8($fp)
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 2
	lw $t1 4($sp)
	div $t1 $a0
	mflo $a0
	addiu $sp $sp 4		
    lw $ra 4($sp)
    addiu $sp $sp 16
    lw $fp 0($sp)
    jr $ra

main:
	li $a0 4
    neg $a0, $a0       # -4
	li $a0 1
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 2
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 3
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		# 1+2+3
	li $a0 5
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 3
	lw $t1 4($sp)
	sub $a0 $t1 $a0
	addiu $sp $sp 4		# 5-3
	li $a0 2
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 2
	lw $t1 4($sp)
	mult $a0 $t1
	mflo $a0
	addiu $sp $sp 4		# 2*2
	li $a0 84
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 2
	lw $t1 4($sp)
	div $t1 $a0
	mflo $a0
	addiu $sp $sp 4		# 84/2
	li $a0 1
    beq $a0, 0, else0
then0:
	li $a0 5
    j end0
else0:
	li $a0 7
end0:   # iftruethen5else7fi
	li $a0 0
    beq $a0, 0, else1
then1:
	li $a0 7
    j end1
else1:
	li $a0 5
end1:   # iffalsethen7else5fi
    sw $fp 0($sp)
	li $a0 5
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 4
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 3
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 2
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 1
    sw $a0 0($sp)
    addiu $sp $sp -4
    jal myfunc
	li $a0 5
    sw $a0, myvar
	lw $a0 myvar
    sw $a0, myvar2
    sw $fp 0($sp)
	lw $a0 myvar2
    sw $a0 0($sp)
    addiu $sp $sp -4
	lw $a0 myvar
    sw $a0 0($sp)
    addiu $sp $sp -4
    jal avg
	li $v0, 10
	syscall		#exit
