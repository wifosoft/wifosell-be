#!/bin/bash

# Set the color variable
green='\033[0;32m'
# Clear the color after that
clear='\033[0m'

RESTORE='\033[0m'

RED='\033[00;31m'
GREEN='\033[00;32m'
YELLOW='\033[00;33m'
BLUE='\033[00;34m'
PURPLE='\033[00;35m'
CYAN='\033[00;36m'
LIGHTGRAY='\033[00;37m'

LRED='\033[01;31m'
LGREEN='\033[01;32m'
LYELLOW='\033[01;33m'
LBLUE='\033[01;34m'
LPURPLE='\033[01;35m'
LCYAN='\033[01;36m'
WHITE='\033[01;37m'

JIRA_ID=''
JIRA_TASK_MODULE=''
GITHUB_JIRA_COMMIT=''
function test_colors() {
  echo -e "${GREEN}Hello ${CYAN}THERE${RESTORE} Restored here ${LCYAN}HELLO again ${RED} Red socks aren't sexy ${BLUE} neither are blue ${RESTORE} "
}

function pause() {
  echo -en "${CYAN}"
  read -p "[Paused]  $*" FOO_discarded
  echo -en "${RESTORE}"
}
function fnc_spline() {
  echo -e
  echo "====================================================================="
  echo -e
}
# reset to white
echo -en "${RESTORE}"

function opt_commit() {
  while :; do
    echo -e
    echo -en "+ ${GREEN} Git Commit feature ${RESTORE}\n"
    echo -e
    echo -e "\t(1) Commit new task"
    echo -e "\t(2) Commit transition task"
    echo -e "\t(0) Return"
    echo -n "Please enter your choice:"

    read choice1
    echo -e
    fnc_spline
    case $choice1 in
    "1" | "1 ")
      clear
      echo -en "You choose ${LCYAN}[option 1]${RESTORE} to commit new task"
      echo -e
      if [ -z "$JIRA_ID" ]; then
        #if JIRA_ID is empty, assign to one
        echo "JIRA_ID is empty, create new one: "
        read _ipt_jira_id
        JIRA_ID="WFSLL-${_ipt_jira_id}"
        echo -e
        #add all file
        git add .

        echo -e "- Write module: (SHOP/USER/...) for tag [SHOP]"
        read _ipt_task_module
        JIRA_TASK_MODULE=_ipt_task_module

        #set new_transition
        echo -e "- Choose transition: (#start-work/#review/#done/#block)"
        read _ipt_transition

        echo -e "- Commit message: "
        read _ipt_commit_message


        GITHUB_JIRA_COMMIT=$(git commit -m "[$JIRA_TASK_MODULE] $JIRA_ID #${_ipt_transition} ${_ipt_commit_message}")
        #commit
        echo GITHUB_JIRA_COMMIT
        JIRA_ID=""

      else

        echo "Variable is ok"
      fi

      ;;
    "0" | "0 ")
      break
      ;;

    *)
      echo -en "- ${LRED}invalid answer, please try again ${RESTORE}"
      echo -e
      ;;
    esac
  done
}

while :; do
  echo -en "${GREEN} Wifosell toolkit **Wgit** ${RESTORE}"
  echo -e
  echo -e "\t(1) Git commit "
  echo -e "\t(2) Git push "
  echo -e "\t(0) Exit"
  echo -n "Please enter your choice:"
  read choice

  echo -e
  fnc_spline
  case $choice in
  "1")
    clear
    opt_commit
    ;;
  "0" | "0")
    exit
    ;;
  *)
    echo -en "- ${LRED}invalid answer, please try again ${RESTORE}"
    echo -e
    ;;
  esac
done
