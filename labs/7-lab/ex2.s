#print_string("1");
#if N > 64 then
#print_string("Large value")
#else
#print_string("Small value")
#end-if
#print_string("2");


        .data
msg_1:      .asciiz "1\n"
msg_2:      .asciiz "2\n"
large_value: .asciiz "Large value\n"
small_value: .asciiz "Small value\n"

        .text
        .globl main
main:   li $v0, 4       # syscall 4 (print_str)
        la $a0, msg_1     # argument: string
        syscall         # print the string

        li $v1, 30
        ble $v1, 64, smaller
            li $v0, 4
            la $a0, large_value
            syscall

            j end

smaller:
            li $v0, 4
            la $a0, small_value
            syscall

end:

        li $v0, 4
        la $a0, msg_2
        syscall


exit:
        li $v0, 10
        syscall