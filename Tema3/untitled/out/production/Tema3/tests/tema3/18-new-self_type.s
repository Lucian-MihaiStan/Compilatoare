.data
    .align	2
    .globl	class_nameTab
    .globl	Int_protObj
    .globl	String_protObj
    .globl	bool_const0
    .globl	bool_const1
    .globl	Main_protObj
    .globl	_int_tag
    .globl	_string_tag
    .globl	_bool_tag
_int_tag:
    .word 10
_string_tag:
    .word 11
_bool_tag:
    .word 12

str_const0:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const0
    .asciiz ""
    .align 2
str_const1:
    .word   11
    .word   6
    .word   String_dispTab
    .word   int_const1
    .asciiz "Object"
    .align 2
str_const2:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const2
    .asciiz "IO"
    .align 2
str_const3:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "A"
    .align 2
str_const4:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "B"
    .align 2
str_const5:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "D"
    .align 2
str_const6:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "E"
    .align 2
str_const7:
    .word   11
    .word   6
    .word   String_dispTab
    .word   int_const4
    .asciiz "Main"
    .align 2
str_const8:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "G"
    .align 2
str_const9:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "C"
    .align 2
str_const10:
    .word   11
    .word   5
    .word   String_dispTab
    .word   int_const3
    .asciiz "F"
    .align 2
str_const11:
    .word   11
    .word   6
    .word   String_dispTab
    .word   int_const5
    .asciiz "Int"
    .align 2
str_const12:
    .word   11
    .word   6
    .word   String_dispTab
    .word   int_const1
    .asciiz "String"
    .align 2
str_const13:
    .word   11
    .word   6
    .word   String_dispTab
    .word   int_const4
    .asciiz "Bool"
    .align 2

int_const0:
    .word   10
    .word   4
    .word   Int_dispTab
    .word   0
int_const1:
    .word   10
    .word   4
    .word   Int_dispTab
    .word   6
int_const2:
    .word   10
    .word   4
    .word   Int_dispTab
    .word   2
int_const3:
    .word   10
    .word   4
    .word   Int_dispTab
    .word   1
int_const4:
    .word   10
    .word   4
    .word   Int_dispTab
    .word   4
int_const5:
    .word   10
    .word   4
    .word   Int_dispTab
    .word   3

bool_const0:
    .word   13
    .word   4
    .word   Bool_dispTab
    .word   0
bool_const1:
    .word   13
    .word   4
    .word   Bool_dispTab
    .word   1

class_nameTab:
	.word	str_const1
	.word	str_const2
	.word	str_const3
	.word	str_const4
	.word	str_const5
	.word	str_const6
	.word	str_const7
	.word	str_const8
	.word	str_const9
	.word	str_const10
	.word	str_const11
	.word	str_const12
	.word	str_const13

class_objTab:
    .word   Object_protObj
    .word   Object_init

    .word   IO_protObj
    .word   IO_init

    .word   A_protObj
    .word   A_init

    .word   B_protObj
    .word   B_init

    .word   D_protObj
    .word   D_init

    .word   E_protObj
    .word   E_init

    .word   Main_protObj
    .word   Main_init

    .word   G_protObj
    .word   G_init

    .word   C_protObj
    .word   C_init

    .word   F_protObj
    .word   F_init

    .word   Int_protObj
    .word   Int_init

    .word   String_protObj
    .word   String_init

    .word   Bool_protObj
    .word   Bool_init


Object_protObj:
    .word   0
    .word   3
    .word   Object_dispTab

IO_protObj:
    .word   1
    .word   3
    .word   IO_dispTab

A_protObj:
    .word   2
    .word   4
    .word   A_dispTab

B_protObj:
    .word   3
    .word   5
    .word   B_dispTab

D_protObj:
    .word   4
    .word   5
    .word   D_dispTab

E_protObj:
    .word   5
    .word   5
    .word   E_dispTab

Main_protObj:
    .word   6
    .word   6
    .word   Main_dispTab

G_protObj:
    .word   7
    .word   6
    .word   G_dispTab

C_protObj:
    .word   8
    .word   5
    .word   C_dispTab

F_protObj:
    .word   9
    .word   5
    .word   F_dispTab

Int_protObj:
    .word   10
    .word   4
    .word   Int_dispTab
	.word	0

String_protObj:
    .word   11
    .word   5
    .word   String_dispTab
	.word	int_const0
	.asciiz	""
	.align	2

Bool_protObj:
    .word   12
    .word   4
    .word   Bool_dispTab
	.word	0


Object_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort

IO_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string

A_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f

B_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   B.g

D_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   B.g

E_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   B.g

Main_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   B.g
    .word   Main.main
    .word   Main.i
    .word   Main.getA
	.globl	heap_start
G_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   B.g
    .word   Main.main
    .word   Main.i
    .word   Main.getA

C_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   C.h
    .word   C.f

F_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   IO.in_int
    .word   IO.in_string
    .word   IO.out_int
    .word   IO.out_string
    .word   A.f
    .word   C.h
    .word   C.f

Int_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort

String_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort
    .word   String.substr
    .word   String.length
    .word   String.concat

Bool_dispTab:
    .word   Object.copy
    .word   Object.type_name
    .word   Object.abort


heap_start:
    .word   0
    .text
    .globl Int_init
    .globl String_init
    .globl Bool_init
    .globl Main_init

	.globl Main.main

Object_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

IO_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     Object_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

Int_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     Object_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

String_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     Object_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

Bool_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     Object_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

A_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     IO_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

B_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     A_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

C_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     A_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

D_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     B_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

E_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     B_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

F_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     C_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

Main_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     E_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

G_init:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    jal     Main_init
    move	$a0 $s0
    lw		$fp 12($sp)
    lw		$s0 8($sp)
    lw		$ra 4($sp)
    addiu	$sp $sp 12
    jr		$ra

A.f:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    la      $a0 3
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra

B.g:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    la      $a0 2
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra

C.f:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    la      $a0 5
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra

C.h:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    la      $a0 4
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra

Main.getA:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra

Main.i:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra

Main.main:
    addiu	$sp $sp -12
    sw		$fp 12($sp)
    sw		$s0 8($sp)
    sw		$ra 4($sp)
    addiu	$fp $sp 4
    move	$s0 $a0
    lw		$fp 12($sp)
	lw		$s0 8($sp)
	lw		$ra 4($sp)
	addiu	$sp $sp 12
	jr		$ra
