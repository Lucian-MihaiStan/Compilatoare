.data
    .align	2
    .globl	class_nameTab
    .globl	Int_protObj
    .globl	String_protObj
    .globl	bool_const_false
    .globl	bool_const_true
    .globl	Main_protObj
    .globl	_int_tag
    .globl	_string_tag
    .globl	_bool_tag
_int_tag:
    .word 2
_string_tag:
    .word 3
_bool_tag:
    .word 4

str_const0:
    .word 3
    .word 5
    .word String_dispTab
    .word int_const0
    .asciiz ""
    .align 2
str_const1:
    .word 3
    .word 6
    .word String_dispTab
    .word int_const1
    .asciiz "Object"
    .align 2
str_const2:
    .word 3
    .word 5
    .word String_dispTab
    .word int_const2
    .asciiz "IO"
    .align 2
str_const3:
    .word 3
    .word 6
    .word String_dispTab
    .word int_const3
    .asciiz "Int"
    .align 2
str_const4:
    .word 3
    .word 6
    .word String_dispTab
    .word int_const1
    .asciiz "String"
    .align 2
str_const5:
    .word 3
    .word 6
    .word String_dispTab
    .word int_const4
    .asciiz "Bool"
    .align 2
str_const6:
    .word 3
    .word 6
    .word String_dispTab
    .word int_const4
    .asciiz "Main"
    .align 2

int_const_0:
    .word 2
    .word 4
    .word Int_dispTab
    .word 0
int_const_1:
    .word 2
    .word 4
    .word Int_dispTab
    .word 1
int_const_2:
    .word 2
    .word 4
    .word Int_dispTab
    .word 2
int_const_3:
    .word 2
    .word 4
    .word Int_dispTab
    .word 3
int_const_4:
    .word 2
    .word 4
    .word Int_dispTab
    .word 4



Process finished with exit code 0    
