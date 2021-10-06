#!/bin/bash
while :
do
echo "Main Menu:"
echo -e "\t(a) More Menu Options "
echo -e "\t(b) Exit"
echo -n "Please enter your choice:"
read choice
case $choice in
    "a"|"A")
    while :
    do
    echo "Secondary menu"
    echo -e "\t(c) Print this menu again"
    echo -e "\t(d) Return to main menu"
    echo -n "Please enter your choice:"
    read choice1
    case $choice1 in
        "c"|"C")
        ;;
        "d"|"D")
        break
        ;;
            *)
            echo "invalid answer, please try again"
            ;;
    esac
    done
    ;;
    "b"|"B")
    exit
    ;;
        *)
        echo "invalid answer, please try again"
        ;;
esac
done