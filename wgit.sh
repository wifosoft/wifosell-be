#!/bin/bash
# Author: @snowdence
# Wifosell Project
# @2021
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
JIRA_TASK_TRANSITION=''
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

function opt_commit_transition() {
  echo -en "You choose ${LCYAN}[option 2]${RESTORE} to make transition: "

  git add .

  if [ -z "$JIRA_ID" ]; then
    echo "No Existed!!!"
  else
    echo -en "${LCYAN}- Choose transition: (#(1).start-work / #(2). review / #(3). done / #(4). block, #(0). None ${RESTORE} : "
    while :; do
      read _ipt_transition
      case $_ipt_transition in
      "1" | "1 ")
        JIRA_TASK_TRANSITION="start-work"
        break;
        ;;
      "2")
        JIRA_TASK_TRANSITION="review"
        break;
        ;;
      "3")
        JIRA_TASK_TRANSITION="done"
        break;
        ;;
      "4")
        JIRA_TASK_TRANSITION="block"
        break;
        ;;
      "0")
        JIRA_TASK_TRANSITION=""
        break;
        ;;
      *)
        echo -en "- ${LRED}invalid answer, please try again ${RESTORE}"
        echo -e
        ;;
      esac
    done
    echo -en "${LCYAN}- Commit message: ${RESTORE}"
    read _ipt_commit_message

    GITHUB_JIRA_COMMIT="git commit -m \"[${JIRA_TASK_MODULE}] ${JIRA_ID} #${JIRA_TASK_TRANSITION} ${_ipt_commit_message}\""
    GITHUB_JIRA_COMMIT=${GITHUB_JIRA_COMMIT//[^[:ascii:]]/}

    #commit
    echo " GIT >>  $GITHUB_JIRA_COMMIT"
    eval $GITHUB_JIRA_COMMIT
  fi
}
function opt_commit_new_task() {
  echo -en "You choose ${LCYAN}[option 1]${RESTORE} to commit new task: "
  echo -e
  if [ -z "$JIRA_ID" ]; then
    #if JIRA_ID is empty, assign to one
    echo -en "${LCYAN}JIRA_ID is empty, create new one: ${RESTORE}"
    read _ipt_jira_id
    JIRA_ID="WFSLL-${_ipt_jira_id}"
    echo -e
    #add all file
    git add .

    echo -en "${LCYAN}- Write module: (shop, user,...) :${RESTORE}"
    read _ipt_task_module
    _ipt_task_module=$(echo $_ipt_task_module | tr 'a-z' 'A-Z')
    JIRA_TASK_MODULE="${_ipt_task_module}"

    #set new_transition
    echo -en "${LCYAN}- Choose transition: (#(1).start-work / #(2). review / #(3). done / #(4). block)${RESTORE} : "
    while :; do
      read _ipt_transition
      case $_ipt_transition in
      "1" | "1 ")
        JIRA_TASK_TRANSITION="start-work"
        break
        ;;
      "2")
        JIRA_TASK_TRANSITION="review"
        break
        ;;
      "3")
        JIRA_TASK_TRANSITION="done"
        break
        ;;
      "4")
        JIRA_TASK_TRANSITION="block"
        break
        ;;
      *)
        echo -en "- ${LRED}invalid answer, please try again ${RESTORE}"
        echo -e
        ;;
      esac
    done
    echo -en "${LCYAN}- Commit message: ${RESTORE}"
    read _ipt_commit_message

    GITHUB_JIRA_COMMIT="git commit -m \"[${JIRA_TASK_MODULE}] ${JIRA_ID} #${JIRA_TASK_TRANSITION} ${_ipt_commit_message}\""
    GITHUB_JIRA_COMMIT=${GITHUB_JIRA_COMMIT//[^[:ascii:]]/}

    #commit
    echo " GIT >>  $GITHUB_JIRA_COMMIT"
    eval $GITHUB_JIRA_COMMIT

  else
    echo "Variable is ok"
  fi
}
function opt_commit() {
  while :; do
    echo -e
    echo -en "+ ${GREEN} Git Commit feature ${RESTORE}\n"
    echo -e
    echo -e "\t(1) Commit new task"
    echo -e "\t(2) Commit transition"
    echo -e "\t(3) Flush current task"
    echo -e "\t(4) Push to github"
    echo -e "\t(0) Return"
    echo -n "Please enter your choice:"

    read choice1
    echo -e
    fnc_spline
    case $choice1 in
    "1" | "1 ")
      clear
      opt_commit_new_task
      ;;
    "2" | "2 ")
      clear
      echo -e "> Commit make transition : "
      opt_commit_transition
      ;;
    "3" | "3 ")
      JIRA_ID=''
      JIRA_TASK_MODULE=''
      JIRA_TASK_TRANSITION=''
      GITHUB_JIRA_COMMIT=''
      ;;
    "4" | "4 ")
      git push
      ;;
    "0")
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
  echo -e "\t(2) Coming soon...!!! "
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
